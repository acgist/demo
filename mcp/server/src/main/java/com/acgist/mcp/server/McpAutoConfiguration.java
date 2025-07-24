package com.acgist.mcp.server;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.mcp.server.service.article.ArticleService;
import com.acgist.mcp.server.service.weather.WeatherService;

@Configuration
public class McpAutoConfiguration {

    @Bean
    public ToolCallbackProvider weatherTools(
        ArticleService articleService,
        WeatherService weatherService
    ) {
        return MethodToolCallbackProvider.builder().toolObjects(articleService, weatherService).build();
    }
    
}

