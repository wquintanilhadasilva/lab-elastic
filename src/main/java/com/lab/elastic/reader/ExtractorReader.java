package com.lab.elastic.reader;

import lombok.Setter;
import org.springframework.util.Assert;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Reader para o conteúdo enviado em Extrations da resposta retornada pelo OCR
 *
 * @author Wedson Silva
 * @see <a href="https://dev.azure.com/oobj-devops/Recebe/_workitems/edit/925">Azzure #925</a>
 * @since 04/01/2023
 */
public class ExtractorReader {
	
	private final Map<String, Object> extractions;
	
	@Setter
	private String defaultKey;
	
	public ExtractorReader(Object extrations) {
		Assert.notNull(extrations, "Não foi possível extrair o conteúdo do documento [Extrations]");
		this.extractions = (Map<String, Object>) extrations;
		defaultKey = "0";
	}
	
	public ExtractorReader(Map<String, Object> extrations) {
		Assert.notNull(extrations, "Extrations não pode ser nulo");
		this.extractions = extrations;
		defaultKey = "0";
	}
	
	public ExtractorReader(String jsonContent) {
		Assert.notNull(jsonContent, "Extrations não pode ser nulo");
		if (jsonContent.isEmpty()) {
			this.extractions = new HashMap<>();
		} else {
			this.extractions = ExtractorField.convertJson2Map(jsonContent);
		}
		defaultKey = "0";
	}
	
	public ExtractorReader(String content, Boolean isXml, Boolean isList) {
		Assert.notNull(content, "Content não pode ser nulo");
		if (content.isEmpty()) {
			this.extractions = new HashMap<>();
		} else {
			if (isXml) {
				if (isList) {
					this.extractions = ExtractorField.convertXml2List(content);
				} else {
					this.extractions = ExtractorField.convertXml2Map(content);
				}
			} else {
				this.extractions = ExtractorField.convertJson2Map(content);
			}
		}
		defaultKey = "0";
	}
	
	public int size() {
		return extractions.size();
	}
	
	public Set<String> keys() {
		return extractions.keySet();
	}
	
	public Set<Map.Entry<String, Object>> entrySet() {
		return extractions.entrySet();
	}
	
	/**
	 * Retorna um valor de um campo como String
	 *
	 * @param field nome do atributo
	 *
	 * @return valor do atributo como String. Nulo caso não exista
	 */
	public String asString(String field) {
		return ExtractorField.builder()
				.value(extractions.getOrDefault(field, null))
				.field(field)
				.build()
				.asString();
	}
	
	/**
	 * Retorna um valor de um campo como Integer
	 *
	 * @param field nome do atributo
	 *
	 * @return valor do atributo como Integer. Nulo caso não exista
	 */
	public Integer asInteger(String field) {
		return ExtractorField.builder()
				.value(extractions.getOrDefault(field, null))
				.field(field)
				.build()
				.asInteger();
	}
	
	/**
	 * Retorna um valor de um campo como Long
	 *
	 * @param field nome do atributo
	 *
	 * @return valor do atributo como Long. Nulo caso não exista
	 */
	public Long asLong(String field) {
		return ExtractorField.builder()
				.value(extractions.getOrDefault(field, null))
				.field(field)
				.build()
				.asLong();
	}
	
	/**
	 * Retorna um valor de um campo como Double
	 *
	 * @param field nome do atributo
	 *
	 * @return valor do atributo como Double. Nulo caso não exista
	 */
	public Double asDouble(String field) {
		return ExtractorField.builder()
				.value(extractions.getOrDefault(field, null))
				.field(field)
				.build()
				.asDouble();
	}
	
	/**
	 * Retorna um valor de um campo como BigDecimal
	 *
	 * @param field nome do atributo
	 *
	 * @return valor do atributo como BigDecimal. Nulo caso não exista
	 */
	public BigDecimal asBigDecimal(String field) {
		return ExtractorField.builder()
				.value(extractions.getOrDefault(field, null))
				.field(field)
				.build()
				.asBigDecimal();
	}
	
	/**
	 * Retorna um valor de um campo como LocalDate
	 *
	 * @param field nome do atributo
	 *
	 * @return valor do atributo como LocalDate. Nulo caso não exista
	 */
	public LocalDate asLocalDate(String field) {
		return ExtractorField.builder()
				.value(extractions.getOrDefault(field, null))
				.field(field)
				.build()
				.asLocalDate();
	}
	
	/**
	 * Retorna um valor de um campo como LocalDate
	 *
	 * @param field  nome do atributo
	 * @param format formato de entrada
	 *
	 * @return valor do atributo como LocalDate. Nulo caso não exista
	 */
	public LocalDate asLocalDate(String field, String format) {
		return ExtractorField.builder()
				.value(extractions.getOrDefault(field, null))
				.field(field)
				.build()
				.asLocalDate(format);
	}
	
	/**
	 * Retorna um valor de um campo como LocalDateTime
	 *
	 * @param field nome do atributo
	 *
	 * @return valor do atributo como LocalDateTime. Nulo caso não exista
	 */
	public LocalDateTime asLocalDateTime(String field) {
		return ExtractorField.builder()
				.value(extractions.getOrDefault(field, null))
				.field(field)
				.build()
				.asLocalDateTime();
	}
	
	/**
	 * Retorna um valor de um campo como LocalDateTime
	 *
	 * @param field  nome do atributo
	 * @param format formato de entrada
	 *
	 * @return valor do atributo como LocalDateTime. Nulo caso não exista
	 */
	public LocalDateTime asLocalDateTime(String field, String format) {
		return ExtractorField.builder()
				.value(extractions.getOrDefault(field, null))
				.field(field)
				.build()
				.asLocalDateTime(format);
	}
	
	/**
	 * Retorna um valor de um campo como Map
	 *
	 * @param field nome do atributo
	 *
	 * @return valor do atributo como Map. Nulo caso não exista
	 */
	public <T, V> Map<T, V> asJson2Map(String field) {
		return ExtractorField.builder()
				.value(extractions.getOrDefault(field, null))
				.field(field)
				.build()
				.asJson2Map();
	}
	
	public <T, V> Map<T, V> asMap(String field) {
		return ExtractorField.builder()
				.value(extractions.getOrDefault(field, null))
				.field(field)
				.build()
				.asMap();
	}
	
	/**
	 * Obtem a referência do Field
	 *
	 * @param field nome do atributo desejado
	 *
	 * @return referência da abstração do {@link ExtractorField} contendo o valor do atributo
	 */
	public ExtractorField field(String field) {
		return ExtractorField.builder()
				.value(extractions.getOrDefault(field, null))
				.field(field)
				.build();
	}
	
	/**
	 * Obtem o campo a partir de uma lista de valores que determinam seu nome possível
	 *
	 * @param field lista de nomes possíveis para o campo
	 *
	 * @return referência do primeiro campo que encontrar em relação um dos nomes informados
	 */
	public ExtractorField like(String... field) {
		// Para eliminar os valores duplicados na fonte de pesquisa
		HashSet<String> valuesFromArray = new HashSet<>(Arrays.asList(field));
		
		var result = extractions.entrySet()
				.stream()
				.filter(p -> checkContains(p.getKey(), valuesFromArray))
				.findFirst();
		
		ExtractorField value = ExtractorField.builder()
				.field(defaultKey)
				.build();
		if (result.isPresent()) {
			var extrator = new ExtractorReader(result.get().getValue());
			value = extrator.asDefaultField();
		}
		return value;
	}
	
	public ExtractorReader likeAsExtrator(String... field) {
		// Para eliminar os valores duplicados na fonte de pesquisa
		HashSet<String> valuesFromArray = new HashSet<>(Arrays.asList(field));
		
		var result = extractions.entrySet()
				.stream()
				.filter(p -> checkContains(p.getKey(), valuesFromArray))
				.findFirst();
		
		ExtractorReader value = new ExtractorReader(new HashMap<>());
		if (result.isPresent()) {
			value = new ExtractorReader(result.get().getValue());
		}
		return value;
	}
	
	public ExtractorReader likeAsExtrator(boolean isJson, String... field) {
		// Para eliminar os valores duplicados na fonte de pesquisa
		HashSet<String> valuesFromArray = new HashSet<>(Arrays.asList(field));
		
		var result = extractions.entrySet()
				.stream()
				.filter(p -> checkContains(p.getKey(), valuesFromArray))
				.findFirst();
		
		ExtractorReader value = new ExtractorReader(new HashMap<>());
		if (result.isPresent()) {
			if (isJson) {
				value = new ExtractorReader(asJson2Map(result.get().getKey()));
			} else {
				value = new ExtractorReader(result.get().getValue());
			}
		}
		return value;
	}
	
	public ExtractorField asDefaultField() {
		return ExtractorField.builder()
				.value(extractions.getOrDefault(defaultKey, null))
				.field(defaultKey)
				.build();
	}
	
	public ExtractorReader asExtractorReader(String field, boolean isJson) {
		ExtractorReader reader = new ExtractorReader(new HashMap<>());
		boolean contaisValue = extractions.containsKey(field);
		if (contaisValue) {
			if (isJson) {
				reader = new ExtractorReader(asJson2Map(field));
			} else {
				reader = new ExtractorReader(extractions.getOrDefault(field, null));
			}
		}
		return reader;
	}
	
	public ExtractorReader asExtractorReader(String field) {
		return this.asExtractorReader(field, false);
	}
	
	private boolean checkContains(String value, HashSet<String> valuesFromArray) {
		for (String option : valuesFromArray) {
			// Não pode inverter os valores comparados pois quem deve conter é a string value
			if (value.toLowerCase().contains(option.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	public void ifPresentOrElse(Consumer<ExtractorReader> func, Runnable emptyAction) {
		if (this.extractions != null) {
			func.accept(this);
		} else {
			emptyAction.run();
		}
	}
	
	public ExtractorReader asList(int row) {
		return ExtractorField.builder()
				.value(extractions.getOrDefault(row, null))
				.field(String.valueOf(row))
				.build()
				.asExtractorReader();
	}
	
	public List<ExtractorReader> asList() {
		var list = new ArrayList<ExtractorReader>();
		extractions.entrySet().stream().forEach(entry ->
			list.add(
				ExtractorField.builder()
					.value(extractions.getOrDefault(entry.getKey(), null))
					.field(String.valueOf(entry.getKey()))
					.build()
					.asExtractorReader()
			)
		);
		return list;
	}
	
	/**
	 * Retorna o conteúdo do campo informado como uma lista de {@link ExtractorReader}
	 * Utilize esse método quando tiver certeza de que o conteúdo deve sempre ser uma listagem
	 * O Reader vai tentar converter o valor, se existir, em uma Collection
	 * @param field nome do campo
	 * @return Lista vazia se não existir o campo ou lista preenchida com {@link ExtractorReader}
	 */
	public List<ExtractorReader> asList(String field) {
		var list = new ArrayList<ExtractorReader>();
		extractions.entrySet().stream().filter(f -> f.getKey().equals(field)).forEach(entry ->
			buildField(String.valueOf(entry.getKey()), extractions.getOrDefault(entry.getKey(), null), list)
		);
		return list;
	}
	
	private void buildField(String fieldName, Object fieldValue, List<ExtractorReader> fields) {
		if (fieldValue == null) {
			fields.add(ExtractorField.builder()
					.value(fieldValue)
					.field(fieldName)
					.build()
					.asExtractorReader()
			);
			return;
		}
		if (fieldValue instanceof Collection) {
			Collection<?> collection = (Collection<?>) fieldValue;
			fields.addAll(collection.stream().map(ExtractorReader::new).collect(Collectors.toList()));
			return;
		}
		fields.add(
			ExtractorField.builder()
				.value(fieldValue)
				.field(fieldName)
				.build()
				.asExtractorReader()
		);
	}
	
	@Override
	public String toString() {
		return this.extractions.toString();
	}
	
	public boolean isEmpty() {
		return this.extractions == null || this.extractions.isEmpty();
	}
	
}
