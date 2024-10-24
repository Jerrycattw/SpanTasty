package com.eatspan.SpanTasty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class FirstController {
	
	
	@Autowired
	private JavaMailSender mailSender;

	@GetMapping("/")
	public String home() {
		return "spantasty/index";
	}
	
	
	
	@GetMapping("/about")
	public String about() {
		return "about";
	}
	
	
	@GetMapping("/StarCups")
	public String starCups() {
		return "starcups/index";
	}
	
	
	
	
	/*
	 * for大家測試email
	 * 可以先去看群組的記事本
	 * 
	 * 1.環境變數設置 MAIL_USERNAME MAIL_PASSWORD
	 * 2.方法內改成自己的email
	 * 3.直接/SpanTasty/testmail即可 收到信!
	 * 會跳500是因為沒有return路徑，但信會成功寄出去(除非是授權失敗表示帳密有錯)
	 * 
	 * 我有寫好發信的模板，可以套用模板發信只需設置內文，可以參考我couponService
	 * 如果只是想簡單可以修改setText就好
	 *
	 * 
	 * */
	@GetMapping("/testmail")
	 public void sendSimpleMail() throws Exception {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setFrom("receipt0210@gmail.com");//誰寄信(你設定的信箱)  要修改!
	        message.setTo("receipt0210@gmail.com");//寄給誰  要修改!
	        message.setSubject("主旨：Hello hao");
	        message.setText("內容：這是一封測試信件，恭喜您成功發送了唷");

	        mailSender.send(message);
	  }
	
	
}
