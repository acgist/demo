package com.acgist.mcp.server;

import java.util.Map;

import org.junit.jupiter.api.Test;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.GetPromptRequest;
import io.modelcontextprotocol.spec.McpSchema.GetPromptResult;
import io.modelcontextprotocol.spec.McpSchema.ReadResourceRequest;
import io.modelcontextprotocol.spec.McpSchema.ReadResourceResult;

public class ClientTests {
    
    @Test
    public void testTool() {
        final var transport = HttpClientSseClientTransport.builder("http://localhost:8080").build();
        final var client = McpClient.sync(transport).build();
        client.initialize();
        client.ping();
        final var result = client.listTools();
        System.out.println(result);
        System.out.println("====");
        client.callTool(new CallToolRequest("getWeather", Map.of("city", "北京"))).content().forEach(System.out::println);
        System.out.println("====");
        client.callTool(new CallToolRequest("getWeather", Map.of("city", "广州"))).content().forEach(System.out::println);
        System.out.println("====");
        client.callTool(new CallToolRequest("batchGenerateTitle", Map.of("title", "人工智能"))).content().forEach(System.out::println);
        client.closeGracefully();
    }
    
    @Test
    public void testPrompt() {
        final var transport = HttpClientSseClientTransport.builder("http://localhost:8080").build();
        final var client = McpClient.sync(transport).build();
        client.initialize();
        client.ping();
        final var result = client.listPrompts();
        System.out.println(result);
        final GetPromptResult prompt = client.getPrompt(new GetPromptRequest("测试提示", Map.of("name", "阿胜")));
        prompt.messages().forEach(System.out::println);
        client.closeGracefully();
    }
    
    @Test
    public void testResource() {
        final var transport = HttpClientSseClientTransport.builder("http://localhost:8080").build();
        final var client = McpClient.sync(transport).build();
        client.initialize();
        client.ping();
        final var result = client.listResources();
        System.out.println(result);
        final ReadResourceResult resource = client.readResource(new ReadResourceRequest("/data/txt/测试.txt"));
        resource.contents().forEach(System.out::println);
        client.closeGracefully();
    }

}
