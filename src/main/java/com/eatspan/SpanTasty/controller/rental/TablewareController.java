package com.eatspan.SpanTasty.controller.rental;

import java.io.File;



import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eatspan.SpanTasty.dto.rental.TablewareKeywordDTO;
import com.eatspan.SpanTasty.entity.rental.Tableware;
import com.eatspan.SpanTasty.entity.rental.TablewareStock;
import com.eatspan.SpanTasty.entity.reservation.Restaurant;
import com.eatspan.SpanTasty.service.rental.TablewareService;
import com.eatspan.SpanTasty.service.rental.TablewareStockService;
import com.eatspan.SpanTasty.service.reservation.RestaurantService;

@Controller
@RequestMapping("/tableware")
@PropertySource("upload.properties")
public class TablewareController {
	
	@Autowired
	private TablewareService tablewareService;
	@Autowired
	private TablewareStockService tablewareStockService;
	@Autowired
	private RestaurantService restaurantService;
	
	@Value("${upload.rentalPath}")
	private String uploadPath;
	
	
	//導入新增頁面
	@GetMapping("/add")
	public String toAddTableware(Model model) {
		List<Restaurant> restaurants = restaurantService.findAllRestaurants();
		// 過濾掉除了餐廳狀態1的餐廳
		List<Restaurant> availableRestaurants = restaurants.stream()
				.filter(restaurant -> restaurant.getRestaurantStatus() == 1)
				.collect(Collectors.toList());
		model.addAttribute("restaurants", availableRestaurants);
		return "spantasty/rental/addTableware";
	}
	
	
	//新增資料
	@PostMapping("/addPost")
	protected String addTablewareAndStocks(
			@ModelAttribute Tableware tableware, 
			@RequestParam("timg") MultipartFile timg,
			@RequestParam Map<String, String> allParams,
			Model model) throws IOException {
		tableware.setTablewareStatus(1);
		
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs(); // 确保递归创建目录
		}
		if (timg != null && !timg.isEmpty()) {
			String fileName = timg.getOriginalFilename();
			String extension = fileName.substring(fileName.lastIndexOf("."));
			String newFileName = tableware.getTablewareName() + extension;
			File filePart = new File(uploadPath + File.separator + newFileName);
			timg.transferTo(filePart);
			tableware.setTablewareImage("/SpanTasty/upload/rental/" + newFileName);
		}
		tablewareService.addTableware(tableware);
		int tablewareId = tableware.getTablewareId();
		
		List<Restaurant> allRestaurants = restaurantService.findAllRestaurants();
		Map<Integer, Integer> restaurantStockMap = new HashMap<>(); 
		System.out.println(allParams);
		
		// 解析餐廳 ID 和庫存
		for (Map.Entry<String, String> entry : allParams.entrySet()) {
			// 使用正則表達式來匹配 restaurantId 和 stock
			if (entry.getKey().matches("^restaurantId\\[\\d+]$")) {
				// 提取餐廳 ID 索引
				String index = entry.getKey().replaceAll("[^0-9]", ""); // 正確提取數字索引
				Integer restaurantId = Integer.parseInt(entry.getValue());
				String stockKey = "stock[" + index + "]"; // 正確生成 stockKey

				// 檢查 stockKey 是否正確匹配到庫存數值
				if (allParams.containsKey(stockKey)) {
					String stockValue = allParams.get(stockKey);
					System.out.println("匹配到庫存值: " + stockValue + "，對應餐廳ID: " + restaurantId);

					Integer stock = !stockValue.isEmpty() ? Integer.parseInt(stockValue) : 0;

					// 將餐廳ID與庫存放入 map，並打印數據以便確認
					System.out.println("餐廳ID: " + restaurantId + ", 儲存庫存: " + stock);
					restaurantStockMap.put(restaurantId, stock);
				} else {
					System.out.println("未匹配到 stockKey: " + stockKey);
				}
			}
		}
		List<TablewareStock> tablewareStocks = new ArrayList<>();
		
		// 遍歷所有餐廳，對沒有輸入的庫存設為 0
		for (Restaurant restaurant : allRestaurants) {
		    Integer restaurantId = restaurant.getRestaurantId();
		    Integer stock = restaurantStockMap.getOrDefault(restaurantId, 0);  // 如果沒有輸入庫存，則預設為 0
		    System.out.println("餐廳ID: " + restaurantId + ", 庫存: " + stock);
		    TablewareStock tablewareStock = new TablewareStock();
		    tablewareStock.setRestaurantId(restaurantId);
		    tablewareStock.setTablewareId(tablewareId);
		    tablewareStock.setStock(stock);
		    tablewareStocks.add(tablewareStock);
		}
		// 批量插入库存记录
		for (TablewareStock tablewareStock : tablewareStocks) {
			tablewareStockService.addStock(tablewareStock);
		}
		return "redirect:/tableware/getAll";
	}

	
	///更改頁面
	@GetMapping("/set/{id}")
	public String toSetTableware(@PathVariable("id") Integer tablewareId, Model model) {
		Tableware tableware = tablewareService.findTablewareById(tablewareId);
		model.addAttribute("tableware", tableware);
		return "spantasty/rental/setTableware";
	}

	
	//更改資料
	@PutMapping("/setPut1")
	protected String updateTableware(
			@ModelAttribute Tableware tableware,
			@RequestParam("timg") MultipartFile timg,
			Model model) throws IOException {
	    File fileSaveDir = new File(uploadPath);
	    if (!fileSaveDir.exists()) {
	        fileSaveDir.mkdirs();
	    }
	    if (!timg.isEmpty()) {
	        String fileName = timg.getOriginalFilename();
	        String extension = fileName.substring(fileName.lastIndexOf("."));
	        String newFileName = tableware.getTablewareName() + "_" + System.currentTimeMillis() + extension;
	        // 將檔案寫入指定路徑
	        File fileToSave = new File(uploadPath + File.separator + newFileName);
	        timg.transferTo(fileToSave);
	        tableware.setTablewareImage("/SpanTasty/upload/rental/" + newFileName);
	    }
	    tablewareService.addTableware(tableware);
	    return "redirect:/tableware/getAll";
	}
	
	
	//更改狀態
	@PutMapping("/setPut2/{id}")
	public String updateStatus(@PathVariable("id") Integer tablewareId, Model model) {
		tablewareService.updateTablewareStatus(tablewareId);
		return "redirect:/tableware/getAll";
	}
	
	
	//更改狀態
	@PostMapping("/setPost/{id}")
	@ResponseBody
	public ResponseEntity<String> updateTablewareStatus(@PathVariable("id") Integer tablewareId, Model model) {
		tablewareService.updateTablewareStatus(tablewareId);
		return ResponseEntity.ok("Status updated successfully");
	}
	
	
	//查詢全部
	@GetMapping("/getAll")
	public String getAllTablewares(Model model, @RequestParam(value="p", defaultValue = "1") Integer page) {
		Page<Tableware> tablewarePages = tablewareService.findAllTablewarePages(page);
		model.addAttribute("tablewarePages",tablewarePages);
		return "spantasty/rental/getAllTablewares";
	}
	
	
	//	查詢餐具(By關鍵字)
	@ResponseBody
	@PostMapping("/get")
	public ResponseEntity<List<Tableware>> findTablewaresByKeyword(@RequestBody TablewareKeywordDTO keywordDTO) {
		String Keyword = keywordDTO.getTablewareKeyword();
		List<Tableware> tablewares = tablewareService.findTablewaresByKeywords(Keyword);
		return ResponseEntity.ok(tablewares);
	}
}
