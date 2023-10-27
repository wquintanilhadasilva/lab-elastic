package com.lab.elastic.services;

import com.lab.elastic.dto.CreateRequestDto;
import com.lab.elastic.repository.entidades.ElasticRequest;
import com.lab.elastic.repository.entidades.MongoRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.Map;
import java.util.Optional;

/**
 * Mapeador de Entidade e Agrados para DTO e vice-versa
 *
 * @author Wedson Silva
 * @see <a href="https://dev.azure.com/oobj-devops/Engineering/_workitems/edit/1973">Azzure #1973</a>
 * @since 08/08/2023
 */
@Mapper(componentModel = "spring")
public interface RequestMapper {
	
	@Mapping(target = "id", source = "requestId")
	@Mapping(target = "tipoRequisicao", source = "requestType")
	MongoRequest toDocument(CreateRequestDto requisicaoDto);
	
	@Mapping(target = "id", source = "requestId")
	@Mapping(target = "tipoRequisicao", source = "requestType")
	ElasticRequest toIndex(CreateRequestDto requisicaoDto);
	
	@Mapping(target = "cnpj", source="emissor")
	@Mapping(target = "habilitada", expression = "java(convertBoolean(\"habilitada\", data))")
	CreateRequestDto toDto(Map<String, String> data);
	
	default Boolean convertBoolean(String field, Map<String, ? extends Object> data) {
		return Optional.ofNullable(data.getOrDefault(field, null)).map(v -> Boolean.parseBoolean(v.toString()))
				.orElse(false);
	}

}
