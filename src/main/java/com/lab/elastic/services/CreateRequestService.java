package com.lab.elastic.services;

import com.lab.elastic.dto.CreateRequestDto;

/**
 * Serviço para criar requisições no Elasticsearch
 *
 * @author Wedson Silva
 * @see <a href="https://dev.azure.com/oobj-devops/Engineering/_workitems/edit/1973">Azzure #1973</a>
 * @since 28/09/2023
 */
public interface CreateRequestService {
	
	void process(CreateRequestDto requestDto);
}
