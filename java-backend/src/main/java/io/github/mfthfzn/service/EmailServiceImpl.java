package io.github.mfthfzn.service;

import io.github.mfthfzn.util.MailUtil;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class EmailServiceImpl implements EmailService {

  private final String username;

  private final String password;

  private final String senderEmail;

  private final Properties props;

  private final ExecutorService executorService = Executors.newFixedThreadPool(10);

  public EmailServiceImpl() {
    Dotenv dotenv = Dotenv.load();
    this.username = dotenv.get("SMTP_EMAIL");
    this.password = dotenv.get("SMTP_PASSWORD");
    this.senderEmail = dotenv.get("SENDER_EMAIL");

    String host = dotenv.get("SMTP_HOST");
    String port = dotenv.get("SMTP_PORT");

    props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.port", port);

//    props.put("mail.debug", "true");
  }

  @Override
  public void sendResetPasswordLink(String name, String toAddress, String resetLink) {

    executorService.submit(() -> {
      try {
        Session session = Session.getInstance(props, new Authenticator() {
          @Override
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
          }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(toAddress)
        );
        message.setSubject("Password Reset Request");

        String htmlContent = MailUtil.getResetPasswordTemplate(name, resetLink);

        message.setContent(htmlContent, "text/html; charset=utf-8");

        Transport.send(message);
        log.info("Email reset password has been sent to : {}", toAddress);

      } catch (MessagingException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        throw new RuntimeException("Failed to send a email : " + e.getMessage());
      }
    });
  }
}
