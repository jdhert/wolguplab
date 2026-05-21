package com.wolguplab.backend.simulation.seoulliving.calculator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.wolguplab.backend.simulation.seoulliving.domain.LifestyleType;
import com.wolguplab.backend.simulation.seoulliving.domain.SeoulLivingRiskLevel;

@Component
public class SeoulLivingCalculator {

	public Result calculate(
		Long annualSalary,
		Long deposit,
		Long monthlyRent,
		Long monthlyLivingCost,
		Long monthlySavingGoal,
		LifestyleType lifestyle
	) {
		long monthlyNetIncome = calculateMonthlyNetIncome(annualSalary);
		int housingCostRatio = calculateHousingCostRatio(monthlyRent, monthlyNetIncome);
		long expectedMonthlySavings = monthlyNetIncome - monthlyRent - monthlyLivingCost;
		SeoulLivingRiskLevel riskLevel = determineRiskLevel(
			housingCostRatio,
			expectedMonthlySavings,
			monthlySavingGoal,
			monthlyNetIncome
		);

		return new Result(
			monthlyNetIncome,
			housingCostRatio,
			expectedMonthlySavings,
			riskLevel,
			createWarnings(housingCostRatio, expectedMonthlySavings, monthlySavingGoal, deposit),
			List.of(riskLevel.recommendation(), lifestyle.recommendation())
		);
	}

	long calculateMonthlyNetIncome(Long annualSalary) {
		double multiplier = salaryMultiplier(annualSalary);
		return Math.max(1L, Math.round(annualSalary * multiplier / 12));
	}

	private double salaryMultiplier(Long annualSalary) {
		if (annualSalary <= 30_000_000L) {
			return 0.88;
		}
		if (annualSalary <= 50_000_000L) {
			return 0.85;
		}
		if (annualSalary <= 70_000_000L) {
			return 0.82;
		}
		return 0.78;
	}

	private int calculateHousingCostRatio(Long monthlyRent, long monthlyNetIncome) {
		if (monthlyRent == 0) {
			return 0;
		}
		return Math.toIntExact(Math.round((double) monthlyRent / monthlyNetIncome * 100));
	}

	private SeoulLivingRiskLevel determineRiskLevel(
		int housingCostRatio,
		long expectedMonthlySavings,
		Long monthlySavingGoal,
		long monthlyNetIncome
	) {
		long minimumStableSavings = Math.round(monthlyNetIncome * 0.15);
		if (housingCostRatio <= 25
			&& expectedMonthlySavings >= monthlySavingGoal
			&& expectedMonthlySavings >= minimumStableSavings) {
			return SeoulLivingRiskLevel.LOW;
		}
		if (housingCostRatio <= 35 && expectedMonthlySavings >= 0) {
			return SeoulLivingRiskLevel.MEDIUM;
		}
		return SeoulLivingRiskLevel.HIGH;
	}

	private List<String> createWarnings(
		int housingCostRatio,
		long expectedMonthlySavings,
		Long monthlySavingGoal,
		Long deposit
	) {
		List<String> warnings = new ArrayList<>();
		if (housingCostRatio > 30) {
			warnings.add("월세 비중이 실수령 대비 높습니다.");
		}
		if (expectedMonthlySavings < 0) {
			warnings.add("월세와 생활비가 월 실수령을 초과합니다.");
		}
		if (expectedMonthlySavings >= 0 && expectedMonthlySavings < monthlySavingGoal) {
			warnings.add("현재 소비 구조에서는 목표 저축액 달성이 어렵습니다.");
		}
		if (deposit < 5_000_000L) {
			warnings.add("초기 보증금 여유가 낮아 선택 가능한 매물이 제한될 수 있습니다.");
		}
		if (warnings.isEmpty()) {
			warnings.add("현재 입력 조건에서 즉시 확인되는 주요 위험 신호는 낮습니다.");
		}
		return warnings;
	}

	public record Result(
		Long monthlyNetIncome,
		Integer housingCostRatio,
		Long expectedMonthlySavings,
		SeoulLivingRiskLevel riskLevel,
		List<String> warnings,
		List<String> recommendations
	) {
	}
}
