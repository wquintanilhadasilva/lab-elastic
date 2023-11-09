package com.lab.elastic.repository.entidades;

import static com.lab.elastic.repository.entidades.CollectionsIndexHelper.ANALYZER;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.annotations.Routing;
import org.springframework.data.elasticsearch.annotations.Setting;
import java.time.LocalDateTime;
import java.util.Map;
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
@Document(indexName = CollectionsIndexHelper.REQUISICAO_INDEX)
@Setting(settingPath = CollectionsIndexHelper.DEFAULT_SETTING, shards = 3)
@Mapping(mappingPath = "/elasticsearch/mappings/mapping-request.json")
@Routing("cnpjBase")
public class ElasticRequest {
	
	/*
	   Aqui os Enuns são tratados como Strings, o Valor String do Enum será armazenado,
	   isso vai evitar necessidade de ajustes caso novos elementos sejam acrescentados.
	   Isso pois o objetivo é manter a informação e realizar filtro, ainda não tem o propósito
	   de tratar de forma específica e diferenciada caso os valores sejam diferentes.
	 */
	
	@Id
	@Field(type = FieldType.Keyword)
	private String id;
	
	@Field(type = FieldType.Long)
	private Long idEmpresa;
	
	@MultiField(mainField = @Field(type = FieldType.Text, analyzer = ANALYZER),
			otherFields = { @InnerField(suffix = "sort", type = FieldType.Keyword) })
	private String razaoSocial;

	@MultiField(mainField = @Field(type = FieldType.Text, analyzer = ANALYZER),
			otherFields = { @InnerField(suffix = "sort", type = FieldType.Keyword) })
	private String nomeFantasia;

	@MultiField(mainField = @Field(type = FieldType.Text, analyzer = ANALYZER),
			otherFields = { @InnerField(suffix = "sort", type = FieldType.Keyword) })
	private String nomeExibicao;

	@MultiField(mainField = @Field(type = FieldType.Text, analyzer = ANALYZER),
			otherFields = { @InnerField(suffix = "sort", type = FieldType.Keyword) })
	private String nomeFantasiaNormalizado;

	@MultiField(mainField = @Field(type = FieldType.Text, analyzer = ANALYZER),
			otherFields = { @InnerField(suffix = "sort", type = FieldType.Keyword) })
	private String razaoSocialNormalizado;

	@MultiField(mainField = @Field(type = FieldType.Text, analyzer = ANALYZER),
			otherFields = { @InnerField(suffix = "sort", type = FieldType.Keyword) })
	private String nomeExibicaoNormalizado;

	@Field(type = FieldType.Long)
	private Long filialId;

	@Field(type = FieldType.Keyword)
	private String emissor; // CNPJ ou CPF do emissor do documento fiscal

	@MultiField(mainField = @Field(type = FieldType.Text, analyzer = ANALYZER),
			otherFields = { @InnerField(suffix = "sort", type = FieldType.Keyword) })
	private String filialNomeApresentacao;

	@Field(type = FieldType.Keyword)
	private String filialInscricaoMunicipal;

	@Field(type = FieldType.Boolean)
	private Boolean filialHabilitada;

	@Field(type = FieldType.Long)
	private Long idMunicipio;

	@MultiField(mainField = @Field(type = FieldType.Text, analyzer = ANALYZER),
			otherFields = { @InnerField(suffix = "sort", type = FieldType.Keyword) })
	private String nomeMunicipio;

	@Field(type = FieldType.Keyword)
	private String siglaUF;

	@Field(type = FieldType.Keyword)
	private String codigoIBGE;

	@MultiField(mainField = @Field(type = FieldType.Text, analyzer = ANALYZER),
			otherFields = { @InnerField(suffix = "sort", type = FieldType.Keyword) })
	private String nomeMunicipioPesquisa;

	@Field(type = FieldType.Keyword)
	private String cnpjBase;

	@Field(type = FieldType.Boolean)
	private Boolean habilitada;

	@Field(type = FieldType.Long)
	private Long idConta;

	@MultiField(mainField = @Field(type = FieldType.Text, analyzer = ANALYZER),
			otherFields = { @InnerField(suffix = "sort", type = FieldType.Keyword) })
	private String nomeConta;

	@MultiField(mainField = @Field(type = FieldType.Text, analyzer = ANALYZER),
			otherFields = { @InnerField(suffix = "keyword", type = FieldType.Keyword),
							@InnerField(suffix = "sort", type = FieldType.Keyword) })
	private Set<String> recursosAtivos;

	@Field(type = FieldType.Date, store = true, format = DateFormat.date_hour_minute_second_millis)
	private LocalDateTime validadeCertificado;

	/* Dados da requisição */
	@Field(type = FieldType.Keyword)
	private String webhookurl;

	@Field(type = FieldType.Keyword)
	private String integrationId;

	@Field(type = FieldType.Keyword)
	private String uuid;

	@Field(type = FieldType.Date, store = true, format = DateFormat.date_hour_minute_second_millis)
	private LocalDateTime requestReceivedDateTime;

	@Field(type = FieldType.Keyword)
	private String filename;

	@Field(type = FieldType.Keyword)
	private String contentType;

	@Field(type = FieldType.Keyword)
	private String integracao;

	@Field(type = FieldType.Keyword)
	private String modelo;

	@Field(type = FieldType.Keyword)
	private String documento;

	@Field(type = FieldType.Keyword)
	private String storageBucket;

	@Field(type = FieldType.Keyword)
	private String storageKey;

	@Field(type = FieldType.Date, store = true, format = DateFormat.date_hour_minute_second_millis)
	private LocalDateTime dataArmazenamento;

	@Field(type = FieldType.Keyword)
	private TipoRequisicao tipoRequisicao;


	/* Em caso de erro */
	@Field(type = FieldType.Boolean)
	private Boolean indicadorErro;

	@MultiField(mainField = @Field(type = FieldType.Text, analyzer = ANALYZER),
			otherFields = { @InnerField(suffix = "sort", type = FieldType.Keyword) })
	private String mensagemDeErro;
	
//	@Field(type = FieldType.Object, index = false)
	private Map<String, Object> payload;
	
}
