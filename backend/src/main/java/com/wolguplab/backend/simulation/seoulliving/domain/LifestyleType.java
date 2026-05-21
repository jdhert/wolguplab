package com.wolguplab.backend.simulation.seoulliving.domain;

import java.util.Arrays;

import com.wolguplab.backend.common.error.InvalidSimulationInputException;

public enum LifestyleType {
	FRUGAL("절약형", "절약형 생활을 유지하되 고정비가 늘어나는 항목을 먼저 점검하세요."),
	BALANCED("균형형", "균형형 생활을 유지하려면 월세와 저축 목표를 함께 관리하세요."),
	COMFORTABLE("여유형", "여유형 생활을 원한다면 월세 상한을 더 보수적으로 잡는 것이 안전합니다.");

	private final String label;
	private final String recommendation;

	LifestyleType(String label, String recommendation) {
		this.label = label;
		this.recommendation = recommendation;
	}

	public String label() {
		return label;
	}

	public String recommendation() {
		return recommendation;
	}

	public static LifestyleType fromLabel(String label) {
		return Arrays.stream(values())
			.filter(type -> type.label.equals(label))
			.findFirst()
			.orElseThrow(() -> new InvalidSimulationInputException("lifestyle", "지원하지 않는 생활 스타일입니다."));
	}
}
