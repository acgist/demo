package com.acgist.dd;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.acgist.dd.datasource.DynamicDatasource;
import com.acgist.dd.datasource.service.DatasourceService;

@SpringBootTest
class DdApplicationTests {

    @Autowired
    private DatasourceService datasourceService;
    @Autowired
    private DynamicDatasource dynamicDatasource;
    
	@Test
	void test() {
//	    this.datasourceService.buildTable();
	    this.datasourceService.insert();
	    this.datasourceService.select();
	    this.datasourceService.update();
	    this.datasourceService.select();
	    this.datasourceService.delete();
	    this.datasourceService.select();
	    this.dynamicDatasource.executeForDataId("mcp-1", dataId -> {
//	        this.datasourceService.buildTable();
	        final boolean remove = this.dynamicDatasource.set("mcp-1");
	        System.out.println(remove);
	        this.datasourceService.insert();
	        this.datasourceService.select();
	        this.datasourceService.update();
	        this.datasourceService.select();
	        this.datasourceService.delete();
	        this.datasourceService.select();
	        return true;
	    });
	    this.dynamicDatasource.executeForDataId("mcp-2", dataId -> {
//	        this.datasourceService.buildTable();
	        final boolean remove = this.dynamicDatasource.set("mcp-2");
            System.out.println(remove);
	        this.datasourceService.insert();
	        this.datasourceService.select();
	        this.datasourceService.update();
	        this.datasourceService.select();
	        this.datasourceService.delete();
	        this.datasourceService.select();
	        return true;
	    });
	    this.dynamicDatasource.executeForEachDataId(dataId -> {
//          this.datasourceService.buildTable();
            this.datasourceService.insert();
            this.datasourceService.select();
            this.datasourceService.update();
            this.datasourceService.select();
            this.datasourceService.delete();
            this.datasourceService.select();
            return true;
	    });
	}

}
