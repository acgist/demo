package com.acgist.duckdb.data.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@TableName(value = "t_user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @TableId(value = "id")
    private Long id;
    @TableField(value = "name")
    private String name;
    
}
