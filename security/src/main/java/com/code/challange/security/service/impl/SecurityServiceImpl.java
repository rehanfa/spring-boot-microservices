package com.code.challange.security.service.impl;

import com.code.challange.security.common.RoleConstants;
import com.code.challange.security.common.SecurityToken;
import com.code.challange.security.service.SecurityService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SecurityServiceImpl implements SecurityService {
	private Map<String, List<String>> userMap;

	@PostConstruct
	public void init() {
		userMap = new HashMap<>();
		List<String> roles = new ArrayList<String>();
		roles.add(RoleConstants.USER);
		userMap.put("user01", roles);
	}

	@Override
	public boolean authenticate(SecurityToken securityToken) {
		boolean retVal = false;
		if(securityToken != null && securityToken.getUserName() != null) {
			List<String> roles = userMap.get(securityToken.getUserName());
			if (roles != null) {
				securityToken.setRoles(roles);
				retVal = true;
			}
		}
		return retVal;
	}

	@Override
	public SecurityToken hasAccess(SecurityToken securityToken, String serviceName) {
		SecurityToken retVal = null;
		if (authenticate(securityToken)) {
			List<String> roles = userMap.get(securityToken.getUserName());
			if (roles != null && !roles.isEmpty() && roles.contains(RoleConstants.USER)) {
				retVal = securityToken;
			}
		}
		return retVal;
	}
	

}
