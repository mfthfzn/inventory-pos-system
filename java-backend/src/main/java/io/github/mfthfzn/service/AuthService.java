package io.github.mfthfzn.service;

import io.github.mfthfzn.dto.*;

public interface AuthService {

  LoginResponse authenticate(LoginRequest loginRequest);

  ForgotPasswordResponse processForgotPassword(ForgotPasswordRequest forgotPasswordRequest);

  ResetPasswordResponse validateResetPasswordToken(String token);

}
