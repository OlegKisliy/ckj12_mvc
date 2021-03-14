package com.example.demo.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="folders")
public class Folder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	
	@ManyToOne
	@ToString.Exclude
	private User user;
	
	@OneToMany(mappedBy = "folder")
	private List<Note> notes;

	public Folder(String name, List<Note> notes) {
		super();
		this.name = name;
		this.notes = notes;
	}
	
	public void addNote(Note note) {
		note.setFolder(this);
		notes.add(note);
	}

}