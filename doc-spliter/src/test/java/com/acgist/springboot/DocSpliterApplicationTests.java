package com.acgist.springboot;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.doc.spliter.DocSpliter;
import com.acgist.doc.spliter.DocSpliterApplication;
import com.acgist.doc.spliter.DocSpliters;

@SpringBootTest(classes = DocSpliterApplication.class)
class DocSpliterApplicationTests {

    @Autowired
    private DocSpliters docSpliters;
    
    @Test
    public void testDocSplit() {
        final String file = "C:\\Users\\acgis\\桌面\\附件：广西卓洁电力工程检修有限公司风电场运行规程.pdf";
        final DocSpliter docSpliter = this.docSpliters.getDocSpliter(file);
        final List<String> list = docSpliter.split(file);
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }
    
}
