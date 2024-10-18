package com.eatspan.SpanTasty.service.reservation;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.eatspan.SpanTasty.config.MailConfig;
import com.eatspan.SpanTasty.dto.reservation.ReserveCheckDTO;
import com.eatspan.SpanTasty.dto.reservation.TimeSlotDTO;
import com.eatspan.SpanTasty.entity.account.Member;
import com.eatspan.SpanTasty.entity.reservation.Reserve;
import com.eatspan.SpanTasty.entity.reservation.Restaurant;
import com.eatspan.SpanTasty.entity.reservation.TableType;
import com.eatspan.SpanTasty.repository.reservation.ReserveRepository;
import com.eatspan.SpanTasty.repository.reservation.RestaurantRepository;
import com.eatspan.SpanTasty.repository.reservation.TableTypeRepository;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class ReserveService {
	
	@Autowired
	private ReserveRepository reserveRepository;
	@Autowired
	private RestaurantRepository restaurantRepository;
	@Autowired
	private TableTypeRepository tableTypeRepository;
	
	@Autowired
	private MailConfig mailConfig;// javaMail要注入----------------------------
	
	@Autowired
	private JavaMailSender mailSender;// javaMail要注入----------------------------
	
	@Autowired
	private freemarker.template.Configuration freemarkerConfig; // javaMail要注入----------------------------
	
	// 新增訂位
	public Reserve addReserve(Reserve reserve) {
		
		reserve.setFinishedTime(reserve.getReserveTime().plusMinutes(reserve.getRestaurant().getEattime()));
		
		return reserveRepository.save(reserve);
	}
	
	
	// 刪除訂位
	public void deleteReserve(Integer reserveId) {
		reserveRepository.deleteById(reserveId);
	}
	
	
	// 更新訂位
	@Transactional
	public Reserve updateReserve(Reserve reserve) {
		Optional<Reserve> optional = reserveRepository.findById(reserve.getReserveId());
		if(optional.isPresent()) {
			reserve.setFinishedTime(reserve.getReserveTime().plusMinutes(reserve.getRestaurant().getEattime()));
			return reserveRepository.save(reserve);
		}
		return null;
	}
	
	
	// 查詢訂位byId
	public Reserve findReserveById(Integer reserveId) {
		return reserveRepository.findById(reserveId).orElse(null);
	}

	
	// 查詢所有訂位
	public List<Reserve> findAllReserves() {
		return reserveRepository.findAll();
	}
	
	
	// 查詢會員所有訂位
	public List<Reserve> findByMember(Member member) {
		return reserveRepository.findByMemberOrderByReserveTimeDesc(member);
	}
	
	
	
	// 查詢訂位by可變條件
	public List<Reserve> findReserveByCriteria(String memberName, String phone, Integer restaurantId, String tableTypeId, LocalDateTime reserveTimeStart, LocalDateTime reserveTimeEnd){
		return reserveRepository.findReserveByCriteria(memberName, phone, restaurantId, tableTypeId, reserveTimeStart, reserveTimeEnd);
	}
	
	
	// 查詢餐廳某日所有訂位
	public List<Reserve> findReserveByRestaurantAndDate(Integer restaurantId, LocalDate checkDate){
		return reserveRepository.findReserveByRestaurantAndDate(restaurantId, checkDate);
	}
	
	
	
	// 查詢餐廳該天所有時段特定桌位種類的預約狀況
    public List<ReserveCheckDTO> getReserveCheck(Integer restaurantId, String tableTypeId, LocalDate checkDate) {
    	
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid restaurant ID"));

        // 根據餐廳的營業時間與每個時間段的用餐限制，計算出時間段
        List<TimeSlotDTO> timeSlots = generateTimeSlots(restaurant);
        List<ReserveCheckDTO> reserveChecks = new ArrayList<>();

        for (TimeSlotDTO timeSlot : timeSlots) {
            // 使用自定義的查詢方法，查詢每個時間段的預訂數量與總桌數
            Integer reservedTableCount = reserveRepository.countReservationsInTimeSlot(restaurantId, tableTypeId, checkDate, timeSlot.getSlotStar(), timeSlot.getSlotEnd());
            Integer totalTableCount = reserveRepository.countAvailableTables(restaurantId, tableTypeId);
            if(totalTableCount==null) totalTableCount=0;
            // 設定開放訂位的桌數比例
            totalTableCount = (Integer) (totalTableCount * restaurant.getReservePercent() / 100);
            ReserveCheckDTO bean = new ReserveCheckDTO(timeSlot.getSlotStar(), timeSlot.getSlotEnd(), totalTableCount, reservedTableCount);
            reserveChecks.add(bean);
        }

        return reserveChecks;
    }

    
    
    // 計算餐廳的所有可用時段
    private List<TimeSlotDTO> generateTimeSlots(Restaurant restaurant) {
        List<TimeSlotDTO> timeSlots = new ArrayList<>();
        LocalTime slotStart = restaurant.getRestaurantOpentime();
        LocalTime slotEnd;

        while (slotStart.isBefore(restaurant.getRestaurantClosetime())) {
            slotEnd = slotStart.plusMinutes(restaurant.getEattime());
            timeSlots.add(new TimeSlotDTO(slotStart, slotEnd));
            slotStart = slotStart.plusMinutes(restaurant.getReserveTimeScale());  // 訂位的時間區間
        }

        return timeSlots;
    }
    
    
    
    // 依照預定人數查詢訂位的桌位種類ID
    public String getTableTypeIdByReserveSeat(Integer reserveSeat) {
		
    	List<TableType> tableTypes = tableTypeRepository.findAll();
    	
    	for(TableType tableType : tableTypes) {
    		if(reserveSeat == tableType.getTableTypeName()) {
    			return tableType.getTableTypeId();
    		}
    	}
    	
    	for(TableType tableType : tableTypes) {
    		if(reserveSeat+1 == tableType.getTableTypeName()) {
    			return tableType.getTableTypeId();
    		}
    	}
    	
    	return null;
	}
    
    
    public Map<String, Integer> getReserveSum(LocalDateTime slotStartDate, LocalDateTime slotEndDate) {

        // 如果沒有傳入開始日期，則使用一年前的日期作為開始日期
        if (slotStartDate == null) {
            slotStartDate = LocalDateTime.now().minusYears(1); // 默認為一年前
        }

        // 如果沒有傳入結束日期，則使用當前日期作為結束日期
        if (slotEndDate == null) {
            slotEndDate = LocalDateTime.now(); // 默認為當前時間
        }

        List<Restaurant> restaurants = restaurantRepository.findAll();
        Map<String, Integer> restaurantReserveMap = new HashMap<>();

        for (Restaurant restaurant : restaurants) {
            Integer restaurantId = restaurant.getRestaurantId();
            Integer countReservationsInDate = reserveRepository.countReservationsInDateSlot(restaurantId, slotStartDate, slotEndDate);
            restaurantReserveMap.put(restaurant.getRestaurantName(), countReservationsInDate);
        }

        return restaurantReserveMap;
    }

    public List<Integer> getReserveInMonth(Integer year) {
    	
    	if (year == null) year = 2024;
        
        List<Integer> reservationCounts = new ArrayList<>();
        // 獲取每個月份的訂位數量
        for (Month month : Month.values()) {
            // 計算每個月的第一天和最後一天
            LocalDate monthStart = LocalDate.of(year, month, 1);
            LocalDate monthEnd = LocalDate.of(year, month, month.length(year % 4 == 0)); // 考慮閏年

            // 獲取該月的預訂數量
            Integer count = reserveRepository.countReservationsInMonth(monthStart, monthEnd);
            reservationCounts.add(count);
        }

        return reservationCounts;

    }
    
    
	public void sendMail(Reserve reserve) throws MessagingException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper  helper = new MimeMessageHelper(mimeMessage,true);
		//設置mail
		helper.setFrom(mailConfig.getUserName3());//誰寄信(application設定的信箱)
		helper.setTo(reserve.getMember().getEmail());//誰收信
		helper.setSubject("【☕訂位成功通知】您在 starcups "+ reserve.getRestaurant().getRestaurantName() +" 的預訂已經完成");//主旨
		
		//設置模板
		//設置model
		Map<String, Object> model = new HashMap<String,Object>();
		//透過model傳入的物件("參數名","東西")
		model.put("reserve",reserve);
		//get模板，並將modal傳入模板
		String templateString = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfig.getTemplate("reserveMail.html"), model);
		
		//設置mail內文
		helper.setText(templateString,true);
		
		//設置資源，順序要在內文之後
		FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/images/mail/logo-starcups.png"));
		helper.addInline("logo",file);
		
		mailSender.send(mimeMessage);	
	}
	
	

}
