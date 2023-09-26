package com.lab.elastic.reader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.Builder;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.Assert;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Representa um valor de um campo extraído da estrutura dinâmica retornada pelo OCR
 *
 * @author Wedson Silva
 * @see <a href="https://dev.azure.com/oobj-devops/Recebe/_workitems/edit/925">Azzure #925</a>
 * @since 04/01/2023
 */
@Builder
public class ExtractorField {
	
	private final Object value;
	private final String field;
	
	/**
	 * Construtor com objetos obrigatórios
	 *
	 * @param value valor do campo
	 * @param field identificação do campo
	 */
	public ExtractorField(Object value, String field) {
		Assert.notNull(field, "[field] não pode ser nulo");
		this.value = value;
		this.field = field;
	}
	
	/**
	 * Retorna um valor de um campo como String
	 *
	 * @return valor do atributo como String. Nulo caso não exista
	 */
	public String asString() {
		try {
			if (value != null) {
				return String.valueOf(value);
			}
			return null;
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * Retorna um valor de um campo como Integer
	 *
	 * @return valor do atributo como Integer. Nulo caso não exista
	 */
	public Integer asInteger() {
		try {
			if (value != null) {
				return Integer.parseInt(value.toString());
			}
			return null;
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * Retorna um valor de um campo como Long
	 *
	 * @return valor do atributo como Long. Nulo caso não exista
	 */
	public Long asLong() {
		try {
			if (value != null) {
				return Long.valueOf(value.toString());
			}
			return null;
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * Retorna um valor de um campo como Double
	 *
	 * @return valor do atributo como Double. Nulo caso não exista
	 */
	public Double asDouble() {
		try {
			if (value != null) {
				return Double.valueOf(value.toString());
			}
			return null;
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * Retorna um valor de um campo como BigDecimal
	 *
	 * @return valor do atributo como BigDecimal. Nulo caso não exista
	 */
	public BigDecimal asBigDecimal() {
		return asBigDecimal(false);
	}
	
	public Boolean asBoolean() {
		if (isNull()) {
			return false;
		}
		return Boolean.valueOf(value.toString());
	}
	
	/**
	 * Retorna um valor de um campo como BigDecimal
	 *
	 * @param commanReplace indicador se é para fazer o replace da vírgula por ponto
	 *
	 * @return valor do atributo como BigDecimal. Nulo caso não exista
	 */
	public BigDecimal asBigDecimal(boolean commanReplace) {
		try {
			if (Objects.isNull(value)) {
				return null;
			}
			String valueString = value.toString();
			if (Strings.isEmpty(valueString)) {
				return null;
			}
			if (commanReplace) {
				String valueTemp = valueString;
				valueTemp = valueTemp.trim();
				valueTemp = valueTemp.replace(".", "");
				valueTemp = valueTemp.replace(",", ".");
				return BigDecimal.valueOf(Double.valueOf(valueTemp.toString()));
			} else {
				return BigDecimal.valueOf(Double.valueOf(valueString));
			}
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * Retorna um valor de um campo como LocalDate
	 *
	 * @return valor do atributo como LocalDate. Nulo caso não exista
	 */
	public LocalDate asLocalDate() {
		try {
			if (value != null) {
				return LocalDate.parse(value.toString());
			}
			return null;
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * Retorna um valor de um campo como LocalDate
	 *
	 * @param format formato de entrada
	 *
	 * @return valor do atributo como LocalDate. Nulo caso não exista
	 */
	public LocalDate asLocalDate(String format) {
		try {
			if (value != null) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
				return LocalDate.parse(value.toString(), formatter);
			}
			return null;
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * Retorna um valor de um campo como LocalDateTime
	 *
	 * @return valor do atributo como LocalDateTime. Nulo caso não exista
	 */
	public LocalDateTime asLocalDateTime() {
		try {
			if (value != null) {
				return LocalDateTime.parse(value.toString(), DateTimeFormatter.ISO_DATE_TIME);
			}
			return null;
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * Retorna um valor de um campo como LocalDateTime
	 *
	 * @param format formato de entrada
	 *
	 * @return valor do atributo como LocalDateTime. Nulo caso não exista
	 */
	public LocalDateTime asLocalDateTime(String format) {
		try {
			if (value != null) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
				return LocalDateTime.parse(value.toString(), formatter);
			}
			return null;
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * Retorna um valor de um campo como Object
	 *
	 * @param format formato de entrada
	 *
	 * @return valor do atributo como Object. Nulo caso não exista
	 */
	public Object asObject(String format) {
		return value;
	}
	
	/**
	 * Retorna um valor de um campo como Map
	 *
	 * @return valor do atributo como Map. Nulo caso não exista
	 */
	public <T, V> Map<T, V> asJson2Map() {
		if (value != null) {
			String jsonContent = String.valueOf(value);
			Map<T, V> data = convertJson2Map(jsonContent);
			return data;
		}
		return null;
	}
	
	public <T, V> Map<T, V> asMap() {
		if (value != null) {
			Map<T, V> data = (Map<T, V>) value;
			return data;
		}
		return null;
	}
	
	public <T> List<T> asMapList() {
		if (value != null) {
			List<T> data = (List<T>) value;
			return data;
		}
		return null;
	}
	
	public List<ExtractorReader> asExtrationsReaderList() {
		if (value != null) {
			List<ExtractorReader> data = new ArrayList<>();
			List<?> vl = (List<?>) value;
			vl.stream().forEach(v -> {
				data.add(new ExtractorReader(v));
			});
			return data;
		}
		return Collections.emptyList();
	}
	
	public ExtractorReader asExtractorReader() {
		if (value != null) {
			ExtractorReader data = new ExtractorReader(value);
			return data;
		}
		return new ExtractorReader(new HashMap<>());
	}
	
	public boolean isNull() {
		return this.value == null;
	}
	
	public boolean isNotNull() {
		return this.value != null;
	}
	
	public static <T, V> Map<T, V> convertJson2Map(String jsonContent) {
		if (jsonContent != null) {
			ObjectMapper mapper = new ObjectMapper();
			Map<T, V> data = null;
			try {
				data = mapper.readValue(jsonContent, HashMap.class);
			} catch (JsonProcessingException e) {
				throw new RuntimeException("Erro ao converter o conteúdo json [" + jsonContent + "] em Map<>", e);
			}
			return data;
		}
		return null;
	}
	
	public static <K, V> Map<K, V> convertXml2Map(String xmlContent) {
		if (xmlContent != null) {
			ObjectMapper mapper = new XmlMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			Map<K, V> data = null;
			try {
				data = mapper.readValue(xmlContent, Map.class);
			} catch (JsonProcessingException e) {
				throw new RuntimeException("Erro ao converter o conteúdo xml [" + xmlContent + "] em Map<>", e);
			}
			return (Map<K, V>) data;
		}
		return null;
	}
	
	public static <K, V> Map<K, V> convertXml2List(String xmlContent) {
		
		if (xmlContent == null) {
			return null;
		}
		
		ObjectMapper mapper = new XmlMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		final Map<Object, Object> data = new HashMap<>();
		try {
			var list = mapper.readValue(xmlContent, List.class);
			if (list != null && !list.isEmpty()) {
				int length = list.size();
				IntStream.range(0, length).forEach(index -> data.put(index, list.get(index)));
			}
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Erro ao converter o conteúdo xml [" + xmlContent + "] em Map<>", e);
		}
		return (Map<K, V>) data;
	}
	
}
