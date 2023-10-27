package com.lab.elastic.services;

import com.lab.elastic.dto.CreateRequestDto;
import com.lab.elastic.repository.MongoRequestRepository;
import com.lab.elastic.repository.entidades.MongoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Serviço para criar requisições no Elasticsearch
 *
 * @author Wedson Silva
 * @see <a href="https://dev.azure.com/oobj-devops/Engineering/_workitems/edit/1973">Azzure #1973</a>
 * @since 28/09/2023
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE) // Coloca primeiro no MongoDB
public class CreateRequestMongo implements CreateRequestResolver {
	
	private final List<CreateRequestResolver> createServices;
	private final MongoRequestRepository mongoRequestRepository;
	private final RequestMapper requestMapper;
	
	@Override
	public void accept(CreateRequestDto requestDto) {
		log.debug("Verificar se já existe uma requisição para [{}]", requestDto.getRequestId());
		if (existeRequisicao(requestDto)) {
			// Se existir, não faz nada
			return;
		}
		log.debug("Criar um documento de requisição [{}]", requestDto);
		// No mapeamento do DTO para o Documento Mongo, não leva o atributo payload pq esse dado não fica no mongoDB
		MongoRequest mongoRequest = requestMapper.toDocument(requestDto);
		// Configura a data de armazenamento da requisição
		mongoRequest.setDataArmazenamento(DateTimeUtils.AMERICA_SAO_PAULO.now());
		// Grava no mongoDB os dados sem o payload
		var result =  mongoRequestRepository.insert(mongoRequest);
		log.debug("Requisição [{}] inserida no mongoDB", result.getId());
	}
	
	private boolean existeRequisicao(CreateRequestDto requisicao) {
		return mongoRequestRepository.findById(requisicao.getRequestId()).isPresent();
	}
	
}
