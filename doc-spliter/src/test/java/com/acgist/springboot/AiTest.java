package com.acgist.springboot;

import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.OllamaEmbeddingClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;

import reactor.core.publisher.Flux;

public class AiTest {

    @Test
    public void testApi() {
        final OllamaApi api = new OllamaApi("http://192.168.8.50:11434");
        final OllamaChatClient client = new OllamaChatClient(
            api, OllamaOptions.create()
            .withModel("glm4")
        );
        final Scanner scanner = new Scanner(System.in);
        while(true) {
            final String line = scanner.nextLine();
            if("/exit".equals(line)) {
                break;
            }
//          final String response = client.call(line);
//          System.out.println(response);
            final Flux<String> stream = client.stream(line);
            stream.doOnNext(x -> {
                System.out.println(new String(x.getBytes()));
            });
        }
        scanner.close();
    }
    
    @Test
    public void testEmbedding() {
        final OllamaApi api = new OllamaApi("http://192.168.8.50:11434");
        final OllamaEmbeddingClient client = new OllamaEmbeddingClient(api)
            .withDefaultOptions(OllamaOptions.create().withModel("quentinz/bge-large-zh-v1.5"));
        final VectorStore vectorStore = new SimpleVectorStore(client);
        vectorStore.add(List.of(
            new Document("北京是中国的首都"),
            new Document("熊猫喜欢竹笋"),
            new Document("熊猫是肉食动物"),
            new Document("春天的竹笋最甜美"),
            new Document("熊猫喜欢春天")
        ));
        System.out.println("向量构建完成");
        final Scanner scanner = new Scanner(System.in);
        while(true) {
            final String line = scanner.nextLine();
            if("/exit".equals(line)) {
                break;
            }
            final List<Document> list = vectorStore.similaritySearch(SearchRequest.query(line).withTopK(4));
            list.forEach(v -> {
                System.out.println(v.getContent());
            });
        }
        scanner.close();
    }
    
}
