//package com.acgist.activiti;
//
//import org.activiti.engine.RuntimeService;
//import org.activiti.engine.TaskService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/activiti")
//public class ActivitiController {
//
//	@Autowired
//	private TaskService taskService;
//	@Autowired
//    private RuntimeService runtimeService;
//	
//	public void start() {
//		this.runtimeService.startProcessInstanceById("");
//	}
//	
//	public void list() {
//		this.taskService.createTaskQuery()
////			.processDefinitionId("") // 任务ID
//			.taskAssignee("") // 负责人
//			.listPage(0, 2);
//	}
//	
//}
