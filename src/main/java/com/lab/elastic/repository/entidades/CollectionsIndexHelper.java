package com.lab.elastic.repository.entidades;

import lombok.experimental.UtilityClass;

/**
 * Constantes utilizadas no mapeamento de índices no Elasticsearch
 * @author Wedson Silva
 * @see <a href="https://dev.azure.com/oobj-devops/Engineering/_workitems/edit/1973">Azzure #1973</a>
 * @since 30/08/2023
 */
@UtilityClass
public class CollectionsIndexHelper {
	
	public static final String DEFAULT_SETTING = "/elasticsearch/elasticsearch-settings.json";
	public static final String ANALYZER = "trim_case_insensitive";
	public static final String REQUISICAO_INDEX = "requisicoes-index";
	
}
