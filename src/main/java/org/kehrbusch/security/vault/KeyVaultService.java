package org.kehrbusch.security.vault;

import org.kehrbusch.security.vault.entities.KeyRequest;
import org.kehrbusch.security.vault.entities.KeyResponse;
import org.kehrbusch.security.vault.entities.UnsealResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface KeyVaultService {
    @PUT("/v1/sys/unseal")
    Call<UnsealResponse> authorize(
            @Body KeyRequest keyRequest
    );

    @GET("/v1/cubbyhole/{keyvault}")
    Call<KeyResponse> getKeys(
            @Header("X-Vault-Token") String rootToken,
            @Path("keyvault") String keyvaultPath
    );
}
