package com.acgist.springboot;

import java.nio.charset.StandardCharsets;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.client.RestClientBuilderConfigurer;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.client.support.RestTemplateAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class BootConfiguration {

    @Bean
    @ConditionalOnMissingBean
    RestClient.Builder restClientBuilder(RestClientBuilderConfigurer restClientBuilderConfigurer) {
        final RestClient.Builder builder = RestClient.builder()
            .baseUrl("https://www.acgist.com")
            .requestFactory(ClientHttpRequestFactories.get(ClientHttpRequestFactorySettings.DEFAULTS));
        return restClientBuilderConfigurer.configure(builder);
    }
    
    @Bean
    public HttpClient httpClient(RestClient.Builder builder) {
        final RestClient client = builder.build();
        final RestClientAdapter adapter = RestClientAdapter.create(client);
        final HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(HttpClient.class);
    }
    
    @Bean
    public HttpClient httpClientWeb() {
        final WebClient client = WebClient.builder().baseUrl("https://www.acgist.com").build();
        final WebClientAdapter adapter = WebClientAdapter.create(client);
        final HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(HttpClient.class);
    }
    
    @Bean
    public HttpClient httpClientRest() {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("https://www.acgist.com"));
        restTemplate.getMessageConverters().forEach(v -> {
            if(v instanceof StringHttpMessageConverter x) {
                x.setDefaultCharset(StandardCharsets.UTF_8);
            }
        });
        final RestTemplateAdapter adapter = RestTemplateAdapter.create(restTemplate);
        final HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(HttpClient.class);
    }
    
}
