package com.gsee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gsee.model.Livre;

@Repository
public interface LivreRepository extends JpaRepository<Livre, Long>{

	Optional<Livre> findByLibelle(String libelle);
	
	@Query(value="SELECT * FROM livres WHERE id=:idLivre AND etat=:etat",nativeQuery=true)
	Livre CheckLivreAvailability(Long idLivre, Boolean etat);
}
