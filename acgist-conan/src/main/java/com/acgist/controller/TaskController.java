package com.acgist.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acgist.bean.Filter;
import com.acgist.bean.Source;
import com.acgist.bean.Task;
import com.acgist.bean.Task.Match;
import com.acgist.core.TaskManager;

@Controller
@RequestMapping("/task")
public class TaskController {

	private static final String KEYS_SPLIT = " "; // 关键词分割
	
	@Autowired
	private String taskPath;
	@Autowired
	private TaskManager taskManager;
	
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Task> list() {
		return taskManager.list();
	}

	@ResponseBody
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public Task view(String id) {
		return taskManager.view(id);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(String id) {
		taskManager.delete(id);
		return "redirect:/";
	}
	
	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public String cancel(String id) {
		taskManager.view(id).cancel();
		return "redirect:/";
	}
	
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String submit(String name, String match, Integer page, String keys,
			String[] sources, String[] filters) {
		List<String> keysList = Stream.of(keys.split(KEYS_SPLIT))
			.filter(key -> key != null && !key.trim().isEmpty())
			.map(key -> key.trim())
			.collect(Collectors.toList());
		List<Source> sourcesList = Stream.of(sources)
			.map(source -> Source.valueOf(source))
			.collect(Collectors.toList());
		List<Filter> filtersList = Stream.of(filters)
			.map(filter -> Filter.valueOf(filter))
			.collect(Collectors.toList());
		Task task = Task.build(taskPath, name, Match.valueOf(match), page, keysList, sourcesList, filtersList);
		if(task == null) {
			return "redirect:/html/task.html";
		}
		taskManager.submit(task);
		return "redirect:/html/task.html?id=" + task.getId();
	}
	
}
