package com.acgist.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.acgist.bean.Task;

/**
 * 任务管理器
 */
@Component
public class TaskManager {

	@Autowired
	private String taskPath;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskManager.class);
	private static final ExecutorService EXECUTORS = Executors.newFixedThreadPool(10);
	private static final ConcurrentHashMap<String, Task> TASKS = new ConcurrentHashMap<>(100);
	
	@PostConstruct
	public void init() {
		load();
	}
	
	/**
	 * 提交任务
	 */
	public void submit(Task task) {
		LOGGER.info("开始任务，任务ID：{}，任务名称：{}", task.getId(), task.getName());
		serialize(task);
		putTask(task);
		EXECUTORS.execute(new TaskExecutor(task));
	}
	
	public List<Task> list() {
		return TASKS.values().stream().collect(Collectors.toList());
	}
	
	public Task view(String id) {
		return TASKS.get(id);
	}
	
	public void delete(String id) {
		view(id).cancel().delete();
		TASKS.remove(id);
	}
	
	/**
	 * 序列化
	 */
	private void serialize(Task task) {
		task.serialize();
	}
	
	/**
	 * 加载序列化任务
	 */
	private void load() {
		File file = new File(taskPath);
		LOGGER.info("初始化TASK，序列化路径：{}", taskPath);
		File[] tasks = file.listFiles((dir, name) -> {
			return name.endsWith(".json");
		});
		if(tasks == null) {
			return;
		}
		for (File taskFile : tasks) {
			try {
				Task task = Task.read(new String(Files.readAllBytes(Paths.get(taskFile.toURI())), "UTF-8"));
				putTask(task);
			} catch (IOException e) {
			}
		}
	}
	
	/**
	 * 加入TASKS
	 */
	private void putTask(Task task) {
		if(!TASKS.containsKey(task.getId())) {
			TASKS.put(task.getId(), task);
		}
	}
	
}
