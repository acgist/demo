package com.acgist.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootApplicationTests {

    @Autowired
    private HttpClient httpClient;
    @Autowired
    private HttpClient httpClientWeb;
    @Autowired
    private HttpClient httpClientRest;
    
	@Test
	void contextLoads() {
	    final String homepage = this.httpClient.homepage();
	    System.out.println(homepage.length());
	    final String homepageWeb = this.httpClientWeb.homepage();
	    System.out.println(homepageWeb.length());
	    final String homepageRest = this.httpClientRest.homepage();
	    System.out.println(homepageRest.length());
	}

}
