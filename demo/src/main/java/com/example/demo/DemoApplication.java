package com.example.demo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.http.HttpClient;

@SpringBootApplication
public class DemoApplication {
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

//	@Bean
//	public WebMvcConfigurer configure(){
//		return new WebMvcConfigurer() {
//			public void addCorsMapping(CorsRegistry reg){
//				reg.addMapping("/**").allowedOrigins("*").allowedMethods("GET","POST","PUT","DELETE");
//			}
//		};
//	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}


}
