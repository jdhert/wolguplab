package com.wolguplab.backend.simulation.seoulliving.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.wolguplab.backend.common.error.GlobalExceptionHandler;
import com.wolguplab.backend.simulation.seoulliving.calculator.SeoulLivingCalculator;
import com.wolguplab.backend.simulation.seoulliving.service.SeoulLivingSimulationService;

@WebMvcTest(SeoulLivingSimulationController.class)
@Import({SeoulLivingSimulationService.class, SeoulLivingCalculator.class, GlobalExceptionHandler.class})
class SeoulLivingSimulationControllerTest {

	private static final String ENDPOINT = "/api/simulations/seoul-living";

	@Autowired
	private MockMvc mockMvc;

	@Test
	void validRequestReturnsAllReportFields() throws Exception {
		mockMvc.perform(post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(validRequestJson()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.*", hasSize(9)))
			.andExpect(jsonPath("$.grade").isString())
			.andExpect(jsonPath("$.headline").isString())
			.andExpect(jsonPath("$.districtLabel").isString())
			.andExpect(jsonPath("$.riskLevel").isString())
			.andExpect(jsonPath("$.monthlyNetIncome").isNumber())
			.andExpect(jsonPath("$.housingCostRatio").isNumber())
			.andExpect(jsonPath("$.expectedMonthlySavings").isNumber())
			.andExpect(jsonPath("$.warnings").isArray())
			.andExpect(jsonPath("$.recommendations").isArray());
	}

	@Test
	void exampleRequestReturnsExactExampleResponseValues() throws Exception {
		mockMvc.perform(post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(validRequestJson()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.grade").value("가능"))
			.andExpect(jsonPath("$.headline").value("현재 조건에서는 서울 자취가 비교적 안정적입니다."))
			.andExpect(jsonPath("$.districtLabel").value("마포구"))
			.andExpect(jsonPath("$.riskLevel").value("LOW"))
			.andExpect(jsonPath("$.monthlyNetIncome").value(4_100_000))
			.andExpect(jsonPath("$.housingCostRatio").value(22))
			.andExpect(jsonPath("$.expectedMonthlySavings").value(1_700_000))
			.andExpect(jsonPath("$.warnings", hasSize(1)))
			.andExpect(jsonPath("$.warnings[0]").value("현재 입력 조건에서 즉시 확인되는 주요 위험 신호는 낮습니다."))
			.andExpect(jsonPath("$.recommendations", hasSize(2)))
			.andExpect(jsonPath("$.recommendations[0]").value("현재 수준을 유지하되 비상금과 이사 초기 비용을 별도로 준비하세요."))
			.andExpect(jsonPath("$.recommendations[1]").value("균형형 생활을 유지하려면 월세와 저축 목표를 함께 관리하세요."));
	}

	@Test
	void missingAnnualSalaryReturnsValidationError() throws Exception {
		String json = """
			{
			  "district": "마포구",
			  "deposit": 10000000,
			  "monthlyRent": 900000,
			  "monthlyLivingCost": 1500000,
			  "monthlySavingGoal": 700000,
			  "lifestyle": "균형형"
			}
			""";

		performAndExpectFieldValidationError(json, "annualSalary");
	}

	@ParameterizedTest
	@ValueSource(longs = {0L, -1L})
	void annualSalaryLessThanOneReturnsValidationError(long annualSalary) throws Exception {
		performAndExpectFieldValidationError(validRequestJson("annualSalary", Long.toString(annualSalary)), "annualSalary");
	}

	@Test
	void annualSalaryGreaterThanMaximumReturnsValidationError() throws Exception {
		performAndExpectFieldValidationError(validRequestJson("annualSalary", "500000001"), "annualSalary");
	}

	@ParameterizedTest
	@ValueSource(strings = {"deposit", "monthlyRent", "monthlyLivingCost", "monthlySavingGoal"})
	void negativeMonthlyMoneyFieldsReturnValidationError(String field) throws Exception {
		performAndExpectFieldValidationError(validRequestJson(field, "-1"), field);
	}

	@Test
	void decimalMoneyValueReturnsValidationErrorWithoutStackTrace() throws Exception {
		mockMvc.perform(post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(validRequestJson("monthlyRent", "900000.5")))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
			.andExpect(jsonPath("$.message").value("입력값을 확인해주세요."))
			.andExpect(jsonPath("$.fieldErrors", hasSize(1)))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("monthlyRent"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("월세는 원 단위 정수로 입력해주세요."))
			.andExpect(content().string(not(containsString("Exception"))))
			.andExpect(content().string(not(containsString("HttpMessageNotReadable"))))
			.andExpect(content().string(not(containsString("trace"))));
	}

	@Test
	void invalidDistrictReturnsValidationErrorWithDistrictField() throws Exception {
		performAndExpectFieldValidationError(validRequestJson("district", "\"부산진구\""), "district");
	}

	@Test
	void invalidLifestyleReturnsValidationErrorWithLifestyleField() throws Exception {
		performAndExpectFieldValidationError(validRequestJson("lifestyle", "\"극단형\""), "lifestyle");
	}

	@Test
	void malformedJsonReturnsValidationErrorWithoutStackTrace() throws Exception {
		mockMvc.perform(post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ invalid json"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
			.andExpect(jsonPath("$.message").value("입력값을 확인해주세요."))
			.andExpect(jsonPath("$.fieldErrors", hasSize(1)))
			.andExpect(content().string(not(containsString("Exception"))))
			.andExpect(content().string(not(containsString("JsonParse"))))
			.andExpect(content().string(not(containsString("trace"))));
	}

	@Test
	void unknownJsonFieldReturnsValidationErrorToKeepContractClosed() throws Exception {
		String json = """
			{
			  "annualSalary": 60000000,
			  "district": "마포구",
			  "deposit": 10000000,
			  "monthlyRent": 900000,
			  "monthlyLivingCost": 1500000,
			  "monthlySavingGoal": 700000,
			  "lifestyle": "균형형",
			  "extraField": "not allowed"
			}
			""";

		mockMvc.perform(post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
			.andExpect(jsonPath("$.message").value("입력값을 확인해주세요."))
			.andExpect(jsonPath("$.fieldErrors", hasSize(1)))
			.andExpect(jsonPath("$.fieldErrors[0].field").value("extraField"))
			.andExpect(jsonPath("$.fieldErrors[0].message").value("지원하지 않는 입력 항목입니다."));
	}

	@Test
	void webMvcTestDoesNotRequirePostgreSql() throws Exception {
		mockMvc.perform(post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(validRequestJson()))
			.andExpect(status().isOk());
	}

	private void performAndExpectFieldValidationError(String json, String field) throws Exception {
		mockMvc.perform(post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
			.andExpect(jsonPath("$.message").value("입력값을 확인해주세요."))
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[0].field").value(field))
			.andExpect(jsonPath("$.fieldErrors[0].message").isString())
			.andExpect(content().string(not(containsString("Exception"))))
			.andExpect(content().string(not(containsString("trace"))));
	}

	private String validRequestJson() {
		return """
			{
			  "annualSalary": 60000000,
			  "district": "마포구",
			  "deposit": 10000000,
			  "monthlyRent": 900000,
			  "monthlyLivingCost": 1500000,
			  "monthlySavingGoal": 700000,
			  "lifestyle": "균형형"
			}
			""";
	}

	private String validRequestJson(String field, String replacementValue) {
		return switch (field) {
			case "annualSalary" -> validRequestJson().replace("\"annualSalary\": 60000000", "\"annualSalary\": " + replacementValue);
			case "district" -> validRequestJson().replace("\"district\": \"마포구\"", "\"district\": " + replacementValue);
			case "deposit" -> validRequestJson().replace("\"deposit\": 10000000", "\"deposit\": " + replacementValue);
			case "monthlyRent" -> validRequestJson().replace("\"monthlyRent\": 900000", "\"monthlyRent\": " + replacementValue);
			case "monthlyLivingCost" -> validRequestJson().replace("\"monthlyLivingCost\": 1500000", "\"monthlyLivingCost\": " + replacementValue);
			case "monthlySavingGoal" -> validRequestJson().replace("\"monthlySavingGoal\": 700000", "\"monthlySavingGoal\": " + replacementValue);
			case "lifestyle" -> validRequestJson().replace("\"lifestyle\": \"균형형\"", "\"lifestyle\": " + replacementValue);
			default -> throw new IllegalArgumentException("Unsupported field: " + field);
		};
	}
}
