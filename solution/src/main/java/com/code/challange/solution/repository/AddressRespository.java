package com.code.challange.solution.repository;

import com.code.challange.solution.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface AddressRespository  extends JpaRepository<Address, Integer> {
	@Query("SELECT address FROM Address address JOIN FETCH address.location JOIN FETCH address.state WHERE address.postCode = (:postCode)")
	Address findByPostCode(Integer postCode);
	@Query("SELECT address FROM Address address JOIN FETCH address.location JOIN FETCH address.state WHERE address.name = (:name)")
	Address findByName(String name);
	Address findByStateName(String name);

}
