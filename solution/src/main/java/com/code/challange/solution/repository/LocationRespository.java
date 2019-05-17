package com.code.challange.solution.repository;

import com.code.challange.solution.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationRespository  extends JpaRepository<Location, Integer> {

}
