# Milestone 1: Implementation

## Features Implemented

### 1. User Registration
- Role-based registration (Patient/Doctor)
- Automatic profile creation
- Medical history with JSONB storage for patient questionnaires
- Base64 photo upload support
- Email verification token generation (24h expiration)
- SendGrid async email sending

**Key Files:**
- `AuthService.java`: Registration logic
- `RegisterRequest.java`: DTO with validation
- `Patient.java`, `Doctor.java`: Profile entities
- `MedicalHistory.java`: JSONB questionnaire storage

---

### 2. Email Verification
- UUID-based verification tokens
- Token expiration handling
- Welcome email after verification
- Account activation flow

**Key Files:**
- `AuthService.verifyEmail()`
- `EmailService.java`: SendGrid integration

---

### 3. JWT Authentication
- Token generation with HMAC SHA-256
- 24-hour token expiration
- User details in token payload (userId, role)
- Email verification check before login

**Key Files:**
- `JwtTokenProvider.java`: Token generation/validation
- `JwtAuthenticationFilter.java`: Request filtering
- `UserDetailsServiceImpl.java`: User loading

---

### 4. Password Reset
- Email-based reset flow
- Reset tokens with 24h expiration
- Password encryption with bcrypt
- Email notifications

**Key Files:**
- `AuthService.forgotPassword()`
- `AuthService.resetPassword()`

---

### 5. Security Configuration
- CORS enabled for all origins with credentials support
- JWT filter integration
- Public endpoint configuration
- OPTIONS preflight request handling
- Stateless session management

**Key Files:**
- `SecurityConfig.java`: Security filter chain
- `CorsConfig.java`: CORS settings
- `JwtAuthenticationEntryPoint.java`: 401 handler

---

### 6. Exception Handling
- Custom exceptions for specific error cases
- Global exception handler
- Consistent ApiResponse format

**Custom Exceptions:**
- `UserAlreadyExistsException`
- `ResourceNotFoundException`
- `InvalidTokenException`
- `TokenExpiredException`
- `EmailNotVerifiedException`

**File:** `GlobalExceptionHandler.java`

---

## Database Tables Created

- `users`: Authentication and role
- `patients`: Patient profiles with gender
- `doctors`: Doctor profiles with gender (no fee field)
- `medical_history`: Patient questionnaires (JSONB)

---

## Email Templates

1. **Verification Email**: Sent on registration
2. **Welcome Email**: Sent after email verification
3. **Password Reset Email**: Sent on forgot password request

All emails sent asynchronously via SendGrid Web API.

---

## API Documentation

Swagger UI auto-generated with:
- Request/response schemas
- JWT Bearer authentication
- Interactive testing
- Auto-detects production vs local environment
