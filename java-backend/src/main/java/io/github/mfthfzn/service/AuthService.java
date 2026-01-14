package io.github.mfthfzn.service;

import io.github.mfthfzn.dto.AuthRequest;
import io.github.mfthfzn.dto.AuthResponse;

public interface AuthService {

  AuthResponse authenticate(AuthRequest authRequest);

}
