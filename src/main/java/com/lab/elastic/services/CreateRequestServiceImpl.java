package com.lab.elastic.services;

import com.lab.elastic.dto.CreateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Serviço de criação de Requisições no MongoDB
 * @author Wedson Silva
 * @see <a href="https://dev.azure.com/oobj-devops/Engineering/_workitems/edit/1973">Azzure #1973</a>
 * @since 30/09/2023
 */
@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class CreateRequestServiceImpl implements CreateRequestService {
	
	private final List<CreateRequestResolver> resolvers;
//	private final DadosEmpresaService dadosEmpresaService;
	
	@Override
	@Async
	public void process(CreateRequestDto requestDto) {
//		// Obtém os dados da empresa que está realizando a requisição de emissão
//		DadosEmpresaDTO emissor = dadosEmpresaService.getByEmissor(requestDto.getCnpj());
//		if (emissor == null) {
//			throw new BusinessRuleException(BusinessRuleType.FILIAL_NAO_ENCONTRADA);
//		}
//		if (!emissor.isHabilitada()) {
//			throw new BusinessRuleException(BusinessRuleType.FILIAL_DESATIVADA);
//		}
//
//		// Copia os dados da empresa para o DTO de criação da requisição
//		BeanUtils.copyProperties(emissor, requestDto);
		
		// Executa o pipeline de resolvers para criar a requisição
		// Cada resolver grava a requisição em algum lugar específico
		resolvers.stream().forEach(service -> service.accept(requestDto));
	}
	
}
