# Matibabu

Matibabu is a modular healthcare information system. This repository contains the backend infrastructure and will also host the web frontend.

The backend is designed around clean architecture principles: domain logic is kept independent of frameworks, databases, and transport layers so the system can evolve from a local SQLite setup to a networked PostgreSQL deployment without rewriting the core healthcare concepts.

## What is being built

- **Patient registry** — register and look up patients.
- **Encounters** — episodes of care tied to a patient (domain model exists; HTTP API is not exposed yet).
- **Future modules** — medical records, DHIS2 integration, offline/online synchronization, authentication, and authorization.

## Tech stack

### Backend

- Java 25
- Spring Boot 4.1.0
- Spring Data JPA + Hibernate
- SQLite for local development
- PostgreSQL for production/remote deployment
- Flyway for database migrations
- MapStruct for object mapping
- UUID v7 for entity identifiers
- Maven (wrapper included)

### Frontend

Not implemented yet. A web frontend will be added to this repository and consume the backend REST API at `http://localhost:8080/api`.

## Project layout

```text
matibabu/
├── backend/          # Spring Boot application
│   ├── src/main/java/com/matibabu/backend/
│   │   ├── api/          # HTTP controllers and DTOs
│   │   ├── application/  # Use cases and application services
│   │   ├── config/       # Spring bean composition
│   │   ├── domain/       # Domain models and repository interfaces
│   │   └── infrastructure/   # Persistence adapters, JPA entities
│   └── src/main/resources/
│       ├── application*.properties
│       └── db/migration/ # Flyway migrations
├── database/         # Shared/local database files
├── docs/             # Architecture docs and ADRs
└── README.md
```

## Prerequisites

- JDK 25 (the Maven wrapper will use it automatically)
- A web browser or HTTP client (curl, Postman, etc.) for testing the API

## Start the backend

```bash
cd backend
./mvnw spring-boot:run
```

The application starts on `http://localhost:8080`.

I verified this works by packaging and running the application locally:

```bash
cd backend
./mvnw package -DskipTests
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

### Configuration

The active profile is `local` by default (`spring.profiles.active=local`).

`backend/src/main/resources/application-local.properties`:

- Uses a local SQLite file at `./matibabu-local.db`
- Runs Flyway migrations automatically
- Prints SQL to the console (`spring.jpa.show-sql=true`)

For production, create or switch to the `prod` profile and provide PostgreSQL credentials.

## API overview

Base URL: `http://localhost:8080`

### Patients

| Method | Endpoint | Status | Description |
|--------|----------|--------|-------------|
| POST | `/api/patients` | ✅ Implemented | Register a new patient |
| GET | `/api/patients/{id}` | ✅ Implemented | Get a patient by UUID |
| GET | `/api/patients?page=0&size=20` | ❌ Not implemented | List patients |
| PUT | `/api/patients/{id}` | ❌ Not implemented | Update a patient |
| DELETE | `/api/patients/{id}` | ❌ Not implemented | Delete/archive a patient |

### Register a patient

```bash
curl -X POST http://localhost:8080/api/patients \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Kamau",
    "dateOfBirth": "1995-06-15",
    "phoneNumber": "+254712345678",
    "gender": "MALE"
  }'
```

Response (`201 Created`):

```json
{
  "id": "019185c3-...",
  "firstName": "John",
  "lastName": "Kamau",
  "dateOfBirth": "1995-06-15",
  "phoneNumber": "+254712345678",
  "createdAt": "2026-08-22T04:20:49.743Z",
  "gender": "MALE"
}
```

### Get a patient

```bash
curl http://localhost:8080/api/patients/{id}
```

Returns `200 OK` with the patient, or `404 Not Found` if the ID does not exist.

### Gender values

Valid values for `gender`:

- `MALE`
- `FEMALE`
- `OTHER`
- `UNKNOWN`

## Important notes for frontend development

1. **Field naming** — the current API uses `gender` and `phoneNumber`. The `docs/api/patient-registry.md` document mentions `sex` and `address`, but those are not exposed in the current implementation.
2. **Database vs API fields** — the database migrations include additional patient fields (`national_id`, `medical_record_number`, `email`, `residence`, emergency contacts, blood group, insurance, `is_active`, `updated_at`), but these are not yet available through the API.
3. **Encounters** — the `Encounter` domain model and repository exist, but there is no HTTP controller yet. Do not build UI screens around encounters until the API endpoints are added.
4. **CORS** — if the frontend runs on a separate dev server (e.g., Vite on `localhost:5173`), you may need to configure CORS in the backend. Ask before implementing; the project does not have CORS configured yet.
5. **No authentication** — the API is currently open. Auth will be added later.

## Run tests

```bash
cd backend
./mvnw test
```

## Database migrations

Migrations live in `backend/src/main/resources/db/migration/` and run automatically on startup.

Current migrations:

- `V20260817090000__create_patients_table.sql`
- `V20260817120000__create_encounters_table.sql`
- `V20260821100000__add_patient_details.sql`
- `V20260821110000__add_encounter_created_at.sql`

## Architecture

The backend follows a layered/clean architecture:

```text
API (controllers, DTOs)
    ↓
Application (use cases, services)
    ↓
Domain (entities, value objects, repository interfaces)
    ↑
Infrastructure (JPA, adapters, external APIs)
```

Dependencies point toward the domain. The domain has no knowledge of Spring, JPA, HTTP, or the database.

See `docs/architecture/` and `docs/decisions/` for the full architecture overview and ADRs.

## License

See `LICENSE`.
