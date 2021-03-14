package com.example.demo.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.example.demo.model.Folder;
import com.example.demo.model.Note;
import com.example.demo.repository.FolderRepository;

@Service
public class FolderService {
	
	private FolderRepository folderRepository;

	public FolderService(FolderRepository folderRepository) {
		super();
		this.folderRepository = folderRepository;
	}
	
	public Folder createNewFolder(Folder folder) {
		if(folderRepository.findByName(folder.getName())!=null) {
			throw new FoldernameExistsException(folder.getName());	
		}
		folder.setNotes(new ArrayList<Note>());
		return folderRepository.save(folder);
	}

}