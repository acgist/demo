package com.acgist.dd.datasource.service;

/**
 * 动态数据源测试
 * 
 * @author acgist
 */
public interface DatasourceService {

    /**
     * 建表
     */
    void buildTable();
    
    /**
     * 插入
     */
    void insert();
    
    /**
     * 删除
     */
    void delete();
    
    /**
     * 修改
     */
    void update();
    
    /**
     * 查询
     */
    void select();

}
