package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.model.UserSecurity;
import com.example.demo.repository.UserRepository;

@Service
public class UserSecurityDetailsService implements UserDetailsService {

	private UserRepository userRepository;

	@Autowired
	public UserSecurityDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// вводим Credentials в форму логина
	// POST запрос на сервер
	//
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("Not found username: " + username);
		}

		UserSecurity us = new UserSecurity(user);
		return us;
	}

}