package com.wolguplab.backend.simulation.seoulliving.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.wolguplab.backend.simulation.seoulliving.domain.LifestyleType;
import com.wolguplab.backend.simulation.seoulliving.domain.SeoulLivingRiskLevel;

class SeoulLivingCalculatorTest {

	private final SeoulLivingCalculator calculator = new SeoulLivingCalculator();

	@ParameterizedTest
	@MethodSource("salaryBoundaries")
	void calculatesMonthlyNetIncomeBySalaryBoundaries(long annualSalary, long expectedMonthlyNetIncome) {
		SeoulLivingCalculator.Result result = calculate(annualSalary, 10_000_000L, 0L, 0L, 0L, LifestyleType.BALANCED);

		assertThat(result.monthlyNetIncome()).isEqualTo(expectedMonthlyNetIncome);
	}

	@Test
	void annualSalaryOneReturnsMinimumMonthlyNetIncome() {
		SeoulLivingCalculator.Result result = calculate(1L, 10_000_000L, 0L, 0L, 0L, LifestyleType.BALANCED);

		assertThat(result.monthlyNetIncome()).isEqualTo(1L);
	}

	@Test
	void zeroMonthlyRentReturnsZeroHousingCostRatio() {
		SeoulLivingCalculator.Result result = calculate(60_000_000L, 10_000_000L, 0L, 1_500_000L, 700_000L, LifestyleType.BALANCED);

		assertThat(result.housingCostRatio()).isZero();
	}

	@Test
	void exampleRequestReturnsExactCalculatedReportValues() {
		SeoulLivingCalculator.Result result = calculate(60_000_000L, 10_000_000L, 900_000L, 1_500_000L, 700_000L, LifestyleType.BALANCED);

		assertThat(result.monthlyNetIncome()).isEqualTo(4_100_000L);
		assertThat(result.housingCostRatio()).isEqualTo(22);
		assertThat(result.expectedMonthlySavings()).isEqualTo(1_700_000L);
		assertThat(result.riskLevel()).isEqualTo(SeoulLivingRiskLevel.LOW);
	}

	@Test
	void calculatesDeterministicMediumRisk() {
		SeoulLivingCalculator.Result result = calculate(60_000_000L, 10_000_000L, 1_300_000L, 2_200_000L, 700_000L, LifestyleType.BALANCED);

		assertThat(result.housingCostRatio()).isEqualTo(32);
		assertThat(result.expectedMonthlySavings()).isEqualTo(600_000L);
		assertThat(result.riskLevel()).isEqualTo(SeoulLivingRiskLevel.MEDIUM);
	}

	@Test
	void calculatesDeterministicHighRisk() {
		SeoulLivingCalculator.Result result = calculate(30_000_000L, 10_000_000L, 1_000_000L, 1_500_000L, 300_000L, LifestyleType.BALANCED);

		assertThat(result.housingCostRatio()).isEqualTo(45);
		assertThat(result.expectedMonthlySavings()).isEqualTo(-300_000L);
		assertThat(result.riskLevel()).isEqualTo(SeoulLivingRiskLevel.HIGH);
	}

	@Test
	void addsWarningsInPlanOrder() {
		SeoulLivingCalculator.Result result = calculate(30_000_000L, 1_000_000L, 1_000_000L, 1_500_000L, 300_000L, LifestyleType.BALANCED);

		assertThat(result.warnings()).containsExactly(
			"월세 비중이 실수령 대비 높습니다.",
			"월세와 생활비가 월 실수령을 초과합니다.",
			"초기 보증금 여유가 낮아 선택 가능한 매물이 제한될 수 있습니다."
		);
	}

	@Test
	void addsSavingGoalWarningBeforeDepositWarning() {
		SeoulLivingCalculator.Result result = calculate(60_000_000L, 1_000_000L, 900_000L, 2_600_000L, 1_000_000L, LifestyleType.BALANCED);

		assertThat(result.warnings()).containsExactly(
			"현재 소비 구조에서는 목표 저축액 달성이 어렵습니다.",
			"초기 보증금 여유가 낮아 선택 가능한 매물이 제한될 수 있습니다."
		);
	}

	@Test
	void addsFallbackWarningWhenNoRiskSignalExists() {
		SeoulLivingCalculator.Result result = calculate(60_000_000L, 10_000_000L, 900_000L, 1_500_000L, 700_000L, LifestyleType.BALANCED);

		assertThat(result.warnings()).containsExactly("현재 입력 조건에서 즉시 확인되는 주요 위험 신호는 낮습니다.");
	}

	@Test
	void addsRiskRecommendationFirstAndLifestyleRecommendationSecond() {
		SeoulLivingCalculator.Result result = calculate(30_000_000L, 10_000_000L, 1_000_000L, 1_500_000L, 300_000L, LifestyleType.COMFORTABLE);

		assertThat(result.recommendations()).containsExactly(
			"월세 상한을 낮추거나 희망 지역을 조정한 뒤 다시 계산해보세요.",
			"여유형 생활을 원한다면 월세 상한을 더 보수적으로 잡는 것이 안전합니다."
		);
	}

	private SeoulLivingCalculator.Result calculate(
		long annualSalary,
		long deposit,
		long monthlyRent,
		long monthlyLivingCost,
		long monthlySavingGoal,
		LifestyleType lifestyle
	) {
		return calculator.calculate(annualSalary, deposit, monthlyRent, monthlyLivingCost, monthlySavingGoal, lifestyle);
	}

	private static Stream<Arguments> salaryBoundaries() {
		return Stream.of(
			Arguments.of(30_000_000L, 2_200_000L),
			Arguments.of(30_000_001L, 2_125_000L),
			Arguments.of(50_000_000L, 3_541_667L),
			Arguments.of(50_000_001L, 3_416_667L),
			Arguments.of(70_000_000L, 4_783_333L),
			Arguments.of(70_000_001L, 4_550_000L)
		);
	}
}
