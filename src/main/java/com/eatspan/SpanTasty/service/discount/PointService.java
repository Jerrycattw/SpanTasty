package com.eatspan.SpanTasty.service.discount;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	private List<PointMemberDTO> convertToDTO(List<PointMemberProjection> projections){
		return projections.stream()
	            .map(this::convertToDTO)
	            .collect(Collectors.toList());
	}
	
	// 新增點數紀錄
	public void insertOneRecord(Point point) {

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
		System.out.println(convertToDTO(projection));
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
		
	}
	
	public void deleteOneRecord(Integer pointId) {
		pointRepo.deleteById(pointId);
	}
	
	public  List<PointMemberDTO> searchPointMember(String keyWord) {		
		List<PointMemberProjection> projection = pointRepo.searchPointMembers(keyWord);
		return  convertToDTO(projection);
	}
	
}
