package com.teosgame.ban.banapi.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.teosgame.ban.banapi.model.DBSecret;
import com.teosgame.ban.banapi.service.SecretsManagerService;

@Profile("!dev")
@Configuration
public class AWSDataSourceConfig {
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.dbName}")
    private String dbName;

    @Autowired
    private SecretsManagerService service;

    @Bean
    public DataSource dataSource() {

        DBSecret secret = service.getDbSecret();

        return DataSourceBuilder
            .create()
            .username(secret.getUsername())
            .password(secret.getPassword())
            .url("jdbc:" + secret.getEngine() + "://" + secret.getHost() + "/" + dbName)
            .driverClassName(driverClassName)
            .build();
    }
}
