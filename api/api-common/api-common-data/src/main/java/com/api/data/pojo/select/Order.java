package com.api.data.pojo.select;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * select - 查询排序
 */
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 方向
	 */
	public enum Direction {
		
		/** 递增 */
		asc,
		
		/** 递减 */
		desc;

		/**
		 * 从String中获取Direction
		 * 
		 * @param value 值
		 * @return String对应的Direction
		 */
		public static final Direction fromString(String value) {
			return Direction.valueOf(value);
		}
		
	}

	/** 默认方向 */
	private static final Direction DEFAULT_DIRECTION = Direction.desc;

	/** 属性 */
	private String property;

	/** 方向 */
	private Direction direction = DEFAULT_DIRECTION;

	/**
	 * 初始化一个新创建的Order对象
	 */
	public Order() {
	}

	/**
	 * @param property 属性
	 * @param direction 方向
	 */
	public Order(String property, Direction direction) {
		this.property = property;
		this.direction = direction;
	}

	/**
	 * 返回递增排序
	 * @param property 属性
	 * @return 递增排序
	 */
	public static final Order asc(String property) {
		return new Order(property, Direction.asc);
	}

	/**
	 * 返回递减排序
	 * @param property 属性
	 * @return 递减排序
	 */
	public static final Order desc(String property) {
		return new Order(property, Direction.desc);
	}
	
	public static final List<Order> orders(Order ... orders) {
		if(orders == null) {
			return null;
		}
		return List.of(orders);
	}
	
	/**
	 * 获取属性
	 * @return 属性
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * 获取方向
	 * @return 方向
	 */
	public Direction getDirection() {
		return direction;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (!Order.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		Order other = (Order) obj;
		return new EqualsBuilder()
			.append(getProperty(), other.getProperty())
			.append(getDirection(), other.getDirection())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(getProperty())
			.append(getDirection())
			.toHashCode();
	}

}