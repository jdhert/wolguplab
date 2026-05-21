package com.wolguplab.backend.simulation.seoulliving.dto;

import java.util.List;

import com.wolguplab.backend.simulation.seoulliving.domain.SeoulLivingRiskLevel;

public record SeoulLivingSimulationResponse(
	String grade,
	String headline,
	String districtLabel,
	SeoulLivingRiskLevel riskLevel,
	Long monthlyNetIncome,
	Integer housingCostRatio,
	Long expectedMonthlySavings,
	List<String> warnings,
	List<String> recommendations
) {
}
