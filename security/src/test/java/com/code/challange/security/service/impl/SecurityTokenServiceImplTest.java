package com.code.challange.security.service.impl;

import com.code.challange.security.common.SecurityToken;
import com.code.challange.security.service.SecurityTokenService;
import org.junit.Before;
import org.junit.Test;

import java.security.GeneralSecurityException;

import static org.junit.Assert.*;

public class SecurityTokenServiceImplTest {

    private SecurityTokenServiceImpl  securityTokenService = null;
    private SecurityToken securityToken = null;
    private String ecodedString =  null;

    @Before
    public void setUp() throws Exception {
        securityTokenService = new SecurityTokenServiceImpl();
        securityTokenService.init();
        securityToken = new SecurityToken();
        securityToken.setUserName("some-user");
        securityToken.setPassword("some-pass");
        ecodedString = securityTokenService.encode(securityToken);
    }

    @Test
    public void encode() throws GeneralSecurityException {
        assertEquals(securityToken.getUserName(), securityTokenService.decode(securityTokenService.encode(securityToken)).getUserName());
    }

    @Test
    public void decode() throws GeneralSecurityException {
        assertEquals(ecodedString, securityTokenService.encode(securityTokenService.decode(ecodedString)));
    }
}