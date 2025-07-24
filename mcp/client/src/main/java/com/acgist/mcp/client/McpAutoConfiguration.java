package com.acgist.mcp.client;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpAutoConfiguration {

    @Bean
    public ChatClient chatClient(OllamaChatModel ollamaChatModel, ToolCallbackProvider toolCallbackProvider) {
        return ChatClient.builder(ollamaChatModel)
            .defaultToolCallbacks(toolCallbackProvider.getToolCallbacks())
            .defaultSystem("你是一个智能助手")
            .build();
    }
    
}
