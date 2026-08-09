# CIMB Call Monitoring — Take-Home Test

Call monitoring web app for supervisors: authenticate, browse call records (search / filter / sort / paginate), and manage users.

## For reviewers

**Please open `README.md` on each feature branch** for full setup steps, API details, and AI-usage notes:

| Branch | What it contains | README |
| --- | --- | --- |
| [`back-end`](../../tree/back-end) | Spring Boot REST API + PostgreSQL + JWT + Flyway + tests | [`README.md`](../../blob/back-end/README.md) |
| [`front-end`](../../tree/front-end) | Vue 3 SPA (auth, call monitoring, users) | [`README.md`](../../blob/front-end/README.md) |
| [`postman-collection`](../../tree/postman-collection) | Postman collection for API smoke tests | [`README.md`](../../blob/postman-collection/README.md) |

This `main` README is the **project overview** only. Branch READMEs are the source of truth for running and reviewing each part.

---

## Project overview (what already works)

### Backend (`back-end`)

- JWT auth: register, login, Bearer-protected routes
- Call monitoring API: list with search, sentiment filter, date range, sort, pagination
- Users API: list users
- Flyway migrations (`V1`–`V4`) + seed call data
- Default supervisor user seeded on boot
- Unit / integration tests (Auth, CallRecord service, specifications)
- Actuator health endpoint

### Frontend (`front-end`)

- Login / register screens wired to the API
- Dashboard: Call Monitoring + Users
- Search, filter, sort; table / list / grid views
- Desktop pagination; mobile infinite scroll
- Light / dark theme
- JWT attached via Axios interceptor

### Postman (`postman-collection`)

- Ready requests for auth, call records, and users

---

## Tech stack

| Area | Stack |
| --- | --- |
| Backend | Java 21, Spring Boot 4.1, Spring Security, JWT (jjwt), Spring Data JPA, Flyway, PostgreSQL 16, Maven Wrapper |
| Frontend | Vue 3, Vite 8, Pinia, Vue Router, Axios, Tailwind CSS 4, DaisyUI, Heroicons, Vitest |
| Infra / tools | Docker Compose (Postgres), Postman |
| Tests | Backend: JUnit / Mockito / `@DataJpaTest` (H2) · Frontend: Vitest |

---

## How to run (quick start)

Run **Postgres → backend → frontend** (and optionally Postman).

### 1. Database (PostgreSQL)

From the `back-end` branch / folder:

```bash
docker compose up -d
```

Defaults: `localhost:5432`, database `call_monitoring`, user/password `postgres` / `postgres`.

### 2. Backend

Requires **Java 21+**.

```bash
# Windows
.\mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

- API: `http://localhost:8080`
- Health: `GET http://localhost:8080/actuator/health`

| Seeded user | Password |
| --- | --- |
| `supervisor` | `password123` |

More detail (endpoints, auth flow, tests): see **`README.md` on `back-end`**.

### 3. Frontend

Requires **Node.js 18+** (20+ recommended).

```bash
npm install
npm run dev
```

- App: `http://localhost:5173`
- Proxies `/api` → `http://localhost:8080` (see frontend README for `.env`)

More detail (features, structure, AI notes): see **`README.md` on `front-end`**.

### 4. Postman (optional)

Import the collection from the `postman-collection` branch, set `baseUrl` to `http://localhost:8080`, login, then call protected endpoints with the returned token.

---

## Suggested review order

1. Read this overview
2. Open **`back-end`** → run API + tests → skim `README.md`
3. Open **`front-end`** → run UI against the API → skim `README.md`
4. (Optional) Import **`postman-collection`** and hit the endpoints
