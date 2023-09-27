package com.lab.elastic.controllers;

import com.lab.elastic.services.ElasticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@RequestMapping("/_find")
@RequiredArgsConstructor
public class FindController {
	
	private final ElasticRepository repository;
	
	@GetMapping("/")
	public ResponseEntity<?> consultar() {
		return ResponseEntity.ok("ok");
	}
	
	@PostMapping("/")
	public ResponseEntity<?> inserir(@RequestBody Dominio dominio) throws IOException {
		repository.index(dominio);
		return ResponseEntity.ok("ok");
	}

}
