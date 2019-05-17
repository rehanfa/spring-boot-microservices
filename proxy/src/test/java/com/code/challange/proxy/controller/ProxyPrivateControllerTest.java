package com.code.challange.proxy.controller;

import com.code.challange.proxy.common.SecurityToken;
import com.code.challange.proxy.common.SecurityTokenConstants;
import com.code.challange.proxy.dto.Address;
import com.code.challange.proxy.dto.Location;
import com.code.challange.proxy.dto.State;
import com.code.challange.proxy.service.ProxyService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProxyPrivateController.class)
public class ProxyPrivateControllerTest {
    Address address = null;
    HttpHeaders httpHeaders = null;
    State state = null;
    SecurityToken securityToken = null;
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProxyService proxyService;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() throws Exception {
        state = new State();
        state.setAbbreviation("VIC");
        state.setName("Victoria");
        state.setStateId(10);


        Location location = new Location();
        location.setLatitude(-37.8232);
        location.setLongitude(144.97290000000001);
        location.setLocality("MELBOURNE CITY");

        address = new Address();
        address.setName("Melbourne");

        address.setPostCode(3000);
        address.setState(state);
        address.setLocation(location);
        httpHeaders = new HttpHeaders();
        httpHeaders.add(SecurityTokenConstants.HEADER, SecurityTokenConstants.PREFIX + "some-token");
        securityToken = new SecurityToken();
        securityToken.setUserName("some-user");
        securityToken.setPassword("some-password");
        List<String> roles = new ArrayList<>(1);
        roles.add("USER");
        securityToken.setRoles(roles);
    }

    @Test
    public void addSuburbDetails_success() throws Exception{

        given(proxyService.addSuburbDetails(Mockito.any())).willReturn(address);
        given(proxyService.hasAccess(Mockito.any(), Mockito.anyString())).willReturn(securityToken);
        mvc.perform(post("/private/suburbs/add").headers(httpHeaders).contentType(MediaType.APPLICATION_JSON).content(getJson(address))).andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name", is(address.getName())))
                .andExpect(jsonPath("$.postCode", is(address.getPostCode())));
        verify(proxyService, VerificationModeFactory.times(1)).addSuburbDetails(Mockito.any());
        reset(proxyService);
    }

    @Test
    public void addSuburbDetails_failure() throws Exception{

        given(proxyService.addSuburbDetails(Mockito.any())).willReturn(address);
        given(proxyService.hasAccess(Mockito.any(), Mockito.anyString())).willReturn(new SecurityToken());
        mvc.perform(post("/private/suburbs/add").headers(httpHeaders).contentType(MediaType.APPLICATION_JSON).content(getJson(address)))
                .andExpect(status().is4xxClientError());
        verify(proxyService, VerificationModeFactory.times(0)).addSuburbDetails(Mockito.any());
        reset(proxyService);
    }

    private String getJson(Address address) throws IOException {
        String retVal;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        retVal =  objectMapper.writeValueAsString(address);
        return retVal;
    }

}