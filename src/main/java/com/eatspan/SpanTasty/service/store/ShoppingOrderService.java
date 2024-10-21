package com.eatspan.SpanTasty.service.store;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.context.annotation.SessionScope;

import com.eatspan.SpanTasty.config.MailConfig;
import com.eatspan.SpanTasty.entity.reservation.Reserve;
import com.eatspan.SpanTasty.entity.store.Product;
import com.eatspan.SpanTasty.entity.store.ShoppingItem;
import com.eatspan.SpanTasty.entity.store.ShoppingItemId;
import com.eatspan.SpanTasty.entity.store.ShoppingOrder;
import com.eatspan.SpanTasty.repository.store.ShoppingItemRepository;
import com.eatspan.SpanTasty.repository.store.ShoppingOrderRepository;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class ShoppingOrderService {

	@Autowired
	private ShoppingOrderRepository shoppingOrderRepo;

	@Autowired
	@Lazy
	private ShoppingItemService shoppingItemService;
	
	@Autowired 
	ShoppingItemRepository shoppingItemRepo;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private HttpSession session;

	@Autowired
	private MailConfig mailConfig;// javaMail要注入----------------------------
	
	@Autowired
	private JavaMailSender mailSender;// javaMail要注入----------------------------
	
	@Autowired
	private freemarker.template.Configuration freemarkerConfig; // javaMail要注入----------------------------
	@PersistenceContext
	private EntityManager entityManager;
	

	public Integer calculateTotalAmount(Integer shoppingId) {
	    String hql = "SELECT SUM(i.product.productPrice * i.shoppingItemQuantity) "
	            + "FROM ShoppingItem i WHERE i.shoppingOrder.shoppingId = :shoppingId";
	    TypedQuery<Long> query = entityManager.createQuery(hql, Long.class);
	    query.setParameter("shoppingId", shoppingId);

	    Long totalAmount = query.getSingleResult();
	    return (totalAmount != null) ? totalAmount.intValue() : 0; // 轉換為 Integer
	}
	
	
	@Transactional
	public ShoppingOrder addShoppingOrder(Integer memberId, Integer productId, Integer shoppingItemQuantity) {
	    
	    ShoppingOrder shoppingOrder = new ShoppingOrder();
	    shoppingOrder.setMemberId(memberId);
	    shoppingOrder.setShoppingDate(LocalDateTime.now());
	    shoppingOrder.setShoppingStatus(1);
	    shoppingOrder.setShoppingTotal(0); 

	    ShoppingOrder savedOrder = shoppingOrderRepo.save(shoppingOrder);

	    Optional<Product> productOpt = productService.findProductByIdS(productId);
	    if (!productOpt.isPresent()) {
	        throw new RuntimeException("Product not found with ID: " + productId);
	    }
	    
	    Product product = productOpt.get();
	    Integer price = product.getProductPrice(); 
	    
	    // 創建複合主鍵
	    ShoppingItemId itemId = new ShoppingItemId(savedOrder.getShoppingId(), productId); // 使用已保存的訂單 ID

	    ShoppingItem shoppingItem = new ShoppingItem(itemId, shoppingItemQuantity, price);

	    shoppingItem.setShoppingOrder(savedOrder);
	    
	    shoppingItemRepo.save(shoppingItem); 

	    shoppingOrder.setShoppingTotal(shoppingOrder.getShoppingTotal() + (price * shoppingItemQuantity));
	    shoppingOrderRepo.save(shoppingOrder);

	    return savedOrder; 
	}

	@Transactional
	public ShoppingOrder addShoppingOrder(Integer memberId, List<Integer> productId, List<Integer> shoppingItemQuantity) {
	    
	    ShoppingOrder shoppingOrder = new ShoppingOrder();
	    shoppingOrder.setMemberId(memberId);
	    shoppingOrder.setShoppingDate(LocalDateTime.now());
	    shoppingOrder.setShoppingStatus(1);
	    shoppingOrder.setShoppingTotal(0);

	    ShoppingOrder savedOrder = shoppingOrderRepo.save(shoppingOrder);

	    for (int i = 0; i < productId.size(); i++) {
	        Integer currentProductId = productId.get(i);
	        Integer currentQuantity = shoppingItemQuantity.get(i);

	        Optional<Product> productOpt = productService.findProductByIdS(currentProductId);
	        if (!productOpt.isPresent()) {
	            throw new RuntimeException("Product not found with ID: " + currentProductId);
	        }
	        
	        Product product = productOpt.get();
	        Integer price = product.getProductPrice(); 
	        
	        // 創建複合主鍵
	        ShoppingItemId itemId = new ShoppingItemId(savedOrder.getShoppingId(), currentProductId); 

	        ShoppingItem shoppingItem = new ShoppingItem(itemId, currentQuantity, price);
	        shoppingItem.setShoppingOrder(savedOrder);
	        
	        shoppingItemRepo.save(shoppingItem); 

	        // 更新訂單總金額
	        shoppingOrder.setShoppingTotal(shoppingOrder.getShoppingTotal() + (price * currentQuantity));
	    }

	    shoppingOrderRepo.save(shoppingOrder);

	    return savedOrder; 
	}


	
	
	@Transactional
	public ShoppingOrder createNewShoppingOrder(Integer memberId) {
	    ShoppingOrder shoppingOrder = new ShoppingOrder();
	    shoppingOrder.setMemberId(memberId);
	    shoppingOrder.setShoppingDate(LocalDateTime.now());
	    shoppingOrder.setShoppingStatus(1);
	    shoppingOrder.setShoppingMemo(""); // 可以根據需要設置默認備註
	    shoppingOrder.setShoppingTotal(0); // 初始金額

	    ShoppingOrder savedOrder = shoppingOrderRepo.save(shoppingOrder);
	    return savedOrder;
	}
	

	@Transactional
	public void deleteShoppingOrder(Integer shoppingId) {
		shoppingItemService.deleteAllShoppingItems(shoppingId);
		shoppingOrderRepo.deleteById(shoppingId);
	}

	@Transactional
	public ShoppingOrder updateShoppingOrder(ShoppingOrder shoppingOrder) {
		Optional<ShoppingOrder> optional = shoppingOrderRepo.findById(shoppingOrder.getShoppingId());
		 if (optional.isPresent()) {
		        ShoppingOrder existingshoppingOrder = optional.get();
		        
		        if (shoppingOrder.getShoppingMemo() != null) {
		            existingshoppingOrder.setShoppingMemo(shoppingOrder.getShoppingMemo());
		        }
		        if (shoppingOrder.getShoppingStatus() != null) {
		            existingshoppingOrder.setShoppingStatus(shoppingOrder.getShoppingStatus());
		        }
	
		        existingshoppingOrder.setShoppingTotal(calculateTotalAmount(shoppingOrder.getShoppingId()));
	
		        return shoppingOrderRepo.save(existingshoppingOrder);
		    }
		    return null; 
		}

	

	
	public ShoppingOrder findShoppingOrderById(Integer id) {
		Optional<ShoppingOrder> optional = shoppingOrderRepo.findById(id);

		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	public List<ShoppingOrder> findAllShoppingOrder() {
		return shoppingOrderRepo.findAll();
	}
	
	@Transactional
	public List<ShoppingOrder> findOrdersByMemberId(Integer memberId) {
	    return shoppingOrderRepo.findByMemberId(memberId);
	}

	// 按照購物日期排序，搜尋最近的一筆訂單
	@Transactional
	public ShoppingOrder getLatestShoppingOrderByMemberId(Integer memberId) {
	    List<ShoppingOrder> orders = shoppingOrderRepo.findByMemberId(memberId);
	    if (orders != null && !orders.isEmpty()) {
	        return orders.stream()
	                     .max((o1, o2) -> o1.getShoppingDate().compareTo(o2.getShoppingDate()))
	                     .orElse(null);
	    }
	    return null; 
	}

	
//	    // 取得購物訂單
//	    public String ecpayCheckout(Integer shoppingId) {
//	        // 获取当前时间
//	        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
//	        
//	        // 计算总金额
//	        Integer totalAmount = calculateTotalAmount(shoppingId);
//
//	        // 设置 ECPay 结账信息
//	        AllInOne all = new AllInOne("");
//	        AioCheckOutALL obj = new AioCheckOutALL();
////	        obj.setMerchantTradeNo(String.valueOf(shoppingId)); // 使用 shoppingId 作為交易編號
//	        obj.setMerchantTradeNo("SC" + System.currentTimeMillis()); // 使用 shoppingId 作為交易編號
//	        obj.setMerchantTradeDate(currentDateTime); // 使用當前時間
//	        obj.setTotalAmount(String.valueOf(totalAmount)); // 使用總金額
//	        obj.setItemName("商品名稱"); // 可根據需要設定商品名稱
//	        obj.setTradeDesc("test Description");
////	        obj.setReturnURL("https://5b6d-61-222-34-1.ngrok-free.app/SpanTasty/StarCups/allProduct");
//	        obj.setReturnURL("https://5b6d-61-222-34-1.ngrok-free.app/SpanTasty/StarCups/OrderConfirm");
//	        obj.setNeedExtraPaidInfo("N");
////	        obj.setClientBackURL("http://localhost:8080/SpanTasty/StarCups/allProduct");
//	        obj.setClientBackURL("http://localhost:8080/SpanTasty/StarCups/OrderConfirm");
//
//	        // 生成 ECPay 表单			
//	        return all.aioCheckOut(obj, null);
//	    }

    // 取得購物訂單
    public String ecpayCheckout(Integer shoppingId) {
        // 获取当前时间
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        
        // 计算总金额
        Integer totalAmount = calculateTotalAmount(shoppingId);
        
        System.out.println(totalAmount);
        
        
        
//        ShoppingOrder shopping =  findShoppingOrderById(shoppingId);
//        Integer discountAmount = shopping.getDiscountAmount();
//	    if (discountAmount == null) {
//	        discountAmount = 0;
//	    }
	    
	    // 計算 finalAmount
//	    Integer shoppingTotal = shopping.getShoppingTotal();
//	    Integer finalAmount = shoppingTotal - discountAmount;
	    
//        ShoppingOrder shopping = new ShoppingOrder();
        
        // 设置 ECPay 结账信息
        AllInOne all = new AllInOne("");
        AioCheckOutALL obj = new AioCheckOutALL();
        obj.setMerchantTradeNo(String.valueOf(shoppingId)); 
//        obj.setMerchantTradeNo("SC" + System.currentTimeMillis()); 
        obj.setMerchantTradeDate(currentDateTime);
        obj.setTotalAmount(String.valueOf(totalAmount)); 
//        obj.setTotalAmount(String.valueOf(shopping.getFinalAmount())); 
//        obj.setTotalAmount(String.valueOf(finalAmount)); 

        // 獲取所有商品名稱和金額資訊
        List<ShoppingItem> shoppingItems = shoppingItemService.findShoppingItemById(shoppingId);
        StringBuilder itemNames = new StringBuilder();

        for (ShoppingItem item : shoppingItems) {
            Product product = item.getProduct(); 
            if (product != null) {
                Integer quantity = item.getShoppingItemQuantity(); 
                Integer price = item.getShoppingItemPrice(); 
                itemNames.append(product.getProductName())
                          .append(" ")
                          .append(quantity)
                          .append("*NT$")
                          .append(price)
                          .append(".  /  "); 
            }
        }

        // 移除最後的逗號和空格
        if (itemNames.length() > 0) {
            itemNames.setLength(itemNames.length() - 2);
        }

        obj.setItemName(itemNames.toString()); // 設置商品名稱
        obj.setTradeDesc("test Description");
//        obj.setReturnURL("https://5b6d-61-222-34-1.ngrok-free.app/SpanTasty/StarCups/allProduct");
        obj.setReturnURL("https://5b6d-61-222-34-1.ngrok-free.app/SpanTasty/StarCups/allProduct");
        obj.setNeedExtraPaidInfo("N");
//        obj.setClientBackURL("http://localhost:8080/SpanTasty/StarCups/allProduct");
        obj.setClientBackURL("http://localhost:8080/SpanTasty/StarCups/OrderConfirm");

        // 生成 ECPay 表单			
        return all.aioCheckOut(obj, null);
    }

    
    public void sendMail(ShoppingOrder shopping) throws MessagingException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper  helper = new MimeMessageHelper(mimeMessage,true);
		//設置mail
		helper.setFrom(mailConfig.getUserName3());//誰寄信(application設定的信箱)
//		helper.setTo(reserve.getMember().getEmail());//誰收信
		helper.setTo("spantasty@gmail.com");//誰收信
		helper.setSubject("謝謝您在☕starcups的訂購");//主旨
		
		//設置模板
		//設置model
		Map<String, Object> model = new HashMap<String,Object>();
		//透過modal傳入的物件("參數名","東西")
		//model.put("userName",memberName);
		
		model.put("shopping",shopping);
		//get模板，並將modal傳入模板
		String templateString = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfig.getTemplate("storeMail.html"), model);
		
		//設置mail內文
		helper.setText(templateString,true);
		
		//設置資源，順序要在內文之後
		FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/images/mail/logo-starcups.png"));
		helper.addInline("logo",file);
		
		mailSender.send(mimeMessage);	
	}
}
