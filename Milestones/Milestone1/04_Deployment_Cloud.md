# Milestone 1: Deployment

## Production Environment

**Platform**: Railway
**URL**: https://healthservicesapp-production.up.railway.app
**Status**: Live

---

## Components

### Application Server
- Spring Boot 3.5.7 on Java 21
- Auto-deployed on git push
- Zero-downtime deployments

### Database
- PostgreSQL 15.14 on Railway
- Private network connection
- Automated backups

### Email Service
- SendGrid Web API
- Async email sending

---

## Environment Variables

```env
DATABASE_URL=jdbc:postgresql://...
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=***
JWT_SECRET=*** (256-bit)
JWT_EXPIRATION=86400000
SENDGRID_API_KEY=***
EMAIL_FROM=healthservicesbackend@gmail.com
PATIENT_FRONTEND_URL=http://localhost:3000
DOCTOR_FRONTEND_URL=http://localhost:3001
```

---

## Deployment Process

1. Push code to GitHub
2. Railway auto-detects changes
3. Builds with Maven
4. Deploys new container
5. Health check verification

**Build Command:**
```bash
./mvnw clean install -DskipTests
```

**Start Command:**
```bash
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

---

## CORS Configuration

- **Allowed Origins**: All (*) with credentials
- **Methods**: GET, POST, PUT, DELETE, OPTIONS
- **Headers**: All (including Authorization)

---

## Monitoring

**Health Endpoint:**
```bash
curl https://healthservicesapp-production.up.railway.app/actuator/health
```

**Logs**: Available in Railway dashboard

---

## Database Access

**Via Railway Dashboard:**
1. Select PostgreSQL service
2. Click "Query" tab
3. Execute SQL queries

**Schema Management**: Hibernate auto-update (`ddl-auto=update`)
