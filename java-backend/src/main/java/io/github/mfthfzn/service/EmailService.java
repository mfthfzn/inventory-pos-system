package io.github.mfthfzn.service;

public interface EmailService {

  void sendResetPasswordLink(String name, String toAddress, String resetLink);

}
