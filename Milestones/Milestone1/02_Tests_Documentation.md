# Milestone 1: Tests

## Summary

- **Total Tests**: 48
- **Status**: All passing
- **Framework**: JUnit 5 + Mockito

---

## Test Classes

### 1. AuthServiceTest (16 tests)
**Location**: `src/test/java/com/healthapp/backend/service/AuthServiceTest.java`

**Coverage:**
- Patient/Doctor registration (success and failure cases)
- Email verification (valid, invalid, expired tokens)
- Login (valid credentials, unverified email, invalid credentials)
- Password reset flow (valid, invalid, expired tokens)

---

### 2. JwtTokenProviderTest (8 tests)
**Location**: `src/test/java/com/healthapp/backend/security/JwtTokenProviderTest.java`

**Coverage:**
- Token generation and validation
- Email extraction from tokens
- Invalid/malformed token handling
- Different secret validation
- Multi-role token generation

---

### 3. UserDetailsServiceImplTest (9 tests)
**Location**: `src/test/java/com/healthapp/backend/security/UserDetailsServiceImplTest.java`

**Coverage:**
- User loading by email
- Non-existing user handling
- Unverified user account status
- Role-based authority assignment
- Account status flags

---

### 4. AuthControllerTest (14 tests)
**Location**: `src/test/java/com/healthapp/backend/controller/AuthControllerTest.java`

**Coverage:**
- HTTP endpoint testing with MockMvc
- Request validation
- Response status codes
- JSON response structure
- Patient and Doctor registration flows

---

### 5. HealthServicesAppApplicationTests (1 test)
**Location**: `src/test/java/com/healthapp/backend/HealthServicesAppApplicationTests.java`

**Coverage:**
- Spring Boot context loading

---

## Running Tests

```bash
./mvnw test
```
