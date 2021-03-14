package com.example.demo.service;

public class FoldernameExistsException extends RuntimeException{
	public FoldernameExistsException(String msg) {
		super(msg);
	}

}