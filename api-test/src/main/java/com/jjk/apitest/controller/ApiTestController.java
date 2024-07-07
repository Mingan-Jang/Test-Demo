package com.jjk.apitest.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.jjk.apitest.dto.ApiTestRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Reverse Geocode", description = "Reverse Geocode Operations")
@Controller
@RequestMapping("/api")
public class ApiTestController {

	@Operation(summary = "Reverse Geocode", description = "Reverse geocoding based on latitude and longitude")
	@PostMapping("/reverse-geocode")
	public ResponseEntity<String> reverseGeocode(ApiTestRequestDTO reqGeoDTO) {

		String apiKey = null;

		try {
			apiKey = new String(Files.readAllBytes(Paths.get("..\\api-keys\\google.geokey")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		String urlTemplate = "https://maps.googleapis.com/maps/api/geocode/json";

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlTemplate)
				.queryParam("latlng", reqGeoDTO.getLat() + "," + reqGeoDTO.getLon())
				.queryParam("location_type", reqGeoDTO.getLocationType())
				.queryParam("language", reqGeoDTO.getLanguage()).queryParam("result_type", reqGeoDTO.getResultType())
				.queryParam("key", apiKey);

		RestTemplate restTemplate = new RestTemplate();
		String url = builder.toUriString();
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		return response;
	}

	@Operation(summary = "Reverse Geocode", description = "Reverse geocoding based on latitude and longitude")
	@GetMapping("/reverse-meow")
	public ResponseEntity<String> meow(ApiTestRequestDTO reqGeoDTO) {
		return null;
	}
}