# Database Schema Design

## Database Entities

### 1. User
```
id                  UUID PRIMARY KEY
email               VARCHAR(255) UNIQUE NOT NULL
password            VARCHAR(255) NOT NULL
role                ENUM(PATIENT, DOCTOR, ADMIN)
verified            BOOLEAN DEFAULT FALSE
created_at          TIMESTAMP
```

### 2. Patient
```
id                      UUID PRIMARY KEY
user_id                 UUID FOREIGN KEY → User
first_name              VARCHAR(100) NOT NULL
last_name               VARCHAR(100) NOT NULL
dob                     DATE NOT NULL
phone                   VARCHAR(20) NOT NULL
gender                  VARCHAR(20) NOT NULL
address                 TEXT
profile_photo_base64    TEXT
insurance_info          TEXT
```

### 3. Doctor
```
id                      UUID PRIMARY KEY
user_id                 UUID FOREIGN KEY → User
first_name              VARCHAR(100) NOT NULL
last_name               VARCHAR(100) NOT NULL
phone                   VARCHAR(20) NOT NULL
gender                  VARCHAR(20) NOT NULL
profile_photo_base64    TEXT
license_number          VARCHAR(100) UNIQUE NOT NULL
specialization          VARCHAR(100) NOT NULL
experience              INTEGER
education               TEXT
bio                     VARCHAR(500)
languages               TEXT[] or JSON
clinic_address          TEXT
approved                BOOLEAN DEFAULT FALSE
```

### 4. MedicalHistory
```
id                  UUID PRIMARY KEY
patient_id          UUID FOREIGN KEY → Patient
questionnaire_json  JSONB
created_at          TIMESTAMP
updated_at          TIMESTAMP
```

### 5. Availability
```
id              UUID PRIMARY KEY
doctor_id       UUID FOREIGN KEY → Doctor
day_of_week     ENUM(MON, TUE, WED, THU, FRI, SAT, SUN)
start_time      TIME NOT NULL
end_time        TIME NOT NULL
slot_duration   INTEGER DEFAULT 30
break_start     TIME
break_end       TIME
```

### 6. Unavailability
```
id          UUID PRIMARY KEY
doctor_id   UUID FOREIGN KEY → Doctor
start_date  DATE NOT NULL
end_date    DATE NOT NULL
reason      TEXT
created_at  TIMESTAMP
```

### 7. Appointment
```
id                      UUID PRIMARY KEY
patient_id              UUID FOREIGN KEY → Patient
doctor_id               UUID FOREIGN KEY → Doctor
date                    DATE NOT NULL
time                    TIME NOT NULL
status                  ENUM(PENDING, CONFIRMED, COMPLETED, CANCELLED)
reason                  TEXT
reschedule_count        INTEGER DEFAULT 0
cancelled_by            ENUM(PATIENT, DOCTOR)
cancellation_reason     TEXT
created_at              TIMESTAMP
updated_at              TIMESTAMP
```

### 8. Review
```
id              UUID PRIMARY KEY
doctor_id       UUID FOREIGN KEY → Doctor
patient_id      UUID FOREIGN KEY → Patient
appointment_id  UUID FOREIGN KEY → Appointment UNIQUE
rating          INTEGER CHECK (rating >= 1 AND rating <= 5)
comment         VARCHAR(500)
created_at      TIMESTAMP
```

### 9. Notification
```
id          UUID PRIMARY KEY
user_id     UUID FOREIGN KEY → User
type        ENUM(EMAIL, SYSTEM)
message     TEXT NOT NULL
read        BOOLEAN DEFAULT FALSE
sent_at     TIMESTAMP
```

### 10. AuditLog
```
id          UUID PRIMARY KEY
user_id     UUID FOREIGN KEY → User
action      VARCHAR(255)
resource    VARCHAR(255)
timestamp   TIMESTAMP
```

---

## Entity Relationships
```
User 1 ──── 1 Patient
User 1 ──── 1 Doctor
Patient 1 ──── N Appointment
Doctor 1 ──── N Appointment
Patient 1 ──── 1 MedicalHistory
Doctor 1 ──── N Availability
Doctor 1 ──── N Unavailability
Appointment 1 ──── 0..1 Review
Doctor 1 ──── N Review
Patient 1 ──── N Review
User 1 ──── N Notification
User 1 ──── N AuditLog
```

---

## Indexes
```sql
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_doctor_license ON doctors(license_number);
CREATE INDEX idx_doctor_approved ON doctors(approved);
CREATE INDEX idx_appointment_date ON appointments(date);
CREATE INDEX idx_appointment_status ON appointments(status);
CREATE INDEX idx_appointment_patient ON appointments(patient_id);
CREATE INDEX idx_appointment_doctor ON appointments(doctor_id);
CREATE INDEX idx_review_doctor ON reviews(doctor_id);
CREATE INDEX idx_availability_doctor ON availability(doctor_id);
```