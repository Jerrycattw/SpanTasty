package com.eatspan.SpanTasty.service.rental;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.eatspan.SpanTasty.config.MailConfig;
import com.eatspan.SpanTasty.entity.rental.Rent;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class OvertimeRentSendMailService {
	
	
	@Autowired
	private RentService rentService;
	@Autowired
	private MailConfig mailConfig;// javaMail要注入
	@Autowired
	private JavaMailSender mailSender;// javaMail要注入
	@Autowired
	private freemarker.template.Configuration freemarkerConfig; // javaMail要注入
	
	
	// 每天早上8點發送超時信
	@Scheduled(cron = "0 0 8 * * ?")
	public void findOvertimeRents() throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, MessagingException, IOException, TemplateException {
		List<Rent> overtimeRents = rentService.findByDueDateTomorrow();
		
		for(Rent rent: overtimeRents) {
			sendMail(rent);
		}
	}
	
	
	// 發信
	public void sendMail(Rent rent) throws MessagingException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
		//設置mail
		helper.setFrom(mailConfig.getUserName4());//誰寄信(application設定的信箱)
		helper.setTo(rent.getMember().getEmail());//誰收信
		helper.setSubject("【☕租借到期通知】您在 starcups 的租借訂單即將到期");//主旨
		
		//設置模板
		//設置model
		Map<String, Object> model = new HashMap<String,Object>();
		//透過modal傳入的物件("參數名","東西")
		model.put("rent", rent);
		//get模板，並將modal傳入模板
		String templateString = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfig.getTemplate("rentOvertimeMail.html"), model);
		
		//設置mail內文
		helper.setText(templateString,true);
		
		//設置資源，順序要在內文之後
		FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/images/mail/logo-starcups.png"));
		helper.addInline("logo",file);
		
		mailSender.send(mimeMessage);	
	}
}
