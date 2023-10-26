package com.lab.elastic.services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.json.JsonData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.lab.elastic.controllers.Dominio;
import com.lab.elastic.reader.PathUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Repository
@Slf4j
public class ElasticRepository {
	
	private final ElasticsearchClient elasticsearchClient;
	private final ObjectMapper objectMapper;
	
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
	
	public void index(Map<String, ? extends Object> dominio, String payloaField) throws IOException {
		
		if (Strings.isBlank(payloaField)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo Payload não informado!");
		}
		
		var obj = PathUtils.getValue(dominio, "agregado.outro.field");
		log.info("path [{}]", obj);
		
		String payloadString = (String) dominio.getOrDefault(payloaField, null);
		JsonNode jsonDomain = objectMapper.valueToTree(dominio);

		xml2JsonNode(payloadString).ifPresent(payloadJsonValue -> {
			setPayload(jsonDomain, payloadJsonValue, payloaField);
		});
		
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
		log.info("Retorno Original [{}]", r);
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

}
