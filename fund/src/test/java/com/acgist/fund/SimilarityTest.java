package com.acgist.fund;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.fund.pojo.entity.FundEntity;
import com.acgist.fund.service.FundService;

@SpringBootTest
public class SimilarityTest {

	@Autowired
	private FundService fundService;
	
	/**
	 * <p>最低相似数量</p>
	 */
	private static final int MIN_SIMILARITY = 4;
	
	@Test
	public void compare() {
		final var list = this.fundService.list();
		final var similarity = new ArrayList<String>();
		for (FundEntity source : list) {
			for (FundEntity target : list) {
				// 忽略相同
				if(source.getCode().equals(target.getCode())) {
					continue;
				}
//				// 只要自选
//				if(!source.getSelect() || !target.getSelect()) {
//					continue;
//				}
				final long count = compare(source, target);
				if(count > MIN_SIMILARITY) {
					if(similarity.contains(source.getCode() + target.getCode())) {
						continue;
					} else {
						similarity.add(target.getCode() + source.getCode());
					}
					System.out.print("基金编号：" + source.getCode());
					System.out.print("-");
					System.out.print("基金名称：" + source.getName());
					System.out.println("-");
					System.out.print("基金编号：" + target.getCode());
					System.out.print("-");
					System.out.print("基金名称：" + target.getName());
					System.out.println("-");
					System.out.println("相似度：" + count);
				}
			}
		}
	}
	
	private static final long compare(FundEntity source, FundEntity target) {
		return source.getStockPositions().stream()
			.filter(sourceValue -> target.getStockPositions().stream()
				.anyMatch(targetValue -> !sourceValue.getName().isEmpty() && !targetValue.getName().isEmpty() && sourceValue.getName().equals(targetValue.getName()))
			)
			.count();
	}
	
}
