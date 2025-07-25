package com.acgist.mcp.client;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpAutoConfiguration {
    
    @Bean
    public ChatClient chatClient(
        OllamaChatModel      ollamaChatModel,
        OllamaEmbeddingModel ollamaEmbeddingModel,
        ToolCallbackProvider toolCallbackProvider
    ) {
        // 注意：测试使用
        final SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(ollamaEmbeddingModel).build();
        simpleVectorStore.add(List.of(
            new Document("常用的开发语言：C++/Java/Python/JavaScript/acgist"),
            new Document("流体电池最大电压：5V")
        ));
        // 注意：理论上来说这个应该和对话是一对一的
        return ChatClient.builder(ollamaChatModel)
            .defaultAdvisors(
                // 历史会话
                MessageChatMemoryAdvisor.builder(
                    MessageWindowChatMemory.builder()
                        .chatMemoryRepository(new InMemoryChatMemoryRepository())
                        .maxMessages(10)
                        .build()
                ).build(),
//              // RAG
                QuestionAnswerAdvisor.builder(simpleVectorStore)
                .searchRequest(SearchRequest.builder().similarityThreshold(0.8D).topK(6).build())
                .build()
            )
            .defaultToolCallbacks(toolCallbackProvider)
//          .defaultToolCallbacks(toolCallbackProvider.getToolCallbacks())
            .defaultSystem("你是一个智能助手")
            .build();
    }
    
}
