package io.github.mfthfzn.service;

import org.junit.jupiter.api.Test;

public class EmailServiceTest {

  EmailServiceImpl emailService = new EmailServiceImpl();

  @Test
  void testSendEmail() {

    emailService.sendResetPasswordLink("Dinpix", "dinpixdinpix@gmail.com", "http://127.0.0.1:5500/reset-password?token=abcdfghjikdnwabduabdawdad");

  }
}