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
import com.gsee.model.Admin;
import com.gsee.model.Role;
import com.gsee.model.User;
import com.gsee.repository.AdminRepository;
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
@RequestMapping("/api/admin")
//@Api(value="User Management System", description="Operations pertaining to user in User Management System")
public class AdminController {

	@Autowired
	AdminRepository adminRepository;
	
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
	
	Optional<Admin> admin = adminRepository.findByEmail(loginRequest.getEmail());
	if (!admin.isPresent()) {
		
		return admin;
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
		public ResponseEntity<?> registerAdmin(@Valid @RequestBody SignupRequest signUpRequest) {

			if (adminRepository.existsByEmail(signUpRequest.getEmail())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Error: Email is already in use!"));
			}
			
			if (adminRepository.existsByEmail(signUpRequest.getEmail())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Error: Email is already taken!"));
			}

			Admin admin = new Admin(signUpRequest.getNom(), signUpRequest.getPrenom(), signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getContact());
					//admin(signUpRequest.getEmail(), signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getContact());
			
			Set<String> strRoles = signUpRequest.getRole();
			Set<Role> roles = new HashSet<>();
			
			Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
			roles.add(adminRole);
			/*if (strRoles == null) {
				Role userRole = roleRepository.findByName(ERole.ROLE_USER);
				if(userRole == null) {
					logger.error("Error: Role is not found.");
				}
				roles.add(userRole);
			} else {
				strRoles.forEach(role -> {
					switch (role) {
					case "admin":
						Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
						if(adminRole == null) {
							logger.error("Error: Role is not found.");
						}
						roles.add(adminRole);

						break;
					default:
						Role userRole = roleRepository.findByName(ERole.ROLE_USER);
						if(userRole == null) {
							logger.error("Error: Role is not found.");
						}
						roles.add(userRole);
					}
				});
			}
			*/
			admin.setRoles(roles);
			adminRepository.save(admin);
			logger.info("User registered successfully!");
			
			return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

		}
		
		//Update Admin
		//@CrossOrigin(origins = "http://localhost:4200")
		@PostMapping("/update/{id}")
		public ResponseEntity<Admin> updateAdmin(@PathVariable Long id, @Valid @RequestBody User userDetails) throws ResourceNotFoundException{
			Admin admin = adminRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));
			admin.setUsername(userDetails.getUsername());
			admin.setEmail(userDetails.getEmail());
			admin.setContact(userDetails.getContact());
			//admin.setPassword(encoder.encode(userDetails.getPassword()));
			final Admin updateAdmin = adminRepository.save(admin);
			return ResponseEntity.ok(updateAdmin);
		}
		
		// Delete one Admin with his id
		@DeleteMapping("/delete/{id}")
		public ResponseEntity<MessageResponse> deleteAdminById(@PathVariable Long id) {
			Admin admin = adminRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Admin" + id));
			if (adminRepository.existsById(id)) {
				adminRepository.delete(admin);
				logger.info("Admin delete succefully!");
				return ResponseEntity.ok(new MessageResponse("Admin delete successfully!"));
			} else {
				logger.error("Admin is not found");
				return ResponseEntity.ok(new MessageResponse("Admin is not found"));
			}
			
		}
		
		
		//Change Password
		@PostMapping("/password/{email}")
		public ResponseEntity<Admin> addUserPassaword(@PathVariable String email, @Valid @RequestBody User userDetails) throws ResourceNotFoundException{
			Admin admin = adminRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found for this email :: " + email));
			admin.setPassword(encoder.encode(userDetails.getPassword()));
			final Admin addPassword = adminRepository.save(admin);
			return ResponseEntity.ok(addPassword);
		}
		
		// Get all Users
		@GetMapping("/getall")
		public List<Admin> getUsers(){
			return adminRepository.findAll();
		}
		
		// Get one user by his Id
		@GetMapping("/getbyid/{id}")
		public ResponseEntity<Admin> getUserById(
				@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
			Admin admin = adminRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Admin not found for this id :: " + id));
			if (adminRepository.existsById(id)) {
				//encoder.(admin.getPassword()); 
				logger.info("User found");
				return ResponseEntity.ok().body(admin);
			}else {
				logger.error("User not found");
				return ResponseEntity.ok().body(null);
			}
			
		}
}
