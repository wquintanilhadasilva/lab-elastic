package com.lab.elastic.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories("com.lab.elastic.repository.*")
public class ElasticsearchConfig {
	
	@Value("${spring.data.elasticsearch.host}")
	private String host;
	
	@Value("${spring.data.elasticsearch.port}")
	private int port;
	
	@Value("${spring.data.elasticsearch.username}")
	private String userName;
	
	@Value("${spring.data.elasticsearch.password}")
	private String password;
	
	@Bean
	public RestClient restClientBuilder() {
		final CredentialsProvider credentialsProvider =
				new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY,
				new UsernamePasswordCredentials(userName, password));
		
		RestClientBuilder builder = RestClient.builder(
						new HttpHost(host, port))
				.setDefaultHeaders(compatibilityHeaders())
				.setHttpClientConfigCallback(
					httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
						.addInterceptorLast((HttpResponseInterceptor) (response, context) ->
											response.addHeader("X-Elastic-Product", "Elasticsearch"))
				);
				
		return builder.build();
	}
	
	@Bean
	public ElasticsearchClient elasticsearchClient(RestClient restClient, ObjectMapper objectMapper) {
		
		ElasticsearchTransport transport = new RestClientTransport(
				restClient, new JacksonJsonpMapper(objectMapper));
		return new ElasticsearchClient(transport);
	}
	
	private Header[] compatibilityHeaders() {
		return new Header[]{new BasicHeader(HttpHeaders.ACCEPT, "application/vnd.elasticsearch+json;compatible-with=7"),
			new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.elasticsearch+json;compatible-with=7")};
	}
	
}
	
