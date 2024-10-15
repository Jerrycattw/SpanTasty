package com.eatspan.SpanTasty.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@PropertySource("mail.properties")
public class MailConfig {
	
	@Value("${userName}")
	private String userName;
	
	@Value("${userName2}")
	private String userName2;
	
}
