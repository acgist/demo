package com.api.data.asyn.repository;

import org.springframework.stereotype.Repository;

import com.api.data.asyn.pojo.entity.LogEntity;
import com.api.data.repository.BaseExtendRepository;

/**
 * repository - 日志信息
 */
@Repository
public interface LogRepository extends BaseExtendRepository<LogEntity> {

}
