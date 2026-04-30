package com.acgist.sharding;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_order")
public class OrderEntity {

	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflake")
//	@GenericGenerator(name = "snowflake", strategy = "com.acgist.sharding.SnowflakeBuilder")
	private Long id;
	@Column(name = "name")
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
