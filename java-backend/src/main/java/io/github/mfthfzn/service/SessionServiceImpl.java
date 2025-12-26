package io.github.mfthfzn.service;

import io.github.mfthfzn.dto.SessionRequest;
import io.github.mfthfzn.dto.SessionResponse;
import io.github.mfthfzn.entity.TokenSession;
import io.github.mfthfzn.entity.User;
import io.github.mfthfzn.repository.TokenSessionRepositoryImpl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
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

  @Override
  public SessionResponse checkSession(SessionRequest sessionRequest) {
    Optional<TokenSession> session = Optional.ofNullable(tokenSessionRepository.findTokenByEmail(sessionRequest.getEmail()));
    SessionResponse sessionResponse = new SessionResponse();
    if (session.isPresent()) {
      TokenSession tokenSession = session.get();
      if (sessionRequest.getToken().equals(tokenSession.getToken())) {
        LocalDateTime expiredAt = tokenSession.getExpiredAt();

        sessionResponse.setExpired(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).isAfter(expiredAt));
      } else {
        sessionResponse.setExpired(true);
      }
    } else {
      sessionResponse.setExpired(true);
    }
    return sessionResponse;
  }

  @Override
  public SessionResponse removeSession(SessionRequest sessionRequest) {

    Optional<TokenSession> tokenSession = Optional.ofNullable(tokenSessionRepository.findTokenByEmail(sessionRequest.getEmail()));
    SessionResponse sessionResponse = new SessionResponse();
    if (tokenSession.isEmpty()) {
      sessionResponse.setRemoved(false);
    } else {
      boolean removeSession = tokenSessionRepository.removeSession(tokenSession.get());
      sessionResponse.setRemoved(removeSession);
    }

    return sessionResponse;
  }

}
