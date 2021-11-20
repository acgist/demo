package com.acgist.springboot;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.acgist.controller.EmailController;

@RunWith(SpringRunner.class)
@SpringBootConfiguration
public class SpringbootApplicationTests {

	private MockMvc mvc;
	
	@Before
	public void init() {
		mvc = MockMvcBuilders.standaloneSetup(new EmailController()).build();
	}
	
	@Test
	public void test() throws Exception {
		String content = mvc.perform(MockMvcRequestBuilders.get("/book").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
//				.andExpect(MockMvcResultMatchers.jsonPath("$.name", ))
				.andExpect(content().string("hello world"))
				.andExpect(content().string(IsEqual.equalTo("hello world")))
				.andReturn().getResponse().getContentAsString();
		System.out.println(content);
	}

}
