package com.zuzex.booker.repository;

import com.zuzex.booker.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByLogin(String login);

    Optional<RefreshToken> findByToken(String token);
}
