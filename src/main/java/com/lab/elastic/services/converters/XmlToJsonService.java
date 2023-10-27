package com.lab.elastic.services.converters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@Slf4j
public class XmlToJsonService {
	
	private final ObjectMapper objectMapper;
	
	private Pattern pattern = Pattern.compile("^(.+/)([^/]+)$");
	private XmlMapper xmlMapper = new XmlMapper();
	
	public Optional<JsonNode> xml2JsonNode(String xml) throws IOException {
		return this.xml2JsonNode(xml, null);
	}
	
	public Optional<JsonNode> xml2JsonNode(String xml, Set<String> pathToArrayConvert) throws IOException {
		if (Strings.isBlank(xml)) {
			log.warn("Nenhum conteúdo xml para ser convertido [{}]", xml);
			return Optional.empty();
		}
		
		if (pathToArrayConvert == null || pathToArrayConvert.isEmpty()) {
			log.warn("Nenhum path a ser convertido em array!");
		}
		
		//Remove a tag xml para ser inserida antes do novo elemento root
		String root = xml.replaceAll("^<\\?xml[^>]+>", "");
		
		/**
		 * Remove o cabeçalho xml do original e acrescenta um novo elemento ROOT para que
		 * o próprio root do xml esteja presente no json resultante
		 */
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<root>");
		sb.append(root);
		sb.append("</root>");
		
		JsonNode jsonNode = xmlMapper.readTree(sb.toString());
		
		// Agora converte os campos em array
		convert(pathToArrayConvert, jsonNode);
		
		return Optional.ofNullable(jsonNode);
	}
	
	private void convert(Set<String> pathToArrayConvert, JsonNode jsonNode) {
		if (pathToArrayConvert == null || pathToArrayConvert.isEmpty()) {
			return;
		}
		pathToArrayConvert.stream().forEach(path -> convertToArray(path, jsonNode));
	}
	
	private void convertToArray(String path, JsonNode jsonNode) {
		JsonNode element = jsonNode.at(path);
		if (element.isEmpty() || element.isArray()) {
			return;
		}
		Matcher matcher = pattern.matcher(path);
		
		if (matcher.matches()) {
			String prefixPath = matcher.group(1);
			prefixPath = prefixPath.substring(0, prefixPath.length() -1); //remove a barra ao final
			String field = matcher.group(2);
			
			var array = objectMapper.createArrayNode();
			array.add(element);
			
			ObjectNode objectNode = (ObjectNode) jsonNode.at(prefixPath);
			objectNode.set(field, array);
		}
	}
}
