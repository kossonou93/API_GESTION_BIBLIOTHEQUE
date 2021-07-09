package com.gsee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gsee.model.Bibliotecaire;

@Repository
public interface BibliotecaireRepository extends JpaRepository<Bibliotecaire, Long>{

	Bibliotecaire findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
	
	Optional<Bibliotecaire> findByEmail(String email);
	
	Optional<Bibliotecaire> findById(Long id);
	
	Bibliotecaire findByEmailIgnoreCase(String email);
}
