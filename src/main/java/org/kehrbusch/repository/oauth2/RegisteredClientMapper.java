package org.kehrbusch.repository.oauth2;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class RegisteredClientMapper {
    private static final String tokenDurationIdentifier = "settings.token.access-token-time-to-live";

    public RegisteredClient map(OAuth2RegisteredClient oAuth2RegisteredClient){
            RegisteredClient.Builder builder = RegisteredClient.withId(oAuth2RegisteredClient.getId())
                .clientId(oAuth2RegisteredClient.getClientId())
                .clientSecret(oAuth2RegisteredClient.getClientSecret())
                .redirectUri(oAuth2RegisteredClient.getRedirectUris());

        for (String scope : oAuth2RegisteredClient.getScopes()){
            builder.scope(scope);
        }

        for (String method : oAuth2RegisteredClient.getClientAuthenticationMethods()) {
            builder.clientAuthenticationMethod(new ClientAuthenticationMethod(method.trim()));
        }

        for (String grantType : oAuth2RegisteredClient.getAuthorizationGrantTypes()) {
            builder.authorizationGrantType(new AuthorizationGrantType(grantType.trim()));
        }

        long seconds = oAuth2RegisteredClient.getTokenDuration();
        Duration duration = Duration.ofSeconds(seconds);
        ClientSettings clientSettings = ClientSettings.builder()
                .settings(settingsMap -> settingsMap.put(tokenDurationIdentifier, duration))
                .build();
        builder.clientSettings(clientSettings);

        return builder.build();
    }

    public OAuth2RegisteredClient map(RegisteredClient registeredClient){
        OAuth2RegisteredClient oAuth2RegisteredClient = new OAuth2RegisteredClient();
        oAuth2RegisteredClient.setId(registeredClient.getId());
        oAuth2RegisteredClient.setClientId(registeredClient.getClientId());
        oAuth2RegisteredClient.setClientSecret(registeredClient.getClientSecret());
        oAuth2RegisteredClient.setRedirectUris(registeredClient.getRedirectUris().iterator().next());
        oAuth2RegisteredClient.setClientName(registeredClient.getClientName());

        Instant instant = registeredClient.getClientIdIssuedAt();
        if (instant != null){
            oAuth2RegisteredClient.setClientIdIssued(LocalDateTime.from(instant));
        }

        List<String> scopes = new ArrayList<>(registeredClient.getScopes());
        oAuth2RegisteredClient.setScopes(scopes);

        List<String> clientAuthenticationMethods = new ArrayList<>();
        registeredClient.getClientAuthenticationMethods().forEach(a -> clientAuthenticationMethods.add(a.getValue()));
        oAuth2RegisteredClient.setClientAuthenticationMethods(clientAuthenticationMethods);

        List<String> authorizationGrantTypes = new ArrayList<>();
        registeredClient.getAuthorizationGrantTypes().forEach(a -> authorizationGrantTypes.add(a.getValue()));
        oAuth2RegisteredClient.setAuthorizationGrantTypes(authorizationGrantTypes);

        Duration duration = registeredClient.getClientSettings().getSetting(tokenDurationIdentifier);
        oAuth2RegisteredClient.setTokenDuration(duration.getSeconds());

        return oAuth2RegisteredClient;
    }
}
