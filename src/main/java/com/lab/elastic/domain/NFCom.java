package com.lab.elastic.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NFCom {
	InfNFCom infNFCom;
//	String modeloType;
//	Object infNfe;
//	Object emit;
//	Object nfDetList;
//	Object infAdic;
}

