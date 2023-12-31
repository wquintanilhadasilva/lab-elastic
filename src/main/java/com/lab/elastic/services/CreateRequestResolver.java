package com.lab.elastic.services;

import com.lab.elastic.dto.CreateRequestDto;
import org.springframework.scheduling.annotation.Async;

/**
 * Serviço para criar uma nova requisição
 *
 * @author Wedson Silva
 * @see <a href="https://dev.azure.com/oobj-devops/Engineering/_workitems/edit/1973">Azzure #1973</a>
 * @since 28/08/2023
 */
public interface CreateRequestResolver {
	
	/**
	 * Cria uma nova requisição na aplicação
	 * @param requestDto Referência do DTO da requisição a ser criada
	 */
	void accept(CreateRequestDto requestDto);
	
}
