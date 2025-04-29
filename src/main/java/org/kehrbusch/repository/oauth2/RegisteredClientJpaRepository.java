package org.kehrbusch.repository.oauth2;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisteredClientJpaRepository extends CrudRepository<OAuth2RegisteredClient, String> {
    Optional<OAuth2RegisteredClient> findFirstById(String id);
    Optional<OAuth2RegisteredClient> findFirstByClientId(String clientId);
}
