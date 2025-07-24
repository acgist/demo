package com.acgist.mcp.client.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @Autowired
    private ChatClient chatClient;

    @RequestMapping("/chat")
    public String chat(@RequestParam(value = "message", defaultValue = "北京天气如何") String message) {
        final String response = this.chatClient.prompt()
            .user(message)
            .call()
            .content();
        return response;
    }

}
