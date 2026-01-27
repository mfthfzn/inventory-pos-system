package io.github.mfthfzn.repository;

import io.github.mfthfzn.entity.RefreshToken;

import java.util.Optional;

public interface TokenRepository {

  void insert(RefreshToken refreshTokenSession);

  Optional<RefreshToken> findByEmail(String email);

  void removeByEmail(String email);

}
