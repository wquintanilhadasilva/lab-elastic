package com.lab.elastic.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Det {
	private Prod prod;
	private Imposto imposto;
}
