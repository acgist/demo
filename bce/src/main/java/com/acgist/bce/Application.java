package com.acgist.bce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        if(!Encrypt.encryptMain(args)) {
            return;
        }
        final SpringApplication application = new SpringApplicationBuilder(Application.class).build();
        if(Encrypt.decrypt) {
            Encrypt.customResourceLoader(application);
        }
        application.run(args);
    }
    
}
