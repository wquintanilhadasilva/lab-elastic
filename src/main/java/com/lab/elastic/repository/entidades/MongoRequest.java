package com.lab.elastic.repository.entidades;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Representa uma requisição enviada para o motor de processamento. Contém informações básicas para rastrear
 * uma requisição que chega na aplicação para processar um documento fiscal e/ou realizar uma operação que
 * envolva um documento fiscal.
 * Essa requisição é enviada por um cliente e, portanto, ela contém o vínculo a uma filial, empresa e conta.
 *
 * @author Wedson Silva
 * @see <a href="https://dev.azure.com/oobj-devops/Engineering/_workitems/edit/1973">Azzure #1973</a>
 * @since 24/08/2023
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = CollectionsIndexHelper.REQUISICAO_INDEX)
@CompoundIndexes({
	@CompoundIndex(name = "idx_conta", def = "{'idConta' : 1, 'cnpjBase': 1, 'emissor' : 1}")
})
public class MongoRequest {
	
	/**
	 * Para maiores informações sobre a construção de índices compostos, consulte:
	 * @link "<a href="https://www.mongodb.com/docs/manual/tutorial/equality-sort-range-rule/#std-label-esr-indexing-rule"></a>
	 */
	
	/*
	   Aqui os Enuns são tratados como Strings, o Valor String do Enum será armazenado,
	   isso vai evitar necessidade de ajustes caso novos elementos sejam acrescentados.
	   Isso pois o objetivo é manter a informação e realizar filtro, ainda não tem o propósito
	   de tratar de forma específica e diferenciada caso os valores sejam diferentes.
	 */
	
	@Id
	@Field(RequisicaoProperties.ID)
	private String id;
	
	@Field(RequisicaoProperties.IDEMPRESA)
	private Long idEmpresa;
	
	@Field(RequisicaoProperties.RAZAOSOCIAL)
	private String razaoSocial;
	
	@Field(RequisicaoProperties.NOMEFANTASIA)
	private String nomeFantasia;
	
	@Field(RequisicaoProperties.NOMEEXIBICAO)
	private String nomeExibicao;
	
	@Field(RequisicaoProperties.NOMEFANTASIANORMALIZADO)
	private String nomeFantasiaNormalizado;
	
	@Field(RequisicaoProperties.RAZAOSOCIALNORMALIZADO)
	private String razaoSocialNormalizado;
	
	@Field(RequisicaoProperties.NOMEEXIBICAONORMALIZADO)
	private String nomeExibicaoNormalizado;
	
	@Field(RequisicaoProperties.FILIALID)
	private Long filialId;
	
	@Field(RequisicaoProperties.EMISSOR)
	private String emissor; // CNPJ ou CPF do emissor do documento fiscal
	
	@Field(RequisicaoProperties.FILIALNOMEAPRESENTACAO)
	private String filialNomeApresentacao;
	
	@Field(RequisicaoProperties.FILIALINSCRICAOMUNICIPAL)
	private String filialInscricaoMunicipal;
	
	@Field(RequisicaoProperties.FILIALHABILITADA)
	private Boolean filialHabilitada;
	
	@Field(RequisicaoProperties.IDMUNICIPIO)
	private Long idMunicipio;
	
	@Field(RequisicaoProperties.NOMEMUNICIPIO)
	private String nomeMunicipio;
	
	@Field(RequisicaoProperties.SIGLAUF)
	private String siglaUF;
	
	@Field(RequisicaoProperties.CODIGOIBGE)
	private String codigoIBGE;
	
	@Field(RequisicaoProperties.NOMEMUNICIPIOPESQUISA)
	private String nomeMunicipioPesquisa;
	
	@Field(RequisicaoProperties.CNPJBASE)
	private String cnpjBase;
	
	@Field(RequisicaoProperties.HABILITADA)
	private Boolean habilitada;
	
	@Field(RequisicaoProperties.IDCONTA)
	private Long idConta;
	
	@Field(RequisicaoProperties.NOMECONTA)
	private String nomeConta;
	
	@Field(RequisicaoProperties.RECURSOSATIVOS)
	private Set<String> recursosAtivos;
	
	@Field(RequisicaoProperties.VALIDADECERTIFICADO)
	private LocalDateTime validadeCertificado;
	
	/* Dados da requisição */
	@Field(RequisicaoProperties.WEB_HOOK_URL)
	private String webhookurl;
	
	@Field(RequisicaoProperties.INTEGRATION_ID)
	private String integrationId;
	
	@Field(RequisicaoProperties.UUID)
	private String uuid;
	
	@Field(RequisicaoProperties.DATAREQUISICAO)
	@Indexed(name = "idx_data-requisicao")
	private LocalDateTime requestReceivedDateTime;
	
	@Field(RequisicaoProperties.FILENAME)
	private String filename;
	
	@Field(RequisicaoProperties.CONTENT_TYPE)
	private String contentType;
	
	@Field(RequisicaoProperties.INTEGRACAO)// IntegracaoType
	private String integracao;
	
	@Field(RequisicaoProperties.MODELO) // ModeloType
	@Indexed(name = "idx_modelo")
	private String modelo;
	
	@Field(RequisicaoProperties.DOCUMENTO) //DocumentoType
	@Indexed(name = "idx_tipo-documento")
	private String documento;
	
	@Field(RequisicaoProperties.STORAGE_BUCKET)
	private String storageBucket;
	
	@Field(RequisicaoProperties.STORAGE_KEY)
	private String storageKey;
	
	@Field(RequisicaoProperties.DATAARMAZENAMENTO)
	private LocalDateTime dataArmazenamento;
	
	@Field(RequisicaoProperties.TIPOREQUISICAO)
	private TipoRequisicao tipoRequisicao;
	
	
	/* Em caso de erro */
	@Field(RequisicaoProperties.INDICADOR_ERRO)
	private Boolean indicadorErro;
	
	@Field(RequisicaoProperties.MENSAGEM_ERRO)
	private String mensagemDeErro;
	
	public static class RequisicaoProperties {
		
		public static final String ID = "id";
		public static final String IDEMPRESA = "idEmpresa";
		public static final String RAZAOSOCIAL = "razaoSocial";
		public static final String NOMEFANTASIA = "nomeFantasia";
		public static final String NOMEEXIBICAO = "nomeExibicao";
		public static final String NOMEFANTASIANORMALIZADO = "nomeFantasiaNormalizado";
		public static final String RAZAOSOCIALNORMALIZADO = "razaoSocialNormalizado";
		public static final String NOMEEXIBICAONORMALIZADO = "nomeExibicaoNormalizado";
		public static final String FILIALID = "filialId";
		public static final String EMISSOR = "emissor";
		public static final String FILIALNOMEAPRESENTACAO = "filialNomeApresentacao";
		public static final String FILIALINSCRICAOMUNICIPAL = "filialInscricaoMunicipal";
		public static final String FILIALHABILITADA = "filialHabilitada";
		public static final String IDMUNICIPIO = "idMunicipio";
		public static final String NOMEMUNICIPIO = "nomeMunicipio";
		public static final String SIGLAUF = "siglaUF";
		public static final String CODIGOIBGE = "codigoIBGE";
		public static final String NOMEMUNICIPIOPESQUISA = "nomeMunicipioPesquisa";
		public static final String CNPJBASE = "cnpjBase";
		public static final String HABILITADA = "habilitada";
		public static final String IDCONTA = "idConta";
		public static final String NOMECONTA = "nomeConta";
		public static final String RECURSOSATIVOS = "recursosAtivos";
		public static final String VALIDADECERTIFICADO = "validadeCertificado";
		public static final String WEB_HOOK_URL = "webhookurl";
		public static final String INTEGRATION_ID = "integrationId";
		public static final String UUID = "uuid";
		public static final String DATAREQUISICAO = "requestReceivedDateTime";
		public static final String FILENAME = "filename";
		public static final String CONTENT_TYPE = "contentType";
		public static final String INTEGRACAO = "integracao";
		public static final String MODELO = "modelo";
		public static final String DOCUMENTO = "documento";
		public static final String STORAGE_BUCKET = "storageBucket";
		public static final String STORAGE_KEY = "storageKey";
		public static final String DATAARMAZENAMENTO = "dataArmazenamento";
		public static final String TIPOREQUISICAO = "tipoRequisicao";
		public static final String INDICADOR_ERRO = "indicadorErro";
		public static final String MENSAGEM_ERRO = "mensagemDeErro";
	}
}
