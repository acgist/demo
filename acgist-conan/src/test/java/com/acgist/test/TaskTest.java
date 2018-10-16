package com.acgist.test;

import java.io.IOException;

import org.junit.Test;

import com.acgist.bean.Task;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TaskTest {

	@Test
	public void json() throws JsonParseException, JsonMappingException, IOException {
		String json = "{\"id\":\"73e122df-235f-4873-8713-e4c72dc800df\",\"name\":\"测试\",\"match\":\"all\",\"state\":\"over\",\"page\":1,\"processed\":11,\"totalPage\":11,\"keys\":[\"碧螺萧萧\",\"\"],\"sources\":[\"baidu\"],\"taskFollows\":[{\"value\":\"百度\",\"times\":7}]}";
		ObjectMapper mapper = new ObjectMapper();
//		JavaType inner = mapper.getTypeFactory().constructParametricType(TaskFollow.class);
//		JavaType javaType = mapper.getTypeFactory().constructParametricType(Task.class, TaskFollow.class);
		Task task = mapper.readValue(json, Task.class);
		System.out.println(task.getId());
		System.out.println(task.getName());
		System.out.println(task.getState());
		System.out.println(task.getPage());
		System.out.println(task.getProcessed());
		System.out.println(task.getTotalPage());
		System.out.println(task.getMatch());
		System.out.println(task.getSources());
		task.getTaskFollows().forEach(value -> {
			System.out.println(value.getValue() + "-" + value.getTimes());
		});
	}
	
}
