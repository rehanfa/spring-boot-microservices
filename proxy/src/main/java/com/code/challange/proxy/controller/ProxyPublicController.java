package com.code.challange.proxy.controller;

import com.code.challange.proxy.service.ProxyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.code.challange.proxy.dto.Address;
import com.code.challange.proxy.common.SecurityToken;

@RestController
@EnableEurekaClient
@RequestMapping("/public")
public class ProxyPublicController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyPublicController.class);
	
	@Autowired
    private ProxyService proxyService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private String suburbServiceUrl = "http://suburb-service/api/";
	
	@PostMapping(value = "/login")
	@ResponseStatus(code = HttpStatus.CREATED)
	public String login(@RequestBody SecurityToken securityToken) {
		String retVal = null;
		try {
			if (securityToken != null && securityToken.getUserName() != null && securityToken.getPassword() != null) {
				securityToken.setPassword(passwordEncoder.encode(securityToken.getPassword()));
				retVal = proxyService.login(securityToken);
			}
		} catch (Exception ex) {
			LOGGER.error("error in login endpoint", ex);
		}
		return retVal;
	}
	
	@GetMapping(value = "/suburbs/postcode/{postCode}")
	public Address findSuburbByPostCode(@PathVariable Integer postCode) {
		Address retVal = null;
		try {
			if (postCode != null) {
				retVal = proxyService.findSuburbByPostCode(postCode);
			}
		} catch (Exception ex) {
			LOGGER.error("error in findSuburbByPostCode endpoint", ex);
		}
		return retVal;
	}
	
	@GetMapping(value = "/suburbs/name/{name}")
	public Address findSuburbByName(@PathVariable String name) {
		Address retVal = null;
		try {
			if (name != null) {
				retVal = proxyService.findSuburbByName(name);
			}
		} catch (Exception ex) {
			LOGGER.error("error in findSuburbByName endpoint", ex);
		}
		return retVal;
	}

}
