package com.example.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Folder;

@Repository
public interface FolderRepository extends CrudRepository<Folder, Integer> {

	public List<Folder> findByNameContaining(String name);

	Folder findByName(String name);

}