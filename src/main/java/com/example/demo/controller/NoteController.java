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

import com.example.demo.model.Note;
import com.example.demo.model.User;
import com.example.demo.repository.NoteRepository;
import com.example.demo.repository.UserRepository;

@Controller
@RequestMapping("/notes")
public class NoteController {

	private UserRepository userRepository;

	private NoteRepository noteRepository;

	@Autowired
	public NoteController(UserRepository userRepository, NoteRepository noteRepository) {
		this.userRepository = userRepository;
		this.noteRepository = noteRepository;
	}

	@GetMapping("/all")
	public String notes(Model model, Principal principal) {
		if (principal != null) {
			User user = userRepository.findByUsername(principal.getName());
			List<Note> notes = user.getNotes();
			model.addAttribute("notes", notes);
		} else {
			model.addAttribute("notes", noteRepository.findAll());
		}
		return "notes";
	}

	@GetMapping("/create")
	public String create() {
		return "note_form";
	}

	@PostMapping("/add")
	public String add(@ModelAttribute("note") Note note, Principal prl) {
		if (prl != null) {
			User user = userRepository.findByUsername(prl.getName());
			user.addNote(note);
			noteRepository.save(note);
			userRepository.save(user);
		}
		return "redirect:/notes/all";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") int id) {
		noteRepository.deleteById(id);
		return "redirect:/notes/all";
	}

	@GetMapping("/search")
	public String search(@RequestParam(name = "word") String word, Model model) {
		// поиск записок по label
		List<Note> notes = noteRepository.findByLabelContainingOrMessageContaining(word, word);
		model.addAttribute("notes", notes);
		return "notes";
	}
}