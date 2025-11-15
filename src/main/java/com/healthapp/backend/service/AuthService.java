package com.healthapp.backend.service;

import com.healthapp.backend.dto.*;
import com.healthapp.backend.entity.*;
import com.healthapp.backend.enums.Role;
import com.healthapp.backend.exception.*;
import com.healthapp.backend.repository.*;
import com.healthapp.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Transactional
    public ApiResponse register(RegisterRequest request) {
        // Check if email exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        // Validate role-specific fields
        if (request.getRole() == Role.PATIENT && request.getDob() == null) {
            throw new IllegalArgumentException("Date of birth is required for patients");
        }

        if (request.getRole() == Role.DOCTOR && request.getLicenseNumber() == null) {
            throw new IllegalArgumentException("License number is required for doctors");
        }

        if (request.getRole() == Role.DOCTOR && request.getSpecialization() == null) {
            throw new IllegalArgumentException("Specialization is required for doctors");
        }

        // Create User
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setVerified(false);

        // Generate verification token
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setTokenExpiryDate(LocalDateTime.now().plusHours(24));

        // Save user
        user = userRepository.save(user);

        // Create profile based on role
        if (request.getRole() == Role.PATIENT) {
            createPatientProfile(user, request);
        } else if (request.getRole() == Role.DOCTOR) {
            createDoctorProfile(user, request);
        }

        // Send verification email
        emailService.sendVerificationEmail(
                user.getEmail(),
                verificationToken,
                user.getRole().name()
        );

        return new ApiResponse(
                true,
                "Registration successful! Please check your email to verify your account."
        );
    }

    private void createPatientProfile(User user, RegisterRequest request) {
        Patient patient = new Patient();
        patient.setUser(user);
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setDob(request.getDob());
        patient.setPhone(request.getPhone());
        patient.setGender(request.getGender());
        patient.setAddress(request.getAddress());
        patient.setProfilePhotoBase64(request.getProfilePhotoBase64());
        patient.setInsuranceInfo(request.getInsuranceInfo());

        patientRepository.save(patient);

        // Create medical history if questionnaire provided
        if (request.getMedicalQuestionnaire() != null && !request.getMedicalQuestionnaire().isEmpty()) {
            MedicalHistory medicalHistory = new MedicalHistory();
            medicalHistory.setPatient(patient);
            medicalHistory.setQuestionnaireJson(request.getMedicalQuestionnaire());
            medicalHistoryRepository.save(medicalHistory);
        }
    }

    private void createDoctorProfile(User user, RegisterRequest request) {
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setPhone(request.getPhone());
        doctor.setGender(request.getGender());
        doctor.setProfilePhotoBase64(request.getProfilePhotoBase64());
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setExperience(request.getExperience());
        doctor.setEducation(request.getEducation());
        doctor.setBio(request.getBio());
        doctor.setLanguages(request.getLanguages());
        doctor.setClinicAddress(request.getClinicAddress());
        doctor.setApproved(false);

        doctorRepository.save(doctor);
    }

    @Transactional
    public ApiResponse verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid verification token"));

        // Check if token expired
        if (user.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Verification token has expired");
        }

        // Verify user
        user.setVerified(true);
        user.setVerificationToken(null);
        user.setTokenExpiryDate(null);
        userRepository.save(user);

        // Send welcome email
        String firstName = getFirstName(user);
        emailService.sendWelcomeEmail(user.getEmail(), firstName);

        return new ApiResponse(true, "Email verified successfully! You can now login.");
    }

    public LoginResponse login(LoginRequest request) {
        // Authenticate
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Get user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        // Check if verified
        if (!user.getVerified()) {
            throw new EmailNotVerifiedException("Please verify your email before logging in");
        }

        // Generate token
        String token = tokenProvider.generateToken(authentication);

        return new LoginResponse(token, user.getId(), user.getEmail(), user.getRole());
    }

    @Transactional
    public ApiResponse forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with this email"));

        // Generate reset token
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setTokenExpiryDate(LocalDateTime.now().plusHours(24));
        userRepository.save(user);

        // Send email
        emailService.sendPasswordResetEmail(email, resetToken, user.getRole().name());

        return new ApiResponse(true, "Password reset link sent to your email");
    }

    @Transactional
    public ApiResponse resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid reset token"));

        // Check expiry
        if (user.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Reset token has expired");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setTokenExpiryDate(null);
        userRepository.save(user);

        return new ApiResponse(true, "Password reset successful! You can now login.");
    }

    private String getFirstName(User user) {
        if (user.getPatient() != null) {
            return user.getPatient().getFirstName();
        } else if (user.getDoctor() != null) {
            return user.getDoctor().getFirstName();
        }
        return "User";
    }
}
