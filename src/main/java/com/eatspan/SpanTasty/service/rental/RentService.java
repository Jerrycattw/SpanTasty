package com.eatspan.SpanTasty.service.rental;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatspan.SpanTasty.entity.account.Member;
import com.eatspan.SpanTasty.entity.rental.Rent;
import com.eatspan.SpanTasty.entity.rental.RentItem;
import com.eatspan.SpanTasty.entity.rental.Tableware;
import com.eatspan.SpanTasty.entity.store.Product;
import com.eatspan.SpanTasty.entity.store.ShoppingItem;
import com.eatspan.SpanTasty.repository.rental.RentItemRepository;
import com.eatspan.SpanTasty.repository.rental.RentRepository;
import com.eatspan.SpanTasty.repository.rental.TablewareRepository;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;

@Service
public class RentService {
	
	@Autowired
	private RentRepository rentRepository;
	@Autowired
	private RentItemRepository rentItemRepository;
	@Autowired
	private TablewareRepository tablewareRepository;
	@Autowired
	private RentItemService rentItemService;
	
	
	//新增訂單
	public Rent addRent(Rent rent) {
		return rentRepository.save(rent);
	}
	
	
	//刪除訂單
	public void deleteRent(Integer rentId) {
		rentRepository.deleteById(rentId);
	}
	
	
	//更新訂單
	@Transactional
	public Rent updateRent(Rent rent) {
		Optional<Rent> optional = rentRepository.findById(rent.getRentId());
		if(optional.isPresent()) {
			return rentRepository.save(rent);
		}
		return null;
	}
	
	
	//更新訂單
	@Transactional
	public Rent updateRent(Integer rentId, Integer rentDeposit, Date rentDate, Integer restaurantId, Integer memberId, Date dueDate, Date returnDate, Integer rentStatus, String rentMemo, Integer returnRestaurantId) {
		Optional<Rent> optional = rentRepository.findById(rentId);
		if(optional.isPresent()) {
			Rent rent = optional.get();
			rent.setRentDeposit(rentDeposit);
			rent.setRentDate(rentDate);
			rent.setRestaurantId(restaurantId);
			rent.setMemberId(memberId);
			rent.setDueDate(dueDate);
			rent.setReturnDate(returnDate);
			rent.setRentStatus(rentStatus);
			rent.setRentMemo(rentMemo);
			rent.setReturnRestaurantId(returnRestaurantId);
			return rent;
		}
		return null;
	}
	
	
	//查詢訂單(By條件)
	public List<Rent> findRentsByCriteria(Integer rentId, Integer memberId, Integer restaurantId, Integer rentStatus, Date rentDateStart, Date rentDateEnd){
		return rentRepository.findByCriteria(rentId, memberId, restaurantId, rentStatus, rentDateStart, rentDateEnd);
	}
	
	
	//查詢訂單(By過期)
	public List<Rent> findOvertimeRents(){
		return rentRepository.findByOverDueDate();
	}
	
	
	//查詢訂單(By訂單編號)
	public Rent findRentById(Integer rentId) {
		Optional<Rent> optional = rentRepository.findById(rentId);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	
	//查詢所有訂單
	public List<Rent> findAllRents() {
		return rentRepository.findAll();
	}

	
	//查詢所有訂單(Page)
	public Page<Rent> findAllRentPages(Integer page){
		Pageable pageable = PageRequest.of(page-1, 10, Sort.Direction.ASC, "rentId");
		return rentRepository.findAll(pageable);
	}
	
	
	//
	@Transactional
	public Rent addRentOrder(Integer memberId, Integer restaurantId) {
		Rent rent = new Rent();
		rent.setRestaurantId(restaurantId);
		rent.setMemberId(memberId);
		Date rentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(rentDate);
		calendar.add(Calendar.DAY_OF_YEAR, 7);
		Date dueDate = calendar.getTime();
		rent.setRentDate(rentDate);
		rent.setDueDate(dueDate);
		rent.setRentStatus(1);
		rent.setRentMemo("未歸還");
		rent.setRentDeposit(0);
		rentRepository.save(rent);
		return rent;
	}
	
	
	//
	public List<Rent> findRentsByMemberId(Integer memberId) {
		return rentRepository.findByMemberId(memberId);
	}
	
	
	//
	public Member findMemberByRentId(Integer rentId) {
		return rentRepository.findMemberByRentId(rentId);
	}


	// 取得購物訂單
    public String ecpayCheckout(Integer rentId) {
        // 获取当前时间
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        
        // 计算总金额
        Rent rent = findRentById(rentId);
        Integer totalAmount = rent.getRentDeposit();

        // 设置 ECPay 结账信息
        AllInOne all = new AllInOne("");
        AioCheckOutALL obj = new AioCheckOutALL();
        obj.setMerchantTradeNo(String.valueOf(rentId)); // 使用 shoppingId 作為交易編號
        obj.setMerchantTradeNo("SC" + System.currentTimeMillis()); // 使用 shoppingId 作為交易編號
        obj.setMerchantTradeDate(currentDateTime); // 使用當前時間
        obj.setTotalAmount(String.valueOf(totalAmount)); // 使用總金額

        // 獲取所有商品名稱和金額資訊
        List<RentItem> rentItems = rentItemService.findRentItemsByRentId(rentId);
        StringBuilder itemNames = new StringBuilder();

        for (RentItem item : rentItems) {
            Tableware tableware = item.getTableware(); // 假設 ShoppingItem 中有 Product 對象
            if (tableware != null) {
                Integer quantity = item.getRentItemQuantity(); // 獲取數量
                Integer price = item.getRentItemDeposit(); // 獲取價格
                itemNames.append(tableware.getTablewareName())
                          .append(" ")
                          .append(quantity)
                          .append("*NT$")
                          .append(price)
                          .append(".  /  "); // 格式化商品資訊
            }
        }

        // 移除最後的逗號和空格
        if (itemNames.length() > 0) {
            itemNames.setLength(itemNames.length() - 2);
        }

        obj.setItemName(itemNames.toString()); // 設置商品名稱
        obj.setTradeDesc("test Description");
        obj.setReturnURL("https://5b6d-61-222-34-1.ngrok-free.app/SpanTasty/StarCups/rental/OrderConfirm");
        obj.setNeedExtraPaidInfo("N");
        obj.setClientBackURL("http://localhost:8080/SpanTasty/StarCups/rental/OrderConfirm");

        // 生成 ECPay 表单			
        return all.aioCheckOut(obj, null);
    }

    
    public List<Rent> findByDueDateTomorrow(){
    	return rentRepository.findByDueDateTomorrow();
    }
}


