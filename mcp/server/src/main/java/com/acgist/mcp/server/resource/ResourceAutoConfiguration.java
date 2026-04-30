package com.acgist.mcp.server.resource;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ResourceAutoConfiguration {

    @Bean
    public List<McpServerFeatures.SyncResourceSpecification> resources() {
        final var testResource      = new McpSchema.Resource("/data/txt/测试.txt", "测试文档", "测试文档", "text/plain", null);
        final var testSpecification = new McpServerFeatures.SyncResourceSpecification(testResource, (exchange, request) -> {
            log.info("调用资源接口");
            return new McpSchema.ReadResourceResult(List.of(new McpSchema.TextResourceContents(request.uri(), "text/plain", "这是一段用于测试的内容！")));
        });
        return List.of(testSpecification);
    }
    
}
