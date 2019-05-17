package com.code.challange.security.contoller;

import com.code.challange.security.common.SecurityToken;
import com.code.challange.security.service.SecurityService;
import com.code.challange.security.service.SecurityTokenService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SecurityController.class)
public class SecurityControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private SecurityTokenService securityTokenService;
    @MockBean
    private SecurityService securityService;

    private String token = "token";
    private SecurityToken securityToken;

    @Before
    public void setUp() throws Exception {
        token = "some-token";
        securityToken = new SecurityToken();
        securityToken.setUserName("some-user");
        securityToken.setPassword("some-password");
    }

    @Test
    public void login_success() throws Exception {

        given(securityService.authenticate(Mockito.any())).willReturn(true);
        given(securityTokenService.encode(Mockito.any())).willReturn(token);
        mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(getJson(securityToken))).andExpect(status().is2xxSuccessful());

        verify(securityService, VerificationModeFactory.times(1)).authenticate(Mockito.any());
        verify(securityTokenService, VerificationModeFactory.times(1)).encode(Mockito.any());
        reset(securityService);
        reset(securityTokenService);
    }

    @Test
    public void login_failure() throws Exception {

        given(securityService.authenticate(Mockito.any())).willReturn(false);
        given(securityTokenService.encode(Mockito.any())).willReturn(token);
        mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(getJson(securityToken))).andExpect(status().is2xxSuccessful());

        verify(securityService, VerificationModeFactory.times(1)).authenticate(Mockito.any());
        verify(securityTokenService, VerificationModeFactory.times(0)).encode(Mockito.any());
        reset(securityService);
        reset(securityTokenService);
    }

    @Test
    public void hasAccess_success() throws Exception {

        given(securityService.hasAccess(Mockito.any(),Mockito.anyString())).willReturn(securityToken);
        given(securityTokenService.decode(Mockito.any())).willReturn(securityToken);
        mvc.perform(get("/some-token/some-path")).andExpect(status().is2xxSuccessful());

        verify(securityService, VerificationModeFactory.times(1)).hasAccess(Mockito.any(), Mockito.anyString());
        verify(securityTokenService, VerificationModeFactory.times(1)).decode(Mockito.any());
        reset(securityService);
        reset(securityTokenService);
    }

    @Test
    public void hasAccess_failure() throws Exception {

        given(securityService.hasAccess(Mockito.any(),Mockito.anyString())).willReturn(securityToken);
        given(securityTokenService.decode(Mockito.any())).willReturn(null);
        mvc.perform(get("/some-token/some-path")).andExpect(status().is2xxSuccessful());

        verify(securityService, VerificationModeFactory.times(0)).hasAccess(Mockito.any(), Mockito.anyString());
        verify(securityTokenService, VerificationModeFactory.times(1)).decode(Mockito.any());
        reset(securityService);
        reset(securityTokenService);
    }


    private String getJson(SecurityToken securityToken) throws IOException {
        String retVal;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        retVal =  objectMapper.writeValueAsString(securityToken);
        return retVal;
    }
}