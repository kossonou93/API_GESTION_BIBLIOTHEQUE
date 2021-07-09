package com.gsee.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gsee.controller.ResourceNotFoundException;
import com.gsee.model.ERole;
import com.gsee.model.Bibliotecaire;
import com.gsee.model.Role;
import com.gsee.model.User;
import com.gsee.repository.BibliotecaireRepository;
import com.gsee.repository.RoleRepository;
import com.gsee.response.JwtResponse;
import com.gsee.response.MessageResponse;
import com.gsee.security.jwt.JwtUtils;
import com.gsee.request.LoginRequest;
import com.gsee.request.SignupRequest;
import com.gsee.security.service.UserDetailsImpl;
import com.gsee.security.service.UserDetailsServiceImpl;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/bibliotecaire")
//@Api(value="User Management System", description="Operations pertaining to user in User Management System")
public class BibliotecaireController {

	@Autowired
	BibliotecaireRepository bibliotecaireRepository;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder encoder;
	
	private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
	// Sign in one User with his Email and password
@PostMapping("/signin")
public Object authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
	
	Optional<Bibliotecaire> bibliotecaire = bibliotecaireRepository.findByEmail(loginRequest.getEmail());
	if (!bibliotecaire.isPresent()) {
		
		return bibliotecaire;
	} else {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);
			
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
			List<String> roles = userDetails.getAuthorities().stream()
					.map(item -> item.getAuthority()).collect(Collectors.toList());
			return Map.ofEntries(
					Map.entry("StatusCode", 200),
					Map.entry("Message", "Success"),
					Map.entry("Data", new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles))
					);
	}
		//return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
		//return ResponseEntity.ok(user);
}
		
		
		// Add one User
		@SuppressWarnings("static-access")
		@PostMapping("/add")
		public ResponseEntity<?> registerBibliotecaire(@Valid @RequestBody SignupRequest signUpRequest) {

			if (bibliotecaireRepository.existsByEmail(signUpRequest.getEmail())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Error: Email is already in use!"));
			}
			
			if (bibliotecaireRepository.existsByEmail(signUpRequest.getEmail())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Error: Email is already taken!"));
			}

			Bibliotecaire bibliotecaire = new Bibliotecaire(signUpRequest.getNom(), signUpRequest.getPrenom(), signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getContact());
					//bibliotecaire(signUpRequest.getEmail(), signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getContact());
			
			Set<String> strRoles = signUpRequest.getRole();
			Set<Role> roles = new HashSet<>();
			Role bibliotecaireRole = roleRepository.findByName(ERole.ROLE_BIBLIOTHECAIRE);
			roles.add(bibliotecaireRole);
			/*if (strRoles == null) {
				Role userRole = roleRepository.findByName(ERole.ROLE_USER);
				if(userRole == null) {
					logger.error("Error: Role is not found.");
				}
				roles.add(userRole);
			} else {
				strRoles.forEach(role -> {
					switch (role) {
					case "bibliotecaire":
						Role bibliotecaireRole = roleRepository.findByName(ERole.ROLE_BIBLIOTHECAIRE);
						if(bibliotecaireRole == null) {
							logger.error("Error: Role is not found.");
						}
						roles.add(bibliotecaireRole);

						break;
					default:
						Role userRole = roleRepository.findByName(ERole.ROLE_BIBLIOTHECAIRE);
						if(userRole == null) {
							logger.error("Error: Role is not found.");
						}
						roles.add(userRole);
					}
				});
			}*/

			bibliotecaire.setRoles(roles);
			bibliotecaireRepository.save(bibliotecaire);
			logger.info("User registered successfully!");
			
			return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

		}
		
		//Update Bibliotecaire
		//@CrossOrigin(origins = "http://localhost:4200")
		@PostMapping("/update/{id}")
		public ResponseEntity<Bibliotecaire> updateBibliotecaire(@PathVariable Long id, @Valid @RequestBody User userDetails) throws ResourceNotFoundException{
			Bibliotecaire bibliotecaire = bibliotecaireRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));
			bibliotecaire.setUsername(userDetails.getUsername());
			bibliotecaire.setEmail(userDetails.getEmail());
			bibliotecaire.setContact(userDetails.getContact());
			//bibliotecaire.setPassword(encoder.encode(userDetails.getPassword()));
			final Bibliotecaire updateBibliotecaire = bibliotecaireRepository.save(bibliotecaire);
			return ResponseEntity.ok(updateBibliotecaire);
		}
		
		// Delete one Bibliotecaire with his id
		@DeleteMapping("/delete/{id}")
		public ResponseEntity<MessageResponse> deleteBibliotecaireById(@PathVariable Long id) {
			Bibliotecaire bibliotecaire = bibliotecaireRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bibliotecaire" + id));
			if (bibliotecaireRepository.existsById(id)) {
				bibliotecaireRepository.delete(bibliotecaire);
				logger.info("Bibliotecaire delete succefully!");
				return ResponseEntity.ok(new MessageResponse("Bibliotecaire delete successfully!"));
			} else {
				logger.error("Bibliotecaire is not found");
				return ResponseEntity.ok(new MessageResponse("Bibliotecaire is not found"));
			}
			
		}
		
		
		//Change Password
		@PostMapping("/password/{email}")
		public ResponseEntity<Bibliotecaire> addUserPassaword(@PathVariable String email, @Valid @RequestBody User userDetails) throws ResourceNotFoundException{
			Bibliotecaire bibliotecaire = bibliotecaireRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found for this email :: " + email));
			bibliotecaire.setPassword(encoder.encode(userDetails.getPassword()));
			final Bibliotecaire addPassword = bibliotecaireRepository.save(bibliotecaire);
			return ResponseEntity.ok(addPassword);
		}
		
		// Get all Users
		@GetMapping("/getall")
		public List<Bibliotecaire> getUsers(){
			return bibliotecaireRepository.findAll();
		}
		
		// Get one user by his Id
		@GetMapping("/getbyid/{id}")
		public ResponseEntity<Bibliotecaire> getUserById(
				@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
			Bibliotecaire bibliotecaire = bibliotecaireRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bibliotecaire not found for this id :: " + id));
			if (bibliotecaireRepository.existsById(id)) {
				//encoder.(bibliotecaire.getPassword()); 
				logger.info("User found");
				return ResponseEntity.ok().body(bibliotecaire);
			}else {
				logger.error("User not found");
				return ResponseEntity.ok().body(null);
			}
			
		}
}
