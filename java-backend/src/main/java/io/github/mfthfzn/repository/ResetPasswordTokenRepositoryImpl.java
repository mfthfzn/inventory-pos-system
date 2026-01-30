package io.github.mfthfzn.repository;

import io.github.mfthfzn.entity.RefreshToken;
import io.github.mfthfzn.entity.ResetPasswordToken;
import io.github.mfthfzn.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class ResetPasswordTokenRepositoryImpl implements ResetPasswordTokenRepository {

  private final EntityManagerFactory entityManagerFactory;

  public ResetPasswordTokenRepositoryImpl(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  public void insert(ResetPasswordToken resetPasswordToken) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {
      transaction.begin();

      User userReference = entityManager.getReference(User.class, resetPasswordToken.getUser().getEmail());
      resetPasswordToken.setUser(userReference);

      entityManager.persist(resetPasswordToken);

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
  public Optional<ResetPasswordToken> findByEmail(String email) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {

      transaction.begin();
      ResetPasswordToken resetPasswordToken = entityManager.find(ResetPasswordToken.class, email);
      transaction.commit();

      return Optional.ofNullable(resetPasswordToken);
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
      entityManager.createQuery("DELETE FROM ResetPasswordToken t WHERE t.email = :email")
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
