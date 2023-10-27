package com.lab.elastic.controllers;

import com.lab.elastic.dto.CreateRequestDto;
import com.lab.elastic.services.CreateRequestService;
import com.lab.elastic.services.RequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/request")
@RequiredArgsConstructor
@Slf4j
public class RequestController {
	
	private final CreateRequestService createRequestService;
	private final RequestMapper mapper;
	
	@PostMapping("/")
	public ResponseEntity<?> inserir(@RequestBody CreateRequestDto dominio) throws IOException {
		createRequestService.process(dominio);
		return ResponseEntity.ok(dominio);
	}
	
	@PostMapping("/map")
	public ResponseEntity<?> inserir(@RequestParam(name = "payload")String payloaField, @RequestBody Map<String, String> dados) throws IOException {
		log.info("Dados enviados [{}], campo payload [{}]", dados, payloaField);
		var dto = mapper.toDto(dados);
		createRequestService.process(dto);
		return ResponseEntity.ok(dto);
	}
	
}
