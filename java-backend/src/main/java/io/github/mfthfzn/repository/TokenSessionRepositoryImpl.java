package io.github.mfthfzn.repository;

import io.github.mfthfzn.entity.TokenSession;
import io.github.mfthfzn.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
public class TokenSessionRepositoryImpl implements TokenSessionRepository{

  private EntityManagerFactory entityManagerFactory;

  public TokenSessionRepositoryImpl(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  public boolean setTokenSession(User user, String token, LocalDateTime expiredAt) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    try (entityManager) {
      EntityTransaction transaction = entityManager.getTransaction();
      transaction.begin();

      User userReference = entityManager.getReference(User.class, user.getEmail());

      TokenSession tokenSession = new TokenSession();
      tokenSession.setUser(userReference);
      tokenSession.setToken(token);
      tokenSession.setExpiredAt(expiredAt);

      entityManager.persist(tokenSession);

      transaction.commit();

      return true;
    } catch (Exception exception) {
      log.error("e: ", exception);
      return false;
    }
  }

  @Override
  public TokenSession findTokenByEmail(String email) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    try (entityManager) {
      EntityTransaction transaction = entityManager.getTransaction();
      transaction.begin();

      Optional<TokenSession> tokenSession = Optional.ofNullable(entityManager.find(TokenSession.class, email));

      transaction.commit();

      return tokenSession.orElse(null);
    } catch (Exception exception) {
      return null;
    }
  }
}
