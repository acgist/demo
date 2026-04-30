package com.acgist.android.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.acgist")
@SpringBootApplication
public class Boot {

    public static void boot() {
        SpringApplication.run(Boot.class, new String[]{ });
    }

}
