package com.code.challange.security.service.impl;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.code.challange.security.common.SecurityToken;
import com.code.challange.security.service.SecurityTokenService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SecurityTokenServiceImpl implements SecurityTokenService {
	private JWTSigner jwtSigner;
	private JWTVerifier jwtVerifier;
	private final String SECRET = "SEC_KEY";
	private final String USER_NAME = "USER_NAME";
	private final String PASSWORD = "PASSWORD";
	private final String ROLES = "ROLES";
	private final String LOGIN_EXPIRY = "LOGIN_EXPIRY";

	@PostConstruct
	public void init() {
		jwtSigner = new JWTSigner(SECRET);
		jwtVerifier = new JWTVerifier(SECRET);
	}

	public String encode(SecurityToken securityToken) {
		String retVal = null;
		long expiryMillis = 0l;
		if (securityToken != null) {
			if (securityToken.getLoginExpiry() != null) {
				expiryMillis = securityToken.getLoginExpiry();
			} else {
				expiryMillis = Instant.now().plus(4,ChronoUnit.MINUTES).toEpochMilli();
			}
			Map<String, Object> map = new HashMap<>();
			map.put(USER_NAME, securityToken.getUserName());
			map.put(PASSWORD, securityToken.getPassword());
			map.put(ROLES, securityToken.getRoles());
			map.put(LOGIN_EXPIRY, expiryMillis);
			retVal = jwtSigner.sign(map);
		}
		return retVal;

	}

	@SuppressWarnings("unchecked")
	public SecurityToken decode(String tokenStr) throws GeneralSecurityException {
		SecurityToken retVal = null;
		if (tokenStr != null) {
			try {
				Map<String, Object> map = jwtVerifier.verify(tokenStr);
				retVal = new SecurityToken();
				retVal.setUserName((String)map.get(USER_NAME));
				retVal.setPassword((String)map.get(PASSWORD));
				retVal.setRoles((List<String>)map.get(ROLES));
				retVal.setLoginExpiry((Long)map.get(LOGIN_EXPIRY));
			} catch (InvalidKeyException | NoSuchAlgorithmException | IllegalStateException | SignatureException
					| IOException | JWTVerifyException e) {
				throw new GeneralSecurityException(e);
			}
		}
		return retVal;
	}

}
