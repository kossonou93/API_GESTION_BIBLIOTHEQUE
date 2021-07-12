package com.gsee.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gsee.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{
	///*
	@Query(value="SELECT * FROM livres WHERE id=:idLivre AND etat=:etat)",nativeQuery=true)
	List<?> CheckLivre(Long idLivre, Boolean etat);
}
