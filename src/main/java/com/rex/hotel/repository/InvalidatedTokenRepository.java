package com.rex.hotel.repository;

import com.rex.hotel.model.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken,String> {
}
