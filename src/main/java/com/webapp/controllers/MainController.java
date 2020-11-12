package com.webapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(path = "v1")
public class MainController {

	@GetMapping
	public String index() {
		return "index";
	}

	@GetMapping(path = "health")
	public ResponseEntity<Boolean> healthCheck() {
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

}
