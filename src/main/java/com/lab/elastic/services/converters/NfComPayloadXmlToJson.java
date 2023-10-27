package com.lab.elastic.services.converters;

import com.lab.elastic.services.ModeloType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class NfComPayloadXmlToJson implements PayloadConverter {
	
	private final XmlToJsonService xmlToJsonService;
	
	private static final Set<String> arrayPaths;
	
	/**
	 * Mapeia os atributos do xml que são arrays conforme layout XSD
	 * Necessário para os casos em que houver apenas um elemento, o
	 * mapeamento json possa convertê-los em array com um item e não
	 * como atributo simples
	 */
	static {
		arrayPaths = new HashSet<>();
		arrayPaths.add("/NFCom/infNFCom/det");
		arrayPaths.add("/NFCom/infNFCom/det/imposto/ICMSUFDest");
		arrayPaths.add("/NFCom/infNFCom/det/gProcRef/gProc");
		arrayPaths.add("/NFCom/infNFCom/autXML");
		arrayPaths.add("/NFCom/infNFCom/infAdic/infCpl");
	}
	
	@Override
	public boolean match(ModeloType modeloType) {
		return modeloType.equals(ModeloType.NFCOM);
	}
	
	@Override
	public String convert(String content) {
		// Converte o conteúdo xml em json
		try {
			var result = xmlToJsonService.xml2JsonNode(content, arrayPaths);
			if (result.isEmpty()) {
				return null;
			}
			return result.get().toString();
		} catch (IOException e) {
			log.error("Erro ao converter o xml em json", e);
			throw new RuntimeException(e);
		}
	}
	
}
