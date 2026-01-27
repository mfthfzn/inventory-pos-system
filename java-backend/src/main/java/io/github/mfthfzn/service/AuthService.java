package io.github.mfthfzn.service;

import io.github.mfthfzn.dto.LoginRequest;
import io.github.mfthfzn.dto.LoginResponse;

public interface AuthService {

  LoginResponse authenticate(LoginRequest loginRequest);

  void changePassword(String email, String newPassword);

}
