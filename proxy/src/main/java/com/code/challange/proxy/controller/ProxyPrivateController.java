package com.code.challange.proxy.controller;

import com.code.challange.proxy.common.RoleConstants;
import com.code.challange.proxy.dto.Address;
import com.code.challange.proxy.service.ProxyService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RestController
@EnableEurekaClient
@PreAuthorize("hasRole('USER')")
@RequestMapping("/private")
public class ProxyPrivateController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyPrivateController.class);

	@Autowired
    private ProxyService proxyService;

	@PostMapping(value = "/suburbs/add")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Address addSuburbDetails(@RequestBody Address address) {
		Address retVal = null;
		try {
			if (address != null) {
				retVal = proxyService.addSuburbDetails(address);
			}
		} catch(Exception ex) {
			LOGGER.error("error in addSuburbDetails endpoint", ex);
		}
		return retVal;
	 }

	private String getJson(Address address) throws IOException {
		String retVal;
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		retVal =  objectMapper.writeValueAsString(address);
		return retVal;
	}

}
