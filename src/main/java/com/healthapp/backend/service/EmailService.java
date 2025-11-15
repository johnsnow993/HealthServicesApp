package com.healthapp.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.frontend.patient.url}")
    private String patientFrontendUrl;

    @Value("${app.frontend.doctor.url}")
    private String doctorFrontendUrl;

    public void sendVerificationEmail(String toEmail, String token, String userRole) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Email Verification - HealthApp");

            String frontendUrl = userRole.equals("PATIENT") ? patientFrontendUrl : doctorFrontendUrl;
            String verificationLink = frontendUrl + "/verify-email?token=" + token;

            message.setText(
                    "Welcome to HealthApp!\n\n" +
                            "Please verify your email by clicking the link below:\n" +
                            verificationLink + "\n\n" +
                            "Token: " + token + "\n\n" +
                            "This link will expire in 24 hours.\n\n" +
                            "Best regards,\nHealthApp Team"
            );

            mailSender.send(message);
            System.out.println(" Verification email sent to: " + toEmail);
            System.out.println(" Verification token: " + token);
        } catch (Exception e) {
            System.err.println(" Failed to send email: " + e.getMessage());
            System.out.println(" Verification token for " + toEmail + ": " + token);
        }
    }

    public void sendPasswordResetEmail(String toEmail, String token, String userRole) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Password Reset - HealthApp");

            String frontendUrl = userRole.equals("PATIENT") ? patientFrontendUrl : doctorFrontendUrl;
            String resetLink = frontendUrl + "/reset-password?token=" + token;

            message.setText(
                    "Hello,\n\n" +
                            "You requested to reset your password.\n" +
                            "Click the link below:\n" +
                            resetLink + "\n\n" +
                            "Token: " + token + "\n\n" +
                            "This link will expire in 24 hours.\n\n" +
                            "Best regards,\nHealthApp Team"
            );

            mailSender.send(message);
            System.out.println(" Password reset email sent to: " + toEmail);
            System.out.println(" Reset token: " + token);
        } catch (Exception e) {
            System.err.println(" Failed to send email: " + e.getMessage());
            System.out.println(" Reset token for " + toEmail + ": " + token);
        }
    }

    public void sendWelcomeEmail(String toEmail, String firstName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Welcome to HealthApp!");

            message.setText(
                    "Hello " + firstName + ",\n\n" +
                            "Your email has been verified successfully!\n\n" +
                            "You can now login to HealthApp.\n\n" +
                            "Best regards,\nHealthApp Team"
            );

            mailSender.send(message);
            System.out.println(" Welcome email sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println(" Failed to send welcome email: " + e.getMessage());
        }
    }
}