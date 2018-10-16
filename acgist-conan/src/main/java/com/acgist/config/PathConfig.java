package com.acgist.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 路径配置
 */
@Configuration
public class PathConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(PathConfig.class);
	
	@Value("${system.task.path:/data/task}")
	private String taskPath;
	
	/**
	 * 任务路径
	 */
	@Bean
	public String taskPath() {
		String taskPath;
		String classesPath = rootPath();
		if(this.taskPath.startsWith("/")) {
			taskPath = classesPath + this.taskPath.substring(1);
		} else {
			taskPath = classesPath + this.taskPath;
		}
		LOGGER.info("taskPath：{}", taskPath);
		return taskPath;
	}
	
	private static final String rootPath() {
		String path = PathConfig.class.getResource("/").getFile();
		while(true) {
			if(path.indexOf(".jar") >= 0) {
				path = path.substring(0, path.lastIndexOf("/"));
			} else {
				break;
			}
		}
		if(!path.endsWith("/")) {
			path = path + "/";
		}
		if(path.startsWith("file:")) {
			path = path.substring("file:".length());
		}
		LOGGER.info("rootPath：{}", path);
		return path;
	}
	
}
