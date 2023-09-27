package com.lab.elastic.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Prod {
	private String cProd;
	private String xProd;
	private String cClass;
	private String uMed;
	private String qFaturada;
	private String vItem;
	private String vProd;
}
