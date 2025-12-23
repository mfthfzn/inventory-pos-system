package io.github.mfthfzn.repository;

import io.github.mfthfzn.entity.TokenSession;
import io.github.mfthfzn.entity.User;

import java.time.LocalDateTime;

public interface TokenSessionRepository {

  boolean setTokenSession(User user, String token, LocalDateTime expiredAt);

  TokenSession findTokenByEmail(String email);

}
