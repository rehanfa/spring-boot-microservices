package com.code.challange.solution.repository;


import com.code.challange.solution.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRespository  extends JpaRepository<State, Integer> {
	State findByName(String name);

}
