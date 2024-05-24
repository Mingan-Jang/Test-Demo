package com.playground.playground.mapping;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(prefix = "country")
@Data
@Component
public class CountryMapping {

	private Map<String, String> mapping;

}
