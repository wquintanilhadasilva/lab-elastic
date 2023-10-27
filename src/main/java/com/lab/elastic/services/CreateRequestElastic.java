package com.lab.elastic.services;

import com.lab.elastic.dto.CreateRequestDto;
import com.lab.elastic.repository.ElasticRequestRepository;
import com.lab.elastic.services.converters.PayloadConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


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
public class CreateRequestElastic implements CreateRequestResolver {
	
	private final ElasticRequestRepository elasticSearchRepository;
	private final RequestMapper requestMapper;
	private final List<PayloadConverter> payloadConverter;
	
	@Override
	public void accept(CreateRequestDto requestDto) {
		log.debug("Verificar se já existe uma requisição para [{}]", requestDto.getRequestId());
		if (existeRequisicao(requestDto)) {
			return; // Se existir, não faz nada
		}
		
		log.debug("Indexando requisicao [{}]", requestDto);
		
		var indexDocument = requestMapper.toIndex(requestDto);
		var modelo = Optional.ofNullable(ModeloType.valueOf(requestDto.getModelo())).orElseThrow(() ->
			new RuntimeException("Modelo [{" + requestDto.getModelo() + "}] não encontrado para conversão")
		);
		
		var converter = payloadConverter.stream().filter(c -> c.match(modelo)).findFirst()
			.orElseThrow(
				() -> new RuntimeException("Não foi encontrado conversor XML2Json para o modelo [{" +
						requestDto.getModelo() + "}]")
		);
		
		// Converte o conteúdo xml em json
//		indexDocument.setPayload(converter.convert(indexDocument.getPayload()));
		indexDocument.setDataArmazenamento(DateTimeUtils.AMERICA_SAO_PAULO.now());
		
		var result = elasticSearchRepository.save(indexDocument);
		log.debug("Requisição [{}] inserida no mongoDB", result.getId());
	}
	
	private boolean existeRequisicao(CreateRequestDto requisicao) {
		return elasticSearchRepository.findById(requisicao.getRequestId()).isPresent();
	}
	
}
