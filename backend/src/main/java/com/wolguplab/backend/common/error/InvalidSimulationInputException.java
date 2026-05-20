package com.wolguplab.backend.common.error;

public class InvalidSimulationInputException extends RuntimeException {

	private final String field;
	private final String errorDescription;

	public InvalidSimulationInputException(String field, String errorDescription) {
		super(errorDescription);
		this.field = field;
		this.errorDescription = errorDescription;
	}

	public String field() {
		return field;
	}

	public String errorDescription() {
		return errorDescription;
	}
}
