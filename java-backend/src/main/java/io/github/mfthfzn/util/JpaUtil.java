package io.github.mfthfzn.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


public class JpaUtil {

  private static EntityManagerFactory entityManagerFactory;

  public static EntityManagerFactory getEntityManagerFactory() {
    if (entityManagerFactory == null) {
      entityManagerFactory = Persistence.createEntityManagerFactory("MANAJEMEN-INVENTARIS");
    }
    return entityManagerFactory;
  }

}
