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
            String htmlContent =
                "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f5f5f5;'>" +
                "<div style='background-color: white; padding: 30px; border-radius: 8px;'>" +
                "<h2 style='color: #4CAF50; text-align: center;'>Welcome to HealthApp!</h2>" +
                "<p style='font-size: 16px;'>Please verify your email address by entering the verification code below:</p>" +
                "<div style='background-color: #f0f0f0; padding: 20px; margin: 20px 0; border-radius: 4px; text-align: center;'>" +
                "<p style='margin: 0; color: #666; font-size: 14px;'>Your Verification Code</p>" +
                "<p style='margin: 10px 0; font-size: 32px; font-weight: bold; color: #4CAF50; letter-spacing: 2px; font-family: monospace;'>" + token + "</p>" +
                "</div>" +
                "<p style='color: #666; font-size: 14px;'>Copy this code and paste it in the app to verify your account.</p>" +
                "<p style='color: #999; font-size: 12px;'>This code will expire in 24 hours.</p>" +
                "<hr style='border: none; border-top: 1px solid #eee; margin: 20px 0;'>" +
                "<p style='color: #999; font-size: 12px; text-align: center;'>Best regards,<br/>HealthApp Team</p>" +
                "</div></div>";

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
            String htmlContent =
                "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f5f5f5;'>" +
                "<div style='background-color: white; padding: 30px; border-radius: 8px;'>" +
                "<h2 style='color: #FF9800; text-align: center;'>Password Reset Request</h2>" +
                "<p style='font-size: 16px;'>You requested to reset your password. Use the code below to reset it:</p>" +
                "<div style='background-color: #fff3e0; padding: 20px; margin: 20px 0; border-radius: 4px; text-align: center; border: 2px solid #FF9800;'>" +
                "<p style='margin: 0; color: #666; font-size: 14px;'>Your Reset Code</p>" +
                "<p style='margin: 10px 0; font-size: 32px; font-weight: bold; color: #FF9800; letter-spacing: 2px; font-family: monospace;'>" + token + "</p>" +
                "</div>" +
                "<p style='color: #666; font-size: 14px;'>Copy this code and paste it in the app to reset your password.</p>" +
                "<p style='color: #999; font-size: 12px;'>This code will expire in 24 hours.</p>" +
                "<p style='color: #d32f2f; font-size: 13px;'>⚠️ If you didn't request this, please ignore this email and your password will remain unchanged.</p>" +
                "<hr style='border: none; border-top: 1px solid #eee; margin: 20px 0;'>" +
                "<p style='color: #999; font-size: 12px; text-align: center;'>Best regards,<br/>HealthApp Team</p>" +
                "</div></div>";

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
