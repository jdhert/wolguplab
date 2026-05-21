package com.wolguplab.backend.simulation.seoulliving.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wolguplab.backend.simulation.seoulliving.dto.SeoulLivingSimulationRequest;
import com.wolguplab.backend.simulation.seoulliving.dto.SeoulLivingSimulationResponse;
import com.wolguplab.backend.simulation.seoulliving.service.SeoulLivingSimulationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/simulations/seoul-living")
public class SeoulLivingSimulationController {

	private final SeoulLivingSimulationService service;

	public SeoulLivingSimulationController(SeoulLivingSimulationService service) {
		this.service = service;
	}

	@PostMapping
	public SeoulLivingSimulationResponse simulate(@Valid @RequestBody SeoulLivingSimulationRequest request) {
		return service.simulate(request);
	}
}
