# 📣 NotifyMe
<!-- Logo -->
<p align="center">
  <img src="https://raw.githubusercontent.com/varuncns/varuncns/main/notifyme.png" alt="Logo" width="120" />
</p>

![Java](https://img.shields.io/badge/Java-21-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.2.x-brightgreen?logo=spring)

NotifyMe is a modular notification service that delivers **email** and **SMS** alerts asynchronously. Built with **Spring Boot**, **RabbitMQ**, and **PostgreSQL**, it exposes a simple REST API for sending notifications while handling retries, logging and monitoring for you.

---

## Table of Contents
- [Features](#features)
- [Quick Start](#quick-start)
- [API](#api)
- [Sample Payload](#sample-payload)
- [Tech Stack](#tech-stack)
- [Folder Structure](#folder-structure)
- [Security & Best Practices](#security--best-practices)
- [Environment Variables](#environment-variables-application-localproperties)
- [Docker Setup](#docker-setup)
- [Next Milestones](#next-milestones)
- [Author](#-author)

---

## Features
- **Project Setup** – Spring Boot 3.2.5 (Java 21) with a dockerized backend
- **Event Producer** – `POST /api/notify/send` pushes messages to RabbitMQ
- **Email Notifications** – Async delivery using Spring Mail + Thymeleaf templates
- **Notification Logging** – PostgreSQL persistence for every attempt
- **SMS via Twilio** – Regex validation and retry logic
- **Retry Mechanism** – Failed messages are automatically retried (up to 3 times)
- **Validation & Error Handling** – Global exception handler with bean validation
- **Security Layer** – API key filter protecting all `/api/**` endpoints
- **Admin Dashboard** – Summary counts, paginated logs and CSV export
- **Observability & Docs** – Swagger UI and Spring Boot Actuator endpoints
- **Dockerization** – Compose file orchestrating app, RabbitMQ and PostgreSQL
- **Monitoring Dashboard** – Prometheus + Grafana metrics

---

## Quick Start
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

## API
- **POST `/api/notifications`** – send an email or SMS
- **GET `/api/dashboard/summary`** – overall stats
- **GET `/api/dashboard/logs`** – paginated logs (filter by type & status)
- **GET `/api/dashboard/export`** – download logs as CSV

Interactive documentation is available at `/swagger-ui.html`.

---

## Sample Payload
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

## Tech Stack
| Layer        | Tech                             |
|--------------|----------------------------------|
| Language     | Java 21                          |
| Framework    | Spring Boot 3.2.5                |
| Messaging    | RabbitMQ                         |
| DB           | PostgreSQL                       |
| Email        | Spring Mail + Thymeleaf          |
| SMS          | Twilio SDK                       |
| Retry        | RabbitMQ + Retry Queue Pattern   |
| Logging      | SLF4J + Lombok                   |
| Dev Tooling  | Docker, DBeaver, GitHub          |

---

## Folder Structure
```
src/
├── controller/
├── config/
├── dto/
├── entity/
├── repository/
├── service/
│   └── impl/        # Email + SMS logic
└── consumer/        # RabbitMQ listener
```

---

## Security & Best Practices
- SMTP credentials stored in `application-local.properties` (excluded via `.gitignore`)
- **No hardcoded secrets** in code
- Regex-based email/phone validation
- Exception-safe retry logic

---

## Environment Variables (`application-local.properties`)
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

## Docker Setup
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

## Next Milestones
- Scheduled/delayed notification support
- Frontend UI for the dashboard
- CI/CD pipeline for automated deployment

---

## 🧑‍💻 Author
Built with ❤️ by Varun

---
