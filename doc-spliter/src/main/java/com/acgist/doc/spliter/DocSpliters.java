package com.acgist.doc.spliter;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DocSpliters {

    private final ApplicationContext context;
    
    /**
     * @param file 文件
     * 
     * @return 知识库切割机
     */
    public DocSpliter getDocSpliter(String file) {
        final Map<String, DocSpliter> map = this.context.getBeansOfType(DocSpliter.class);
        return map.values().stream().filter(v -> v.isSupport(file)).findFirst().orElseThrow();
    }
    
}
