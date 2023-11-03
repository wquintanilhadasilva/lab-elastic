package com.lab.elastic.dto;

import com.lab.elastic.repository.entidades.RequestType;
import com.lab.elastic.repository.entidades.TipoRequisicao;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.LocalDateTime;

/**
 * DTO para a criação de uma requisição no sistema
 *
 * @author Wedson Silva
 * @see <a href="https://dev.azure.com/oobj-devops/Engineering/_workitems/edit/1973">Azzure #1973</a>
 * @since 30/08/2023
 */
@Builder
@Data
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CreateRequestDto {
	
	@EqualsAndHashCode.Include
	private String requestId;
	
	/* Dados da empresa */
	private Long idEmpresa;
	private String razaoSocial;
	private String nomeFantasia;
	private String nomeExibicao;
	private String nomeFantasiaNormalizado;
	private String razaoSocialNormalizado;
	private String nomeExibicaoNormalizado;
	private Long filialId;
	private String cnpj;
	private String filialNomeApresentacao;
	private String filialInscricaoMunicipal;
	private Boolean filialHabilitada;
	private Long idMunicipio;
	private String nomeMunicipio;
	private String siglaUF;
	private String codigoIBGE;
	private String nomeMunicipioPesquisa;
	private String cnpjBase;
	private boolean habilitada;
	private Long idConta;
	private String nomeConta;
	private LocalDateTime validadeCertificado;
	
	/* Dados da requisição */
	private String webhookurl;
	private String integrationId;
	private String uuid;
	private LocalDateTime requestReceivedDateTime;
	
	private String filename;
	private String contentType;
	// IntegracaoType
	private String integracao;
	// ModeloType
	private String modelo;
	//DocumentoType
	private String documento;
	
	private RequestType requestType;
	
	private String storageBucket;
	private String storageKey;
	
	private TipoRequisicao tipoRequisicao;
	
	/* Em caso de erro */
	private Boolean indicadorErro;
	private String mensagemDeErro;
	
	private String payload;

}
