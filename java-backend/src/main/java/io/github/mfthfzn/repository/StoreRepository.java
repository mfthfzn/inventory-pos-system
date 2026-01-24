package io.github.mfthfzn.repository;

import io.github.mfthfzn.entity.Store;

import java.util.Optional;

public interface StoreRepository {

  void insert(Store store);

  Optional<Store> findByName(String name);

  void remove(Store store);
}
