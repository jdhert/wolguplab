package com.wolguplab.backend.simulation.seoulliving.service;

import org.springframework.stereotype.Service;

import com.wolguplab.backend.simulation.seoulliving.calculator.SeoulLivingCalculator;
import com.wolguplab.backend.simulation.seoulliving.domain.LifestyleType;
import com.wolguplab.backend.simulation.seoulliving.domain.SeoulDistrict;
import com.wolguplab.backend.simulation.seoulliving.dto.SeoulLivingSimulationRequest;
import com.wolguplab.backend.simulation.seoulliving.dto.SeoulLivingSimulationResponse;

@Service
public class SeoulLivingSimulationService {

	private final SeoulLivingCalculator calculator;

	public SeoulLivingSimulationService(SeoulLivingCalculator calculator) {
		this.calculator = calculator;
	}

	public SeoulLivingSimulationResponse simulate(SeoulLivingSimulationRequest request) {
		SeoulDistrict district = SeoulDistrict.fromLabel(request.district());
		LifestyleType lifestyle = LifestyleType.fromLabel(request.lifestyle());
		SeoulLivingCalculator.Result result = calculator.calculate(
			request.annualSalary(),
			request.deposit(),
			request.monthlyRent(),
			request.monthlyLivingCost(),
			request.monthlySavingGoal(),
			lifestyle
		);

		return new SeoulLivingSimulationResponse(
			result.riskLevel().grade(),
			result.riskLevel().headline(),
			district.label(),
			result.riskLevel(),
			result.monthlyNetIncome(),
			result.housingCostRatio(),
			result.expectedMonthlySavings(),
			result.warnings(),
			result.recommendations()
		);
	}
}
