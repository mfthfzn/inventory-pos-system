package io.github.mfthfzn.repository;

import io.github.mfthfzn.entity.Store;
import io.github.mfthfzn.util.JpaUtil;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class StoreRepositoryImpl implements StoreRepository {

  private EntityManagerFactory entityManagerFactory;

  public StoreRepositoryImpl(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  public void saveStore(Store store) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {

      transaction.begin();

      entityManager.persist(store);

      transaction.commit();

    } catch (Exception exception) {
      if (transaction.isActive()) transaction.rollback();
      throw new PersistenceException(exception);
    } finally {
      entityManager.close();
    }
  }

  @Override
  public Optional<Store> findStoreByName(String name) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {

      transaction.begin();
      TypedQuery<Store> store = entityManager.createQuery("SELECT s FROM Store s WHERE s.name = :name", Store.class)
              .setParameter("name", name);
      transaction.commit();
      Store result = store.getSingleResult();

      return Optional.ofNullable(result);
    } catch (Exception exception) {
      if (transaction.isActive()) transaction.rollback();
      throw new PersistenceException(exception);
    } finally {
      entityManager.close();
    }
  }

  @Override
  public void removeStore(Store store) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {

      transaction.begin();
      Store reference = entityManager.getReference(Store.class, store.getId());
      entityManager.remove(reference);
      transaction.commit();

    } catch (Exception exception) {
      if (transaction.isActive()) transaction.rollback();
      throw new PersistenceException(exception);
    } finally {
      entityManager.close();
    }
  }
}
