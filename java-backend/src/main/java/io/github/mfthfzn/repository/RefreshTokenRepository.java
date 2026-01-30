package io.github.mfthfzn.repository;

import io.github.mfthfzn.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {

  void insert(RefreshToken refreshTokenSession);

  Optional<RefreshToken> findByEmail(String email);

  void removeByEmail(String email);

}
