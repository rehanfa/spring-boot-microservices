package com.code.challange.solution;

import com.code.challange.solution.controller.SuburbController;
import com.code.challange.solution.domain.Address;
import com.code.challange.solution.domain.Location;
import com.code.challange.solution.domain.State;
import com.code.challange.solution.repository.AddressRespository;
import com.code.challange.solution.repository.StateRespository;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;


@RunWith(SpringRunner.class)
@WebMvcTest(SuburbController.class)
public class SolutionApplicationTests {
	Address address = null;
	State state = null;
	@Autowired
	private MockMvc mvc;

	@MockBean
	private AddressRespository addressRespository;
	@MockBean
	private StateRespository stateRepository;

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
	}

	@Test
	public void findByPostCode_success() throws Exception{

		given(addressRespository.findByPostCode(3000)).willReturn(address);
		mvc.perform(get("/postcode/3000")).andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(address.getName())))
				.andExpect(jsonPath("$.postCode", is(address.getPostCode())));

		verify(addressRespository, VerificationModeFactory.times(1)).findByPostCode(Mockito.any());
		reset(addressRespository);
	}

	@Test
	public void findByPostCode_failure() throws Exception{

		given(addressRespository.findByPostCode(3000)).willReturn(address);
		mvc.perform(get("/postcode/3001")).andExpect(status().isOk())
				.andExpect(jsonPath("$.name", isEmptyOrNullString()));


		verify(addressRespository, VerificationModeFactory.times(1)).findByPostCode(Mockito.any());
		reset(addressRespository);
	}

	@Test
	public void findSuburbByName_success() throws Exception{

		given(addressRespository.findByName("Melbourne")).willReturn(address);
		mvc.perform(get("/name/Melbourne")).andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(address.getName())))
				.andExpect(jsonPath("$.postCode", is(address.getPostCode())));

		verify(addressRespository, VerificationModeFactory.times(1)).findByName(Mockito.any());
		reset(addressRespository);
	}

	@Test
	public void findSuburbByName_failure() throws Exception{

		given(addressRespository.findByName("Melbourne")).willReturn(address);
		mvc.perform(get("/name/Geelong")).andExpect(status().isOk())
				.andExpect(jsonPath("$.name", isEmptyOrNullString()));


		verify(addressRespository, VerificationModeFactory.times(1)).findByName(Mockito.any());
		reset(addressRespository);
	}

	@Test
	public void addSuburbDetails_success() throws Exception{

		given(addressRespository.save(Mockito.any())).willReturn(address);
		given(stateRepository.findByName(Mockito.any())).willReturn(state);
		mvc.perform(post("/add").contentType(MediaType.APPLICATION_JSON).content(getJson(address))).andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("$.name", is(address.getName())))
				.andExpect(jsonPath("$.postCode", is(address.getPostCode())));
		verify(stateRepository, VerificationModeFactory.times(1)).findByName(Mockito.any());
		verify(addressRespository, VerificationModeFactory.times(1)).save(Mockito.any());
		reset(stateRepository);
		reset(addressRespository);
	}

	private String getJson(Address address) throws IOException {
		String retVal;
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		retVal =  objectMapper.writeValueAsString(address);
		return retVal;
	}

}
