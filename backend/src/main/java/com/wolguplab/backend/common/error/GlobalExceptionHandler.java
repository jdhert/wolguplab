package com.wolguplab.backend.common.error;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.exc.MismatchedInputException;
import tools.jackson.databind.exc.UnrecognizedPropertyException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@Bean
	JsonMapperBuilderCustomizer strictRequestJsonMapperBuilderCustomizer() {
		return builder -> builder
			.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
			.disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
		List<FieldErrorResponse> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
			.map(error -> new FieldErrorResponse(error.getField(), error.getDefaultMessage()))
			.toList();

		return validationResponse(fieldErrors);
	}

	@ExceptionHandler(InvalidSimulationInputException.class)
	public ResponseEntity<ApiErrorResponse> handleInvalidSimulationInput(InvalidSimulationInputException exception) {
		return validationResponse(List.of(new FieldErrorResponse(exception.field(), exception.errorDescription())));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
		return validationResponse(List.of(fieldErrorFromNotReadableMessage(exception)));
	}

	private ResponseEntity<ApiErrorResponse> validationResponse(List<FieldErrorResponse> fieldErrors) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiErrorResponse.validation(fieldErrors));
	}

	private FieldErrorResponse fieldErrorFromNotReadableMessage(HttpMessageNotReadableException exception) {
		Optional<JacksonException> jacksonException = findJacksonException(exception);
		if (jacksonException.isEmpty()) {
			return new FieldErrorResponse("request", "요청 형식을 확인해주세요.");
		}

		JacksonException cause = jacksonException.get();
		if (cause instanceof UnrecognizedPropertyException unrecognizedPropertyException) {
			return new FieldErrorResponse(unrecognizedPropertyException.getPropertyName(), "지원하지 않는 입력 항목입니다.");
		}

		return fieldFromJacksonPath(cause)
			.filter(this::isMoneyField)
			.filter(field -> cause instanceof MismatchedInputException mismatch && Long.class.equals(mismatch.getTargetType()))
			.map(field -> new FieldErrorResponse(field, moneyFieldIntegerMessage(field)))
			.orElseGet(() -> new FieldErrorResponse("request", "요청 형식을 확인해주세요."));
	}

	private Optional<JacksonException> findJacksonException(Throwable throwable) {
		Throwable current = throwable;
		while (current != null) {
			if (current instanceof JacksonException jacksonException) {
				return Optional.of(jacksonException);
			}
			current = current.getCause();
		}
		return Optional.empty();
	}

	private Optional<String> fieldFromJacksonPath(JacksonException exception) {
		return exception.getPath().stream()
			.map(JacksonException.Reference::getPropertyName)
			.filter(propertyName -> propertyName != null && !propertyName.isBlank())
			.findFirst();
	}

	private boolean isMoneyField(String field) {
		return switch (field) {
			case "annualSalary", "deposit", "monthlyRent", "monthlyLivingCost", "monthlySavingGoal" -> true;
			default -> false;
		};
	}

	private String moneyFieldIntegerMessage(String field) {
		return switch (field) {
			case "annualSalary" -> "연봉은 원 단위 정수로 입력해주세요.";
			case "deposit" -> "보증금은 원 단위 정수로 입력해주세요.";
			case "monthlyRent" -> "월세는 원 단위 정수로 입력해주세요.";
			case "monthlyLivingCost" -> "생활비는 원 단위 정수로 입력해주세요.";
			case "monthlySavingGoal" -> "저축 목표는 원 단위 정수로 입력해주세요.";
			default -> "금액은 원 단위 정수로 입력해주세요.";
		};
	}
}
