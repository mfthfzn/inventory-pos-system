package io.github.mfthfzn.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mfthfzn.dto.LoginRequest;
import io.github.mfthfzn.dto.LoginResponse;
import io.github.mfthfzn.repository.TokenSessionRepositoryImpl;
import io.github.mfthfzn.repository.UserRepositoryImpl;
import io.github.mfthfzn.service.AuthServiceImpl;
import io.github.mfthfzn.service.SessionServiceImpl;
import io.github.mfthfzn.service.UserServiceImpl;
import io.github.mfthfzn.util.JpaUtil;
import io.github.mfthfzn.util.JsonUtil;
import io.github.mfthfzn.util.ValidatorUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet(urlPatterns = "/auth/login")
public class AuthController extends HttpServlet {

  private final UserServiceImpl userService =
          new UserServiceImpl(
                  new UserRepositoryImpl(JpaUtil.getEntityManagerFactory())
          );

  private final SessionServiceImpl sessionService =
          new SessionServiceImpl(
                  new TokenSessionRepositoryImpl(JpaUtil.getEntityManagerFactory())
          );

  private final AuthServiceImpl authService =
          new AuthServiceImpl(
                  userService, sessionService
          );

  ObjectMapper objectMapper = JsonUtil.getObjectMapper();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    resp.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");

    resp.setHeader("Access-Control-Allow-Credentials", "true");

    resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
    resp.setHeader("Access-Control-Allow-Headers", "Content-Type");

    String email = req.getParameter("email");
    String password = req.getParameter("password");

    LoginRequest loginRequest = new LoginRequest(email, password);
    Set<ConstraintViolation<Object>> constraintViolations = ValidatorUtil.validate(loginRequest);

    LoginResponse loginResponse = authService.login(loginRequest);;
    String json;
    PrintWriter writer = resp.getWriter();
    resp.setContentType("application/json");

    if (!constraintViolations.isEmpty()) {
      for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
        loginResponse.setMessage(constraintViolation.getMessage());
        break;
      }
      json = objectMapper.writeValueAsString(loginResponse);
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      writer.println(json);
      return;
    }

    if (!loginResponse.isAuth()) {
      loginResponse.setMessage("Email atau password yang Anda masukkan salah!");
      json = objectMapper.writeValueAsString(loginResponse);
      writer.println(json);
      resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    } else {
      String token;
      if ((token = loginResponse.getToken()) != null) {
        log(token);
        // Cookie for session-token
        Cookie cookieToken = new Cookie("session-token", token);
        cookieToken.setHttpOnly(true);
        cookieToken.setSecure(false);
        cookieToken.setPath("/");
        cookieToken.setMaxAge(60 * 60 * 24);
        resp.addCookie(cookieToken);

        // Cookie for email
        Cookie cookieEmail = new Cookie("email", loginResponse.getEmail());
        cookieEmail.setHttpOnly(false);
        cookieEmail.setSecure(false);
        cookieEmail.setPath("/");
        resp.addCookie(cookieEmail);

        // Cookie for email
        Cookie cookieName = new Cookie("name", loginResponse.getName().getFirstName());
        cookieName.setHttpOnly(false);
        cookieName.setSecure(false);
        cookieName.setPath("/");
        resp.addCookie(cookieName);

        // Cookie for role
        Cookie cookieRole = new Cookie("role", loginResponse.getRole().toString());
        cookieRole.setHttpOnly(false);
        cookieRole.setSecure(false);
        cookieRole.setPath("/");
        resp.addCookie(cookieRole);

        resp.setStatus(HttpServletResponse.SC_OK);
        loginResponse.setMessage("Berhasil login!");

        json = objectMapper.writeValueAsString(loginResponse);
        writer.println(json);
      } else {
        loginResponse.setMessage("Gagal generate token session!");
        json = objectMapper.writeValueAsString(loginResponse);
        writer.println(json);
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }

    }
  }
}
