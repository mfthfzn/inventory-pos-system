package io.github.mfthfzn.controller;

import io.github.mfthfzn.entity.User;
import io.github.mfthfzn.repository.UserRepositoryImpl;
import io.github.mfthfzn.service.UserServiceImpl;
import io.github.mfthfzn.util.JpaUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/api/forgot-password")
public class ForgotPasswordController extends BaseController{

  UserRepositoryImpl userRepository = new UserRepositoryImpl(
          JpaUtil.getEntityManagerFactory()
  );

  UserServiceImpl userService = new UserServiceImpl(userRepository);

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String email = req.getParameter("email");

    User user = userService.getUser(email);
  }
}
