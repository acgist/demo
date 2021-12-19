package com.api.core.pojo.layui;

import java.io.Serializable;
import java.util.List;

/**
 * layui - 表格
 */
public class LayuiTable implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int code;
	private int count;
	private String msg;
	private Object data;
	
	public static final LayuiTable build(List<?> list) {
		return build(list, list.size());
	}
	
	public static final LayuiTable build(List<?> list, long count) {
		LayuiTable table = new LayuiTable();
		table.code = 0;
		table.msg = "成功";
		table.count = (int) count;
		table.data = list;
		return table;
	}
	
	public static final LayuiTable fail(int code, String msg) {
		LayuiTable table = new LayuiTable();
		table.code = code;
		table.msg = msg;
		table.count = 0;
		return table;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
