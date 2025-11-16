# Project Plan

## Duration
November 3, 2025 - January 31, 2026 (13 weeks)

---

## Milestone Schedule

| Milestone | Date | Focus |
|-----------|------|-------|
| M0 | Nov 03 | Requirements, Planning, Setup |
| M1 | Nov 17 | Authentication System |
| M2 | Dec 08 | Profile Management, Availability |
| M3 | Jan 12 | Appointments, Search, Reviews, Admin |
| M4 | Jan 26 | Deployment, Testing |
| Final | Jan 31 | Final Delivery |

---

## Phase 1: Authentication (Nov 4 - Nov 17)

- Implement User entity and repository
- Create JWT token provider
- Configure Spring Security
- Build registration endpoint with email verification
- Build login endpoint
- Implement password reset
- Setup email service
- Write unit tests
- Configure Swagger

---

## Phase 2: Profile Management (Nov 18 - Dec 8)

### Patient & Doctor Profiles (Nov 18 - Dec 1)
- Implement Patient entity, repository, service
- Implement Doctor entity, repository, service
- Create profile CRUD endpoints
- Implement medical questionnaire (JSON)
- Add profile photo support (base64)
- Write tests

### Availability Management (Dec 2 - Dec 8)
- Implement Availability and Unavailability entities
- Create availability endpoints
- Time slot generation algorithm
- Handle break times
- Write tests

---

## Phase 3: Core Features (Dec 9 - Jan 12)

### Appointment Booking (Dec 9 - Dec 22)
- Implement Appointment entity, repository, service
- Build booking logic with validation
- Prevent double-booking
- Status management (Pending, Confirmed, Completed, Cancelled)
- Cancellation logic (24-hour rule)
- Reschedule logic (max 1 per appointment)
- Email notifications
- Scheduled reminders (24h and 1h before)
- Write tests

### Doctor Search (Dec 23 - Dec 29)
- Implement search service
- Search by name
- Filters: specialization, languages, availability, rating
- Pagination (20 per page)
- Sorting options
- Write tests

### Reviews & Admin (Dec 30 - Jan 12)
- Implement Review entity, repository, service
- Review submission endpoints
- Rating validation (1-5 stars)
- One review per appointment constraint
- Calculate average ratings
- Admin dashboard endpoints
- Doctor approval workflow
- User suspension
- System statistics
- Audit logging
- Write tests

---

## Phase 4: Deployment (Jan 13 - Jan 26)

- Optimize queries, add indexes
- Error handling
- Complete API documentation
- Security audit
- Code cleanup
- Deploy to Railway
- Load testing
- Prepare presentation

