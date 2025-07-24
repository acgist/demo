package com.acgist.mcp.server;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaApi.ChatRequest;
import org.springframework.ai.ollama.api.OllamaApi.ChatResponse;
import org.springframework.ai.ollama.api.OllamaApi.Message;
import org.springframework.ai.ollama.api.OllamaApi.Message.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OllamaTests {

    @Autowired
    private OllamaApi ollamaApi;

    @Test
    public void testOllama() {
        final ChatRequest request = ChatRequest.builder("deepseek-r1:7b").messages(List.of(
            Message.builder(Role.USER).content("你好").build()
        )).build();
        final ChatResponse response = this.ollamaApi.chat(request);
        System.out.println(response.message().content());
    }
    
}
