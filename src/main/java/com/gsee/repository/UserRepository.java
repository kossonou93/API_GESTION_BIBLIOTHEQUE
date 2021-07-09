package com.gsee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gsee.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	User findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
	
	Optional<User> findByEmail(String email);
	
	Optional<User> findById(Long id);
	
	User findByEmailIgnoreCase(String email);
}
