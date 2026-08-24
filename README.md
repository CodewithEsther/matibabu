Matibabu

Matibabu is a modular healthcare information system for managing patient registration and clinical encounters. The project is being developed with a Java/Spring Boot backend and a Next.js web frontend, with the backend designed around Clean Architecture principles.

The system is designed to keep core healthcare domain logic independent from frameworks, databases, and transport layers, allowing the persistence and delivery mechanisms to evolve without rewriting the underlying domain.

«Status: Active development. Patient registration and retrieval are currently implemented. Additional clinical, authentication, and synchronization capabilities are planned.»

What is currently implemented

Patient Registry

- Register patients through a REST API.
- Retrieve individual patients by UUID.
- Validate and map API requests through DTOs.
- Persist patient data using JPA/Hibernate.
- Manage database schema changes through Flyway migrations.
- Support SQLite for local development.
- PostgreSQL is the target database for remote/production deployments.

Encounters

The core "Encounter" domain model and repository are in place. HTTP endpoints for encounters have not yet been exposed.

Roadmap

Planned functionality includes:

- Medical records and clinical data
- Encounter management APIs
- Authentication and authorization
- Offline/online synchronization
- DHIS2 integration
- Expanded patient management
- Production deployment and operational tooling

Tech Stack

Backend

- Java 25
- Spring Boot 4.1.0
- Spring Data JPA / Hibernate
- SQLite — local development
- PostgreSQL — target remote/production database
- Flyway — database migrations
- MapStruct — DTO/entity mapping
- UUID v7 — entity identifiers
- Maven — build and dependency management

Frontend

- Next.js 16 — App Router
- React 19
- TypeScript 5
- Tailwind CSS 4

Project Structure

matibabu/
├── backend/
│   ├── src/main/java/com/matibabu/backend/
│   │   ├── api/              # HTTP controllers and DTOs
│   │   ├── application/      # Use cases and application services
│   │   ├── config/           # Spring configuration and bean composition
│   │   ├── domain/           # Domain models and repository interfaces
│   │   └── infrastructure/   # Persistence adapters and JPA entities
│   │
│   └── src/main/resources/
│       ├── application*.properties
│       └── db/migration/     # Flyway database migrations
│
├── frontend/
│   ├── src/app/              # Next.js App Router
│   ├── src/components/       # Reusable UI components
│   ├── src/lib/api/          # Backend API clients
│   └── src/types/            # TypeScript types
│
├── database/                 # Local/shared database files
├── docs/
│   ├── architecture/         # Architecture documentation
│   └── decisions/            # Architecture Decision Records
│
└── README.md

Architecture

The backend follows a Clean Architecture approach. Dependencies point inward toward the domain rather than allowing the domain to depend on frameworks or infrastructure.

┌──────────────────────────────┐
│       API / Controllers      │
│       HTTP + DTOs            │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│      Application Layer       │
│    Use Cases + Services      │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│         Domain Layer         │
│ Models + Repository Ports    │
└──────────────▲───────────────┘
               │
               │
┌──────────────┴───────────────┐
│      Infrastructure          │
│ JPA + Database + Adapters    │
└──────────────────────────────┘

The domain layer has no direct knowledge of Spring, JPA, HTTP, or the database implementation.

Additional architectural documentation and ADRs are available under "docs/".

Getting Started

Prerequisites

- JDK 25
- Node.js 22+
- npm
- A web browser or HTTP client such as "curl" or Postman

The Maven wrapper is included in the repository.

Start the Backend

cd backend
./mvnw spring-boot:run

The backend starts on:

http://localhost:8080

Start the Frontend

Start the backend first, then:

cd frontend
npm install
npm run dev

The frontend starts on:

http://localhost:3000

Configuration

The default active Spring profile is "local".

The local configuration:

- Uses SQLite for development.
- Stores the local database in "./matibabu-local.db".
- Runs Flyway migrations automatically.
- Enables SQL logging for development.

For a remote/production environment, configure the "prod" profile with the appropriate PostgreSQL connection details.

API

Base URL:

http://localhost:8080

Patient Registry

Method| Endpoint| Status| Description
"POST"| "/api/patients"| ✅ Implemented| Register a patient
"GET"| "/api/patients/{id}"| ✅ Implemented| Retrieve a patient by UUID
"GET"| "/api/patients?page=0&size=20"| 🚧 Planned| List patients
"PUT"| "/api/patients/{id}"| 🚧 Planned| Update a patient
"DELETE"| "/api/patients/{id}"| 🚧 Planned| Delete/archive a patient

Register a Patient

curl -X POST http://localhost:8080/api/patients \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Kamau",
    "dateOfBirth": "1995-06-15",
    "phoneNumber": "+254712345678",
    "gender": "MALE"
  }'

Example response:

{
  "id": "019185c3-...",
  "firstName": "John",
  "lastName": "Kamau",
  "dateOfBirth": "1995-06-15",
  "phoneNumber": "+254712345678",
  "createdAt": "2026-08-22T04:20:49.743Z",
  "gender": "MALE"
}

Retrieve a Patient

curl http://localhost:8080/api/patients/{id}

The endpoint returns "200 OK" when the patient exists and "404 Not Found" when the supplied UUID cannot be found.

Gender Values

The API currently accepts:

- "MALE"
- "FEMALE"
- "OTHER"
- "UNKNOWN"

Database Migrations

Flyway migrations are located at:

backend/src/main/resources/db/migration/

Current migrations include:

V20260817090000__create_patients_table.sql
V20260817120000__create_encounters_table.sql
V20260821100000__add_patient_details.sql
V20260821110000__add_encounter_created_at.sql

Migrations are applied automatically when the application starts.

Frontend Development Notes

The frontend currently consumes only the API functionality that has been implemented.

Important API/domain considerations:

1. The current API uses "gender" and "phoneNumber".
2. Some fields present in the database schema are not yet exposed through the patient API.
3. The "Encounter" domain model exists, but there are currently no HTTP endpoints for encounters.
4. The local backend permits requests from "http://localhost:3000".
5. If the frontend origin changes, update the CORS configuration in:

backend/src/main/java/com/matibabu/backend/config/WebConfiguration.java

6. Authentication and authorization have not yet been implemented.

Testing

Run the backend test suite with:

cd backend
./mvnw test

The application can also be packaged with:

./mvnw package

Build Verification

The application has been verified locally by packaging and running the generated JAR:

cd backend
./mvnw package -DskipTests
java -jar target/backend-0.0.1-SNAPSHOT.jar

Development Status

Matibabu is an active software-engineering project. The current implementation focuses on establishing the backend architecture, persistence layer, database migrations, and initial patient-registry functionality before expanding into additional healthcare modules.

The project intentionally documents functionality that is not yet implemented rather than presenting planned features as completed.

License

See ""LICENSE"" (LICENSE) for licensing information.