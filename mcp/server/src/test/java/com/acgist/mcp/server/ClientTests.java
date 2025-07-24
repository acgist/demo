package com.acgist.mcp.server;

import java.util.Map;

import org.junit.jupiter.api.Test;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.ListToolsResult;

public class ClientTests {
    
    @Test
    public void testClient() {
        final var transport = HttpClientSseClientTransport.builder("http://localhost:8080").build();
        final var client = McpClient.sync(transport).build();
        client.initialize();
        client.ping();
        final ListToolsResult result = client.listTools();
        System.out.println(result);
        client.callTool(new CallToolRequest("getWeather", Map.of("city", "北京"))).content().forEach(System.out::print);
        client.callTool(new CallToolRequest("getWeather", Map.of("city", "广州"))).content().forEach(System.out::print);
        client.callTool(new CallToolRequest("batchGenerateTitle", Map.of("title", "人工智能"))).content().forEach(System.out::print);
        client.closeGracefully();
    }

}
