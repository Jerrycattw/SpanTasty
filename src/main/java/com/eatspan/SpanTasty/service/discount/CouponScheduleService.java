package com.eatspan.SpanTasty.service.discount;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.eatspan.SpanTasty.dto.discount.CouponScheduleDTO;
import com.eatspan.SpanTasty.entity.discount.CouponMember;
import com.eatspan.SpanTasty.entity.discount.CouponSchedule;
import com.eatspan.SpanTasty.repository.discount.CouponMemberRepository;
import com.eatspan.SpanTasty.repository.discount.CouponScheduleRepository;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class CouponScheduleService {
	
	@Autowired
	private CouponScheduleRepository couponScheduleRepo;
	
	@Autowired
	private CouponMemberRepository couponMemberRepo;
	
	@Autowired
	private JavaMailSender mailSender;// javaMail要注入----------------------------
	
	@Autowired
	private freemarker.template.Configuration freemarkerConfig; // javaMail要注入----------------------------
		
	
	@Scheduled(fixedRate = 30000)//每半分鐘執行
	public void processCouponSchedules() throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, MessagingException, IOException, TemplateException {
		LocalDateTime now = LocalDateTime.now();
		List<CouponSchedule> scheduleToProcess = couponScheduleRepo.findCouponScheduleByStausAndBeforTime("pending", now);
		
		for (CouponSchedule schedule : scheduleToProcess) {
			//寫入優惠券資料庫
			CouponMember couponMember = schedule.getCouponMember();
			couponMember.incrementAmounts();
			couponMemberRepo.save(couponMember);
			
			//寄信
			String memberName = couponMember.getMember().getMemberName();
			String memberEmail = couponMember.getMember().getEmail();
			sendMail(memberName, memberEmail);
			
			//修改schedule狀態
			schedule.setProcessTime(now);
			schedule.setStatus("complete");
			
			couponScheduleRepo.save(schedule);	
		}	
	}
	
	
	public void sendMail(String memberName,String memberEmail) throws MessagingException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper  helper = new MimeMessageHelper(mimeMessage,true);
		//設置mail
		helper.setFrom("receipt0210@gmail.com");//誰寄信(application設定的信箱)
		helper.setTo(memberEmail);//誰收信
		helper.setSubject("【☕週年靜加碼】starcups 全館不限享多種折扣");//主旨
		
		//設置模板
		//設置model
		Map<String, Object> model = new HashMap<String,Object>();
		//透過modal傳入的物件("參數名","東西")
		model.put("userName",memberName);
		//get模板，並將modal傳入模板
		String templateString = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfig.getTemplate("couponMail.html"), model);
		
		//設置mail內文
		helper.setText(templateString,true);
		
		//設置資源，順序要在內文之後
		FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/images/mail/logo-starcups.png"));
		helper.addInline("logo",file);
		
		mailSender.send(mimeMessage);	
	}
	
	private List<CouponScheduleDTO> convertDTO(List<CouponSchedule> couponSchedules){
		return couponSchedules.stream().map(couponSchedule->convertDTO(couponSchedule)).collect(Collectors.toList());
	}
	
	private CouponScheduleDTO convertDTO(CouponSchedule couponSchedule) {
		return new CouponScheduleDTO(
				couponSchedule.getScheduleId(),
				couponSchedule.getScheduleName(),
				couponSchedule.getCouponMember().getCouponMemberId().getCouponId(),
				couponSchedule.getCouponMember().getCouponMemberId().getMemberId(),
				couponSchedule.getScheduleTime(),
				couponSchedule.getCreateTime(),
				couponSchedule.getProcessTime(),
				couponSchedule.getStatus()
				);
				
		
	}
	
	
	
	public List<CouponScheduleDTO> findAllCouponSchedules(){
		return convertDTO(couponScheduleRepo.findAll());
	}
	
	public CouponSchedule findCouponScheduleById(Integer scheduleId) {
		Optional<CouponSchedule> optional = couponScheduleRepo.findById(scheduleId);
		
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	
	public void updateCouponSchedule(CouponSchedule couponSchedule) {
		Optional<CouponSchedule> optional = couponScheduleRepo.findById(couponSchedule.getScheduleId());
		if (optional.isPresent()) {
			CouponSchedule result = optional.get();
			result.setProcessTime(couponSchedule.getProcessTime());
			result.setStatus(couponSchedule.getStatus());
			couponScheduleRepo.save(result);
		}
	}
	
}
