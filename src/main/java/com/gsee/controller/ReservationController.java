package com.gsee.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gsee.model.Etudiant;
import com.gsee.model.Livre;
import com.gsee.model.Reservation;
import com.gsee.repository.EtudiantRepository;
import com.gsee.repository.LivreRepository;
import com.gsee.repository.ReservationRepository;
import com.gsee.request.ReservationRequest;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/reservation")
public class ReservationController {
	
	@Autowired
	EtudiantRepository etudiantRepository;
	
	@Autowired
	LivreRepository livreRepository;
	
	@Autowired
	ReservationRepository reservationRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(ReservationRequest.class);
	
	
	@GetMapping("/getall")
	public List<Reservation> getReservations(){
		return reservationRepository.findAll();
	}
	
	@PostMapping("/add")
	public Object addReservationOrdinateur(@Valid @RequestBody ReservationRequest reservationRequest) {
		Livre livre = livreRepository.findByLibelle(reservationRequest.getLibelle()).orElseThrow(() -> new ResourceNotFoundException("Livre not found for this :: " + reservationRequest.getLibelle()));
		List<Reservation> res = reservationRepository.findAll();
		System.out.println(res);
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<?> reservation=new ArrayList();
		try {
			Date castedDebut=format.parse(reservationRequest.getDebut());
			Date castedFin=format.parse(reservationRequest.getFin());
			System.out.println(castedDebut);
			System.out.println(castedFin);
			 
			 reservation= reservationRepository.CheckLivreAvailability(livre.getId(), false);
			 System.out.println(reservation);
			 
			 if(reservation.isEmpty()) {
				//il ne peut pas reserver
				Etudiant etudiant = etudiantRepository.findByEmail(reservationRequest.getEtudiant()).orElseThrow(() -> new ResourceNotFoundException("Etudiant not found for this email :: " + reservationRequest.getEtudiant()));
				
				Reservation reserv = new Reservation(castedDebut, castedFin, etudiant);
				//reserv.setOrdinateur(ordinateur);
				reservationRepository.save(reserv);
				
				return reservation;
					
				} else {
					return reservation;
				}
		}catch(Exception ex) {
			 System.out.println(ex.getMessage());
		}
		
		return reservation;
		
	}
}
