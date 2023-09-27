package com.lab.elastic.services;

import br.com.oobj.model.xml.wrapper.XmlOptionsUtil;
import br.com.oobj.model.xml.wrapper.nfcom.IXmlNfCom;
import br.com.oobj.model.xml.wrapper.nfcom.XmlNfComWrapperFactory;
import br.inf.portalfiscal.nfcom.NFComDocument;
import br.inf.portalfiscal.nfcom.TNFCom;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.json.JsonData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.lab.elastic.controllers.Dominio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.apache.xmlbeans.XmlException;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Repository
@Slf4j
public class ElasticRepository {
	
	private final ElasticsearchClient elasticsearchClient;
	private final ObjectMapper objectMapper;
	
	public void index(Dominio dominio) throws IOException, XmlException {
		
		
		JsonNode jsonNode = objectMapper.valueToTree(dominio);
		
		var result = wrapper(dominio.getPayload());
		log.info("aaa [{}]", result);
		
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
	
	private String wrapper(String xml) throws JsonProcessingException, XmlException {
		IXmlNfCom nfcom = XmlNfComWrapperFactory.getInstance().getXmlDFe(xml);
		TNFCom tnfecom = NFComDocument.Factory.parse(xml, XmlOptionsUtil.xmlOption(NFComDocument.type, "http://www.portalfiscal.inf.br/nfcom")).getNFCom();
		log.info("IXmlNFCom [{}]", nfcom);
		log.info("TNFCom [{}]", tnfecom);
		log.info("TNFCom2 [{}]", tnfecom.xmlText());
		return tnfecom.toString();
//		inspectObject(nfcom);
		
//		var json = objectMapper.writeValueAsString(nfcom);
//		log.info("XMl wrapper [{}]", json);
		
	}
	
	private Optional<JsonNode> xml2JsonNode(String xml) throws IOException {
		if (Strings.isBlank(xml)) {
			return Optional.empty();
		}
		XmlMapper xmlMapper = new XmlMapper();
		JsonNode jsonNode = xmlMapper.readTree(xml);
		
//
//		var det = jsonNode.get("infNFCom").get("det");
//		var parent = (ObjectNode) jsonNode.get("infNFCom");
//		var array = objectMapper.createArrayNode();
//		array.add(det);
//		var p = parent.set("det",array);
		
		jsonNode = convertToArray("/infNFCom/det", jsonNode);
		
		return Optional.ofNullable(jsonNode);
	}
	
	private JsonNode setPayload(JsonNode source, JsonNode newElement) {
		ObjectNode src = (ObjectNode) source;
		src.set("payload", newElement);
		return src;
	}
	
	
	public static void inspectObject(Object obj) {
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		
		for (Field field : fields) {
			field.setAccessible(true);
			
			if (field.getType().isArray()) {
				System.out.println("Array: " + field.getName());
			} else if (field.getType().isPrimitive() || field.getType().equals(String.class)) {
				System.out.println("Simple: " + field.getName());
			} else {
				System.out.println("Complex: " + field.getName());
				try {
					Object fieldValue = field.get(obj);
					if (fieldValue != null) {
						inspectObject(fieldValue);
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	private JsonNode convertToArray(String path, JsonNode jsonNode) {
		Pattern pattern = Pattern.compile("^(.+/)([^/]+)$");
		Matcher matcher = pattern.matcher(path);
		
		if (matcher.matches()) {
			String primeiroGrupo = matcher.group(1);
			primeiroGrupo = primeiroGrupo.substring(0, primeiroGrupo.length() -1); //remove a barra ao final
			String segundoGrupo = matcher.group(2);
			
			ObjectNode objectNode = (ObjectNode) jsonNode.at(primeiroGrupo);
			
			JsonNode element = jsonNode.at(path);
			
			if (!element.isArray()) {
				var array = objectMapper.createArrayNode();
				array.add(element);
				objectNode.set(segundoGrupo, array);
			}
			
		}
		return  jsonNode;
	}

}
