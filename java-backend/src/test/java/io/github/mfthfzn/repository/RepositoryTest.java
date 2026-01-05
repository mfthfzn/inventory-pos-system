package io.github.mfthfzn.repository;

import io.github.mfthfzn.util.JpaUtilTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class RepositoryTest {

  protected static EntityManagerFactory entityManagerFactory;
  protected static EntityManager entityManager;
  protected static EntityTransaction transaction;

  @BeforeAll
  static void beforeAll() {
    entityManagerFactory = JpaUtilTest.getEntityManagerFactory();
    entityManager = entityManagerFactory.createEntityManager();
    transaction = entityManager.getTransaction();
  }

  @AfterAll
  static void afterAll() {
    entityManager.close();
    entityManagerFactory.close();
  }

  public void truncateAllTable(String... tables) {
    try {
      transaction.begin();
      entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();

      for (String table : tables) {
        String sql = "TRUNCATE TABLE " + table;
        entityManager.createNativeQuery(sql).executeUpdate();
      }

      entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();

      transaction.commit();
    } catch (Exception e) {
      entityManager.getTransaction().rollback();
      throw e;
    }
  }
}
