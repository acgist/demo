package com.acgist.doc.spliter;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "doc.spliter.output")
public class DocSpliterProperties {

    private String path;
    
}
