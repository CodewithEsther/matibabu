# ADR-03 — Matibabu Backend Architecture and Implementation Progress

* **Status:** Accepted
* **Date:** 2026-08-26
* **Decision:** Adopt and continue with the established layered/clean architecture for the Matibabu Offline-First EMR Lite backend.

## Context

The Matibabu Offline-First EMR Lite backend is being developed as a multi-developer Spring Boot application. The backend is organized around clear separation of domain, application, infrastructure, and API responsibilities.

The project requires support for patients, clinical encounters, and medical records while maintaining business rules independently from HTTP and persistence concerns.

During implementation, the following architectural and technical decisions have been established:

* Domain entities contain business rules and state transitions.
* Application services coordinate use cases and repository access.
* Infrastructure adapters handle JPA/database persistence.
* API controllers expose HTTP endpoints.
* DTOs are used at the API boundary.
* MapStruct is used to map between domain objects and persistence entities.
* Repository interfaces are defined against domain/application needs, with infrastructure adapters implementing them.
* Flyway is used for database schema migrations.
* SQLite is currently used for the local/offline database.
* Automated tests use an in-memory database where appropriate rather than relying on the developer's local persistent database.
* UUIDv7/time-ordered UUIDs are used for entity identifiers.
* Apidog is used to manually test HTTP endpoints and verify API behavior.

## Decision

The backend will continue using the following structure:

```text
src/main/java/com/matibabu/backend/

├── api/
│   ├── patient/
│   ├── encounter/
│   ├── medicalrecord/
│   └── exception/
│
├── application/
│   ├── patient/
│   ├── encounter/
│   └── medicalrecord/
│
├── domain/
│   ├── patient/
│   ├── encounter/
│   └── medicalrecord/
│
└── infrastructure/
    └── persistence/
        ├── patient/
        ├── encounter/
        └── medicalrecord/
```

### Domain layer

The domain owns business rules.

For example, an `Encounter` controls its own lifecycle:

```text
ACTIVE
  ├──→ DISCHARGED
  └──→ CANCELLED
```

An encounter that is no longer active cannot be discharged or cancelled again.

The domain therefore performs checks such as:

```text
ensureActive()
```

and throws domain-specific exceptions such as:

```text
EncounterNotActiveException
```

The domain does not depend on Spring MVC, HTTP status codes, or JPA.

### Application layer

The application layer implements use cases and coordinates domain objects with repositories.

Examples include:

* Starting an encounter
* Discharging an encounter
* Cancelling an encounter
* Creating a medical record
* Retrieving a medical record
* Registering a patient
* Searching for a patient

Application-level exceptions represent failures such as an entity required by a use case not existing.

For example:

```text
EncounterNotFoundException
PatientNotFoundException
MedicalRecordNotFoundException
```

### Infrastructure layer

Infrastructure implements persistence concerns.

The project uses repository adapters such as:

```text
PatientRepositoryAdapter
EncounterRepositoryAdapter
MedicalRecordsRepositoryAdapter
```

Spring Data repositories are kept behind these adapters.

MapStruct maps persistence entities to domain objects and vice versa.

### API layer

The API exposes REST endpoints and translates application/domain outcomes into HTTP responses.

The `GlobalExceptionHandler` is responsible for mapping exceptions to appropriate HTTP statuses.

Examples:

| Failure                              |       HTTP status |
| ------------------------------------ | ----------------: |
| Resource not found                   |   `404 Not Found` |
| Duplicate resource/unique constraint |    `409 Conflict` |
| Invalid request                      | `400 Bad Request` |
| Invalid domain state transition      |    `409 Conflict` |

This prevents HTTP-specific concerns from leaking into the domain model.

## Implemented functionality

### Patient management

Patient functionality has been implemented and manually tested through the API.

Implemented capabilities include:

* Patient registration
* Patient retrieval
* Patient lookup by phone number
* Patient validation
* Duplicate phone-number handling
* Patient persistence
* Patient pagination and related repository functionality

Phone-number searching accounts for supported Kenyan local and international formats.

### Encounter management

Encounter lifecycle functionality has been implemented.

Implemented capabilities include:

* Starting an encounter
* Retrieving an encounter
* Discharging an encounter
* Cancelling an encounter
* Preventing invalid lifecycle transitions
* Persisting encounter state
* Validating encounter ownership when creating a medical record

The encounter is associated with a patient through `patientId`.

### Medical records

Medical records have been wired to encounters.

A medical record contains:

* Its own identifier
* `patientId`
* `encounterId`
* `createdAt`
* Clinical information collections such as vitals, observations, diagnoses, and treatments

The application verifies that:

1. The encounter exists.
2. The encounter belongs to the specified patient.
3. The medical record is then created with both patient and encounter relationships.

A unique constraint on `encounter_id` enforces the current rule that an encounter has at most one medical record.

### Database migrations

Flyway migrations have been established, including:

```text
V1__create_patients_table.sql
V2__create_encounters_table.sql
V3__add_patient_details.sql
V4__add_encounter_created_at.sql
V5__rename_residence_to_address.sql
V6__create_medicalrecords_table.sql
```

Schema changes are therefore version-controlled rather than being managed exclusively through Hibernate schema generation.

### Testing

The project has automated tests covering domain, application, and persistence behavior.

Tests use an isolated/in-memory database configuration where database integration testing is required.

The test suite has reached a passing state after updating tests to reflect the introduction of domain-specific exceptions.

### API testing

Apidog is being used to manually verify REST behavior.

The following flows have been tested:

```text
Patient
  ↓
Create
  ↓
Get

Patient
  ↓
Start Encounter
  ↓
Get Encounter
  ↓
Discharge
  ↓
Attempt second discharge → rejected

Patient
  ↓
Start Encounter
  ↓
Cancel
  ↓
Attempt second cancellation → rejected

Encounter
  ↓
Create Medical Record
  ↓
Get Medical Record
```

Invalid lifecycle operations correctly follow the domain business rules.

## Exception-handling decision

Business rules remain inside the domain.

For example:

```text
Encounter
    ↓
ensureActive()
    ↓
EncounterNotActiveException
```

The API layer translates this into:

```text
409 Conflict
```

Application services continue to handle resource existence:

```text
Repository
    ↓
empty Optional
    ↓
EncounterNotFoundException
```

The API layer translates this into:

```text
404 Not Found
```

This separation preserves the independence of the domain from Spring and HTTP concerns.

## Current status

The core patient and encounter flows are operational. Medical records have been connected to encounters and tested through the API. Domain business rules are enforced, and exception handling is being refined to expose appropriate HTTP responses.

The architecture will be extended incrementally as additional EMR features are implemented.
