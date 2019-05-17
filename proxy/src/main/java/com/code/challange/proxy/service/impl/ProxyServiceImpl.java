package com.code.challange.proxy.service.impl;

import com.code.challange.proxy.common.SecurityToken;
import com.code.challange.proxy.dto.Address;
import com.code.challange.proxy.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class ProxyServiceImpl implements ProxyService {
	private String suburbServiceUrl = "http://suburb-service/api/";
	private String securityServiceUrl = "http://security-service/api/";

	@Autowired
    private RestTemplate restTemplate;

	@Override
	public SecurityToken hasAccess(String securityToken, String serviceName) {
		SecurityToken retVal =  restTemplate.getForObject(securityServiceUrl + securityToken +"/"+serviceName, SecurityToken.class);
		return retVal;
	}
	public String login(SecurityToken securityToken) {
		String retVal = null;
		retVal = restTemplate.postForObject(securityServiceUrl + "login/", securityToken, String.class);
		return retVal;
	}

	public Address addSuburbDetails(Address address) {
		Address retVal = null;
		retVal = restTemplate.postForObject(suburbServiceUrl + "add/", address, Address.class);
		return retVal;
	}


	public Address findSuburbByPostCode(Integer postCode) {
		Address retVal = null;
		retVal = restTemplate.getForObject(suburbServiceUrl + "postcode/" + postCode, Address.class);
		return retVal;
	}

	public Address findSuburbByName(String name) {
		Address retVal = null;
		retVal = restTemplate.getForObject(suburbServiceUrl + "name/" + name, Address.class);
		return retVal;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
}
