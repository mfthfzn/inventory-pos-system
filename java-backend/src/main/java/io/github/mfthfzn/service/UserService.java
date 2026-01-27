package io.github.mfthfzn.service;

import io.github.mfthfzn.entity.User;

public interface UserService {

  User getUser(String email);

}
