package com.code.challange.security.service;

import com.code.challange.security.common.SecurityToken;

import java.security.GeneralSecurityException;

public interface SecurityTokenService {
	String encode(SecurityToken securityToken);
	SecurityToken decode(String tokenStr) throws GeneralSecurityException;

}
