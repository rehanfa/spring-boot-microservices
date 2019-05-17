package com.code.challange.proxy.service;

import com.code.challange.proxy.common.SecurityToken;
import com.code.challange.proxy.dto.Address;

public interface ProxyService {
	SecurityToken hasAccess(String securityToken, String serviceName);
	Address addSuburbDetails(Address address);
	String login(SecurityToken securityToken);
	Address findSuburbByPostCode(Integer postCode);
	Address findSuburbByName(String name);
}
