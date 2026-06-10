package com.sakute.project_fumo_backend.domain.service.email;

import java.math.BigDecimal;

public interface EmailService {
    void sendConfirmationEmail(String toEmail, String username, String confirmationLink);

    void sendCommentReplyNotification(String toEmail, String recipientUsername,
                                      String replierUsername, String postTitle, String replyContent);

    void sendDonationNotification(String toEmail, String fundraiserUsername,
                                  String fundraisingTitle, String donorDisplay, BigDecimal amount);
}
