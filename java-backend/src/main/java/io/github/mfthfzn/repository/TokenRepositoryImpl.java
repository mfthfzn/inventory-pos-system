package io.github.mfthfzn.repository;

import io.github.mfthfzn.entity.Token;
import io.github.mfthfzn.entity.User;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class TokenRepositoryImpl implements TokenRepository {

  private final EntityManagerFactory entityManagerFactory;

  public TokenRepositoryImpl(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  public void saveToken(Token token) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {
      transaction.begin();

      User userReference = entityManager.getReference(User.class, token.getUser().getEmail());
      token.setUser(userReference);

      entityManager.persist(token);

      transaction.commit();

    } catch (Exception exception) {
      if (transaction.isActive()) transaction.rollback();
      log.error(exception.getMessage());
      throw new PersistenceException(exception);
    } finally {
      entityManager.close();
    }
  }

  @Override
  public Optional<Token> findRefreshToken(String email) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {
      transaction.begin();

      TypedQuery<Token> resultRefreshToken = entityManager.createQuery("SELECT t FROM Token t WHERE t.email = :email", Token.class)
              .setParameter("email", email);

      Token result = resultRefreshToken.getSingleResult();

      transaction.commit();
      return Optional.ofNullable(result);
    } catch (Exception exception) {
      if (transaction.isActive()) transaction.rollback();
      log.error(exception.getMessage());
      throw new PersistenceException(exception);
    } finally {
      entityManager.close();
    }
  }

  @Override
  public void removeToken(Token token) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {

      transaction.begin();
      entityManager.createQuery("DELETE FROM Token t WHERE t.email = :email")
              .setParameter("email", token.getEmail())
              .executeUpdate();
      transaction.commit();

    } catch (Exception exception) {
      if (transaction.isActive()) transaction.rollback();
      log.error(exception.getMessage());
      throw new PersistenceException(exception);
    } finally {
      entityManager.close();
    }
  }
}
