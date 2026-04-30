package com.acgist.dd.datasource;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 * 
 * 注意：只绑定当前线程，多线程请通过参数传递或者使用其他`ThreadLocal`。
 * 
 * @author acgist
 */
public abstract class DynamicDatasource extends AbstractRoutingDataSource {
    
    public static final String SYSTEM  = "system";
    public static final String DATA_ID = "dataId";
    
    @FunctionalInterface
    public interface Executor<T> {
        
        /**
         * @param dataId dataId
         * 
         * @return 执行结果
         */
        T execute(String dataId);
        
    };
    
    /**
     * @param dataId dataId
     * 
     * @return dataId是否存在
     */
    public abstract boolean checkDataId(String dataId);
    
    /**
     * @param dataId dataId
     */
    public void checkDataIdAndThrow(String dataId) {
        if(this.checkDataId(dataId)) {
            return;
        }
        throw new IllegalArgumentException("无效数据源：" + dataId);
    }
    
    /**
     * 手动设置dataId
     * 
     * final boolean remove = this.set(dataId);
     * try {
     *     ...
     * } finally {
     *      if(remove) {
     *          this.remove();
     *      }
     * }
     * 
     * @param dataId DataId
     * 
     * @return 是否移除
     */
    public abstract boolean set(String dataId);
    
    /**
     * @return dataId
     */
    public abstract String getDataId();
    
    /**
     * @return dataId
     */
    public String getDataIdAndThrow() {
        final String dataId = this.getDataId();
        if(dataId == null) {
            throw new IllegalArgumentException("无效数据源：" + dataId);
        }
        return dataId;
    }
    
    /**
     * 删除dataId
     */
    public abstract void remove();
    
    /**
     * @return 当前所有dataId
     */
    public abstract List<String> allDataId();
    
    /**
     * 执行数据库操作
     * 
     * @param <T> 返回结果泛型
     * 
     * @param executor 执行操作
     * 
     * @return 返回结果
     */
    public <T> T executeForSystem(Executor<T> executor) {
        return this.executeForDataId(DynamicDatasource.SYSTEM, executor);
    }
    
    /**
     * 执行数据库操作
     * 
     * @param <T> 返回结果泛型
     * 
     * @param dataId   dataId
     * @param executor 执行操作
     * 
     * @return 返回结果
     */
    public abstract <T> T executeForDataId(String dataId, Executor<T> executor);
    
    /**
     * 执行数据库操作
     * 
     * @param <T> 返回结果泛型
     * 
     * @param dataId   dataId
     * @param supplier 执行操作
     * 
     * @return 返回结果
     */
    public <T> T executeForDataId(String dataId, Supplier<T> supplier) {
        return this.executeForDataId(dataId, value -> supplier.get());
    }
    
    /**
     * @see #executeForEachDataId(Executor, Object, boolean)
     */
    public <T> Map<String, T> executeForEachDataId(Executor<T> executor) {
        return this.executeForEachDataId(executor, null, false);
    }
    
    /**
     * 执行数据库操作
     * 
     * @param <T> 返回结果泛型
     * 
     * @param executor       执行操作
     * @param defaultValue   默认返回
     * @param catchException 捕获异常
     * 
     * @return 返回结果
     */
    public abstract <T> Map<String, T> executeForEachDataId(Executor<T> executor, T defaultValue, boolean catchException);
    
    /**
     * 执行数据库操作
     * 
     * @param <T> 执行结果泛型
     * 
     * @param executor        执行操作
     * @param executorService 线程池
     * 
     * @return 返回结果
     */
    public abstract <T> Map<String, Future<T>> executeForEachDataId(Executor<T> executor, ExecutorService executorService);
    
    /**
     * @return 数据源映射
     */
    public abstract Map<Object, Object> getDatasources();
    
}
