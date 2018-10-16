package com.acgist.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import com.acgist.bean.Filter;
import com.acgist.bean.Source;
import com.acgist.bean.Task;
import com.acgist.bean.Task.Match;
import com.acgist.bean.Task.State;
import com.acgist.core.TaskManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication
@ComponentScan(basePackages = { "com.acgist" })
public class TaskManagerTest {
	
	@Autowired
	private TaskManager manager;

	@Test
	public void main() throws JsonProcessingException, InterruptedException {
		SpringApplication.run(TaskManagerTest.class, new String[] {});
		Task task = Task.build("/data/task", "测试", Match.all, 1, List.of("碧螺萧萧", ""), List.of(Source.baidu), List.of(Filter.nofilter));
		manager.submit(task);
		while(true) {
			System.out.println(task.getState());
			if(task.getState() == State.over) {
				ObjectMapper mapper = new ObjectMapper();
				System.out.println(mapper.writeValueAsString(task));
				break;
			}
			Thread.sleep(200);
		}
	}

	@Test
	public void list() {
		manager.list().forEach(value -> {
			System.out.println(value.getId());
			System.out.println(value.getName());
		});
	}
	
}
