package com.acgist.springboot;

import org.springframework.web.service.annotation.GetExchange;

public interface HttpClient {

    @GetExchange("/")
    String homepage();
    
}
