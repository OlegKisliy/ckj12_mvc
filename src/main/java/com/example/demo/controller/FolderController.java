package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Folder;
import com.example.demo.model.User;
import com.example.demo.repository.FolderRepository;
import com.example.demo.repository.NoteRepository;
import com.example.demo.repository.UserRepository;

@Controller
@RequestMapping("/folders")
public class FolderController {
	
	private FolderRepository folderRepository;
	private NoteRepository noteRepository;
	private UserRepository userRepository;
	
	@Autowired
	public FolderController(FolderRepository folderRepository, NoteRepository noteRepository,
			UserRepository userRepository) {
		super();
		this.folderRepository = folderRepository;
		this.noteRepository = noteRepository;
		this.userRepository = userRepository;
	}
	
	@GetMapping("/all")
	public String notes(Model model,Principal principal) {
		if(principal!=null){
			User user = userRepository.findByUsername(principal.getName());
			List<Folder>folders = user.getFolders();
		
			model.addAttribute("folders", folders);
		}else {
		model.addAttribute("folders", folderRepository.findAll());
		}
		return "folders";
		
	}
	
	@GetMapping("/create")
	public String createUser() {
		return "folder_form";
		
	}
	
	@PostMapping("/add")
	public String add(@ModelAttribute("folder") Folder folder, Principal principal) {

		if(principal!=null){
			User user = userRepository.findByUsername(principal.getName());
			user.createFolder(folder);
			folderRepository.save(folder);
			userRepository.save(user);		
		}else {
			
		}
		return "redirect:/folders/all";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") int id) {
		folderRepository.deleteById(id);
		return "redirect:/folders/all";
	}
	
	@GetMapping("/search")
	public String search(@RequestParam(name = "word") String word, Model model) {

		List<Folder> folders = folderRepository.findByNameContaining(word);
		model.addAttribute("folders", folders);
		return "folders";
	}

}