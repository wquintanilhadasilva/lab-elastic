package com.lab.elastic.services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.json.JsonData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.lab.elastic.controllers.Dominio;
import com.lab.elastic.reader.PathUtils;
import com.lab.elastic.repository.ElasticRequestRepository;
import com.lab.elastic.repository.entidades.CollectionsIndexHelper;
import com.lab.elastic.repository.entidades.ElasticRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Repository
@Slf4j
public class ElasticRepository implements ElasticRequestRepository {
	
	private final ElasticsearchClient elasticsearchClient;
	private final ObjectMapper objectMapper;
	
	private static final String PAYLOAD_FIELD = "payload";
	private static final String PAYLOADKEY_FIELD = "payloadField";
	private static final String REQUESTID_FIELD = "requestid";
	
	private Pattern pattern = Pattern.compile("^(.+/)([^/]+)$");
	
	public void index(Dominio dominio) throws IOException {
		
		JsonNode jsonNode = objectMapper.valueToTree(dominio);
		
		xml2JsonNode(dominio.getPayload()).ifPresent(v -> {
			setPayload(jsonNode, v);
		});
		
		log.info("Log [{}]", jsonNode);
		
		String json = objectMapper.writeValueAsString(jsonNode);
		log.info("str: [{}]", json);
		Reader input = new StringReader(json);
		
		IndexRequest<JsonData> request = IndexRequest.of(i ->
			i.index("teste")
			.withJson(input)
		);
		
		IndexResponse response = elasticsearchClient.index(request);
		log.info("Retorno [{}]", response);
		
	}
	
	public void index(Map<String, Object> dominio, String payloaField) throws IOException {
		
		if (Strings.isBlank(payloaField)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo Payload não informado!");
		}
		String payloadString = (String) dominio.getOrDefault(payloaField, null);
		
		if (Strings.isBlank(payloadString)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo Payload nulo ou vazio!");
		}
		
		// Teste do leitor dinâmico de propriedade
		var obj = PathUtils.getValue(dominio, "agregado.outro.field");
		log.info("path [{}]", obj);
		
		//Inclui o campo que identifica o atributo payload na estrutura armazenada
		dominio.put(PAYLOADKEY_FIELD, payloaField);
		
		//Limpa o conteúdo do campo payload da requisição original
		dominio.remove(payloaField);
		
		JsonNode jsonDomain = objectMapper.valueToTree(dominio);
		
		log.info("Documento em JSON [{}]", jsonDomain);
		
		String json = objectMapper.writeValueAsString(jsonDomain);
		log.info("str: [{}]", json);
		Reader input = new StringReader(json);
		
		IndexRequest<JsonData> request = IndexRequest.of(i ->
			i.index("teste-map").withJson(input)
		);
		IndexResponse response = elasticsearchClient.index(request);
		log.info("Retorno [{}]", response);
		
		
		JsonData originalData = JsonData.of(dominio);
		IndexRequest<JsonData> originalRequestData = IndexRequest.of(i ->
			i.index("teste-map-original").document(originalData)
		);
		IndexResponse r = elasticsearchClient.index(originalRequestData);
		log.info("Resposta Elastic [{}]", dominio);
		dominio.put("id", r.id());
		log.info("Retorno Original [{}]", dominio);
		
		indexPayload(payloadString,r.id());
	}
	
	private void indexPayload(String payload, String requestid) throws IOException {
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put(REQUESTID_FIELD, requestid);
		jsonMap.put(PAYLOAD_FIELD, payload);
		
		JsonNode jsonNode = objectMapper.valueToTree(jsonMap);
		
		xml2JsonNode(payload).ifPresent(payloadJsonValue -> {
			setPayload(jsonNode, payloadJsonValue, PAYLOAD_FIELD);
		});
		
		String json = objectMapper.writeValueAsString(jsonNode);
		log.info("Payload convertido: [{}]", json);
		Reader input = new StringReader(json);
		
		IndexRequest<JsonData> request = IndexRequest.of(i ->
				i.index("teste-map-original-payload")
					.id(requestid)
					.withJson(input)
		);
		
		IndexResponse response = elasticsearchClient.index(request);
		log.info("Payload gravado [{}]", response);
		
	}
	
	private Optional<JsonNode> xml2JsonNode(String xml) throws IOException {
		if (Strings.isBlank(xml)) {
			return Optional.empty();
		}
		//Remove a tag xml:
		String novoXml = xml.replaceAll("^<\\?xml[^>]+>", "");
		
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<root>");
		sb.append(novoXml);
		sb.append("</root>");
		XmlMapper xmlMapper = new XmlMapper();
		JsonNode jsonNode = xmlMapper.readTree(sb.toString());
		
		convertToArray("/NFCom/infNFCom/det", jsonNode);
		
		return Optional.ofNullable(jsonNode);
	}
	
	private JsonNode setPayload(JsonNode source, JsonNode newElement) {
		return setPayload(source, newElement, "payload");
	}
	
	private JsonNode setPayload(JsonNode source, JsonNode newElement, String payloadField) {
		ObjectNode src = (ObjectNode) source;
		src.set(payloadField, newElement);
		return src;
	}
	
	private void convertToArray(String path, JsonNode jsonNode) {

		JsonNode element = jsonNode.at(path);
		if (element.isEmpty() || element.isArray()) {
			return;
		}
		
		Matcher matcher = pattern.matcher(path);
		
		if (matcher.matches()) {
			String primeiroGrupo = matcher.group(1);
			primeiroGrupo = primeiroGrupo.substring(0, primeiroGrupo.length() -1); //remove a barra ao final
			String segundoGrupo = matcher.group(2);
			
			var array = objectMapper.createArrayNode();
			array.add(element);
			
			ObjectNode objectNode = (ObjectNode) jsonNode.at(primeiroGrupo);
			objectNode.set(segundoGrupo, array);
			
		}
	}
	
	@Override
	public ElasticRequest save(ElasticRequest req) {
	
		IndexRequest<ElasticRequest> request = IndexRequest.of(i ->
				i.index(CollectionsIndexHelper.REQUISICAO_INDEX)
						.document(req)
		);
		
		IndexResponse response = null;
		try {
			response = elasticsearchClient.index(request);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		req.setId(response.id());
		log.info("Retorno [{}]", response);
		return req;
	}
	
	@Override
	public Optional<ElasticRequest> findById(String id) {
		return this.searchById(id, "teste-map-original-payload", ElasticRequest.class);
	}
	
	@Override
	public Optional<Map<String, Object>> find(String id) {
		var result = Optional.empty();
//		Optional<?> opt = this.searchById(id, "teste-map-original-payload", Map.class);
		Optional<?> original = this.searchById(id, "teste-map-original", Map.class);
		
		if (original.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requisição não encontrada!");
		}
		
		Map<String,Object> requisicao = (Map<String, Object>) original.get();
		
		//Adiciona o conteúdo enviado
		String payloadKey = requisicao.get(PAYLOADKEY_FIELD).toString();
		requisicao.put(payloadKey, null);
		
		//Remove o atributo que contém a chave do campo id;
		requisicao.remove(PAYLOADKEY_FIELD);
		
		// Agora pega o json resultante que está noutra collection...
		Optional<?> json = this.searchById(id, "teste-map-original-payload", Map.class);
		if (json.isPresent()) {
			var temp = (Map<String, Object>) json.get();
			var payload = (Map<String, Object>) temp.get(PAYLOAD_FIELD);
			//Substitiu pelo conteúdo enviado que foi encontrado
			requisicao.put(payloadKey, payload);
		}
		
		return Optional.ofNullable(requisicao);
	}
	
	public <T> Optional<T> searchById(String id, String index, Class<T> classz) {
		
		GetResponse<T> response = null;
		try {
			response = elasticsearchClient.get(g ->
							g.index(index)
									.id(id), classz);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Optional<T> result = Optional.empty();
		if (response.found()) {
			result = Optional.ofNullable(response.source());
		}
		return result;
	}
	
}
