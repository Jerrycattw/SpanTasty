package com.eatspan.SpanTasty.enums;

public enum PointTextVariable {
	
	AMOUNT("點數名稱","{AMOUNT}"),
	POINT("點數回饋","{POINT}"),
	EXPIRE_DATE("到期日","{EXPIRE_DATE}"),
	BIRTH_PERIOD("生日期間","{BIRTH_PERIOD}"),
	MULTIPLY_POINT("生日倍數","{MULTIPLY_POINT}"),
	SPACE("換行","{br}");
	
	
	
	private PointTextVariable(String textName, String varibale) {
		this.textName = textName;
		this.varibale = varibale;
	}
	public String getTextName() {
		return textName;
	}
	public String getVaribale() {
		return varibale;
	}
	
	private final String textName;
	private final String varibale;
	
	
	
}
