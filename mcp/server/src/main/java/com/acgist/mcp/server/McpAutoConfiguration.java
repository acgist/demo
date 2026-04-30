package com.acgist.mcp.server;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.mcp.server.tool.article.ArticleTool;
import com.acgist.mcp.server.tool.weather.WeatherTool;

@Configuration
public class McpAutoConfiguration {

    @Bean
    public ToolCallbackProvider weatherTools(
        ArticleTool articleTool,
        WeatherTool weatherTool
    ) {
        return MethodToolCallbackProvider.builder().toolObjects(articleTool, weatherTool).build();
    }
    
}

