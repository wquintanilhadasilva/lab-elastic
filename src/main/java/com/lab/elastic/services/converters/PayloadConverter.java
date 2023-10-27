package com.lab.elastic.services.converters;

import com.lab.elastic.services.ModeloType;

public interface PayloadConverter {
	
	boolean match(ModeloType modeloType);
	String convert(String content);
	
}
