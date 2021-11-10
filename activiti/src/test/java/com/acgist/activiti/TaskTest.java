package com.acgist.activiti;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 任务服务
 */
@SpringBootTest
public class TaskTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskTest.class);
	
	@Autowired
	private TaskService taskService;

	void print(Task task) {
		LOGGER.info("-----------------------");
		LOGGER.info("任务ID：{}", task.getId());
		LOGGER.info("任务发起人：{}", task.getOwner());
		LOGGER.info("任务负责人：{}", task.getAssignee());
		LOGGER.info("任务名称：{}", task.getName());
		LOGGER.info("任务描述：{}", task.getDescription());
		LOGGER.info("任务变量：{}", task.getProcessVariables());
		LOGGER.info("任务变量：{}", task.getTaskLocalVariables());
	}
	
	@Test
	public void testList() {
		this.taskService.createTaskQuery()
			// 委托人
//			.taskOwner(null)
			// 办理人
//			.taskAssignee(null)
//			.taskAssigneeIds(Arrays.asList("技术部经理"))
			// 候选人
//			.taskCandidateUser(null)
			.processDefinitionId("请假:1:112504")
			.list()
			.forEach(this::print);
	}

	@Test
	public void testTaskClaim() {
		// 签收：assignee
		this.taskService.claim("92510", "经理");
	}
	
	@Test
	public void testTaskDelegate() {
		// 委托：owner -> assignee
		this.taskService.delegateTask("92510", "委托管理用户");
	}
	
	@Test
	public void testTaskResolve() {
		// 完成委托
		this.taskService.resolveTask("92510");
	}
	
	@Test
	public void testComplate() {
		final Map<String, Object> variables = new HashMap<>();
		variables.put("value", true);
		variables.put("mark", "安心去吧");
//		variables.put("value", false);
//		variables.put("mark", "年假已经修完");
		this.taskService.complete("125005", variables);
//		this.taskService.complete("117502");
	}
	
}
