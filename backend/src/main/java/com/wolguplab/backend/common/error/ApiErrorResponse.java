package com.wolguplab.backend.common.error;

import java.util.List;

public record ApiErrorResponse(
	String code,
	String message,
	List<FieldErrorResponse> fieldErrors
) {
	public static ApiErrorResponse validation(List<FieldErrorResponse> fieldErrors) {
		return new ApiErrorResponse("VALIDATION_ERROR", "입력값을 확인해주세요.", fieldErrors);
	}
}
