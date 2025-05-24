package org.kehrbusch.security.vault;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Configuration
@Profile("prod")
public class KeyVaultProdConfig {
    @Value("${keyvault.wms.path}")
    private String keyvaultWmsPath;
    @Value("${keyvault.url}")
    private String keyvaultUrl;
    @Value("${odoo.db.driver}")
    private String odooDbDriver;

    @Value("${odoo.db.user.identifier}")
    private String odooDbUserIdentifier;
    @Value("${odoo.db.pw.identifier}")
    private String odooDbPwIdentifier;
    @Value("${odoo.db.name.identifier}")
    private String odooDbNameIdentifier;
    @Value("${odoo.db.url.identifier}")
    private String odooDbUrlIdentifier;
    @Value("${odoo.api.url.identifier}")
    private String odooApiUrlIdentifier;
    @Value("${odoo.api.user.identifier}")
    private String odooApiUserIdentifier;
    @Value("${odoo.api.pw.identifier}")
    private String odooApiPwIdentifier;

    @Bean("wmsKeys")
    public Map<String, String> wmsKeys(VaultAuthInterceptor vaultAuthInterceptor) throws IOException {
        return vaultAuthInterceptor.getKeys(keyvaultWmsPath);
    }

    @Bean
    public KeyVaultService keyVaultService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(keyvaultUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(KeyVaultService.class);
    }

    @Bean("odooDbUsername")
    public String odooDbUsername(@Qualifier("wmsKeys") Map<String, String> wmsKeys){
        return wmsKeys.get(odooDbUserIdentifier);
    }

    @Bean("odooDbPassword")
    public String odooDbPassword(@Qualifier("wmsKeys") Map<String, String> wmsKeys){
        return wmsKeys.get(odooDbPwIdentifier);
    }

    @Bean("odooDbName")
    public String odooDbName(@Qualifier("wmsKeys") Map<String, String> wmsKeys){
        return wmsKeys.get(odooDbNameIdentifier);
    }

    @Bean("odooDbUrl")
    public String odooDbUrl(@Qualifier("wmsKeys") Map<String, String> wmsKeys){
        return wmsKeys.get(odooDbUrlIdentifier);
    }

    @Bean("odooApi")
    public String odooApi(@Qualifier("wmsKeys") Map<String, String> wmsKeys){
        return wmsKeys.get(odooApiUrlIdentifier);
    }

    @Bean("odooApiUser")
    public String odooApiUser(@Qualifier("wmsKeys") Map<String, String> wmsKeys){
        return wmsKeys.get(odooApiUserIdentifier);
    }

    @Bean("odooApiPw")
    public String odooApiPw(@Qualifier("wmsKeys") Map<String, String> wmsKeys){
        return wmsKeys.get(odooApiPwIdentifier);
    }

    @Bean("sealPassword")
    public String sealPassword(ApplicationArguments args) throws IllegalArgumentException {
        if (args.containsOption("seal.password")) {
            return args.getOptionValues("seal.password").get(0);
        } else {
            throw new IllegalArgumentException("Missing seal password to access vault.");
        }
    }

    @Bean("rootToken")
    public String rootToken(ApplicationArguments args) throws IllegalArgumentException {
        if (args.containsOption("root.token")) {
            return args.getOptionValues("root.token").get(0);
        } else {
            throw new IllegalArgumentException("Missing root token to access vault.");
        }
    }

    @Bean
    public DataSource dataSource(@Qualifier("odooDbUrl") String odooDbUrl, @Qualifier("odooDbUsername") String odooDbUsername,
                                 @Qualifier("odooDbPassword") String odooDbPassword) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(odooDbDriver);
        dataSource.setUrl(odooDbUrl);
        dataSource.setUsername(odooDbUsername);
        dataSource.setPassword(odooDbPassword);
        return dataSource;
    }
}
