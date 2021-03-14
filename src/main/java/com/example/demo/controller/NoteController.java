package com.example.demo.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

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
import com.example.demo.model.Note;
import com.example.demo.model.User;
import com.example.demo.repository.FolderRepository;
import com.example.demo.repository.NoteRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FoldernameExistsException;

@Controller
@RequestMapping("/notes")
public class NoteController {

	private NoteRepository noteRepository;
	private UserRepository userRepository;
	private FolderRepository folderRepository;
	

	
	
    @Autowired
	public NoteController(NoteRepository noteRepository, UserRepository userRepository,
			FolderRepository folderRepository) {
		super();
		this.noteRepository = noteRepository;
		this.userRepository = userRepository;
		this.folderRepository = folderRepository;
	}

	@GetMapping("/all/{id}")
	public String notes(@PathVariable("id") int id, Model model) {
		Folder folder = folderRepository.findById(id).get();
		List<Note> notes = folder.getNotes();
		model.addAttribute("notes", notes);
		return "notes";
	}

	@GetMapping("/create")
	public String create() {
		return "note_form";
	}

	@PostMapping("/add")
	public String add(@ModelAttribute("note") Note note, Principal principal) {
		
		Folder folder = folderRepository.findByName(note.getFolder().getName());
	if(folder!=null && principal!=null) {
		folder.addNote(note);
		User user = userRepository.findByUsername(principal.getName());
		user.addNote(note);
		userRepository.save(user);
		noteRepository.save(note);
		folderRepository.save(folder);
	}else {
		throw new FoldernameExistsException("No such folder name");
	}
		return "redirect:/folders/all";
	}


	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") int id) {
		noteRepository.deleteById(id);
		return "redirect:/folders/all";
	}

	@GetMapping("/search")
	public String search(@RequestParam(name = "word") String word, Model model, Principal principal) {
		if (principal != null) {
			User user = userRepository.findByUsername(principal.getName());
			List<Note> notes = noteRepository.findByNameContainingOrDescriptionContaining(word, word);

			notes = notes.stream().filter(n -> n.getUser() == user).collect(Collectors.toList());
			model.addAttribute("notes", notes);
		}
		return "notes";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id")int id,Model model) {
		model.addAttribute("note", noteRepository.findById(id).get());
		return "editNote_form";
	}
	@PostMapping("/change")
	public String change(@ModelAttribute("note") Note note) {
		
		int id=note.getId();
		Note editNote=noteRepository.findById(id).get();
		editNote.setName(note.getName());
		editNote.setDescription(note.getDescription());
		noteRepository.save(editNote);
		return "redirect:/folders/all";
	}
	
}