package com.eatspan.SpanTasty.dto.order;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AddMenuDto {
	
	private String foodName;
	private MultipartFile foodPicture;
	private Integer foodPrice;
	private Integer foodKindId;
	private Integer foodStock;
	private String foodDescription;
	private Integer foodStatus;

}
