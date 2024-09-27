package com.eatspan.SpanTasty.errHandler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;



//全域錯誤處理 (任何一個指定錯誤會跳至這個controller處理)
@ControllerAdvice
public class PhotosUploadHandler {
	
	
	//處理圖片檔案過大的exception
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String uploadSizeHandler() {
		return "errHandler/photoUpload";
	}
	
	
	
	
}
