package io.github.mfthfzn.repository;

import io.github.mfthfzn.entity.RefreshToken;
import io.github.mfthfzn.entity.ResetPasswordToken;

import java.util.Optional;

public interface ResetPasswordTokenRepository {

  void insert(ResetPasswordToken resetPasswordToken);

  Optional<ResetPasswordToken> findByEmail(String email);

  void removeByEmail(String email);

}
