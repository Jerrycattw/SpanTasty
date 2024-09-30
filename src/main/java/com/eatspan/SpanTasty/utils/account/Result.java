package com.eatspan.SpanTasty.utils.account;

public class Result<T> {
	private boolean success;
	private String message;
	private T data;

	// Constructors
	public Result() {
	}

	public Result(boolean success, String message, T data) {
		this.success = success;
		this.message = message;
		this.data = data;
	}

	// Getters and Setters
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public static <T> Result<T> success() {
		return new Result<>(true, "操作成功", null);
	}

	public static <T> Result<T> success(String message) {
		return new Result<>(true, message, null);
	}

	public static <T> Result<T> success(T data) {
		return new Result<>(true, "操作成功", data);
	}

	public static <T> Result<T> success(String message, T data) {
		return new Result<>(true, message, data);
	}

	public static <T> Result<T> failure() {
		return new Result<>(false, "操作失敗", null);
	}

	public static <T> Result<T> failure(String message) {
		return new Result<>(false, message, null);
	}

	public static <T> Result<T> failure(String message, T data) {
		return new Result<>(false, message, data);
	}
}
