package com.code.challange.security.service;

import com.code.challange.security.common.SecurityToken;

public interface SecurityService {
	boolean authenticate(SecurityToken securityToken);
	SecurityToken hasAccess(SecurityToken securityToken, String serviceName);

}
