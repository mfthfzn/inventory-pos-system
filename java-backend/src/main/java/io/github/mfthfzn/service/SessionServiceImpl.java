package io.github.mfthfzn.service;

import io.github.mfthfzn.entity.User;
import io.github.mfthfzn.repository.TokenSessionRepositoryImpl;

import java.time.LocalDateTime;
import java.util.UUID;

public class SessionServiceImpl implements SessionService {

  private final TokenSessionRepositoryImpl tokenSessionRepository;

  public SessionServiceImpl(TokenSessionRepositoryImpl tokenSessionRepository) {
    this.tokenSessionRepository = tokenSessionRepository;
  }

  @Override
  public String generateToken(User user) {
    String token = UUID.randomUUID().toString();
    LocalDateTime expiredAt = LocalDateTime.now().plusDays(1);
    boolean resultSetToken = tokenSessionRepository.setTokenSession(user, token, expiredAt);
    if (resultSetToken) {
      return token;
    } else {
      return null;
    }
  }

}
