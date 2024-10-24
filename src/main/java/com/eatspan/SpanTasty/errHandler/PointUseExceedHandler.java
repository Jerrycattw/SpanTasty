package com.eatspan.SpanTasty.errHandler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PointUseExceedHandler {
	
	@ExceptionHandler(PointUseExceedException.class)
	public String pointUseExceedHandler() {
		return "/spantasty/errHandler/pointUseExceed";
	}
	
}
