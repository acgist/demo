package com.api.data.pojo.select;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * select - 分页查询
 */
public class PageQuery implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** 默认页码 */
	private static final int DEFAULT_PAGE = 1;

	/** 默认每页记录数 */
	private static final int DEFAULT_PAGE_SIZE = 20;

	/** 最大每页记录数 */
	private static final int MAX_PAGE_SIZE = 1000;

	/** 页码 */
	private int page = DEFAULT_PAGE;

	/** 每页记录数 */
	private int pageSize = DEFAULT_PAGE_SIZE;

	/** 筛选 */
	private List<Filter> filters = new ArrayList<Filter>();

	/** 排序 */
	private List<Order> orders = new ArrayList<Order>();

	public PageQuery() {
	}

	public PageQuery(int page, int pageSize) {
		setPage(page);
		setPageSize(pageSize);
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		if (page < 1) {
			page = DEFAULT_PAGE;
		}
		this.page = page - 1;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
			pageSize = DEFAULT_PAGE_SIZE;
		}
		this.pageSize = pageSize;
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void addFilters(Filter ... filters) {
		if (filters == null || filters.length == 0) {
			return;
		}
		this.filters.addAll(Arrays.asList(filters));
	}

	public void addOrders(Order ... orders) {
		if (orders == null || orders.length == 0) {
			return;
		}
		this.orders.addAll(Arrays.asList(orders));
	}

}
