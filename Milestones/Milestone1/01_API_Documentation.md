# Milestone 1: Authentication API

## Production URL
https://healthservicesapp-production.up.railway.app

## API Documentation
https://healthservicesapp-production.up.railway.app/swagger-ui/index.html

---

## Endpoints

### 1. Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "patient@example.com",
  "password": "Password123",
  "role": "PATIENT",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+1234567890",
  "gender": "Male",
  "dob": "1990-01-01",
  "profilePhotoBase64": "data:image/png;base64,...",
  "medicalQuestionnaire": {
    "smoker": false,
    "allergies": "None"
  }
}

Response: 201 Created
{
  "success": true,
  "message": "Registration successful! Please check your email to verify your account."
}
```

---

### 2. Verify Email
```http
POST /api/auth/verify-email?token={verificationToken}

Response: 200 OK
{
  "success": true,
  "message": "Email verified successfully! You can now login."
}
```

---

### 3. Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "patient@example.com",
  "password": "Password123"
}

Response: 200 OK
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": "uuid-here",
  "email": "patient@example.com",
  "role": "PATIENT"
}
```

---

### 4. Forgot Password
```http
POST /api/auth/forgot-password?email={email}

Response: 200 OK
{
  "success": true,
  "message": "Password reset link sent to your email"
}
```

---

### 5. Reset Password
```http
POST /api/auth/reset-password?token={resetToken}&newPassword={newPassword}

Response: 200 OK
{
  "success": true,
  "message": "Password reset successful! You can now login."
}
```

---

### 6. Logout
```http
POST /api/auth/logout
Authorization: Bearer {token}

Response: 200 OK
{
  "success": true,
  "message": "Logout successful"
}
```

---

### 7. Health Check
```http
GET /api/auth/health

Response: 200 OK
{
  "success": true,
  "message": "Auth service is running"
}
```

---

## Authentication

Protected endpoints require JWT token:
```
Authorization: Bearer {token}
```

Token expiration: 24 hours
