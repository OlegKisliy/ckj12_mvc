package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notes")
public class Note {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String description;

	@ToString.Exclude
	@ManyToOne
	private User user;
	
	@ToString.Exclude
	@ManyToOne
	private Folder folder;

	public Note(String name, String description, User user, Folder folder) {
		super();
		this.name = name;
		this.description = description;
		this.user = user;
		this.folder = folder;
	}

	

	

	
}