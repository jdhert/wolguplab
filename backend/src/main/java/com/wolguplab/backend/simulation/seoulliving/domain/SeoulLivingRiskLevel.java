package com.wolguplab.backend.simulation.seoulliving.domain;

public enum SeoulLivingRiskLevel {
	LOW(
		"가능",
		"현재 조건에서는 서울 자취가 비교적 안정적입니다.",
		"현재 수준을 유지하되 비상금과 이사 초기 비용을 별도로 준비하세요."
	),
	MEDIUM(
		"주의 필요",
		"서울 자취는 가능하지만 월세와 소비 조정이 필요합니다.",
		"월세와 생활비를 함께 조정해 목표 저축액을 먼저 확보해보세요."
	),
	HIGH(
		"위험",
		"현재 조건에서는 서울 자취 부담이 커서 조건 조정이 필요합니다.",
		"월세 상한을 낮추거나 희망 지역을 조정한 뒤 다시 계산해보세요."
	);

	private final String grade;
	private final String headline;
	private final String recommendation;

	SeoulLivingRiskLevel(String grade, String headline, String recommendation) {
		this.grade = grade;
		this.headline = headline;
		this.recommendation = recommendation;
	}

	public String grade() {
		return grade;
	}

	public String headline() {
		return headline;
	}

	public String recommendation() {
		return recommendation;
	}
}
