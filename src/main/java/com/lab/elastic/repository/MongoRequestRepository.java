package com.lab.elastic.repository;

import com.lab.elastic.repository.entidades.MongoRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório para operações CRUD no MongoDB para as Requisições
 *
 * @author Wedson Silva
 * @see <a href="https://dev.azure.com/oobj-devops/Engineering/_workitems/edit/1973">Azzure #1973</a>
 * @since 30/08/2023
 */
@Repository
public interface MongoRequestRepository extends MongoRepository<MongoRequest, String> {

}
