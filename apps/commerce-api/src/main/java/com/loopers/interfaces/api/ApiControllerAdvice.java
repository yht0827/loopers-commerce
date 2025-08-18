package com.loopers.interfaces.api;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ApiControllerAdvice {
	@ExceptionHandler
	public ResponseEntity<ApiResponse<?>> handle(CoreException e) {
		log.warn("CoreException : {}", e.getCustomMessage() != null ? e.getCustomMessage() : e.getMessage(), e);
		return failureResponse(e.getErrorType(), e.getCustomMessage());
	}

	@ExceptionHandler
	public ResponseEntity<ApiResponse<?>> handleBadRequest(MethodArgumentTypeMismatchException e) {
		String name = e.getName();
		String type = e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "unknown";
		String value = e.getValue() != null ? e.getValue().toString() : "null";
		String message = String.format("요청 파라미터 '%s' (타입: %s)의 값 '%s'이(가) 잘못되었습니다.", name, type, value);
		return failureResponse(ErrorType.BAD_REQUEST, message);
	}

	@ExceptionHandler
	public ResponseEntity<ApiResponse<?>> handleBadRequest(MissingServletRequestParameterException e) {
		String name = e.getParameterName();
		String type = e.getParameterType();
		String message = String.format("필수 요청 파라미터 '%s' (타입: %s)가 누락되었습니다.", name, type);
		return failureResponse(ErrorType.BAD_REQUEST, message);
	}

	@ExceptionHandler
	public ResponseEntity<ApiResponse<?>> handleBadRequest(MethodArgumentNotValidException e) {
		String message = e.getBindingResult().getFieldErrors().stream()
			.map(error -> {
				String field = error.getField();
				String defaultMessage = error.getDefaultMessage();
				return String.format("필드 '%s': %s", field, defaultMessage);
			})
			.findFirst()
			.orElse("요청 데이터가 유효하지 않습니다.");

		return failureResponse(ErrorType.BAD_REQUEST, message);
	}

	@ExceptionHandler
	public ResponseEntity<ApiResponse<?>> handleBadRequest(HttpMessageNotReadableException e) {
		String errorMessage;
		Throwable rootCause = e.getRootCause();

		switch (rootCause) {
			case InvalidFormatException invalidFormat -> {
				String fieldName = invalidFormat.getPath().stream()
					.map(ref -> ref.getFieldName() != null ? ref.getFieldName() : "?")
					.collect(Collectors.joining("."));

				String valueIndicationMessage = "";
				if (invalidFormat.getTargetType().isEnum()) {
					Class<?> enumClass = invalidFormat.getTargetType();
					String enumValues = Arrays.stream(enumClass.getEnumConstants())
						.map(Object::toString)
						.collect(Collectors.joining(", "));
					valueIndicationMessage = "사용 가능한 값 : [" + enumValues + "]";
				}

				String expectedType = invalidFormat.getTargetType().getSimpleName();
				Object value = invalidFormat.getValue();

				errorMessage = String.format("필드 '%s'의 값 '%s'이(가) 예상 타입(%s)과 일치하지 않습니다. %s",
					fieldName, value, expectedType, valueIndicationMessage);
			}
			case MismatchedInputException mismatchedInput -> {
				String fieldPath = mismatchedInput.getPath().stream()
					.map(ref -> ref.getFieldName() != null ? ref.getFieldName() : "?")
					.collect(Collectors.joining("."));
				errorMessage = String.format("필수 필드 '%s'이(가) 누락되었습니다.", fieldPath);

			}
			case JsonMappingException jsonMapping -> {
				String fieldPath = jsonMapping.getPath().stream()
					.map(ref -> ref.getFieldName() != null ? ref.getFieldName() : "?")
					.collect(Collectors.joining("."));
				errorMessage = String.format("필드 '%s'에서 JSON 매핑 오류가 발생했습니다: %s",
					fieldPath, jsonMapping.getOriginalMessage());

			}
			case null, default -> errorMessage = "요청 본문을 처리하는 중 오류가 발생했습니다. JSON 메세지 규격을 확인해주세요.";
		}

		return failureResponse(ErrorType.BAD_REQUEST, errorMessage);
	}

	@ExceptionHandler
	public ResponseEntity<ApiResponse<?>> handleBadRequest(ServerWebInputException e) {
		String missingParams = extractMissingParameter(e.getReason() != null ? e.getReason() : "");
		if (!missingParams.isEmpty()) {
			String message = String.format("필수 요청 값 '%s'가 누락되었습니다.", missingParams);
			return failureResponse(ErrorType.BAD_REQUEST, message);
		} else {
			return failureResponse(ErrorType.BAD_REQUEST, null);
		}
	}

	@ExceptionHandler
	public ResponseEntity<ApiResponse<?>> handleBadRequest(MissingRequestHeaderException e) {
		String name = e.getHeaderName();
		String message = String.format("필수 요청 헤더 '%s'가 누락되었습니다.", name);
		return failureResponse(ErrorType.BAD_REQUEST, message);
	}

	@ExceptionHandler
	public ResponseEntity<ApiResponse<?>> handleNotFound(NoResourceFoundException e) {
		return failureResponse(ErrorType.NOT_FOUND, null);
	}

	@ExceptionHandler
	public ResponseEntity<ApiResponse<?>> handle(Throwable e) {
		log.error("Exception : {}", e.getMessage(), e);
		return failureResponse(ErrorType.INTERNAL_ERROR, null);
	}

	private String extractMissingParameter(String message) {
		Pattern pattern = Pattern.compile("'(.+?)'");
		Matcher matcher = pattern.matcher(message);
		return matcher.find() ? matcher.group(1) : "";
	}

	private ResponseEntity<ApiResponse<?>> failureResponse(ErrorType errorType, String errorMessage) {
		return ResponseEntity.status(errorType.getStatus())
			.body(ApiResponse.fail(errorType.getCode(), errorMessage != null ? errorMessage : errorType.getMessage()));
	}
}
