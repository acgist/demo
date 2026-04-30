package com.acgist.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Mapper
public interface DemoMapper {

	List<Demo> list();
	
	/**
	 * 如果需要实现自己实现拦截器
	 */
	Page<Demo> page(Pageable pageable);
	
	default Page<Demo> pageList(Pageable pageable) {
		final PageInfo<Demo> pageInfo = PageHelper
			.startPage(pageable.getPageNumber(), pageable.getPageSize())
			.doSelectPageInfo(() -> this.list());
		return new PageImpl<Demo>(pageInfo.getList(), pageable, pageInfo.getTotal());
	}
	
	void delete(Long id);
	
}
