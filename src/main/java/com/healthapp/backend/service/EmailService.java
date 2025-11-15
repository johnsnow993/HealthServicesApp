package com.healthapp.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${resend.api.key:}")
    private String resendApiKey;

    @Value("${app.email.from:onboarding@resend.dev}")
    private String fromEmail;

    @Value("${app.frontend.patient.url}")
    private String patientFrontendUrl;

    @Value("${app.frontend.doctor.url}")
    private String doctorFrontendUrl;

    private void sendResendEmail(String to, String subject, String htmlContent) {
        try {
            if (resendApiKey == null || resendApiKey.isEmpty()) {
                System.err.println("❌ RESEND_API_KEY not configured!");
                return;
            }

            String url = "https://api.resend.com/emails";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(resendApiKey);

            Map<String, Object> emailData = new HashMap<>();
            emailData.put("from", fromEmail);
            emailData.put("to", new String[]{to});
            emailData.put("subject", subject);
            emailData.put("html", htmlContent);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(emailData, headers);

            restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
            System.out.println("✅ Email sent successfully to: " + to);

        } catch (Exception e) {
            System.err.println("❌ Failed to send email via Resend: " + e.getMessage());
            throw e;
        }
    }

    @Async
    public void sendVerificationEmail(String toEmail, String token, String userRole) {
        try {
            String frontendUrl = userRole.equals("PATIENT") ? patientFrontendUrl : doctorFrontendUrl;
            String verificationLink = frontendUrl + "/verify-email?token=" + token;

            String htmlContent =
                "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>" +
                "<h2 style='color: #4CAF50;'>Welcome to HealthApp!</h2>" +
                "<p>Please verify your email address by clicking the button below:</p>" +
                "<a href='" + verificationLink + "' style='display: inline-block; padding: 12px 24px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 4px; margin: 16px 0;'>Verify Email</a>" +
                "<p>Or copy this link: <br/><code>" + verificationLink + "</code></p>" +
                "<p>Verification token: <code>" + token + "</code></p>" +
                "<p style='color: #666;'>This link will expire in 24 hours.</p>" +
                "<p>Best regards,<br/>HealthApp Team</p>" +
                "</div>";

            sendResendEmail(toEmail, "Email Verification - HealthApp", htmlContent);
            System.out.println("🔑 Verification token: " + token);
        } catch (Exception e) {
            System.err.println("❌ Failed to send verification email: " + e.getMessage());
            System.out.println("🔑 Verification token for " + toEmail + ": " + token);
        }
    }

    @Async
    public void sendPasswordResetEmail(String toEmail, String token, String userRole) {
        try {
            String frontendUrl = userRole.equals("PATIENT") ? patientFrontendUrl : doctorFrontendUrl;
            String resetLink = frontendUrl + "/reset-password?token=" + token;

            String htmlContent =
                "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>" +
                "<h2 style='color: #FF9800;'>Password Reset Request</h2>" +
                "<p>You requested to reset your password. Click the button below:</p>" +
                "<a href='" + resetLink + "' style='display: inline-block; padding: 12px 24px; background-color: #FF9800; color: white; text-decoration: none; border-radius: 4px; margin: 16px 0;'>Reset Password</a>" +
                "<p>Or copy this link: <br/><code>" + resetLink + "</code></p>" +
                "<p>Reset token: <code>" + token + "</code></p>" +
                "<p style='color: #666;'>This link will expire in 24 hours.</p>" +
                "<p>If you didn't request this, please ignore this email.</p>" +
                "<p>Best regards,<br/>HealthApp Team</p>" +
                "</div>";

            sendResendEmail(toEmail, "Password Reset - HealthApp", htmlContent);
            System.out.println("🔑 Reset token: " + token);
        } catch (Exception e) {
            System.err.println("❌ Failed to send password reset email: " + e.getMessage());
            System.out.println("🔑 Reset token for " + toEmail + ": " + token);
        }
    }

    @Async
    public void sendWelcomeEmail(String toEmail, String firstName) {
        try {
            String htmlContent =
                "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>" +
                "<h2 style='color: #4CAF50;'>Welcome to HealthApp, " + firstName + "!</h2>" +
                "<p>Your email has been verified successfully!</p>" +
                "<p>You can now login to HealthApp and start using our services.</p>" +
                "<p>Thank you for joining us!</p>" +
                "<p>Best regards,<br/>HealthApp Team</p>" +
                "</div>";

            sendResendEmail(toEmail, "Welcome to HealthApp!", htmlContent);
        } catch (Exception e) {
            System.err.println("❌ Failed to send welcome email: " + e.getMessage());
        }
    }
}
