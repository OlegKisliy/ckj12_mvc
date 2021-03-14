package com.example.demo.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Folder;
import com.example.demo.model.Note;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
	private UserRepository userRepository;

	private PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public User registerNewUser(User user) {
		if (userRepository.findByUsername(user.getUsername()) != null) {
			throw new UsernameExistsException(user.getUsername());
		}else if(userRepository.findByEmail(user.getEmail())!=null) {
			throw new UsernameExistsException("User with email "+user.getEmail()+" already exist!");
		}
		user.setRole("ROLE_USER");
		user.setNotes(new ArrayList<Note>());
		user.setFolders(new ArrayList<Folder>());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		return userRepository.save(user);
	}
}