package com.gsee.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gsee.ApiGSEEApplication;
import com.gsee.model.Livre;
import com.gsee.repository.LivreRepository;
import com.gsee.response.MessageResponse;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/livre")
public class LivreController {
	
	@Autowired
	LivreRepository livreRepository;
	
	Logger logger = LoggerFactory.getLogger(ApiGSEEApplication.class);
	
	// Add Full Book
	@PostMapping("/add")
	public Livre addLivre(@Valid @RequestBody Livre livre) {
		return livreRepository.save(livre);
	}
	
	@GetMapping("/getall")
	public List<Livre> getUsers(){
		return livreRepository.findAll();
	}
	
	// Get one Book by his Id
	@GetMapping("/getbyid/{id}")
	public ResponseEntity<Livre> getLivreById(
			@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
		Livre livre = livreRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found for this id :: " + id));
		if (livreRepository.existsById(id)) {
			logger.info("Book found");
			return ResponseEntity.ok().body(livre);
		}else {
			logger.error("Book not found");
			return ResponseEntity.ok().body(null);
		}
		
	}
	
	// Update Book
	@PostMapping("/update/{id}")
	public ResponseEntity<Livre> updateLivre(@PathVariable Long id, @Valid @RequestBody Livre livreRequest) throws ResourceNotFoundException{
		Livre livre = livreRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found for this id :: " + id));
		livre.setLibelle(livreRequest.getLibelle());
		livre.setAuteur(livreRequest.getAuteur());
		livre.setOuvrage(livreRequest.getOuvrage());
		livre.setEtat(livreRequest.getEtat());
		final Livre updateLivre = livreRepository.save(livre);
		return ResponseEntity.ok(updateLivre);
	}
	
	// Delete one Book with his id
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<MessageResponse> deleteOrdinateurById(@PathVariable Long id) {
		Livre livre = livreRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found for this id :: " + id));
		if (livreRepository.existsById(id)) {
			livreRepository.delete(livre);
			logger.info("Book delete succefully!");
			return ResponseEntity.ok(new MessageResponse("Book delete successfully!"));
		} else {
			logger.error("Book is not found");
			return ResponseEntity.ok(new MessageResponse("Book is not found"));
		}
	}

}
