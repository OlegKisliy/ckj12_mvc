package com.example.demo.service;

public class UsernameExistsException extends RuntimeException {
	public UsernameExistsException(String msg) {
		super(msg);
	}
}