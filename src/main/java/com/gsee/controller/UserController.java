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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gsee.controller.ResourceNotFoundException;
import com.gsee.model.Role;
import com.gsee.model.User;
import com.gsee.repository.RoleRepository;
import com.gsee.repository.UserRepository;
import com.gsee.request.LoginRequest;
import com.gsee.request.SignupRequest;
import com.gsee.response.JwtResponse;
import com.gsee.response.MessageResponse;
import com.gsee.security.jwt.JwtUtils;
import com.gsee.security.service.UserDetailsImpl;
import com.gsee.security.service.UserDetailsServiceImpl;

//import io.swagger.annotations.Api;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/user")
//@Api(value="User Management System", description="Operations pertaining to user in User Management System")
public class UserController {

	@Autowired
	UserRepository userRepository;
	
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
			
			Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
			if (!user.isPresent()) {
							
							return Map.ofEntries(
									Map.entry("StatusCode", 400),
									Map.entry("Message", "User no found")
									);
							
						}	
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
				//return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
				//return ResponseEntity.ok(user);
		}
		
		/*
		// Add one User
		@SuppressWarnings("static-access")
		@PostMapping("/add")
		public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, Role rol) {
			if (userRepository.existsByUsername(signUpRequest.getUsername())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Error: Username is already taken!"));
			}

			if (userRepository.existsByEmail(signUpRequest.getEmail())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Error: Email is already in use!"));
			}

			User user = new User(signUpRequest.getEmail(), signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getContact());
			
			Set<String> strRoles = signUpRequest.getRole();
			Set<Role> roles = new HashSet<>();
			
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
						Role adminRole = roleRepository.findByName(rol.getName());
						if(adminRole == null) {
							logger.error("Error: Role is not found.");
						}
						roles.add(adminRole);

						break;
					default:
						Role userRole = roleRepository.findByName(rol.getName());
						if(userRole == null) {
							logger.error("Error: Role is not found.");
						}
						roles.add(userRole);
					}
				});
			}
			
			user.setRoles(roles);
			userRepository.save(user);
			logger.info("User registered successfully!");
			
			return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

		}*/
		
		//Add Contact
		@PostMapping("/contact/{email}")
		public ResponseEntity<User> addUserContact(@PathVariable String email, @Valid @RequestBody User userDetails) throws ResourceNotFoundException{
			User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found for this email :: " + email));
			user.setContact(userDetails.getContact());
			final User addContact = userRepository.save(user);
			return ResponseEntity.ok(addContact);
		}
		
		//Add Username
		@PostMapping("/username/{email}")
		public ResponseEntity<User> addUsername(@PathVariable String email, @Valid @RequestBody User userDetails) throws ResourceNotFoundException{
			User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found for this email :: " + email));
			user.setUsername(userDetails.getUsername());
			final User addUsername = userRepository.save(user);
			return ResponseEntity.ok(addUsername);
		}
		
		//Change Password
		@PostMapping("/password/{email}")
		public ResponseEntity<User> addUserPassaword(@PathVariable String email, @Valid @RequestBody User userDetails) throws ResourceNotFoundException{
			User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found for this email :: " + email));
			user.setPassword(encoder.encode(userDetails.getPassword()));
			final User addPassword = userRepository.save(user);
			return ResponseEntity.ok(addPassword);
		}
		
		// Get all Users
		@GetMapping("/getall")
		public List<User> getUsers(){
			return userRepository.findAll();
		}
		
		// Get one user by his Email
		@GetMapping("/getbyemail/{email}")
		public ResponseEntity<User> getUserByEmail(
				@PathVariable(value = "email") String email) throws ResourceNotFoundException {
			User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found for this email :: " + email));
			if (userRepository.existsByEmail(email)) {
				logger.info("User found");
				return ResponseEntity.ok().body(user);
			}else {
				logger.error("User not found");
				return ResponseEntity.ok().body(null);
			}
			
		}
		
}