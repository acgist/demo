package com.api.data.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.api.data.pojo.entity.BaseEntity;
import com.api.data.pojo.select.Filter;
import com.api.data.pojo.select.Order;

/**
 * repository - BaseExtendRepository实现类
 */
public class BaseExtendRepositoryImpl<T extends BaseEntity> extends SimpleJpaRepository<T, String> implements BaseExtendRepository<T> {

	protected EntityManager entityManager;
	protected JpaEntityInformation<T, ?> clazz;
	
	public BaseExtendRepositoryImpl(Class<T> clazz, EntityManager entityManager) {
		this(JpaEntityInformationSupport.getEntityInformation(clazz, entityManager), entityManager);
	}
	
	public BaseExtendRepositoryImpl(JpaEntityInformation<T, ?> clazz, EntityManager entityManager) {
		super(clazz, entityManager);
		this.clazz = clazz;
		this.entityManager = entityManager;
	}

	@Override
	public List<T> findList(int first, int size, List<Order> orders, Filter ... filters) {
		final Class<T> javaType = this.clazz.getJavaType();
		final CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		final CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(javaType);
		final Root<T> root = criteriaQuery.from(javaType);
		this.buildCriteriaQuery(Filter.filters(filters), orders, root, criteriaQuery, criteriaBuilder);
		final TypedQuery<T> typedQuery = this.entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		typedQuery.setFirstResult(first);
		typedQuery.setMaxResults(size);
		return typedQuery.getResultList();
	}
	
}
