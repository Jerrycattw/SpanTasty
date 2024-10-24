package com.eatspan.SpanTasty.controller.rental;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import com.eatspan.SpanTasty.dto.rental.CartRequestDTO;
import com.eatspan.SpanTasty.dto.rental.RestaurantStockDTO;
import com.eatspan.SpanTasty.dto.rental.TablewareFilterDTO;
import com.eatspan.SpanTasty.dto.rental.TablewareIdDTO;
import com.eatspan.SpanTasty.dto.rental.TablewareKeywordDTO;
import com.eatspan.SpanTasty.entity.account.Member;
import com.eatspan.SpanTasty.entity.rental.Rent;
import com.eatspan.SpanTasty.entity.rental.RentItem;
import com.eatspan.SpanTasty.entity.rental.Tableware;
import com.eatspan.SpanTasty.entity.rental.TablewareStock;
import com.eatspan.SpanTasty.entity.reservation.Restaurant;
import com.eatspan.SpanTasty.entity.store.Product;
import com.eatspan.SpanTasty.entity.store.ShoppingItem;
import com.eatspan.SpanTasty.entity.store.ShoppingOrder;
import com.eatspan.SpanTasty.service.account.MemberService;
import com.eatspan.SpanTasty.service.rental.RentItemService;
import com.eatspan.SpanTasty.service.rental.RentService;
import com.eatspan.SpanTasty.service.rental.TablewareService;
import com.eatspan.SpanTasty.service.rental.TablewareStockService;
import com.eatspan.SpanTasty.service.reservation.RestaurantService;
import com.eatspan.SpanTasty.utils.account.JwtUtil;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/StarCups")
public class StarCupsRentController {

	@Autowired
	private TablewareService tablewareService;
	@Autowired
	private TablewareStockService tablewareStockService;
	@Autowired
	private RentService rentService;
	@Autowired
	private RentItemService rentItemService;
	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private HttpSession session;
	
	
	// 導向Home頁面
	@GetMapping("/rental/home")
	public String toHomePage(Model model) {
		return "starcups/rental/rentHomePage";
	}
	
	
	// 導向到LOOKBOOK頁面
	@GetMapping("/rental/lookbook")
	public String showAllTablewares(Model model, @RequestParam(defaultValue = "1") Integer page) {
		Page<Tableware> tablewarePages = tablewareService.findAllTablewarePages(page);
		Integer totalTablewares = tablewareService.countTableware();
		model.addAttribute("totalTablewares", totalTablewares);
		model.addAttribute("tablewarePages", tablewarePages);
		return "starcups/rental/allTablewarePage";
	}
	
	
	// 導向到RENT頁面
	@GetMapping("/rental/rent")
	public String toRentTableware(Model model) {
		List<Restaurant> restaurants = restaurantService.findAllRestaurants()
				.stream()
                .filter(restaurant -> restaurant.getRestaurantStatus() == 1)
                .collect(Collectors.toList());
		List<Tableware> tablewares = tablewareService.findAllTablewares();
		model.addAttribute("tablewares", tablewares);
		model.addAttribute("restaurants" ,restaurants);
		return "starcups/rental/rentTablewarePage";
	}
	
	
	// 選擇餐廳後跑出的資料
	@ResponseBody
	@PostMapping("/rental/showRestaurantStock")
	public ResponseEntity<RestaurantStockDTO> showRestaurantEntity(@RequestBody TablewareFilterDTO tablewareFilterDTO) {
		Restaurant restaurant = restaurantService.findRestaurantById(tablewareFilterDTO.getRestaurantId());
		List<TablewareStock> stocks = tablewareStockService.findStockByRestaurantId(tablewareFilterDTO.getRestaurantId());
		RestaurantStockDTO restaurantStockDTO = new RestaurantStockDTO();
		restaurantStockDTO.setRestaurant(restaurant);
		restaurantStockDTO.setStocks(stocks);
		return ResponseEntity.ok(restaurantStockDTO);
	}
	
	
	// 查詢餐具(By關鍵字)
	@ResponseBody
	@PostMapping("/rental/showTablewares")
	public ResponseEntity<List<Tableware>> findTablewaresByKeyword(@RequestBody TablewareKeywordDTO keywordDTO) {
		String Keyword = keywordDTO.getTablewareKeyword();
		List<Tableware> tablewares = tablewareService.findTablewaresByKeywords(Keyword);
		return ResponseEntity.ok(tablewares);
	}
	
	
	// 導向One頁面
	@GetMapping("/rental/single/{id}")
	public String toOneTableware(@PathVariable("id") Integer tablewareId, Model model) {
		Tableware tableware = tablewareService.findTablewareById(tablewareId);
		model.addAttribute("tableware", tableware);
		return "starcups/rental/singleTablewarePage";
	}
	
	
	//
	@ResponseBody
	@PostMapping("/rental/tableware")
	public ResponseEntity<Tableware> getOneTableware(@RequestBody TablewareIdDTO tablewareIdDTO) {
		Tableware tableware = tablewareService.findTablewareById(tablewareIdDTO.getTablewareId());
		return ResponseEntity.ok(tableware);
	}
	
	
	// 加入購物車
	@ResponseBody
	@PostMapping("/rental/addCart")
	public ResponseEntity<?> addToCart(@RequestHeader(value = "Authorization") String token, @RequestBody List<CartRequestDTO> cartRequestDTOList) {
		try {
			// 解析 JWT token 取得 claims
			Map<String, Object> claims = JwtUtil.parseToken(token);
			Integer memberId = (Integer) claims.get("memberId"); // 獲取會員 ID
			
			// 清空並重新生成 rentId，確保每次請求都是一個新訂單
	        session.removeAttribute("rentId");
	        
			Integer restaurantId = cartRequestDTOList.get(0).getRestaurantId();
			
			Rent rent = rentService.addRentOrder(memberId, restaurantId);
			session.setAttribute("rentId", rent.getRentId());
			
			Integer rentDeposit = 0;
			for(CartRequestDTO cartRequestDTO : cartRequestDTOList) {
	            Integer tablewareId = cartRequestDTO.getTablewareId();
	            Integer rentItemQuantity = cartRequestDTO.getRentItemQuantity();
	            // 新增每個租借項目
	            RentItem rentItem = rentItemService.addRentItemToOrder(rent.getRentId(), tablewareId, rentItemQuantity);
	            rentDeposit += rentItem.getRentItemDeposit();
	        }
			rent.setRentDeposit(rentDeposit);
			rentService.addRent(rent);
			
			return ResponseEntity.status(HttpStatus.OK).body(rent);
		} catch (Exception e) {
			e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("發生錯誤：" + e.getMessage());
		}
	}
	
	
	// 導向結帳畫面
	@GetMapping("/rental/cart")
	public String toCheckout(Model model) {
		Integer rentId = (Integer) session.getAttribute("rentId");
		Rent rent = rentService.findRentById(rentId);
		Member member = rentService.findMemberByRentId(rentId);
		List<Tableware> tablewares = tablewareService.findAllTablewares();
		List<RentItem> rentItems = rentItemService.findRentItemsByRentId(rentId);
		Restaurant restaurant = restaurantService.findRestaurantById(rent.getRestaurantId());
		model.addAttribute("rent", rent);
		model.addAttribute("member", member);
		model.addAttribute("rentItems", rentItems);
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("tablewares", tablewares);
		return "starcups/rental/checkoutTablewarePage";
	}
	
	
	// 導向訂單名細頁面
	@GetMapping("/rental/allRent")
	public String toAllRent(@RequestParam(value = "token") String token, Model model) {
		// 解析 JWT token 取得 claims
		Map<String, Object> claims = JwtUtil.parseToken(token);
		Integer memberId = (Integer) claims.get("memberId"); // 獲取會員 ID
		List<Rent> rents = rentService.findRentsByMemberId(memberId);
		model.addAttribute("rents", rents);
		return "starcups/rental/allRentPage";
	}
	
	
	//
	@PostMapping("/rental/ecpayCheckout")
	@ResponseBody
	public String ecpayCheckout(Model model) {
	    Integer rentId = (Integer) session.getAttribute("rentId");
	    
	    String aioCheckOutALLForm = rentService.ecpayCheckout(rentId);

	    model.addAttribute("aioCheckOutALLForm", aioCheckOutALLForm);
	    
	    
	    return aioCheckOutALLForm;
	}
	
	
	//
	@GetMapping("/rental/OrderConfirm")
	public String checkOutFinish(@RequestParam Map<String, String>map, Model model) {
		Integer rentId = (Integer) session.getAttribute("rentId");
		Rent rent = rentService.findRentById(rentId);
		
		model.addAttribute("rent", rent);
		List<RentItem> rentItems = rentItemService.findRentItemsByRentId(rentId);
		model.addAttribute("rentItems", rentItems);
		List<Tableware> tablewares = tablewareService.findAllTablewares();
		model.addAttribute("tablewares", tablewares);
		Member member = rentService.findMemberByRentId(rentId);
		model.addAttribute("member", member);
		
		return "starcups/rental/rentOrderConfirm";
	}
}
