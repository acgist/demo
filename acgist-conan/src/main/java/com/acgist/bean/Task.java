package com.acgist.bean;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties({ "running", "filePath" })
public class Task {

	private static final Logger LOGGER = LoggerFactory.getLogger(Task.class);
	
	// 匹配模式
	public enum Match {
		all, // 全匹配
		part; // 部分匹配
	}

	public enum State {
		ready, // 准备中
		running, // 运行中
		over, // 已完成
		cancel; // 已取消
	}

	private String id; // ID
	private String name; // 名称
	private Match match; // 匹配模式
	private State state; // 状态
	private Integer page; // 页数
	private String filePath; // 序列化路径
	private String createDate; // 创建时间
	private Integer processed; // 已处理
	private Integer totalPage; // 总页数
	private List<String> keys; // 关键词
	private List<Source> sources; // 搜索引擎
	private List<Filter> filters; // 过滤模式
	private List<TaskFollow> taskFollows; // 任务结果

	private Task() {
		this.id = UUID.randomUUID().toString();
		this.processed = 0;
		this.createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd HH:mm"));
		this.state = State.ready;
		this.taskFollows = new ArrayList<>();
	}

	public static final Task build(String taskPath, String name, Match match, Integer page,
			List<String> keys, List<Source> sources, List<Filter> filters) {
		if(keys == null || keys.isEmpty()) {
			return null;
		}
		if(sources == null || sources.isEmpty()) {
			return null;
		}
		if(filters == null || filters.isEmpty()) {
			filters = List.of(Filter.nofilter);
		}
		Task task = new Task();
		task.name = name;
		task.match = match;
		task.page = page;
		task.keys = keys;
		task.sources = sources;
		task.filters = filters;
		task.totalPage = sources.size() * page + 10 * sources.size() * page; // 分页数 + 内容页数
		task.filePath(taskPath);
		return task;
	}
	
	/**
	 * 融合TaskFollow
	 */
	public synchronized void combineTaskFollow(List<TaskFollow> list) {
		TaskFollow.combine(this.taskFollows, list);
		this.taskFollows.sort((a, b) -> {
			return b.getTimes().compareTo(a.getTimes());
		});
	}

	public synchronized void addProcessed() {
		this.processed++;
	}
	
	/**
	 * 开始运行
	 */
	public Task run() {
		this.setState(State.running);
		return this;
	}
	
	/**
	 * 完成
	 */
	public Task complete() {
		if(this.getState() == State.running) {
			this.setState(State.over);
			this.setProcessed(this.getTotalPage());
		}
		return this;
	}
	
	/**
	 * 取消
	 */
	public Task cancel() {
		if(this.getState() == State.running) {
			this.setState(State.cancel);
		}
		return this;
	}
	
	/**
	 * 删除
	 */
	public Task delete() {
		File file = new File(this.filePath);
		if(file.exists() && file.isFile()) {
			file.delete();
		}
		return this;
	}

	/**
	 * 序列化
	 */
	public Task serialize() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File(this.filePath), this);
		} catch (IOException e) {
			LOGGER.error("TASK序列化异常", e);
		}
		return this;
	}
	
	/**
	 * 是否允许状态
	 */
	public boolean isRunning() {
		return this.getState() == State.running;
	}
	
	/**
	 * 获取文件路径
	 */
	private void filePath(String taskPath) {
		File folder = new File(taskPath);
		if(!folder.exists()) {
			folder.mkdirs();
		}
		String filePath;
		if(taskPath.endsWith("/")) {
			filePath = taskPath + this.getId() + ".json";
		} else {
			filePath = taskPath + "/" + this.getId() + ".json";
		}
		this.filePath = filePath;
	}
	
	/**
	 * 反序列化
	 */
	public static final Task read(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, Task.class);
		} catch (IOException e) {
			LOGGER.error("反序列化异常，JSON字符串：{}", json, e);
		}
		return null;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Integer getProcessed() {
		return processed;
	}

	public void setProcessed(Integer processed) {
		this.processed = processed;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public List<String> getKeys() {
		return keys;
	}

	public void setKeys(List<String> keys) {
		this.keys = keys;
	}

	public List<Source> getSources() {
		return sources;
	}

	public void setSources(List<Source> sources) {
		this.sources = sources;
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}

	public List<TaskFollow> getTaskFollows() {
		return taskFollows;
	}

	public void setTaskFollows(List<TaskFollow> taskFollows) {
		this.taskFollows = taskFollows;
	}

}
