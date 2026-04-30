package com.acgist.dd.datasource.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.acgist.dd.datasource.service.DatasourceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DatasourceServiceImpl implements DatasourceService {

	private final DataSource dataSource;
	private final JdbcTemplate jdbcTemplate;

    @Override
    public void buildTable() {
        try (
            final InputStream input = this.getClass().getResourceAsStream("/build.sql");
            final Connection connection = this.dataSource.getConnection();
        ) {
            final String sql = new String(input.readAllBytes());
            try (
                final PreparedStatement statement = connection.prepareStatement(sql);
            ) {
                final boolean execute = statement.execute();
                log.info("执行建表语句：{} - {}", execute, sql);
            }
        } catch (SQLException e) {
            log.error("建表异常", e);
        } catch (IOException e) {
            log.error("建表异常", e);
        }
    }

    @Override
    public void insert() {
        final int insert = this.jdbcTemplate.update("""
        insert into t_test(id, name) values (1, "name")
        """);
        log.info("insert = {}", insert);
    }

    @Override
    public void delete() {
        final int delete = this.jdbcTemplate.update("""
        delete from t_test where id = 1
        """);
        log.info("delete = {}", delete);
    }

    @Override
    public void update() {
        final int update = this.jdbcTemplate.update("""
        update t_test set name = "acgist" where id = 1
        """);
        log.info("update = {}", update);
    }

    @Override
    public void select() {
        final List<String> list = this.jdbcTemplate.queryForList(
            "select name from t_test where id = 1",
            String.class
        );
        log.info("select = {}", list);
    }

}
