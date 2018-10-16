package com.acgist.core;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.bean.Task;
import com.acgist.data.TaskFollowFoodie;
import com.acgist.data.TextFoodie;
import com.acgist.html.HTMLFoodie;

/**
 * 任务执行器
 */
public class TaskExecutor implements Runnable {

	private Task task;
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskExecutor.class);
	
	public TaskExecutor(Task task) {
		this.task = task;
	}

	public void execute() {
		TextFoodie.keys(this.task.getKeys());
		this.links().parallelStream()
			.map(link -> {
				this.task.addProcessed(); // 已处理+1
				if(this.task.isRunning()) {
					TaskFollowExecutor executor = new TaskFollowExecutor(this.task, link);
					return executor.execute();
				}
				return null;
			})
			.filter(list -> list != null)
			.forEach(list -> {
				task.combineTaskFollow(list);
			});
		TaskFollowFoodie.clean(task);
		this.task.complete().serialize();
		LOGGER.info("任务完成，任务ID：{}，任务名称：{}", this.task.getId(), this.task.getName());
	}

	/**
	 * 获取所有内容页链接
	 */
	public List<String> links() {
		List<String> links = new LinkedList<>();
		this.task.run().getSources().parallelStream().forEach(source -> {
			String searchUrl = source.searchUrl(this.task.getKeys());
			LOGGER.info("{}-{}-初始种子页面链接：{}", this.task.getName(), source.name(), searchUrl);
			HTMLFoodie sourceFoodie = HTMLFoodie.html(searchUrl);
			List<String> pages = sourceFoodie.queryHref(source.getPageQuery()); // 分页链接
			if(!pages.contains(searchUrl)) {
				pages.add(searchUrl);
			}
			AtomicInteger pageIndex = new AtomicInteger(0);
			LOGGER.info("{}-获取分页链接：{}", this.task.getName(), pages);
			pages.parallelStream().forEach(page -> {
				if(pageIndex.get() < this.task.getPage()) {
					pageIndex.incrementAndGet();
					this.task.addProcessed(); // 已处理+1
					HTMLFoodie pageFoodie = HTMLFoodie.html(page);
					pageFoodie.queryHref(source.getLinkQuery()).forEach(value -> {
						if(!links.contains(value)) {
							links.add(value);
						}
					});
					LOGGER.info("{}-获取内容页链接，当前页码：{}", this.task.getName(), pageIndex.get());
				}
			});
		});
		LOGGER.info("{}-内容页数量：{}", this.task.getName(), links.size());
		return links;
	}
	
	@Override
	public void run() {
		this.execute();
	}
	
}
