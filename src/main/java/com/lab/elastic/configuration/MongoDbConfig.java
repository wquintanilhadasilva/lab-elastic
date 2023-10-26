package com.lab.elastic.configuration;

import static java.util.Collections.singletonList;

import com.mongodb.AuthenticationMechanism;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Habilita e configura o suporte ao MondoDB nesse microsserviço
 * @author Wedson Silva
 * @see <a href="https://dev.azure.com/oobj-devops/Engineering/_workitems/edit/1973">Azzure #1973</a>
 * @since 24/08/2023
 */
@Configuration
@EnableMongoRepositories("br.com.oobj.ms.dfe.relatorios.repository.mongo")
public class MongoDbConfig extends AbstractMongoClientConfiguration {
	
	@Value("${spring.data.mongodb.database}")
	private String databaseName;
	@Value("${spring.data.mongodb.authentication-database}")
	private String authenticationDatabase;
	@Value("${spring.data.mongodb.host}")
	private String host;
	@Value("${spring.data.mongodb.port}")
	private Integer port;
	@Value("${spring.data.mongodb.username}")
	private String username;
	@Value("${spring.data.mongodb.password}")
	private String password;
	@Value("${spring.data.mongodb.authentication-mechanism}")
	private String mechanism;
	
	@Override
	protected String getDatabaseName() {
		return databaseName;
	}
	
	@Override
	public boolean autoIndexCreation() {
		return true;
	}
	
	/**
	 * Ajusta a conexão com o MongoDB
	 * @see <a href="https://docs.spring.io/spring-data/mongodb/reference/4.2/mongodb/configuration.html">spring-data#mongodb</a>
	 * @param builder builder para criar a conexão.
	 */
	@Override
	protected void configureClientSettings(MongoClientSettings.Builder builder) {
		
		var credencial = MongoCredential.createCredential(username, authenticationDatabase, password.toCharArray());
		credencial = credencial.withMechanism(AuthenticationMechanism.fromMechanismName(mechanism));
		builder.credential(credencial)
			.applyToClusterSettings(settings  -> {
				settings.hosts(singletonList(new ServerAddress(host, port)));
			});
	}
	
}
