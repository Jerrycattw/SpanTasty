package com.eatspan.SpanTasty.service.discount;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatspan.SpanTasty.dto.discount.PointCenterDTO;
import com.eatspan.SpanTasty.dto.discount.PointMemberDTO;
import com.eatspan.SpanTasty.dto.discount.PointMemberProjection;
import com.eatspan.SpanTasty.entity.discount.Point;
import com.eatspan.SpanTasty.entity.discount.PointSet;
import com.eatspan.SpanTasty.repository.discount.PointRepository;
import com.eatspan.SpanTasty.utils.discount.DateUtils;



@Service
public class PointService {
	
	@Autowired
	private PointSetService pointSetService;
	
	@Autowired
	private PointRepository pointRepo;
	
	private static PointSet currentPointSet;
	
	//pointCenter(點數中心使用)
	public  Map<String, Object> pointCenterResult() {
		Map<String, Object> resultMap = new HashMap<String,Object>();
		//用於活動列表
		List<PointCenterDTO> pointsByTransaction = pointRepo.sumPointsByTrans();
		pointsByTransaction.stream()
			.forEach(PointCenterDTO::calculateAndSetUsedPercentage);
		resultMap.put("pointsByTransaction", pointsByTransaction);
		
		//用於計算全部
		PointCenterDTO pointsAll = pointRepo.sumPointsAll();
		pointsAll.calculateAndSetUsedPercentage();
		resultMap.put("pointsAll", pointsAll);
		
		//簡略點數設定
		currentPointSet = pointSetService.findAllPointSet();
		String setMessage = "每"+currentPointSet.getAmountPerPoint()+"元，累計"+currentPointSet.getPointsEarned()+"點";
		resultMap.put("simpleSet",setMessage);
		
		//點數會員紀錄
		List<PointMemberDTO> pointMembers = getAllPointMember();
		resultMap.put("pointMembers",pointMembers);
		
		return resultMap;
	}
	
	
	
	//convertDTO ponintMeberDTO(會員紀錄總攬使用)
	private PointMemberDTO convertToDTO(PointMemberProjection projection) {
		return new PointMemberDTO(	
			projection.getMemberId(),
            projection.getMemberName(),
            projection.getPhone(),
            projection.getTotalPointBalance(),
            projection.getExpiringPoints(),
            projection.getExpiryDate()
	        );
	    }
	
	//convertDTO ponintMeberDTO(會員紀錄總攬)
	private List<PointMemberDTO> convertToDTO(List<PointMemberProjection> projections){
		return projections.stream()
	            .map(this::convertToDTO)
	            .collect(Collectors.toList());
	}
	
	//是否能消耗點數
//	public boolean canUsePointOrNot() {
//		return true;
//	}
	
	//消耗點數
	@Transactional
	public void usePoint(Integer pointChange,Integer memberId) throws Exception {
		List<Point> points = pointRepo.findByMemberIdBeforeUsePoint(memberId);
		Integer pointChangeAbs = Math.abs(pointChange);
		
		points.forEach(point -> System.out.println(point));
		
		int totalpoints = points.stream().mapToInt(Point::getPointUsage).sum();
		if(pointChangeAbs > totalpoints) {
			System.out.println("111111111111");
			throw new Exception();
		}
		System.out.println("22222222222222");
		//消耗前的map  //Map<pointId,pointUsage>
		Map<Integer, Integer> pointMap = points.stream()
				.map(Point::pointToMapEntry)
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						Map.Entry::getValue,
						(existing, replacement) -> existing,// 處理重複鍵的情況
						LinkedHashMap::new));
		
		System.out.println(pointMap);
		//消耗後的map
		Map<Integer, Integer> newPointMap = new HashMap<Integer, Integer>();
		
        for (Map.Entry<Integer, Integer> entry : pointMap.entrySet()) {
        	Integer pointId = entry.getKey();
            Integer currentPointUsage = entry.getValue();
            
            if(pointChangeAbs > currentPointUsage) {
				newPointMap.put(pointId, 0);	
				pointChangeAbs -= currentPointUsage;
				System.out.println("333333333333333");
			}else if(pointChangeAbs < currentPointUsage) {
				newPointMap.put(pointId, currentPointUsage-pointChangeAbs);
				pointChangeAbs -= currentPointUsage;
				System.out.println("444444444444444444");
				break;
			}
		}
        System.out.println(newPointMap);
		newPointMap.forEach((pointId,pointUsage)->{
			Optional<Point> optional = pointRepo.findById(pointId);
			if (optional.isPresent()) {
				Point point = optional.get();
				point.setPointUsage(pointUsage);
				pointRepo.save(point);
				System.out.println("5555555555555555555555");
				
			}
		});
		
		
	}
	
	
	// 新增點數紀錄
	public void insertOneRecord(Point point) {
		//使用點數 無到期日
		if(point.getPointChange()<0) {			
			point.setExpiryDate(null);
			pointRepo.save(point);
		}else {
			//增加點數 依設定判斷到期日
			String createDateStr = DateUtils.getStringFromDate(point.getCreateDate());		
			currentPointSet = pointSetService.findAllPointSet();// 取得點數設定		
			String Expiry = currentPointSet.getIsExpiry();// 判斷有無到期
			switch (Expiry) {
				case ("isExpiry"):
					int yearInt = Integer.parseInt(createDateStr.substring(0, 4)) + 1;// 到期日為隔年
					String year = String.valueOf(yearInt);
					String month = currentPointSet.getExpiryMonth();// 取得點數設定月
					String day = currentPointSet.getExpiryDay();// 取得點數設定日
					Date expiryDate = DateUtils.GetDateFromString(year + "-" + month + "-" + day);// yyyy-mm-dd
					point.setExpiryDate(expiryDate);
					
					pointRepo.save(point);
					break;
				case ("noExpiry"):
					Date noExpiryDate = DateUtils.GetDateFromString("9999-12-31");// 無到期日時間無限大
					point.setExpiryDate(noExpiryDate);
				
					pointRepo.save(point);
					break;
				}	
		}
		
	}
	
	// 批次新增點數紀錄
	public void insertBatchRecord(List<String> memberIDs, Point pointBean) {
		for (String memberID : memberIDs) {
			int IntMemberID = Integer.parseInt(memberID);
			Point insertBean = new Point();
			
			insertBean.setMemberId(IntMemberID);
			insertBean.setPointChange(pointBean.getPointChange());
			insertBean.setCreateDate(pointBean.getCreateDate());
			insertBean.setTransactionId(pointBean.getTransactionId());
			insertBean.setTransactionType(pointBean.getTransactionType());
			insertOneRecord(insertBean);
		}
	}
	
	//打印訊息(批次新增)
	public String printMessage(List<String> memberIds) {
		StringBuilder message = new StringBuilder();
		message.append("點數將贈送給").append(memberIds.size()).append("位會員。").append(System.lineSeparator());// 換行
		message.append("會員編號為﹔");
		for (int i = 0; i < memberIds.size(); i++) {
			message.append(memberIds.get(i));
			if (i != memberIds.size() - 1) {
				message.append("、");
			} else {
				message.append("。");
			}
		}
		return message.toString();
	}
	
	// 查詢(會員、點數餘額、電話) all
	public List<PointMemberDTO> getAllPointMember() {
		List<PointMemberProjection> pointMembers = pointRepo.findPointMembers();
		return convertToDTO(pointMembers);
	}
	
	//查詢(會員、點數餘額、電話) bymemberID (return bean)
	public PointMemberDTO  getPointMember(Integer memberId) {
		PointMemberProjection projection = pointRepo.findPointMembersByMemberId(memberId);
		return convertToDTO(projection);
	}
	
	//查詢所有紀錄BY memberID
	public List<Point> getAllRecord(Integer memberId){
		return pointRepo.findByMemberId(memberId);
	}
	
	//查詢紀錄 
	public Point getOneRecord(Integer pointId) {
		Optional<Point> optional = pointRepo.findById(pointId);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	//修改
	@Transactional
	public void updatePoint(Point point) {
		Optional<Point> optional = pointRepo.findById(point.getPointId());
		
		Point updatePoint = optional.get();
		updatePoint.setPointChange(point.getPointChange());
		updatePoint.setCreateDate(point.getCreateDate());
		updatePoint.setExpiryDate(point.getExpiryDate());
		updatePoint.setPointUsage(point.getPointUsage());
		updatePoint.setTransactionId(point.getTransactionId());
		updatePoint.setTransactionType(point.getTransactionType());
		updatePoint.setTransactionDescription(point.getTransactionDescription());
		
	}
	
	public void deleteOneRecord(Integer pointId) {
		pointRepo.deleteById(pointId);
	}
	
	public  List<PointMemberDTO> searchPointMember(String keyWord) {		
		List<PointMemberProjection> projection = pointRepo.searchPointMembers(keyWord);
		return  convertToDTO(projection);
	}
	
}
