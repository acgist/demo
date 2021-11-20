package com.acgist.customer;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.service.IUserService;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

@RefreshScope
@RestController
public class Controller {

	@Value("${customer:null}")
	private String config;
	
	@DubboReference(protocol = "dubbo")
	private IUserService userService;

	@PostConstruct
	public void init() {
		FlowRule rule = new FlowRule();
		rule.setResource("name");
		rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
		rule.setCount(100);
		FlowRuleManager.loadRules(Arrays.asList(rule));
		// 熔断规则在限流规则之后：注意两个Count
		DegradeRule degradeRule = new DegradeRule();
		degradeRule.setResource("name");
		degradeRule.setGrade(RuleConstant.DEGRADE_GRADE_RT);
		degradeRule.setCount(10);
		degradeRule.setTimeWindow(10);
		DegradeRuleManager.loadRules(Arrays.asList(degradeRule));
	}
	
	@GetMapping("/name")
	@SentinelResource(value = "name", fallback = "nameFallback", blockHandler = "nameBlockHandler")
	public String name() {
		return this.userService.name();
	}
	
	/**
	 * 异常：异常类型可以没有不能修改
	 */
	public String nameFallback(Throwable e) {
		return "fallback：" + e;
	}
	
	/**
	 * 限流：异常类型可以没有不能修改
	 * 如果没有配置blockHandler默认进入fallback
	 */
	public String nameBlockHandler(BlockException e) {
		return "nameBlockHandler：" + e;
	}
	
	@GetMapping("/block")
	public String block() {
		return "block";
	}
	
	@GetMapping("/config")
	public String config() {
		return this.config;
	}
	
}
