package com.code.challange.proxy.service.impl;

import com.code.challange.proxy.common.SecurityToken;
import com.code.challange.proxy.dto.Address;
import com.code.challange.proxy.dto.Location;
import com.code.challange.proxy.dto.State;
import com.code.challange.proxy.service.ProxyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebMvcTest(ProxyServiceImpl.class)
public class ProxyServiceImplTest {
    private ProxyServiceImpl proxyService = null;
    private String suburbServiceUrl = "http://suburb-service/api/";
    private String securityServiceUrl = "http://security-service/api/";
    private Address address = null;

    @MockBean
    private RestTemplate restTemplate;

    private SecurityToken securityToken = null;
    private String tokenString = null;

    @Before
    public void setUp() throws Exception {
        proxyService = new ProxyServiceImpl();
        securityToken = new SecurityToken();
        securityToken.setUserName("some-user-name");
        securityToken.setPassword("some-user-password");
        tokenString = "some-token";
        proxyService.setRestTemplate(restTemplate);

        State state = new State();
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
    }

    @Test
    public void hasAccess() {
        given(restTemplate.getForObject(securityServiceUrl + "token/service",SecurityToken.class)).willReturn(securityToken);
        SecurityToken token = proxyService.hasAccess("token","service");
        assertEquals(token, securityToken);
    }

    @Test
    public void addSuburbDetails() {

        Mockito.when(restTemplate.postForObject(suburbServiceUrl + "add/", address, Address.class))
                .thenReturn(address);
        Address response = proxyService.addSuburbDetails(address);
        assertEquals(response, address);
    }

    @Test
    public void login() {
        Mockito.when(restTemplate.postForObject(securityServiceUrl + "login/", securityToken, String.class))
                .thenReturn(tokenString);
        String response = proxyService.login(securityToken);
        assertEquals(response, tokenString);
    }

    @Test
    public void findSuburbByPostCode() {
        given(restTemplate.getForObject(suburbServiceUrl + "postcode/3000", Address.class)).willReturn(address);
        Address response = proxyService.findSuburbByPostCode(3000);
        assertEquals(response, address);

    }

    @Test
    public void findSuburbByName() {
        given(restTemplate.getForObject(suburbServiceUrl + "name/Melbourne", Address.class)).willReturn(address);
        Address response = proxyService.findSuburbByName("Melbourne");
        assertEquals(response, address);

    }
}