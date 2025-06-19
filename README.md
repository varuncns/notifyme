# 📣 NotifyMe

NotifyMe is a modular notification service built using **Spring Boot**, **RabbitMQ**, and **PostgreSQL**. It receives notification requests (email or SMS) from external services via a REST API, processes them asynchronously, and logs their status in the database.

---

## 🚀 Features Implemented

### ✅ Milestone 1: Project Setup
- Spring Boot 3.4.5 (Java 21)
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
| Framework    | Spring Boot 3.4.5    |
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
```

---

## 🐳 Docker Setup

You can use Docker Compose to run:
- PostgreSQL DB
- RabbitMQ server

(*Setup file not included yet — can be added in future milestones.*)

---

## 📈 Next Milestones

- **Milestone 7:** Admin APIs to query logs & statistics
- **Milestone 8:** UI dashboard (optional or via frontend integration)
- **Milestone 9:** Scheduled notification support (delayed processing)
- **Milestone 10:** Deployment + CI/CD (GCP or Railway)

---

## 🧑‍💻 Author

Built with ❤️ by Varun

---
