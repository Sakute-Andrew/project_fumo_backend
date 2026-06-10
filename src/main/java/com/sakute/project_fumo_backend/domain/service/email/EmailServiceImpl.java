package com.sakute.project_fumo_backend.domain.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final ClassPathResource LOGO = new ClassPathResource("static/img/logo.png");

    @Async
    @Override
    public void sendConfirmationEmail(String toEmail, String username, String confirmationLink) {
        try {
            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("confirmationLink", confirmationLink);

            String html = templateEngine.process("email-confirmation", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Project FUMO - Підтвердження реєстрації");
            helper.setText(html, true);
            helper.addInline("logo", LOGO);

            mailSender.send(message);
            log.info("Confirmation email sent to {}", toEmail);
        } catch (MessagingException e) {
            log.error("Failed to send confirmation email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Не вдалося надіслати листа підтвердження", e);
        }
    }

    @Async
    @Override
    public void sendCommentReplyNotification(String toEmail, String recipientUsername,
                                             String replierUsername, String postTitle, String replyContent) {
        try {
            Context context = new Context();
            context.setVariable("recipientUsername", recipientUsername);
            context.setVariable("replierUsername", replierUsername);
            context.setVariable("postTitle", postTitle);
            context.setVariable("replyContent", replyContent);

            String html = templateEngine.process("comment-reply", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(replierUsername + " відповів на ваш коментар");
            helper.setText(html, true);
            helper.addInline("logo", LOGO);

            mailSender.send(message);
            log.info("Reply notification sent to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send reply notification to {}: {}", toEmail, e.getMessage(), e);
        }
    }

    @Async
    @Override
    public void sendDonationNotification(String toEmail, String fundraiserUsername,
                                         String fundraisingTitle, String donorDisplay, BigDecimal amount) {
        try {
            Context context = new Context();
            context.setVariable("fundraiserUsername", fundraiserUsername);
            context.setVariable("fundraisingTitle", fundraisingTitle);
            context.setVariable("donorDisplay", donorDisplay);
            context.setVariable("amount", amount);

            String html = templateEngine.process("donation-notification", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Новий донат до \"" + fundraisingTitle + "\"");
            helper.setText(html, true);
            helper.addInline("logo", LOGO);

            mailSender.send(message);
            log.info("Donation notification sent to {}", toEmail);
        } catch (MessagingException e) {
            log.error("Failed to send donation notification to {}: {}", toEmail, e.getMessage());
        }
    }
}
