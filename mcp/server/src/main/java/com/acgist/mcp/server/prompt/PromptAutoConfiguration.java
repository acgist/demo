package com.acgist.mcp.server.prompt;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.GetPromptResult;
import io.modelcontextprotocol.spec.McpSchema.PromptMessage;
import io.modelcontextprotocol.spec.McpSchema.Role;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class PromptAutoConfiguration {

    @Bean
    public List<McpServerFeatures.SyncPromptSpecification> prompts() {
        final var testPrompt = new McpSchema.Prompt(
            "测试提示",
            "测试提示描述",
            List.of(new McpSchema.PromptArgument("测试提示", "测试提示描述", true))
        );
        final var testSpecification = new McpServerFeatures.SyncPromptSpecification(testPrompt, (exchange, request) -> {
            log.info("调用提示接口");
            String name = (String) request.arguments().get("name");
            if (name == null) {
                name = "陌生人";
            }
            final var userMessage = new PromptMessage(Role.USER, new TextContent("你好 " + name + "！有什么我能帮助你的吗？"));
            return new GetPromptResult("这是自动生成的一段提示词", List.of(userMessage));
        });
        return List.of(testSpecification);
    }
    
}
