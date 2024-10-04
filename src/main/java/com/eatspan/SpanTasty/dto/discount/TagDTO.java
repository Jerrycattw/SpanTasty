package com.eatspan.SpanTasty.dto.discount;

import lombok.Data;
import lombok.Getter;

@Data
public class TagDTO {
	
	private String tagName;
    private String tagType;
    
    
    public TagDTO(String tagName, String tagType) {
        this.tagName = tagName;
        this.tagType = tagType;
    }
}
