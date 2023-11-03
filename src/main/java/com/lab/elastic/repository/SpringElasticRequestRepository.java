package com.lab.elastic.repository;

import com.lab.elastic.repository.entidades.ElasticRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringElasticRequestRepository extends ElasticsearchRepository<ElasticRequest, String> {

}
