package com.acgist.doc.spliter;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    DocSpliterProperties.class
})
public class BootConfiguration {
    
}
