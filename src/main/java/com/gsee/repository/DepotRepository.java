package com.gsee.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gsee.model.Depot;

public interface DepotRepository extends JpaRepository<Depot, Long>{

	@Query(value="SELECT * FROM depots WHERE livre_id=:idLivre AND etat=:etat",nativeQuery=true)
	List<?> CheckDepotAvailability(Long idLivre, Boolean etat);
	///*
	@Query(value="SELECT * FROM depots WHERE livre_id=:idLivre AND etat=:etat)",nativeQuery=true)
	List<?> CheckDepot(Long idLivre, Boolean etat);
}
