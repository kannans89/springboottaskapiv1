package com.ksonline.training.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ksonline.training.dto.RegistrationDto;
import com.ksonline.training.services.UserService;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/taskapp")
public class RegistrationController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public void registerUser(@RequestBody RegistrationDto dto) {
		
		System.err.println(dto);
		userService.registerUser(dto);
	}
}
