package com.eatspan.SpanTasty.controller.rental;

import java.io.File;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
		model.addAttribute("restaurants", restaurants);
		return "rental/addTableware";
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
		
		List<String> restaurantIdParams = new ArrayList<>();
		List<String> stockParams = new ArrayList<>();
		
		for (Map.Entry<String, String> entry : allParams.entrySet()) {
			if (entry.getKey().startsWith("restaurantId")) {
				restaurantIdParams.add(entry.getValue());
			} else if (entry.getKey().startsWith("stock")) {
				stockParams.add(entry.getValue());
			}
		}
		List<TablewareStock> tablewareStocks = new ArrayList<>();
		for (int i = 0; i < restaurantIdParams.size(); i++) {
			String restaurantIdParam = restaurantIdParams.get(i);
			String stockParam = stockParams.get(i);
			System.out.println(restaurantIdParam);
			if (restaurantIdParam != null && !restaurantIdParam.isEmpty() && stockParam != null && !stockParam.isEmpty()) {
				Integer restaurantId = Integer.parseInt(restaurantIdParam);
				Integer stock = Integer.parseInt(stockParam);
				
				TablewareStock tablewareStock = new TablewareStock();
				tablewareStock.setRestaurantId(restaurantId);
				tablewareStock.setTablewareId(tablewareId);
				tablewareStock.setStock(stock);
				tablewareStocks.add(tablewareStock);
			}
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
		return "rental/setTableware";
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
		return "rental/getAllTablewares";
	}
	
	
	//導向查詢頁面
	@GetMapping("/toGet")
	public String toGetTablewares() {
		return "rental/getTablewares";
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
