# Call Monitoring Backend

REST API for Call Monitoring, built with **Java Spring Boot**, **PostgreSQL**, and **JWT** authentication.

## Tech Stack

| Layer | Technology |
|---|---|
| Runtime | Java 21 |
| Framework | Spring Boot 4.1 |
| Database | PostgreSQL |
| Migration | Flyway |
| Auth | Spring Security + JWT (Bearer token) |
| Build | Maven Wrapper (`mvnw`) |

## Prerequisites

- Java 21+
- PostgreSQL running locally
- (Optional) curl / Postman for API calls

## Database Setup

1. Create the database:

```sql
CREATE DATABASE call_monitoring;
```

2. Update credentials in `src/main/resources/application.yaml` if needed:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/call_monitoring
    username: postgres
    password: postgres
```

Flyway runs migrations automatically on startup (`V1`–`V4`), including call seed data.

## How to Run

From the `back-end` folder:

```bat
.\mvnw.cmd spring-boot:run
```

macOS / Linux:

```bash
./mvnw spring-boot:run
```

App starts at: `http://localhost:8080`

Health check: `GET http://localhost:8080/actuator/health`

### Default seeded user

On first boot, a default supervisor is created:

| Field | Value |
|---|---|
| username | `supervisor` |
| password | `password123` |

## Auth Method & Setup

Authentication uses **JWT Bearer tokens**.

1. **Register** (public):

```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "newuser",
  "password": "password123",
  "full_name": "New User",
  "email": "newuser@example.com",
  "phone_number": "08123456789"
}
```

2. **Login** (public):

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "supervisor",
  "password": "password123"
}
```

Response includes `token`.

3. **Call protected APIs** with header:

```http
Authorization: Bearer <token>
```

### Protected endpoints (require Bearer token)

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/call-monitoring` | List call records (search, filter, sort, pagination) |
| `GET` | `/api/users` | List all users |

### Example: list call records

```http
GET /api/call-monitoring?search=andi&page=0&size=5&sort_by=call_timestamp&sort_order=desc
Authorization: Bearer <token>
```

### Example: list users

```http
GET /api/users
Authorization: Bearer <token>
```

Public routes (`/api/auth/**`, `/actuator/health`) do not require a token. Everything else requires a valid JWT.

## Unit Tests

| Test class | Type | What it covers |
|---|---|---|
| `AuthServiceTest` | Unit (Mockito) | Login success / wrong password |
| `CallRecordServiceTest` | Unit (Mockito) | Mapping, paging/sort defaults, date validation |
| `CallRecordSpecificationTest` | Integration (`@DataJpaTest` + H2) | Search, period filter, sentiment filter, combined specs |

### Run all tests

```bat
.\mvnw.cmd test
```

### Run specific tests

```bat
.\mvnw.cmd "-Dtest=AuthServiceTest,CallRecordServiceTest,CallRecordSpecificationTest" test
```

## Project Structure (high level)

```
src/main/java/com/ardhian/callmonitoring/
├── auth/                 # register, login, users
├── callrecord/           # call monitoring API
├── common/api/           # ApiResponse + exception handler
├── config/               # data seeder
└── security/             # JWT filter + SecurityConfig

src/main/resources/db/migration/
├── V1__init.sql
├── V2__create_users.sql
├── V3__seed_call_data.sql
└── V4__add_user_email_phone.sql
```

## AI Usage

AI tools were used as a learning / scaffolding assistant. Final code was reviewed, adapted, and verified manually against PostgreSQL and unit tests.

### AI tool used

- **Claude.ai**

### Parts of work assisted by AI

| Area | AI involvement | Remark |
|---|---|---|
| **How to setup** | Step-by-step guidance to bootstrap Spring Boot + PostgreSQL project structure, dependencies, and Flyway mindset | Used to map a JS-centric workflow into Java/Spring conventions; setup steps were validated by running the app locally |
| **Auth method** | Explanation of JWT auth flow in Spring Security, compared to familiar JavaScript patterns (middleware / Express) | Used for learning register/login/filter layering; security config and token flow were re-checked by calling login + protected endpoints |
| **Unit test** | Generated draft test cases for `CallRecordService` and `CallRecordSpecification` | Tests were reviewed, adjusted to project packages/APIs, and executed with Maven (`.\mvnw.cmd test`) |

### Example prompts used

1. **Project setup**
   > I have task to create backend using java spring boot and postgre sql, explain me step by step to create that.

2. **Auth (JS → Java)**
   > First, i want to create user auth. tell me how to do that in java step by step. im proficient using javascript before. tell me how to do it in java, so i can understand that.

3. **Unit tests**
   > Generate test case for call record servce and call record spesification.

### How AI output was verified

- Ran the application with PostgreSQL and confirmed Flyway migrations + seed data.
- Verified auth manually: register/login → use Bearer token → call protected APIs.
- Executed unit/integration tests with Maven and fixed failures (e.g. Postgres `lower(smallint)` search bug, sort field naming, response DTO shape).
- Read and understood generated code before keeping it; adjusted naming, response contracts (`ApiResponse` + snake_case), and business rules (3-month period limit, sentiment filters, allowlisted sort fields).
