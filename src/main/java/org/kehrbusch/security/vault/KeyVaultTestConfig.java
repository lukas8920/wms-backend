package org.kehrbusch.security.vault;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@Profile("test")
public class KeyVaultTestConfig {
    @Value("${odoo.db.driver}")
    private String odooDbDriver;
    @Value("${odoo.db.url}")
    private String odooDbUrl;
    @Value("${odoo.db.username}")
    private String odooDbUsername;
    @Value("${odoo.db.password}")
    private String odooDbPassword;

    @Value("${odoo.db.name}")
    private String odooDbName;
    @Value("${odoo.api.url}")
    private String odooApi;
    @Value("${odoo.api.user}")
    private String odooApiUser;
    @Value("${odoo.api.password}")
    private String odooApiPw;

    @Bean("odooDbName")
    public String odooDbName(){
        return this.odooDbName;
    }

    @Bean("odooApi")
    public String odooApi(){
        return this.odooApi;
    }

    @Bean("odooApiUser")
    public String odooApiUser(){
        return this.odooApiUser;
    }

    @Bean("odooApiPw")
    public String odooApiPw(){
        return this.odooApiPw;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(odooDbDriver);
        dataSource.setUrl(odooDbUrl);
        dataSource.setUsername(odooDbUsername);
        dataSource.setPassword(odooDbPassword);
        return dataSource;
    }
}
