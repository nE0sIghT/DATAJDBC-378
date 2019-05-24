package org.github.ne0sight;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableAutoConfiguration(exclude = org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration.class)
@EnableJdbcRepositories("org.github.ne0sight")
public class Application extends AbstractJdbcConfiguration
{
	@Autowired
	private ThingsRepository thingsRepository;

	public static void main(String[] args)
	{
		SpringApplication.run(Application.class);
	}

	@PostConstruct
	@Transactional
	public void test()
	{
		thingsRepository.findAllById(new ArrayList<Integer>());
	}

	@Bean
	NamedParameterJdbcOperations operations()
	{
		return new NamedParameterJdbcTemplate(dataSource());
	}

	@Bean
	PlatformTransactionManager transactionManager()
	{
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean
	DataSource dataSource()
	{
		return new EmbeddedDatabaseBuilder()
				.generateUniqueName(true)
				.setType(EmbeddedDatabaseType.HSQL)
				.addScript("classpath:schema.sql")
				.build();
	}
}
