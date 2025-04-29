package org.kehrbusch.repository.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class RegisteredClientRepositoryImpl implements RegisteredClientRepository {
    private final RegisteredClientJpaRepository registeredClientJpaRepository;
    private final RegisteredClientMapper registeredClientMapper;

    @Autowired
    public RegisteredClientRepositoryImpl(RegisteredClientJpaRepository registeredClientJpaRepository, RegisteredClientMapper registeredClientMapper) {
        this.registeredClientJpaRepository = registeredClientJpaRepository;
        this.registeredClientMapper = registeredClientMapper;
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        OAuth2RegisteredClient oAuth2RegisteredClient = this.registeredClientMapper.map(registeredClient);
        this.registeredClientJpaRepository.save(oAuth2RegisteredClient);
    }

    @Transactional
    @Override
    public RegisteredClient findById(String id) {
        Optional<OAuth2RegisteredClient> oAuth2RegisteredClient = this.registeredClientJpaRepository.findFirstById(id);
        return oAuth2RegisteredClient.map(this.registeredClientMapper::map).orElse(null);
    }

    @Transactional
    @Override
    public RegisteredClient findByClientId(String clientId) {
        Optional<OAuth2RegisteredClient> oAuth2RegisteredClient = this.registeredClientJpaRepository.findFirstByClientId(clientId);
        return oAuth2RegisteredClient.map(this.registeredClientMapper::map).orElse(null);
    }
}
