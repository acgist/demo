package com.acgist.activiti;

import org.activiti.engine.HistoryService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HistoryTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HistoryTest.class);

	@Autowired
	private HistoryService historyService;

	@Test
	public void testList() {
//		this.historyService.createHistoricProcessInstanceQuery()
		this.historyService.createHistoricTaskInstanceQuery()
//			.taskParentTaskId(null)
			.processInstanceId("115001")
			.orderByHistoricTaskInstanceStartTime().asc()
			.list().forEach(task -> {
				LOGGER.info("-----------------------");
				LOGGER.info("任务ID：{}", task.getId());
				LOGGER.info("任务发起人：{}", task.getOwner());
				LOGGER.info("任务负责人：{}", task.getAssignee());
				LOGGER.info("任务名称：{}", task.getName());
				LOGGER.info("任务描述：{}", task.getDescription());
				LOGGER.info("任务变量：{}", task.getProcessVariables());
				LOGGER.info("任务变量：{}", task.getTaskLocalVariables());
			});
	}
	
	@Test
	public void testHistoricProcessInstance() {
		this.historyService.createHistoricProcessInstanceQuery()
//		.finished()
		.startedBy("acgist").list().forEach(instance -> {
			LOGGER.info("----------------------------");
			LOGGER.info("流程ID：{}", instance.getId());
			LOGGER.info("流程名称：{}", instance.getName());
			LOGGER.info("流程结束时间：{}", instance.getEndTime());
		});
	}
	
}
