package com.code.challange.solution.controller;

import com.code.challange.solution.domain.Address;
import com.code.challange.solution.domain.State;
import com.code.challange.solution.repository.AddressRespository;
import com.code.challange.solution.repository.StateRespository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableEurekaClient
public class SuburbController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SuburbController.class);

	@Autowired
	private AddressRespository addressRespository;
	@Autowired
	private StateRespository stateRepository;
	
	@GetMapping(value = "/postcode/{postCode}")
	public Address findSuburbByPostCode(@PathVariable Integer postCode) {
		Address retVal = new Address();
		try {
			retVal = addressRespository.findByPostCode(postCode);
		} catch (Exception ex) {
			LOGGER.error("error in findSuburbByPostCode endpoint", ex);
		}
		return retVal == null ? new Address() : retVal;
	}
	
	@GetMapping(value = "/name/{name}")
	public Address findSuburbByName(@PathVariable String name) {
		Address retVal = new Address();
		try {
			retVal = addressRespository.findByName(name);
		} catch (Exception ex) {
			LOGGER.error("error in findSuburbByName endpoint", ex);
		}
		return retVal == null ? new Address() : retVal;
	}
	
	@PostMapping(value = "/add")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Address addSuburbDetails(@RequestBody Address address) {
		Address retVal = null;
		try {
			State state = stateRepository.findByName(address.getState().getName());
			if (state != null) {
				address.setState(state);
			} else {
				addressRespository.save(address);
			}
			retVal =  address;
		} catch(Exception ex) {
			LOGGER.error("error in addSuburbDetails endpoint", ex);
		}
		return retVal == null ? new Address() : retVal;
	 }

}
