package com.pegien.WellTalk.WellTalk.Auth.AuthToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token,UUID> {

    Optional<Token> findByValue(String value);

    List<Token> findByProfileId(UUID uid);
}
