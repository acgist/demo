package com.acgist.mcp.client.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    @Autowired
    private ChatClient chatClient;

    @RequestMapping("/chat")
    public String chat(@RequestParam(value = "message", defaultValue = "北京天气如何") String message) {
        return this.chatClient.prompt()
            .user(message)
            .call()
            .content();
    }
    
    @RequestMapping("/chat/stream")
    public Flux<String> chatStream(@RequestParam(value = "message", defaultValue = "北京天气如何") String message) {
        // 注意：这里存在BUG不会自动处理tool
        return this.chatClient.prompt()
            .user(message)
            .stream()
            .content();
    }

}
