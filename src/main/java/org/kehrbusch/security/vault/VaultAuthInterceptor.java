package org.kehrbusch.security.vault;

import org.kehrbusch.security.vault.entities.KeyRequest;
import org.kehrbusch.security.vault.entities.KeyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.util.Map;

@Service
@Profile("prod")
public class VaultAuthInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(VaultAuthInterceptor.class);

    private final String sealPassword;
    private final String rootToken;
    private final KeyVaultService keyVaultService;

    @Autowired
    public VaultAuthInterceptor(@Qualifier("sealPassword") String sealPassword, @Qualifier("rootToken") String rootToken, KeyVaultService keyVaultService){
        this.sealPassword = sealPassword;
        this.rootToken = rootToken;
        this.keyVaultService = keyVaultService;
    }

    public Map<String, String> getKeys(String path) throws IOException {
        Response<KeyResponse> keyResponse1 = this.keyVaultService.getKeys(rootToken, path).execute();
        if (keyResponse1.code() == 503){
            logger.info("Unseal keyvault.");
            KeyRequest keyRequest = new KeyRequest(sealPassword);
            this.keyVaultService.authorize(keyRequest).execute();
            logger.info("Unsealed keyvault.");
            this.sleep();

            Response<KeyResponse> keyResponse2 = this.keyVaultService.getKeys(rootToken, path).execute();
            logger.info("Successful keyvault response.");
            return parseKeyVaultResponse(keyResponse2);
        } else {
            logger.info("Successful keyvault response.");
            return parseKeyVaultResponse(keyResponse1);
        }
    }

    private Map<String, String> parseKeyVaultResponse(Response<KeyResponse> keyResponse) throws IOException {
        if (keyResponse.code() == 200 && keyResponse.body() != null){
            return keyResponse.body().getData();
        }
        if (keyResponse.code() != 200 && keyResponse.errorBody() != null){
            String error = keyResponse.errorBody().string();
            throw new IOException("Error during keyvault request: " + error);
        }

        throw new IOException("Error during keyvault request: " + keyResponse.code());
    }

    private void sleep() throws IOException {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e){
            throw new IOException();
        }
    }
}
