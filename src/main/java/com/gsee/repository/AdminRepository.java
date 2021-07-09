package com.gsee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gsee.model.Admin;
import com.gsee.model.Bibliotecaire;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>{

	Admin findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
	
	Optional<Admin> findByEmail(String email);
	
	Optional<Admin> findById(Long id);
	
	Admin findByEmailIgnoreCase(String email);
}
