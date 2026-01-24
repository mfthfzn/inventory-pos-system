package io.github.mfthfzn.repository;

import io.github.mfthfzn.entity.User;

import java.util.Optional;

public interface UserRepository {

  void insert(User user);

  Optional<User> findByEmail(String email);

  void update(User user);

}
