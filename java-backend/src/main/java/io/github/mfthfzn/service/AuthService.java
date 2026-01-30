package io.github.mfthfzn.service;

import io.github.mfthfzn.dto.ForgotPasswordRequest;
import io.github.mfthfzn.dto.ForgotPasswordResponse;
import io.github.mfthfzn.dto.LoginRequest;
import io.github.mfthfzn.dto.LoginResponse;

public interface AuthService {

  LoginResponse authenticate(LoginRequest loginRequest);

  ForgotPasswordResponse processForgotPassword(ForgotPasswordRequest forgotPasswordRequest);

}
