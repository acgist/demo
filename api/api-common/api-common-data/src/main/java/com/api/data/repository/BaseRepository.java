package com.api.data.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import com.api.data.pojo.entity.BaseEntity;
import com.api.data.pojo.select.Filter;
import com.api.data.pojo.select.Filter.Operator;
import com.api.data.pojo.select.Order;
import com.api.data.pojo.select.PageQuery;
import com.api.data.pojo.select.PageResult;

/**
 * repository - 添加findAll和findPage方法，直接使用接口实现即可使用
 */
@NoRepositoryBean
@Transactional(readOnly = true)
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, String>, JpaSpecificationExecutor<T>  {

	/**
	 * like查询拼接
	 */
	String LIKE = "%";
	
	/**
	 * like查询字符串拼接
	 */
	Function<String, String> likeQuery = (value) -> {return LIKE + value + LIKE;};
	
	/**
	 * 集合查询
	 * @param filters 查询条件
	 */
	default List<T> findAll(Filter ... filters) {
		return findAll((List<Order>) null, filters);
	}
	
	/**
	 * 集合查询
	 * @param order 查询排序
	 * @param filters 查询条件
	 */
	default List<T> findAll(Order order, Filter ... filters) {
		return findAll(Order.orders(order), filters);
	}
	
	/**
	 * 集合查询
	 * @param orders 查询排序
	 * @param filters 查询条件
	 */
	default List<T> findAll(List<Order> orders, Filter ... filters) {
		return findAll(new Specification<T>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				buildCriteriaQuery(Filter.filters(filters), orders, root, criteriaQuery, criteriaBuilder);
				return null;
			}
		});
	}
	
	/**
	 * 分页查询
	 * @param pageQuery 分页信息
	 * @return 分页结果
	 */
	default PageResult<T> findPage(PageQuery pageQuery) {
		Page<T> result = findAll(new Specification<T>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				buildCriteriaQuery(pageQuery.getFilters(), pageQuery.getOrders(), root, criteriaQuery, criteriaBuilder);
				return null;
			}
		}, PageRequest.of(pageQuery.getPage(), pageQuery.getPageSize()));
		return new PageResult<>(result.getContent(), result.getTotalElements(), pageQuery);
	}
	
	/**
	 * 创建查询条件
	 * TODO 泛型优化
	 * @param filters 查询条件
	 * @param orders 排序条件
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	default void buildCriteriaQuery(List<Filter> filters, List<Order> orders, Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
		// 条件
		if(filters != null && !filters.isEmpty()) {
			final List<Predicate> list = new ArrayList<>(filters.size());
			filters
			.stream()
			.filter(filter -> { // 过滤无效值
				final Operator operator = filter.getOperator();
				return filter != null &&
					filter.getProperty() != null &&
					((operator == Operator.isNull || operator == Operator.isNotNull) ? true : filter.getValue() != null);
			})
			.filter(filter -> { // 过滤空字符串
				final Object value = filter.getValue();
				if(value != null && value instanceof String) {
					return !((String) value).isEmpty();
				}
				return true;
			})
			.forEach(filter -> {
				final Operator operator = filter.getOperator();
				if (operator == Operator.eq) {
					list.add(criteriaBuilder.equal(root.get(filter.getProperty()), filter.getValue()));
				} else if (operator == Operator.ne) {
					list.add(criteriaBuilder.notEqual(root.get(filter.getProperty()), filter.getValue()));
				} else if (operator == Operator.gt) {
					list.add(criteriaBuilder.greaterThan(root.get(filter.getProperty()), (Comparable) filter.getValue()));
				} else if (operator == Operator.lt) {
					list.add(criteriaBuilder.lessThan(root.get(filter.getProperty()), (Comparable) filter.getValue()));
				} else if (operator == Operator.ge) {
					list.add(criteriaBuilder.greaterThanOrEqualTo(root.get(filter.getProperty()), (Comparable) filter.getValue()));
				} else if (operator == Operator.le) {
					list.add(criteriaBuilder.lessThanOrEqualTo(root.get(filter.getProperty()), (Comparable) filter.getValue()));
				} else if (operator == Operator.like) {
					list.add(criteriaBuilder.like(root.get(filter.getProperty()), likeQuery.apply(filter.getValue().toString())));
				} else if (operator == Operator.in) {
					list.add(root.get(filter.getProperty()).in(filter.getValue()));
				} else if (operator == Operator.isNull) {
					list.add(root.get(filter.getProperty()).isNull());
				} else if (operator == Operator.isNotNull) {
					list.add(root.get(filter.getProperty()).isNotNull());
				}
			});
//			OR筛选
//			criteriaQuery.where(criteriaBuilder.or(list.toArray(new Predicate[list.size()])));
//			AND筛选
//			criteriaQuery.where(criteriaBuilder.and(list.toArray(new Predicate[list.size()])));
			criteriaQuery.where(list.toArray(new Predicate[list.size()]));
		}
		// 排序
		if(orders != null && !orders.isEmpty()) {
			final List<javax.persistence.criteria.Order> list = new ArrayList<>(orders.size());
			orders
			.stream()
			.filter(order -> {
				return order != null && order.getProperty() != null;
			})
			.forEach(order -> {
				if (order.getDirection() == Order.Direction.asc) {
					list.add(criteriaBuilder.asc(root.get(order.getProperty())));
				} else if (order.getDirection() == Order.Direction.desc) {
					list.add(criteriaBuilder.desc(root.get(order.getProperty())));
				}
			});
			criteriaQuery.orderBy(list);
		}
	}
	
}
