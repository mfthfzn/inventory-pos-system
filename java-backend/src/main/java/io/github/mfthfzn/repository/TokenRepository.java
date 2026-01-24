package io.github.mfthfzn.repository;

import io.github.mfthfzn.entity.Token;

import java.util.Optional;

public interface TokenRepository {

  void insert(Token tokenSession);

  Optional<Token> findByEmail(String email);

  void removeByEmail(String email);

}
