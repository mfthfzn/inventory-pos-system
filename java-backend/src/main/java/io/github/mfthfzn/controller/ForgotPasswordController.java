package io.github.mfthfzn.controller;

import io.github.mfthfzn.dto.ForgotPasswordRequest;
import io.github.mfthfzn.dto.ForgotPasswordResponse;
import io.github.mfthfzn.exception.UserNotFoundException;
import io.github.mfthfzn.repository.ResetPasswordTokenRepository;
import io.github.mfthfzn.repository.ResetPasswordTokenRepositoryImpl;
import io.github.mfthfzn.repository.UserRepository;
import io.github.mfthfzn.repository.UserRepositoryImpl;
import io.github.mfthfzn.service.*;
import io.github.mfthfzn.util.JpaUtil;
import io.github.mfthfzn.util.ValidatorUtil;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@WebServlet(urlPatterns = "/api/auth/forgot-password")
@Slf4j
  public class ForgotPasswordController extends BaseController {

  ResetPasswordTokenRepository resetPasswordTokenRepository = new ResetPasswordTokenRepositoryImpl(
          JpaUtil.getEntityManagerFactory()
  );

  UserRepository userRepository = new UserRepositoryImpl(
          JpaUtil.getEntityManagerFactory()
  );

  AuthService authService = new AuthServiceImpl(userRepository, resetPasswordTokenRepository);

  EmailService emailService = new EmailServiceImpl();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String email = req.getParameter("email");
    ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest(email);
    Set<ConstraintViolation<Object>> constraintViolations = ValidatorUtil.validate(forgotPasswordRequest);

    if (!constraintViolations.isEmpty()) {
      for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
        sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Data request invalid", Map.of(
                "message", constraintViolation.getMessage()
        ));
        break;
      }
      return;
    }

    try {
      ForgotPasswordResponse forgotPasswordResponse = authService.processForgotPassword(forgotPasswordRequest);
      emailService.sendResetPasswordLink(
              forgotPasswordResponse.getUser().getName(), email, forgotPasswordResponse.getLinkResetPassword()
      );
      sendSuccess(resp, HttpServletResponse.SC_OK, "Success to send email",
              Map.of(
                      "message", "Success send email if your email registered"
              ));
    } catch (UserNotFoundException userNotFoundException) {
      sendSuccess(resp, HttpServletResponse.SC_OK, "Success to send email",
              Map.of(
                      "message", "Success send email if your email registered"
              ));
    } catch (PersistenceException persistenceException) {
      log.error(persistenceException.getMessage());
      sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to find user",
              Map.of(
                      "message", "An error occurred on the database server."
              ));
    } catch (Exception exception) {
      exception.printStackTrace();
      log.error(exception.getMessage());
      sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to find user",
              Map.of(
                      "message", "An error occurred on server."
              ));
    }
  }
}
