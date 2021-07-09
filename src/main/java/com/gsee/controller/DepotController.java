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

import com.gsee.model.Depot;
import com.gsee.model.Etudiant;
import com.gsee.model.Livre;
import com.gsee.repository.DepotRepository;
import com.gsee.repository.EtudiantRepository;
import com.gsee.repository.LivreRepository;
import com.gsee.request.ReservationRequest;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/depot")
public class DepotController {

	@Autowired
	EtudiantRepository etudiantRepository;
	
	@Autowired
	DepotRepository depotRepository;
	
	@Autowired
	LivreRepository livreRepository;
	
	//@Autowired
	//ReservationRepository reservationRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(ReservationRequest.class);
	
	
	@GetMapping("/getall")
	public List<Depot> getReservations(){
		return depotRepository.findAll();
	}
	
	@PostMapping("/add")
	public Object addReservationOrdinateur(@Valid @RequestBody ReservationRequest reservationRequest) {
		Livre livre = livreRepository.findByLibelle(reservationRequest.getLibelle()).orElseThrow(() -> new ResourceNotFoundException("Livre not found for this :: " + reservationRequest.getLibelle()));
		List<Depot> res = depotRepository.findAll();
		System.out.println(res);
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<?> depot =new ArrayList();
		try {
			Date castedDebut=format.parse(reservationRequest.getDebut());
			Date castedFin=format.parse(reservationRequest.getFin());
			//System.out.println(castedDebut);
			System.out.println(castedFin);
			 
			 depot= depotRepository.CheckDepotAvailability(livre.getId(), true);
			 System.out.println(depot);
			 
			 //if(reservation.isEmpty()) {
				//il ne peut pas reserver
				Etudiant etudiant = etudiantRepository.findByEmail(reservationRequest.getEtudiant()).orElseThrow(() -> new ResourceNotFoundException("Etudiant not found for this email :: " + reservationRequest.getEtudiant()));
				
				Depot dep = new Depot(castedDebut, castedFin, etudiant);
				//reserv.setOrdinateur(ordinateur);
				depotRepository.save(dep);
				
				return depot;
					
				//} else {
				//return depot;
				//}
		}catch(Exception ex) {
			 System.out.println(ex.getMessage());
		}
		
		return depot;
		
	}
}
