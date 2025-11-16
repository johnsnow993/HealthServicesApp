# Healthcare Platform - Functional Requirements

## Project Overview
A telemedicine platform connecting patients with independent doctors for appointment booking and medical consultations.

---

## 1. AUTHENTICATION & AUTHORIZATION

- User registration (Patient/Doctor)
- Email verification required
- Login/Logout with JWT tokens
- Password reset via email
- Role-based access: Patient, Doctor, Admin
- Password requirements: min 8 chars, 1 uppercase, 1 number
- bcrypt password hashing

---

## 2. PATIENT PROFILE

- Personal info: Name, DOB, Phone, Gender, Address
- Age validation (18+)
- Optional profile photo (base64 encoded, handled by frontend)
- Medical history via questionnaire:
  - Multiple choice questions
  - True/False questions
  - Text responses (e.g., allergies)
- Optional insurance information
- Profile updates allowed

---

## 3. DOCTOR PROFILE

- Personal info: Name, Phone, Gender
- Profile photo (base64 encoded, handled by frontend)
- Medical License Number (required)
- Specialization selection (GP, Cardiologist, etc.)
- Years of experience
- Education/Qualifications
- Bio (max 500 chars)
- Languages spoken (multi-select)
- Clinic address
- Profile visible only after admin approval

---

## 4. DOCTOR DISCOVERY & SEARCH

- List all approved doctors (paginated)
- Search by name
- Filter by: specialization, languages, availability, rating
- View detailed doctor profiles
- Display ratings and reviews

---

## 5. APPOINTMENT BOOKING

- View doctor's available time slots (30 days)
- Book appointment with optional reason
- View upcoming and past appointments
- Cancel appointment (24h+ notice required)
- Reschedule once per appointment
- Email notifications for all actions
- Prevent double-booking

---

## 6. DOCTOR AVAILABILITY

- Set working days and hours
- Set appointment duration (default 30 min)
- Set break times
- Mark vacation/unavailable dates
- Auto-generate time slots
- Changes affect future appointments only

---

## 7. APPOINTMENT STATUS

Four statuses:
- **Pending**: Awaiting doctor confirmation
- **Confirmed**: Doctor accepted
- **Completed**: Finished
- **Cancelled**: By patient or doctor

Doctor can confirm/reject/complete appointments
Email sent on all status changes

---

## 8. NOTIFICATIONS

Email notifications for:
- Registration confirmation
- Email verification
- Appointment confirmation
- Appointment reminders (24h and 1h before)
- Cancellations
- Password reset
- Status changes

Scheduled jobs check for reminders hourly

---

## 9. MEDICAL RECORDS ACCESS

- Doctors view patient medical history for their appointments only
- Access control enforced
- Audit logging for all access

---

## 10. RATINGS & REVIEWS

- Patients rate doctors (1-5 stars) after completed appointments
- Optional text review (max 500 chars)
- One review per appointment
- Average rating displayed on profile
- Admin can delete inappropriate reviews

---

## 11. ADMIN DASHBOARD

- View all users (patients, doctors)
- Approve/reject doctor registrations
- Suspend/reactivate accounts
- View all appointments
- System statistics (user counts, appointments)
- Doctor approval workflow

---

## 12. APPOINTMENT HISTORY

- View complete appointment history
- Filter by date range, status, doctor/patient
- Reverse chronological order

---

## NON-FUNCTIONAL REQUIREMENTS

- **Performance**: API response < 2 seconds
- **Security**: HTTPS, bcrypt, JWT (24h expiration), CORS, input validation
- **Scalability**: Support 1000+ concurrent users
- **Availability**: 99% uptime, daily backups
- **Compliance**: GDPR principles, audit logs
- **Testing**: 70% code coverage minimum
- **Documentation**: Swagger/OpenAPI

---

## API ENDPOINTS SUMMARY

### Authentication (7 endpoints)
```
POST   /api/auth/register
POST   /api/auth/verify-email
POST   /api/auth/login
POST   /api/auth/logout
POST   /api/auth/forgot-password
POST   /api/auth/reset-password
POST   /api/auth/refresh-token
```

### Patients (7 endpoints)
```
GET    /api/patients/profile
PUT    /api/patients/profile
GET    /api/patients/medical-history
PUT    /api/patients/medical-history
GET    /api/patients/appointments
GET    /api/patients/appointments/history
POST   /api/patients/reviews
```

### Doctors (13 endpoints)
```
GET    /api/doctors
GET    /api/doctors/{id}
GET    /api/doctors/profile
PUT    /api/doctors/profile
GET    /api/doctors/availability
POST   /api/doctors/availability
PUT    /api/doctors/availability/{id}
DELETE /api/doctors/availability/{id}
POST   /api/doctors/unavailability
GET    /api/doctors/appointments
GET    /api/doctors/appointments/{id}/patient-history
PUT    /api/doctors/appointments/{id}/status
GET    /api/doctors/appointments/history
```

### Appointments (7 endpoints)
```
POST   /api/appointments
GET    /api/appointments/{id}
PUT    /api/appointments/{id}
DELETE /api/appointments/{id}
PUT    /api/appointments/{id}/reschedule
GET    /api/appointments/upcoming
GET    /api/doctors/{doctorId}/available-slots
```

### Reviews (4 endpoints)
```
POST   /api/reviews
GET    /api/reviews/doctor/{doctorId}
PUT    /api/reviews/{id}
DELETE /api/reviews/{id}
```

### Admin (12 endpoints)
```
GET    /api/admin/users
GET    /api/admin/patients
GET    /api/admin/doctors
GET    /api/admin/doctors/pending
PUT    /api/admin/doctors/{id}/approve
PUT    /api/admin/doctors/{id}/reject
PUT    /api/admin/users/{id}/suspend
PUT    /api/admin/users/{id}/activate
GET    /api/admin/appointments
GET    /api/admin/statistics
GET    /api/admin/statistics/users
GET    /api/admin/statistics/appointments
```

---

## MODULES SUMMARY

Total Modules: 12
1. Authentication & Authorization
2. Patient Profile Management
3. Doctor Profile Management
4. Doctor Discovery & Search
5. Appointment Booking
6. Doctor Availability Management
7. Appointment Status Management
8. Notification System
9. Medical Records Access
10. Rating & Review
11. Admin Dashboard
12. Appointment History