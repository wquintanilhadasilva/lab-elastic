package com.lab.elastic.controllers;

import com.lab.elastic.services.ElasticRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/_find")
@RequiredArgsConstructor
@Slf4j
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
	
	@PostMapping("/map")
	public ResponseEntity<?> inserir(@RequestParam(name = "payload")String payloaField, @RequestBody Map<String, ?> dados) throws IOException {
		log.info("Dados enviados [{}], campo payload [{}]", dados, payloaField);
		repository.index(dados, payloaField);
		return ResponseEntity.ok(dados);
	}

}
