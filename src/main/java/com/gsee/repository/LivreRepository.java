package com.gsee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gsee.model.Livre;

public interface LivreRepository extends JpaRepository<Livre, Long>{

	Optional<Livre> findByLibelle(String libelle);
}
