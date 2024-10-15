package com.eatspan.SpanTasty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpanTastyApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SpanTastyApplication.class, args);
	}

}
