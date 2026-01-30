package io.github.mfthfzn.repository;

import io.github.mfthfzn.entity.RefreshToken;
import io.github.mfthfzn.entity.User;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

  private final EntityManagerFactory entityManagerFactory;

  public RefreshTokenRepositoryImpl(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  public void insert(RefreshToken refreshToken) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {
      transaction.begin();

      User userReference = entityManager.getReference(User.class, refreshToken.getUser().getEmail());
      refreshToken.setUser(userReference);

      entityManager.persist(refreshToken);

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
  public Optional<RefreshToken> findByEmail(String email) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {

      transaction.begin();
      RefreshToken refreshToken = entityManager.find(RefreshToken.class, email);
      transaction.commit();

      return Optional.ofNullable(refreshToken);
    } catch (Exception exception) {
      if (transaction.isActive()) transaction.rollback();
      log.error(exception.getMessage());
      throw new PersistenceException(exception);
    } finally {
      entityManager.close();
    }
  }

  @Override
  public void removeByEmail(String email) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {

      transaction.begin();
      entityManager.createQuery("DELETE FROM RefreshToken t WHERE t.email = :email")
              .setParameter("email", email)
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
