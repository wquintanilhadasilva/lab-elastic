package com.lab.elastic.repository;

import com.lab.elastic.repository.entidades.ElasticRequest;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * Repositório para tratar CRUD das requisições no Elasticsearch
 *
 * @author Wedson Silva
 * @see <a href="https://dev.azure.com/oobj-devops/Engineering/_workitems/edit/1973">Azzure #1973</a>
 * @since 30/08/2023
 */
public interface ElasticRequestRepository {
	
	ElasticRequest save(ElasticRequest dto);
	
	Optional<ElasticRequest> findById(String id);
	
	Optional<Map<String, Object>> find(String id);

}
