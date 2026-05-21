package com.wolguplab.backend.simulation.seoulliving.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = false)
public record SeoulLivingSimulationRequest(
	@NotNull(message = "연봉을 입력해주세요.")
	@Min(value = 1, message = "연봉은 1원 이상이어야 합니다.")
	@Max(value = 500_000_000, message = "연봉은 500,000,000원 이하여야 합니다.")
	Long annualSalary,

	@NotBlank(message = "희망 지역을 선택해주세요.")
	String district,

	@NotNull(message = "보증금을 입력해주세요.")
	@Min(value = 0, message = "보증금은 0원 이상이어야 합니다.")
	@Max(value = 2_000_000_000, message = "보증금은 2,000,000,000원 이하여야 합니다.")
	Long deposit,

	@NotNull(message = "월세를 입력해주세요.")
	@Min(value = 0, message = "월세는 0원 이상이어야 합니다.")
	@Max(value = 20_000_000, message = "월세는 20,000,000원 이하여야 합니다.")
	Long monthlyRent,

	@NotNull(message = "생활비를 입력해주세요.")
	@Min(value = 0, message = "생활비는 0원 이상이어야 합니다.")
	@Max(value = 20_000_000, message = "생활비는 20,000,000원 이하여야 합니다.")
	Long monthlyLivingCost,

	@NotNull(message = "저축 목표를 입력해주세요.")
	@Min(value = 0, message = "저축 목표는 0원 이상이어야 합니다.")
	@Max(value = 20_000_000, message = "저축 목표는 20,000,000원 이하여야 합니다.")
	Long monthlySavingGoal,

	@NotBlank(message = "생활 스타일을 선택해주세요.")
	String lifestyle
) {
}
