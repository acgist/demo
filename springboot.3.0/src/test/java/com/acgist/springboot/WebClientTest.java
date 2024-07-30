package com.acgist.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientTest {

    @Test
    public void testClient() {
        final WebClient client = WebClient.builder().baseUrl("https://www.acgist.com").build();
        final String response = client.get().uri("/")
            // .accept(null)
            .retrieve()
            .bodyToMono(String.class)
            .block();
        System.out.println(response);
    }
    
}
