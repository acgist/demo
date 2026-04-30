package com.acgist.duckdb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackages = "com.acgist.duckdb.data")
@MapperScan(basePackages = "com.acgist.duckdb.**.dao.mapper")
@SpringBootApplication
public class DuckdbApplication {

	public static void main(String[] args) {
		SpringApplication.run(DuckdbApplication.class, args);
	}

}
