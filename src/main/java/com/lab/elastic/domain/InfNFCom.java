package com.lab.elastic.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InfNFCom {
//	private Ide ide;
//	private Emit emit;
//	private Dest dest;
//	private Assinante assinante;
	private List<Det> det; // Agora det é uma lista
//	private Total total;
//	private GFatCentral gFatCentral;
}
