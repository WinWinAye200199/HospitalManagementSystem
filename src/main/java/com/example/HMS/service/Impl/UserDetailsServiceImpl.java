package com.example.HMS.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.HMS.exception.BadRequestException;
import com.example.HMS.model.entities.User;
import com.example.HMS.repository.UserRepository;
import com.example.HMS.security.UserPrincipal;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Username Not Found!"));
		if(user == null) {
			throw new BadRequestException("User not found with this " + email);
		}
		
		return UserPrincipal.build(user);

		
	}

}