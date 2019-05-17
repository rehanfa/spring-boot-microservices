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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProxyPublicController.class)
public class ProxyPublicControllerTest {

    private Address address = null;
    private HttpHeaders httpHeaders = null;
    private State state = null;
    private SecurityToken securityToken = null;
    private String encodedString = "some-encoded-string";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProxyService proxyService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() throws Exception {
        encodedString = "some-encoded-string";
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
    public void login_success() throws Exception {
        given(passwordEncoder.encode(Mockito.any())).willReturn(encodedString);
        given(proxyService.login(Mockito.any())).willReturn(encodedString);
        mvc.perform(post("/public/login").contentType(MediaType.APPLICATION_JSON).content(getJson(securityToken)))
                .andExpect(status().is2xxSuccessful());
        verify(passwordEncoder, VerificationModeFactory.atLeast(1)).encode(Mockito.any());
        verify(proxyService, VerificationModeFactory.atLeast(1)).login(Mockito.any());
        reset(passwordEncoder);
        reset(proxyService);
    }

    @Test
    public void login_failure() throws Exception {
        given(passwordEncoder.encode(Mockito.any())).willReturn(encodedString);
        given(proxyService.login(Mockito.any())).willReturn(encodedString);
        mvc.perform(post("/public/login").contentType(MediaType.APPLICATION_JSON).content(getJson(new SecurityToken())))
                .andExpect(status().is2xxSuccessful());
        verify(passwordEncoder, VerificationModeFactory.atLeast(0)).encode(Mockito.any());
        verify(proxyService, VerificationModeFactory.times(0)).login(Mockito.any());
        reset(passwordEncoder);
        reset(proxyService);
    }

    @Test
    public void findSuburbByPostCode() throws Exception {
        given(proxyService.findSuburbByPostCode(Mockito.any())).willReturn(address);
        mvc.perform(get("/public/suburbs/postcode/3000"))
                .andExpect(status().is2xxSuccessful());
        verify(proxyService, VerificationModeFactory.times(1)).findSuburbByPostCode(Mockito.any());
        reset(proxyService);
    }

    @Test
    public void findSuburbByName() throws Exception {
        given(proxyService.findSuburbByName(Mockito.any())).willReturn(address);
        mvc.perform(get("/public/suburbs/name/Melbourne"))
                .andExpect(status().is2xxSuccessful());
        verify(proxyService, VerificationModeFactory.times(1)).findSuburbByName(Mockito.any());
        reset(proxyService);
    }

    private String getJson(SecurityToken securityToken) throws IOException {
        String retVal;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        retVal =  objectMapper.writeValueAsString(securityToken);
        return retVal;
    }
}