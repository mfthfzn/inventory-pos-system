package io.github.mfthfzn.service;

import io.github.mfthfzn.dto.ForgotPasswordRequest;
import io.github.mfthfzn.dto.ForgotPasswordResponse;
import io.github.mfthfzn.dto.LoginRequest;
import io.github.mfthfzn.dto.LoginResponse;
import io.github.mfthfzn.entity.ResetPasswordToken;
import io.github.mfthfzn.entity.User;
import io.github.mfthfzn.exception.AuthenticateException;
import io.github.mfthfzn.exception.UserNotFoundException;
import io.github.mfthfzn.repository.ResetPasswordTokenRepository;
import io.github.mfthfzn.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
public class AuthServiceImpl implements AuthService {

  private UserRepository userRepository;

  private ResetPasswordTokenRepository resetPasswordTokenRepository;

  public AuthServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public AuthServiceImpl(UserRepository userRepository, ResetPasswordTokenRepository resetPasswordTokenRepository) {
    this.userRepository = userRepository;
    this.resetPasswordTokenRepository = resetPasswordTokenRepository;
  }

  @Override
  public LoginResponse authenticate(LoginRequest loginRequest) {
    User user = userRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new AuthenticateException("Email or Password incorrect"));

    if (!BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
      throw new AuthenticateException("Email or Password incorrect");
    }
    return new LoginResponse(null, null, user);
  }

  @Override
  public ForgotPasswordResponse processForgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
    User user = userRepository.findByEmail(forgotPasswordRequest.getEmail())
            .orElseThrow(() -> new UserNotFoundException("User not found"));

    ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
    String token = UUID.randomUUID().toString();
    resetPasswordToken.setUser(user);
    resetPasswordToken.setToken(token);
    resetPasswordToken.setLocalDateTime(LocalDateTime.now().plusMinutes(15).truncatedTo(ChronoUnit.SECONDS));
    resetPasswordTokenRepository.insert(resetPasswordToken);

    ForgotPasswordResponse forgotPasswordResponse = new ForgotPasswordResponse();
    forgotPasswordResponse.setUser(user);
    forgotPasswordResponse.setLinkResetPassword("http://127.0.0.1:5500/api/reset-password?" + token);

    return forgotPasswordResponse;
  }

}
