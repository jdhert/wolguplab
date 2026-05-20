package com.wolguplab.backend.simulation.seoulliving.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.wolguplab.backend.common.error.InvalidSimulationInputException;
import com.wolguplab.backend.simulation.seoulliving.calculator.SeoulLivingCalculator;
import com.wolguplab.backend.simulation.seoulliving.domain.SeoulDistrict;
import com.wolguplab.backend.simulation.seoulliving.dto.SeoulLivingSimulationRequest;
import com.wolguplab.backend.simulation.seoulliving.dto.SeoulLivingSimulationResponse;

class SeoulLivingSimulationServiceTest {

	private final SeoulLivingSimulationService service = new SeoulLivingSimulationService(new SeoulLivingCalculator());

	@ParameterizedTest
	@MethodSource("districtLabels")
	void acceptsAllTwentyFiveSeoulDistrictLabels(String districtLabel) {
		SeoulLivingSimulationResponse response = service.simulate(validRequest(districtLabel, "균형형"));

		assertThat(response.districtLabel()).isEqualTo(districtLabel);
	}

	@Test
	void rejectsUnknownDistrictWithDistrictField() {
		assertThatThrownBy(() -> service.simulate(validRequest("부산진구", "균형형")))
			.isInstanceOfSatisfying(InvalidSimulationInputException.class, exception -> {
				assertThat(exception.field()).isEqualTo("district");
				assertThat(exception.errorDescription()).isEqualTo("지원하지 않는 서울 자치구입니다.");
			});
	}

	@ParameterizedTest
	@ValueSource(strings = {"절약형", "균형형", "여유형"})
	void acceptsLifestyleLabels(String lifestyle) {
		SeoulLivingSimulationResponse response = service.simulate(validRequest("마포구", lifestyle));

		assertThat(response.recommendations()).hasSize(2);
	}

	@Test
	void rejectsUnknownLifestyleWithLifestyleField() {
		assertThatThrownBy(() -> service.simulate(validRequest("마포구", "극단형")))
			.isInstanceOfSatisfying(InvalidSimulationInputException.class, exception -> {
				assertThat(exception.field()).isEqualTo("lifestyle");
				assertThat(exception.errorDescription()).isEqualTo("지원하지 않는 생활 스타일입니다.");
			});
	}

	@Test
	void echoesDistrictLabelFromRequest() {
		SeoulLivingSimulationResponse response = service.simulate(validRequest("성동구", "균형형"));

		assertThat(response.districtLabel()).isEqualTo("성동구");
	}

	@ParameterizedTest
	@MethodSource("lifestyleRecommendationCases")
	void lifestyleChangesRecommendationTextOnly(String lifestyle, String expectedLifestyleRecommendation) {
		SeoulLivingSimulationResponse balancedBaseline = service.simulate(validRequest("마포구", "균형형"));
		SeoulLivingSimulationResponse response = service.simulate(validRequest("마포구", lifestyle));

		assertThat(response.grade()).isEqualTo(balancedBaseline.grade());
		assertThat(response.headline()).isEqualTo(balancedBaseline.headline());
		assertThat(response.districtLabel()).isEqualTo(balancedBaseline.districtLabel());
		assertThat(response.riskLevel()).isEqualTo(balancedBaseline.riskLevel());
		assertThat(response.monthlyNetIncome()).isEqualTo(balancedBaseline.monthlyNetIncome());
		assertThat(response.housingCostRatio()).isEqualTo(balancedBaseline.housingCostRatio());
		assertThat(response.expectedMonthlySavings()).isEqualTo(balancedBaseline.expectedMonthlySavings());
		assertThat(response.warnings()).isEqualTo(balancedBaseline.warnings());
		assertThat(response.recommendations().get(0)).isEqualTo(balancedBaseline.recommendations().get(0));
		assertThat(response.recommendations().get(1)).isEqualTo(expectedLifestyleRecommendation);
	}

	@Test
	void serviceDoesNotUsePersistenceDependencies() {
		List<String> fieldTypeNames = Arrays.stream(SeoulLivingSimulationService.class.getDeclaredFields())
			.map(Field::getType)
			.map(Class::getName)
			.toList();

		assertThat(fieldTypeNames).containsExactly("com.wolguplab.backend.simulation.seoulliving.calculator.SeoulLivingCalculator");
		assertThat(fieldTypeNames).noneMatch(name -> name.contains("Repository") || name.contains("EntityManager"));
	}

	private SeoulLivingSimulationRequest validRequest(String district, String lifestyle) {
		return new SeoulLivingSimulationRequest(
			60_000_000L,
			district,
			10_000_000L,
			900_000L,
			1_500_000L,
			700_000L,
			lifestyle
		);
	}

	private static Stream<String> districtLabels() {
		return Arrays.stream(SeoulDistrict.values()).map(SeoulDistrict::label);
	}

	private static Stream<Arguments> lifestyleRecommendationCases() {
		return Stream.of(
			Arguments.of("절약형", "절약형 생활을 유지하되 고정비가 늘어나는 항목을 먼저 점검하세요."),
			Arguments.of("균형형", "균형형 생활을 유지하려면 월세와 저축 목표를 함께 관리하세요."),
			Arguments.of("여유형", "여유형 생활을 원한다면 월세 상한을 더 보수적으로 잡는 것이 안전합니다.")
		);
	}
}
