package com.eatspan.SpanTasty.controller.rental;

import java.io.File;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
	protected String addTablewareAndStocks(@ModelAttribute Tableware tableware, @RequestParam("timg") MultipartFile timg, Model model) throws IOException {
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
			
			System.out.println("File uploaded: " + newFileName);
			System.out.println("File path: " + uploadPath + File.separator + newFileName);
		}
		tablewareService.addTableware(tableware);
		
		
//		for (TablewareStock stock : tableware.getTablewareStocks()) {
//	        stock.setTablewareId(tableware.getTablewareId()); // 設定關聯的 tablewareId
//	        tablewareStockService.addStock(stock);
//	    }
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
			@RequestParam("tableware_image") MultipartFile timg,
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
	    } else {
	        // 如果沒有上傳新的圖片，保留現有圖片
	        if (timg != null) {
	        	tableware.setTablewareImage(tableware.getTablewareImage());
	        }
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
	
	
	//查詢全部
	@GetMapping("/getAll")
	public String getAllTablewares(Model model) {
		List<Tableware> tablewares = tablewareService.findAllTablewares();
		model.addAttribute("tablewares",tablewares);
		return "rental/getAllTablewares";
	}
	
	
//	查詢餐具(By關鍵字 Maybe use ajax)
//	@GetMapping("/get")
//	public String findTablewaresBySearch(@RequestParam("keyword") String keyword, Model model) throws IOException {
//		List<Tableware> tablewares = tablewareService.findTablewaresByKeywords(keyword);
//		model.addAttribute("tablewares", tablewares);
//		return "rental/searchTablewares";
//	}
}
