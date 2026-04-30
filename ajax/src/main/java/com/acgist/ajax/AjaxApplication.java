package com.acgist.ajax;

import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class AjaxApplication {

	public static void main(String[] args) {
		SpringApplication.run(AjaxApplication.class, args);
	}

	@Configuration
	class WebConfig implements WebMvcConfigurer {
		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**")
				.allowedOrigins("https://testjd.com:8080", "https://testtaobao.com:8080")
				.allowCredentials(true)
				.allowedHeaders("*")
				.allowedMethods("*")
				.maxAge(1800);
		}
	}
	
	@Configuration
	class TomcatConfiguration {
		@Bean
		public TomcatContextCustomizer sameSiteCookiesConfig() {
			return context -> {
				final Rfc6265CookieProcessor cookieProcessor = new Rfc6265CookieProcessor();
				cookieProcessor.setSameSiteCookies(SameSiteCookies.NONE.getValue());
				context.setCookieProcessor(cookieProcessor);
			};
		}
	}
	
}
