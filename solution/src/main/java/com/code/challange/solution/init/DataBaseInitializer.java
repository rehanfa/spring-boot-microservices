package com.code.challange.solution.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import com.code.challange.solution.domain.Address;
import com.code.challange.solution.domain.Location;
import com.code.challange.solution.domain.State;
import com.code.challange.solution.repository.AddressRespository;
import com.code.challange.solution.repository.StateRespository;

public class DataBaseInitializer implements CommandLineRunner {

	@Autowired
	private StateRespository stateRepository;
	@Autowired
	private AddressRespository addressRepository;
	

	@Override
	public void run(String... args) throws Exception {
		State state = new State();
		state.setAbbreviation("VIC");
		state.setName("Victoria");
		state.setStateId(10);

		
		Location location = new Location();
		location.setLatitude(-37.8232);
		location.setLongitude(144.97290000000001);
		location.setLocality("MELBOURNE CITY");

		Address address = new Address();
		address.setName("Melbourne");
		
		address.setPostCode(3000);
		address.setState(state);
		address.setLocation(location);
		
		addressRepository.save(address);
		
		
		
	}

}
