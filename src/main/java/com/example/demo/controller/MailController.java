package com.example.demo.controller;

import java.io.IOException;
import java.security.Principal;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Message;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;

@Controller
@RequestMapping("/")
public class MailController {
	
	private EmailService emailService;
	private UserRepository userRepository;
	
	public MailController(EmailService emailService, UserRepository userRepository) {
		super();
		this.emailService = emailService;
		this.userRepository = userRepository;
	}
	@GetMapping("/email")
	public String email(@ModelAttribute(name = "user")User user, Model model, Principal principal) {
		if(principal!=null) {
		user = userRepository.findByUsername(principal.getName());
			model.addAttribute("user", user);
		}
	 return "sendEmail_form";	
	}
	
	@PostMapping("/email/send")
	public String sendNewEmail(@ModelAttribute("letter")Message msg, Model model,Principal principal)
			throws AddressException, MessagingException, IOException {
		if(principal!=null) {
			User user = userRepository.findByUsername(principal.getName());
			msg.setFrom(user.getEmail());
		emailService.sendSimpleMessage(msg.getTo(), msg.getSubject(), msg.getText());
		model.addAttribute("message", "Your letter to "+msg.getTo()+" sent succesfully!");
		}
		return "home";
	}
	

}