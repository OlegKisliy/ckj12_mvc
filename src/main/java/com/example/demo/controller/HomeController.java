package com.example.demo.controller;

import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.service.UsernameExistsException;

@Controller
@RequestMapping("/")
public class HomeController {

	private UserRepository userRepository;

	private UserService userService;

	@Autowired
	public HomeController(UserRepository userRepository, UserService userService) {
		this.userRepository = userRepository;
		this.userService = userService;
	}

	@GetMapping("/")
	public String home(Model model, Principal principal) {
		if (principal != null) {
			// юзернейм (уникальный)
			String username = principal.getName();
			User user = userRepository.findByUsername(username);
			model.addAttribute("message", "Welcome, " + user.getName());
		} else {
			model.addAttribute("message", "Welcome to my app!");
		}
		return "home";
	}

	@GetMapping("/login")
	public String signIn() {
		return "login";
	}

	@GetMapping("/signup")
	public String signUp() {
		return "signup";
	}

	@PostMapping("/signup")
	public String registerNewUser(@ModelAttribute("user") User user, HttpServletRequest request) {
		String password = user.getPassword();
		// проверить, что пользователя с таким ЮЗЕРНЕЙМ не существует
		// если юзернейм не использован,
		// проинициализировать пользователя и сохранить.
		// если использован
		// кинуть эксепшн и вернуться в форму регистрации
		try {
			userService.registerNewUser(user);
			request.login(user.getUsername(), password);
		} catch (UsernameExistsException e) {
			return "redirect:/signup";
		} catch (ServletException e) {
			e.printStackTrace();
		}

		return "redirect:/";
	}
}