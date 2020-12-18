package com.acgist.fund.pojo.vo;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Layui结果</p>
 * 
 * @author acgist
 */
public class Layui implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>响应消息</p>
	 */
	private String msg;
	/**
	 * <p>响应编码</p>
	 */
	private Integer code;
	/**
	 * <p>响应数量</p>
	 */
	private Integer count;
	/**
	 * <p>响应结果</p>
	 */
	private Object data;

	public static final Layui success(Object data) {
		final Layui layui = new Layui();
		layui.setCode(0);
		layui.setMsg("成功");
		if(data instanceof List) {
			final List<?> list = (List<?>) data;
			layui.setCount(list.size());
		} else {
			layui.setCount(0);
		}
		layui.setData(data);
		return layui;
	}
	
	public static final Layui fail(Integer code, String msg) {
		final Layui layui = new Layui();
		layui.setCode(code);
		layui.setMsg(msg);
		return layui;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
