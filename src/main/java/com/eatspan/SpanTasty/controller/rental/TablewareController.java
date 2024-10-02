package com.eatspan.SpanTasty.controller.rental;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.eatspan.SpanTasty.entity.rental.Tableware;
import com.eatspan.SpanTasty.entity.rental.TablewareStock;
import com.eatspan.SpanTasty.service.rental.TablewareService;
import com.eatspan.SpanTasty.service.rental.TablewareStockService;

@Controller
@RequestMapping("/tableware")
public class TablewareController {
	@Autowired
	private TablewareService tablewareService;
	@Autowired
	private TablewareStockService tablewareStockService;
	
	@GetMapping("/getAll")
	public String showAllTablewares(Model model) {
		List<Tableware> tablewares = tablewareService.findAllTablewares();
		model.addAttribute("tablewares",tablewares);
		return "rental/ShowAllTablewares";
	}
	
	@GetMapping("/get")
	public String findTablewaresBySearch(@RequestParam("keyword") String keyword, Model model) throws IOException {
		List<Tableware> tablewares = tablewareService.findTablewaresByKeywords(keyword);
		model.addAttribute("tablewares", tablewares);
		return "rental/ShowAllTablewares";
	}
	
	@GetMapping("/get/{id}")
	public String findTablewareById(@PathVariable("id") Integer tablewareId, Model model) {
		Tableware tableware = tablewareService.findTablewareById(tablewareId);
		model.addAttribute("tableware", tableware);
		return "rental/UpdateTableware";
	}
	
//	@GetMapping("/option")
//	public String findSelectOption(Model model) {
//		List<String> restaurantNames = restaurantService.getAllRestaurantName();
//		model.addAttribute("restaurantNames", restaurantNames);
//		return "rental/SaveTableware";
//	}
	
	@PutMapping("/setStatus")
	public String updateStatus(@RequestParam("tableware_id") Integer tablewareId, Model model) throws IOException {
		Tableware tableware = tablewareService.updateTablewareStatus(tablewareId);
		return "redirect:/tableware/showAll";
	}

	@PutMapping("/set")
	protected String updateTableware(
			@RequestParam("tableware_id") Integer tablewareId, 
			@RequestParam("tableware_name") String tablewareName, 
			@RequestParam("tableware_deposit") Integer tablewareDeposit, 
			@RequestParam("tableware_image") MultipartFile timg,
			@RequestParam("tableware_description") String tablewareDescription,
			@RequestParam("tableware_status") Integer tablewareStatus,
			Model model) throws IOException {
		Tableware tableware = tablewareService.findTablewareById(tablewareId);
		tableware.setTablewareName(tablewareName);
		tableware.setTablewareDeposit(tablewareDeposit);
		tableware.setTablewareDescription(tablewareDescription);
		tableware.setTablewareStatus(tablewareStatus);
		
        String uploadPath = "C:/upload/tablewareIMG";
	    File fileSaveDir = new File(uploadPath);
	    if (!fileSaveDir.exists()) {
	        fileSaveDir.mkdirs();
	    }

	    if (!timg.isEmpty()) {
	        String fileName = timg.getOriginalFilename();
	        String extension = fileName.substring(fileName.lastIndexOf("."));
	        String newFileName = tablewareName + "_" + System.currentTimeMillis() + extension;

	        // 將檔案寫入指定路徑
	        File fileToSave = new File(uploadPath + File.separator + newFileName);
	        timg.transferTo(fileToSave);

	        tableware.setTablewareImage("/EEIT187-6/tablewareIMG/" + newFileName);
	    } else {
	        // 如果沒有上傳新的圖片，保留現有圖片
	        if (timg != null) {
	        	tableware.setTablewareImage(tableware.getTablewareImage());
	        }
	    }
	    tablewareService.addTableware(tableware);
	    return "redirect:/Tableware/getAll";
	}
	
	@PostMapping("/add")
	protected String addTablewareAndStocks(
			@RequestParam("tableware_name") String tablewareName, 
			@RequestParam("tableware_deposit") Integer tablewareDeposit, 
			@RequestParam("tableware_image") MultipartFile timg,
			@RequestParam("tableware_description") String tablewareDescription,
			@RequestParam Map<String, String> allParams,
			Model model) throws IOException {
		
		Tableware tableware = new Tableware();
		tableware.setTablewareName(tablewareName);
		tableware.setTablewareDeposit(tablewareDeposit);
		tableware.setTablewareDescription(tablewareDescription);
		tableware.setTablewareStatus(1);
		
		String uploadPath = "C:/upload/tablewareIMG";
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs(); // 确保递归创建目录
		}
		if (!timg.isEmpty()) {
			String fileName = timg.getOriginalFilename();
			String extension = fileName.substring(fileName.lastIndexOf("."));
			String newFileName = tablewareName + "_" + System.currentTimeMillis() + extension;
			File filePart = new File(uploadPath + File.separator + newFileName);
			timg.transferTo(filePart);
			tableware.setTablewareImage("/EEIT187-6/tablewareIMG/" + newFileName);
		}
		tablewareService.addTableware(tableware);
		
		int tablewareId = tableware.getTablewareId();
		
		List<String> restaurantNameParams = new ArrayList<>();
		List<String> stockParams = new ArrayList<>();
		
		for (Map.Entry<String, String> entry : allParams.entrySet()) {
			if (entry.getKey().startsWith("restaurantName")) {
				restaurantNameParams.add(entry.getValue());
			} else if (entry.getKey().startsWith("stock")) {
				stockParams.add(entry.getValue());
			}
		}
		
		List<TablewareStock> tablewareStocks = new ArrayList<>();
		for (int i = 0; i < restaurantNameParams.size(); i++) {
			String restaurantNameParam = restaurantNameParams.get(i);
			String stockParam = stockParams.get(i);
			System.out.println(restaurantNameParam);
			if (restaurantNameParam != null && !restaurantNameParam.isEmpty() && stockParam != null && !stockParam.isEmpty()) {
//				Integer restaurantId = restaurantService.getRestaurantId(restaurantNameParam);
				Integer restaurantId =null;
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
		return "redirect:/Tableware/showAll";
	}
}
