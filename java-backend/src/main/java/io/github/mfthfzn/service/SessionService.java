package io.github.mfthfzn.service;

import io.github.mfthfzn.dto.LoginRequest;
import io.github.mfthfzn.entity.User;

public interface SessionService {

  String generateToken(User user);

}
