package com.gsee.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsee.controller.ResourceNotFoundException;
import com.gsee.model.User;
import com.gsee.repository.UserRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository userRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found for this email :: "));
		
		if(user == null) {
			logger.error("User Not Found with email: " + email);
		}

		return UserDetailsImpl.build(user);
	}

}
