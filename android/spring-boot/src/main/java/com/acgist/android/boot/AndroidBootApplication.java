package com.acgist.android.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class AndroidBootApplication {

    @GetMapping
    public String index() {
        return "hello anroid boot";
    }
    
	public static void main(String[] args) {
		SpringApplication.run(AndroidBootApplication.class, args);
	}

}
