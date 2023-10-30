package com.lab.elastic.controllers;

import com.lab.elastic.services.ElasticRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

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
	public ResponseEntity<?> inserir(@RequestParam(name = "payload")String payloaField, @RequestBody Map<String, Object> dados) throws IOException {
		log.info("Dados enviados [{}], campo payload [{}]", dados, payloaField);
		repository.index(dados, payloaField);
		return ResponseEntity.ok(dados);
	}
	
	
	@GetMapping("/map/{id}")
	public ResponseEntity<?> obter(@PathVariable(name = "id") String id) {
		log.info("Obtendo [{}]", id);
		ResponseEntity<Map<String,Object>> response = ResponseEntity.notFound().build();
		var result = repository.find(id);
		if (result.isPresent()) {
			response = ResponseEntity.ok(result.get());
		}
		return response;
	}

}
