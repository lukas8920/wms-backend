package org.kehrbusch.repository.oauth2;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "oauth2_registered_client")
public class OAuth2RegisteredClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(name = "client_id")
    private String clientId;
    @Column(name = "client_id_issued_at")
    private LocalDateTime clientIdIssued;
    @Column(name = "client_secret")
    private String clientSecret;
    @ElementCollection()
    @CollectionTable(
            name = "oauth2_client_authentication_methods",
            joinColumns = @JoinColumn(name = "oauth2_registered_client_id")
    )
    @Column(name = "client_authentication_method")
    private List<String> clientAuthenticationMethods;
    @ElementCollection()
    @CollectionTable(
            name = "oauth2_authorization_grant_types",
            joinColumns = @JoinColumn(name = "oauth2_registered_client_id")
    )
    @Column(name = "authorization_grant_type")
    private List<String> authorizationGrantTypes;
    @Column(name = "redirect_uris")
    private String redirectUris;
    @ElementCollection()
    @CollectionTable(
            name = "oauth2_scopes",
            joinColumns = @JoinColumn(name = "oauth2_registered_client_id")
    )
    @Column(name = "scope")
    private List<String> scopes;
    @Column(name = "token_duration")
    private Long tokenDuration;
    @Column(name = "client_name")
    private String clientName;
}
