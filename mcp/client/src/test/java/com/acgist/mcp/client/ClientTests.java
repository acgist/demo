package com.acgist.mcp.client;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ClientTests {

    @Autowired
    private ChatClient chatClient;
    
    @Test
    public void testChat() {
        final String content = this.chatClient.prompt("查询一下北京的天气").call().content();
//      final String content = this.chatClient.prompt("查询一下广州的天气").call().content();
//      final String content = this.chatClient.prompt("帮我想一些人工智能的标题").call().content();
        System.out.println(content);
    }
    
}
