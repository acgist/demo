package com.acgist.activiti;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 流程执行
 */
@SpringBootTest
public class RuntimeTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeTest.class);
	
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private IdentityService identityService;
	
	@Test
	public void testStart() {
//		this.runtimeService.activateProcessInstanceById(null);
//		this.runtimeService.suspendProcessInstanceById(null);
		// 设置发起用户
		this.identityService.setAuthenticatedUserId("acgist");
		// 负责人变量
		final Map<String, Object> variables = new HashMap<>();
		variables.put("manager", "技术部经理");
		variables.put("personnel", "人事主管");
		final ProcessInstance processInstance = this.runtimeService.startProcessInstanceById("请假:1:112504", variables);
		LOGGER.info("流程ID：{}", processInstance.getId());
		LOGGER.info("活动ID：{}", processInstance.getActivityId());
//		final Map<String, Object> variables = new HashMap<>();
//		variables.put("请假时间", "明天");
//		variables.put("请假原因", "年假");
	}
	
	@Test
	public void testExecution() {
		this.runtimeService.createExecutionQuery().list().forEach(execution -> {
			LOGGER.info("-----------------------");
			LOGGER.info("执行ID：{}", execution.getId());
			LOGGER.info("执行名称：{}", execution.getName());
			LOGGER.info("执行流程ID：{}", execution.getProcessInstanceId());
			LOGGER.info("执行根流程ID：{}", execution.getRootProcessInstanceId());
		});
	}

	@Test
	public void testProcessInstance() {
		this.runtimeService.createProcessInstanceQuery().startedBy("acgist").list().forEach(instance -> {
			LOGGER.info("----------------------------");
			LOGGER.info("流程ID：{}", instance.getId());
			LOGGER.info("流程名称：{}", instance.getName());
			LOGGER.info("流程变量：{}", instance.getProcessVariables());
		});
	}
	
}
