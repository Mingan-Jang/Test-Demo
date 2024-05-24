package com.playground.playground.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.playground.playground.mapping.CountryMapping;

@RestController
@RequestMapping("/country")
public class CountryController {
    @Autowired
    private CountryMapping countryMapping;

    @GetMapping("/{countryCode}")
    public String getCountryName(@PathVariable String countryCode) {
       
    	System.out.println(countryCode);
    	return countryMapping.getMapping().get(countryCode);
    }
}