package com.user.userservice.repository;

import com.user.userservice.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country,Long> {
      Optional<Country> findByName(String name);
}
