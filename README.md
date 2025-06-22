# 📣 NotifyMe

NotifyMe is a modular notification service built using **Spring Boot**, **RabbitMQ**, and **PostgreSQL**. It receives notification requests (email or SMS) from external services via a REST API, processes them asynchronously, and logs their status in the database.

---

## 🚀 Features Implemented

### ✅ Milestone 1: Project Setup
- Spring Boot 3.2.5 (Java 21)
- Dockerized backend
- PostgreSQL as persistence layer
- RabbitMQ for message queue
- Clean modular architecture

### ✅ Milestone 2: Event Producer
- `POST /api/notify/send`
- Accepts `NotificationEventDTO` payload
- Pushes messages to RabbitMQ Exchange

### ✅ Milestone 3: Email Notification (Async)
- Spring Mail integration
- HTML email using Thymeleaf
- Logs status (`SENT` or `FAILED`) in DB

### ✅ Milestone 4: Notification Logging (PostgreSQL)
- `NotificationLog` entity with:
  - recipientEmail / phoneNumber
  - message, subject
  - type (EMAIL/SMS)
  - status (SENT/FAILED)
  - retryCount, maxRetryReached
  - timestamp, error message
- Repository for storing logs per delivery attempt

### ✅ Milestone 5: SMS Notification via Twilio
- Twilio SDK integrated
- Phone number validation using regex
- Retry and logging similar to email flow

### ✅ Milestone 6: Retry Mechanism
- Retries failed messages (up to 3 times)
- Uses RabbitMQ to requeue failed events
- Logs each attempt to DB with accurate retry count
- Invalid formats also result in `FAILED` log entries

### ✅ Milestone 7: Validation & Error Handling
- Bean validation on request DTOs (`@Valid`)
- Global exception handler returning user friendly errors

### ✅ Milestone 8: Security Layer
- API key filter protecting all `/api/**` endpoints
- Requests must include `x-api-key` header

### ✅ Milestone 9: Admin Dashboard
- `/api/dashboard/summary` returns counts for sent/failed notifications
- `/api/dashboard/logs` paginated list with type/status filters
- `/api/dashboard/export` download logs as CSV

### ✅ Milestone 10: Observability & Docs
- Swagger UI generated from OpenAPI at `/swagger-ui.html`
- Spring Boot Actuator endpoints enabled under `/actuator`

### ✅ Milestone 11: Dockerization
- Dockerfile to package the NotifyMe service
- Docker Compose orchestrates app, RabbitMQ and PostgreSQL

### ✅ Milestone 12: Monitoring Dashboard
- Prometheus scrapes `/actuator/prometheus`
- Grafana container for visualizing metrics

---

## ⚡ Quick Start

1. **Build the project**
   ```bash
   ./mvnw clean package
   ```
2. **Start the full stack**
   ```bash
   docker-compose up -d --build
   ```
3. **Try the API**
   ```bash
   curl -X POST http://localhost:8081/api/notifications \
     -H "Content-Type: application/json" \
     -H "x-api-key: YOUR_KEY" \
     -d '{"recipientEmail":"user@example.com","message":"Hello","type":"EMAIL"}'
   ```

4. **Run tests**
   ```bash
   ./mvnw test
   ```

---

## 📡 API

- **POST `/api/notifications`**
  - Body: `NotificationEventDTO` JSON
  - `type` can be `EMAIL` or `SMS`
  - Requires `x-api-key` header

- **GET `/api/dashboard/summary`** – overall stats
- **GET `/api/dashboard/logs`** – paginated logs with optional `type` & `status`
- **GET `/api/dashboard/export`** – download filtered logs as CSV

Interactive documentation is available at `/swagger-ui.html`.

---

## 🧪 Sample Payload

```json
{
  "recipientEmail": "user@example.com",
  "phoneNumber": "+911234567890",
  "subject": "Ignore this",
  "message": "🚀 This is a test SMS/Email sent from NotifyMe",
  "type": "SMS",
  "retryCount": 0
}
```

---

## 📦 Tech Stack

| Layer        | Tech                  |
|--------------|----------------------|
| Language     | Java 21              |
| Framework    | Spring Boot 3.2.5    |
| Messaging    | RabbitMQ             |
| DB           | PostgreSQL           |
| Email        | Spring Mail + Thymeleaf |
| SMS          | Twilio SDK           |
| Retry        | RabbitMQ + Retry Queue Pattern |
| Logging      | SLF4J + Lombok       |
| Dev Tooling  | Docker, DBeaver, GitHub |

---

## 📁 Folder Structure

```
src/
├── controller/
├── config/
├── dto/
├── entity/
├── repository/
├── service/
│   ├── impl/        # Email + SMS logic
├── consumer/        # RabbitMQ listener
```

---

## 🔐 Security & Best Practices

- SMTP credentials stored in `application-local.properties` (excluded via `.gitignore`)
- **No hardcoded secrets** in code
- Regex-based email/phone validation
- Exception-safe retry logic

---

## 🛠️ Environment Variables (`application-local.properties`)

Create a file named `src/main/resources/application-local.properties` and add your credentials there. This file is ignored by Git.

```properties
# DB
spring.datasource.url=jdbc:postgresql://localhost:5432/notifyme
spring.datasource.username=postgres
spring.datasource.password=your_password

# RabbitMQ
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest

# SMTP
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_email_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Twilio
twilio.account.sid=ACxxxxxxxxxxxxxxxxxxxxx
twilio.auth.token=your_token
twilio.from.phone=+1xxxxxxxxxx

# API key used by `ApiKeyAuthFilter`
notifyme.api.key=CHANGEME
```

---

## 🐳 Docker Setup

This repository includes a `docker-compose.yml` that starts the entire stack:

- PostgreSQL database
- RabbitMQ broker (with management UI)
- NotifyMe service container
- Prometheus and Grafana for metrics

Start everything with:

```bash
docker-compose up -d --build
```

The application image is built from the local sources and runs with the `docker` profile enabled.

---

## 📈 Next Milestones

- Scheduled/delayed notification support
- Frontend UI for the dashboard
- CI/CD pipeline for automated deployment

---

## 🧑‍💻 Author

Built with ❤️ by Varun

---
