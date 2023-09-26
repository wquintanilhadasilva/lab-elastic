# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.16/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.16/maven-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.7.16/reference/htmlsingle/index.html#using.devtools)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.7.16/reference/htmlsingle/index.html#web)
* [Spring Data Elasticsearch (Access+Driver)](https://docs.spring.io/spring-boot/docs/2.7.16/reference/htmlsingle/index.html#data.nosql.elasticsearch)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)



```
curl --request POST \
  --url http://localhost:7085/_find/ \
  --header 'Content-Type: application/json' \
  --data '{
	"descricao": "teste",
	"ordem": 1,
	"payload": "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NFCom xmlns=\"http:\/\/www.portalfiscal.inf.br\/nfcom\"><infNFCom versao=\"1.00\"><ide><cUF>43<\/cUF><tpAmb>2<\/tpAmb><mod>62<\/mod><serie>1<\/serie><nNF>10003<\/nNF><cNF>7081991<\/cNF><dhEmi>2023-08-08T15:00:28-03:00<\/dhEmi><tpEmis>1<\/tpEmis><nSiteAutoriz>0<\/nSiteAutoriz><cMunFG>4314902<\/cMunFG><finNFCom>0<\/finNFCom><tpFat>1<\/tpFat><\/ide><emit><CNPJ>07385111000102<\/CNPJ><IE>0018001360<\/IE><CRT>3<\/CRT><xNome>OOBJ TECNOLOGIA DA INFORMACAO<\/xNome><enderEmit><xLgr>PROF ALGACYR MUNHOZ MADER<\/xLgr><nro>2800<\/nro><xBairro>CIC<\/xBairro><cMun>4314902<\/cMun><xMun>Porto Alegre<\/xMun><CEP>81310020<\/CEP><UF>RS<\/UF><\/enderEmit><\/emit><dest><xNome>E-sales<\/xNome><CNPJ>07385111000102<\/CNPJ><indIEDest>1<\/indIEDest><IE>0963233556<\/IE><enderDest><xLgr>AV FRANCA<\/xLgr><nro>1162<\/nro><xBairro>NAVEGANTES<\/xBairro><cMun>4314902<\/cMun><xMun>PORTO ALEGRE<\/xMun><CEP>90230220<\/CEP><UF>RS<\/UF><\/enderDest><\/dest><assinante><iCodAssinante>123456789<\/iCodAssinante><tpAssinante>3<\/tpAssinante><tpServUtil>6<\/tpServUtil><nContrato>17081340<\/nContrato><dContratoIni>2023-01-01<\/dContratoIni><dContratoFim>2023-12-31<\/dContratoFim><\/assinante><?xml-multiple?><det nItem=\"1\"><prod><cProd>123<\/cProd><xProd>Assinatura de servi\u00E7os de telefonia <\/xProd><cClass>0100101<\/cClass><uMed>2<\/uMed><qFaturada>1<\/qFaturada><vItem>115.00<\/vItem><vProd>115.00<\/vProd><\/prod><imposto><indSemCST>1<\/indSemCST><\/imposto><\/det><total><vProd>115.00<\/vProd><ICMSTot><vBC>0.00<\/vBC><vICMS>0.00<\/vICMS><vICMSDeson>0.00<\/vICMSDeson><vFCP>0.00<\/vFCP><\/ICMSTot><vCOFINS>0.00<\/vCOFINS><vPIS>0.00<\/vPIS><vFUNTTEL>0.00<\/vFUNTTEL><vFUST>0.00<\/vFUST><vRetTribTot><vRetPIS>0.00<\/vRetPIS><vRetCofins>0.00<\/vRetCofins><vRetCSLL>0.00<\/vRetCSLL><vIRRF>0.00<\/vIRRF><\/vRetTribTot><vDesc>0.00<\/vDesc><vOutro>0.00<\/vOutro><vNF>115.00<\/vNF><\/total><gFatCentral><CNPJ>09553244000176<\/CNPJ><cUF>43<\/cUF><\/gFatCentral><\/infNFCom><\/NFCom>"
}'
```