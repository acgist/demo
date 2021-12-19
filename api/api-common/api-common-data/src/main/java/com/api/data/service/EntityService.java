package com.api.data.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.transaction.annotation.Transactional;

import com.api.data.pojo.entity.BaseEntity;
import com.api.data.pojo.select.PageQuery;
import com.api.data.pojo.select.PageResult;
import com.api.data.repository.BaseExtendRepository;

/**
 * service - 实体操作
 */
public class EntityService<T extends BaseEntity> implements APIEntityService {

	protected BaseExtendRepository<T> repository;
	
	public EntityService(BaseExtendRepository<T> repository) {
		this.repository = repository;
	}
	
	/**
	 * 根据ID查询实体对象
	 * @param id 实体ID
	 * @return 实体对象
	 */
	@Transactional(readOnly = true)
	public T find(String id) {
		return repository.findOne(id);
	}
	
	/**
	 * 保存实体对象
	 * @param t 实体对象
	 * @return 实体对象
	 */
	@Transactional
	public T submit(T t) {
		return repository.save(t);
	}
	
	/**
	 * 更新实体对象
	 * @param t 实体对象
	 * @return 实体对象
	 */
	@Transactional
	public T update(T t) {
		return repository.save(t);
	}
	
	/**
	 * 根据ID删除实体对象
	 * @param id 实体ID
	 * @return 删除结果：true-删除成功、false-删除失败
	 */
	@Transactional
	public boolean delete(String id) {
		repository.deleteById(id);
		return true;
	}
	
	/**
	 * 根据ID数组批量删除实体对象
	 * @param ids 实体对象ID数组
	 */
	@Transactional
	public void delete(String ... ids) {
		Stream.of(ids).forEach(id -> delete(id));
	}
	
	/**
	 * 查询所有实体对象
	 * @return 所有实体对象列表
	 */
	@Transactional(readOnly = true)
	public List<T> findAll() {
		return repository.findAll();
	}
	
	/**
	 * 分页查询实体对象
	 * @param pageQuery 分页信息
	 * @return 分页结果
	 */
	@Transactional(readOnly = true)
	public PageResult<T> findPage(PageQuery pageQuery) {
		return repository.findPage(pageQuery);
	}
	
}
