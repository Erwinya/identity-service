# Identity Service

JWT-based identity and user management REST API built with Spring Boot 3, PostgreSQL, and layered architecture.

## Features

- User registration and login with **JWT** (Bearer tokens)
- Password hashing with **BCrypt**
- Request validation and centralized exception handling
- OpenAPI / Swagger UI documentation
- Docker Compose stack (PostgreSQL + application)
- Unit and controller tests

## Tech stack

| Layer | Technology |
|-------|------------|
| Runtime | Java 17 |
| Framework | Spring Boot 3.5 |
| Security | Spring Security + JJWT |
| Persistence | Spring Data JPA + PostgreSQL |
| Docs | springdoc-openapi |
| Build | Maven |

## API overview

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `POST` | `/api/v1/auth/register` | No | Create account, return JWT |
| `POST` | `/api/v1/auth/login` | No | Authenticate, return JWT |
| `GET` | `/api/v1/users/me` | Yes | Current user profile |
| `GET` | `/api/v1/users/{username}` | Yes | Lookup user by username |
| `GET` | `/actuator/health` | No | Liveness/readiness probe |

Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

Health: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)

## Quick start (Docker)

```bash
docker compose up --build
```

API listens on `http://localhost:8080`.

## Local development

### Prerequisites

- Java 17+
- Maven 3.9+ (or use `./mvnw`)

**No database installed?** Use the in-memory H2 profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

API listens on `http://localhost:8080` with an ephemeral H2 database (data is lost on shutdown).

**With PostgreSQL:**

```bash
docker compose up postgres -d
./mvnw spring-boot:run
```

### Configuration

Environment variables (optional; defaults work for local Docker):

| Variable | Default | Purpose |
|----------|---------|---------|
| `DB_URL` | `jdbc:postgresql://localhost:5432/identity_db` | JDBC URL |
| `DB_USERNAME` | `identity` | DB user |
| `DB_PASSWORD` | `identity` | DB password |
| `JWT_SECRET` | (dev placeholder) | Signing secret — **change in production** |
| `JWT_EXPIRATION_MS` | `86400000` | Token TTL (24h) |

## Example requests

### Register

```bash
curl -s -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d "{
    \"username\": \"haluk\",
    \"email\": \"haluk@example.com\",
    \"password\": \"Secret123!\",
    \"firstName\": \"Haluk\",
    \"lastName\": \"Kilincer\"
  }"
```

### Login

```bash
curl -s -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"haluk\",\"password\":\"Secret123!\"}"
```

### Current user

```bash
curl -s http://localhost:8080/api/v1/users/me \
  -H "Authorization: Bearer <accessToken>"
```

## Project structure

```
src/main/java/com/halukkilincer/identity/
├── config/          # Security & OpenAPI
├── controller/      # REST endpoints
├── dto/             # Request/response records
├── entity/          # JPA entities
├── exception/       # Domain errors + global handler
├── mapper/          # Entity ↔ DTO mapping
├── repository/      # Spring Data repositories
├── response/        # Envelope response type
├── security/        # JWT filter, UserDetails, JwtService
└── service/         # Business logic
```

## Tests

```bash
./mvnw test
```

## License

MIT
