package com.hushijie.springboottest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hushijie.springboottest.*.mapper")
public class SpringboottestDemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringboottestDemoApplication.class, args);
	}
}
