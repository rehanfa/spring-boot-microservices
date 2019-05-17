package com.code.challange.security.contoller;

import com.code.challange.security.common.SecurityToken;

import com.code.challange.security.service.SecurityService;
import com.code.challange.security.service.SecurityTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.GeneralSecurityException;
import java.util.ArrayList;

@RestController
@EnableEurekaClient
public class SecurityController {

	
	private final static Logger LOGGER = LoggerFactory.getLogger(SecurityController.class);

	@Autowired
	private SecurityTokenService securityTokenService;
	@Autowired
	private SecurityService securityService;

	@PostMapping(value = "/login")
	@ResponseStatus(code = HttpStatus.CREATED)
	public String login(@RequestBody SecurityToken securityToken) {
		String retVal = null;
		try {
			if (securityService.authenticate(securityToken)) {
				retVal = securityTokenService.encode(securityToken);
			}
		} catch (Exception ex) {
			LOGGER.error("error in login endpoint", ex);
		}
		return retVal;
	}
	
	@GetMapping(value = "/{token}/{serviceName}")
	public SecurityToken hasAccess(@PathVariable String token, @PathVariable String serviceName) {
		SecurityToken retVal = null;
		try {
			SecurityToken securityToken  = securityTokenService.decode(token);
			if (securityToken != null) {
				retVal = securityService.hasAccess(securityToken, serviceName);
			}

		} catch (GeneralSecurityException e) {
			LOGGER.error("Error in hasAccess call", e);
		}
		return retVal;
	}

}
