package com.lab.elastic.repository;

import com.lab.elastic.repository.entidades.ElasticRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório para tratar CRUD das requisições no Elasticsearch
 *
 * @author Wedson Silva
 * @see <a href="https://dev.azure.com/oobj-devops/Engineering/_workitems/edit/1973">Azzure #1973</a>
 * @since 30/08/2023
 */
@Repository
public interface ElasticRequestRepository extends ElasticsearchRepository<ElasticRequest, String> {

}
