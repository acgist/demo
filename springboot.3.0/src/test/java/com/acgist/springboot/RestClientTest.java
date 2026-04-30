package com.acgist.springboot;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestClient;

public class RestClientTest {

    @Test
    public void testClient() {
        final RestClient client = RestClient.builder().baseUrl("https://www.acgist.com")
//        .requestFactory(null)
//        .defaultHeader(null, null)
//        .requestInterceptor(null)
            .messageConverters((list) -> {
                list.forEach(v -> {
                    if(v instanceof StringHttpMessageConverter x) {
                        x.setDefaultCharset(StandardCharsets.UTF_8);
                    }
                });
            })
        .build();
        System.out.println(client.get().retrieve().body(String.class));
    }
    
}
