package com.acgist.dd.datasource;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DynamicDatasourceImpl extends DynamicDatasource {

    private final ThreadLocal<String> localDataId = new ThreadLocal<>();
    private final Map<Object, Object> datasources = new ConcurrentHashMap<>();
    
    @Override
    public boolean checkDataId(String dataId) {
        return this.datasources.containsKey(dataId);
    }
    
    @Override
    public boolean set(String dataId) {
        final String old = this.localDataId.get();
        if(old != null) {
            if(old.equals(dataId)) {
                // 可以重复设置
                return false;
            }
            throw new IllegalArgumentException("禁止数据源嵌套切换");
        }
        this.localDataId.set(dataId);
        MDC.put(DynamicDatasource.DATA_ID, dataId);
        return true;
    }
    
    @Override
    public String getDataId() {
        String dataId = this.localDataId.get();
        if(dataId != null) {
            return dataId;
        }
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        // TODO: 自行实现
        if(authentication.getPrincipal() instanceof Principal principal) {
            dataId = principal.getName();
        }
        return dataId;
    }
    
    @Override
    public void remove() {
        this.localDataId.remove();
        MDC.remove(DynamicDatasource.DATA_ID);
    }
    
    @Override
    public List<String> allDataId() {
        return this.datasources.keySet().stream()
            .filter(v -> !DynamicDatasource.SYSTEM.equals(v))
            .map(String::valueOf)
            .collect(Collectors.toList());
    }
    
    @Override
    public <T> T executeForDataId(String dataId, Executor<T> executor) {
        final boolean remove = this.set(dataId);
        try {
            return executor.execute(dataId);
        } finally {
            if(remove) {
                this.remove();
            }
        }
    }
    
    @Override
    public <T> Map<String, T> executeForEachDataId(Executor<T> executor, T defaultValue, boolean catchException) {
        return this.datasources.entrySet().stream()
            .filter(v -> !DynamicDatasource.SYSTEM.equals(v.getKey()))
            .collect(Collectors.toMap(
                entry -> entry.getKey().toString(),
                entry -> {
                    if(catchException) {
                        try {
                            return this.executeForDataId(entry.getKey().toString(), executor);
                        } catch (Exception e) {
                            log.error("执行数据库操作异常", e);
                        }
                        return defaultValue;
                    } else {
                        return this.executeForDataId(entry.getKey().toString(), executor);
                    }
                }
            ));
    }
    
    @Override
    public <T> Map<String, Future<T>> executeForEachDataId(Executor<T> executor, ExecutorService executorService) {
        return this.datasources.entrySet().stream()
            .filter(v -> !DynamicDatasource.SYSTEM.equals(v.getKey()))
            .collect(Collectors.toMap(
                entry -> entry.getKey().toString(),
                entry -> executorService.submit(() -> executor.execute(entry.getKey().toString()))
            ));
    }

    @Override
    public Map<Object, Object> getDatasources() {
        return this.datasources;
    }
    
    @Override
    protected Object determineCurrentLookupKey() {
        String dataId = this.localDataId.get();
        if(dataId == null) {
            dataId = this.getDataId();
        }
        if(dataId == null) {
            dataId = DynamicDatasource.SYSTEM;
        }
        int index = 0;
        while(!DynamicDatasource.SYSTEM.equals(dataId) && !this.datasources.containsKey(dataId)) {
            try {
                log.info("等待数据源注入：{}", dataId);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("等待数据源注入异常", e);
            }
            if(++index > 10) {
                throw new IllegalArgumentException("数据连接超时");
            }
        }
        return dataId;
    }

}
