package io.github.mfthfzn.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.mfthfzn.dto.JwtPayload;
import io.github.mfthfzn.entity.User;

public interface TokenService {

  String generateAccessToken(JwtPayload jwtPayload);

  String generateRefreshToken(JwtPayload jwtPayload);

  void verifyRefreshToken(String token);

  void verifyAccessToken(String token);

  JwtPayload getUserFromToken(String token);

  String getRefreshToken(String token);

  void removeRefreshToken(JwtPayload jwtPayload, String refreshToken);

}
