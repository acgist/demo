package com.acgist.duckdb.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.acgist.duckdb.data.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface RelMapper extends BaseMapper<User> {
    
    @Insert("""
        <script>
        insert into t_rel(a_id, b_id) values
        <foreach collection="bId" item="value" separator=",">(#{aId}, #{value})</foreach>
        </script>
        """)
    void insert(Long aId, List<Long> bId);
    
    @Delete("delete from t_rel where a_id = #{aId}")
    void deleteById(Long aId);

}
