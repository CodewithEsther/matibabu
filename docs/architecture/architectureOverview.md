# Matibabu Backend Architecture Overview

## 1. Purpose

Matibabu is being developed as a modular healthcare backend with a strong separation between:

* Domain logic
* Application/use cases
* HTTP/API concerns
* Infrastructure and persistence
* External integrations
* Security
* Synchronization

The architecture is designed to support multiple backend developers working in parallel without allowing infrastructure or framework concerns to leak into the core domain.

The system should remain adaptable to:

* Local SQLite development
* Remote PostgreSQL deployment
* DHIS2 integration
* Offline/online synchronization
* Authentication and authorization
* Future clinical modules

---

## 2. Architectural Style

The backend follows a Clean Architecture / layered architecture with feature-oriented packaging.

The primary dependency direction is:

```text
API
 │
 ▼
Application
 │
 ▼
Domain
 ▲
 │
Infrastructure
```

The important rule is that dependencies should point toward the core.

The domain must not depend on:

* Spring
* Spring Data
* JPA
* Hibernate
* SQLite
* PostgreSQL
* HTTP
* JSON
* DHIS2
* Authentication frameworks

---

## 3. Package Structure

The current structure is:

```text
com.matibabu.backend
│
├── api/
│   ├── exception/
│   │   └── GlobalExceptionHandler
│   │
│   └── patient/
│       ├── PatientController
│       ├── PatientResponse
│       └── RegisterPatientRequest
│
├── application/
│   └── patient/
│       ├── RegisterPatientUseCase
│       ├── RegisterPatientService
│       ├── GetPatientUseCase
│       ├── GetPatientService
│       └── PatientNotFoundException
│
├── config/
│   └── PatientConfiguration
│
├── domain/
│   └── patient/
│       ├── Patient
│       ├── Gender
│       └── PatientRepository
│
└── infrastructure/
    └── persistence/
        └── patient/
            ├── PatientEntity
            ├── PatientMapper
            ├── SpringDataPatientRepository
            └── PatientRepositoryAdapter
```

This structure is feature-oriented while maintaining architectural boundaries.

---

## 4. Domain Layer

The domain represents healthcare concepts and business rules.

For example:

```text
domain/patient/
```

contains the `Patient` aggregate/entity and the `PatientRepository` abstraction.

The domain should not know how patients are stored.

For example, the domain defines:

```java
Optional<Patient> findById(UUID id);
```

but does not know whether the implementation uses:

* SQLite
* PostgreSQL
* MongoDB
* an external service
* an in-memory repository

---

## 5. Application Layer

The application layer contains use cases.

Examples:

```text
RegisterPatientUseCase
GetPatientUseCase
```

Application services orchestrate domain operations and repository abstractions.

They should not contain HTTP-specific logic.

For example:

```text
GetPatientService
        │
        ▼
PatientRepository
```

The application layer should remain independent of Spring wherever practical.

---

## 6. API Layer

The API layer translates HTTP requests into application use cases and application results into HTTP responses.

Patient API components include:

```text
PatientController
RegisterPatientRequest
PatientResponse
```

The domain `Patient` should not be exposed directly as an HTTP response.

Instead:

```text
Patient
   ↓
PatientResponse
   ↓
JSON
```

This prevents the external API contract from becoming coupled to the internal domain model.

---

## 7. API Exception Handling

Cross-cutting HTTP exception handling belongs under:

```text
api/exception/
```

For example:

```text
PatientNotFoundException
        ↓
GlobalExceptionHandler
        ↓
HTTP 404
```

`PatientNotFoundException` remains an application-level exception.

`GlobalExceptionHandler` is responsible for translating it into an HTTP response.

---

## 8. Infrastructure Layer

Infrastructure contains implementations of abstractions defined by the core.

For persistence:

```text
PatientRepository
        ▲
        │
PatientRepositoryAdapter
        │
        ▼
Spring Data / Hibernate
        │
        ▼
Database
```

The infrastructure layer may depend on:

* Spring
* JPA
* Hibernate
* JDBC
* database drivers
* external APIs

The domain and application layers should not depend on those technologies.

---

## 9. Dependency Injection and Configuration

Application services are intentionally kept free of Spring annotations where possible.

Spring composition is handled through:

```text
config/
```

For example:

```text
PatientConfiguration
        │
        ├── RegisterPatientService
        │
        └── GetPatientService
```

This creates a composition root where framework-specific dependency wiring occurs.

---

## 10. Identity

Patients use UUID v7 identifiers.

The identifier is generated when a new domain object is created.

UUID v7 was selected because it provides globally unique identifiers while also incorporating time-ordered characteristics, making it more appropriate for database workloads than completely random UUID versions.

The identifier is part of the domain identity and is persisted by the infrastructure layer.

---

## 11. Persistence

The current development database is SQLite.

The architecture is being designed so that the application is not coupled to SQLite.

The intended production/remote database can therefore be changed without rewriting the domain or application layers.

Current conceptual structure:

```text
Application
     ↓
PatientRepository
     ↓
Persistence Adapter
     ↓
SQLite
```

Future:

```text
Application
     ↓
PatientRepository
     ↓
Persistence Adapter
     ↓
PostgreSQL
```

---

## 12. Core Healthcare Concepts

The system contains several important domain areas:

```text
Patient
Encounter
Medical Record
Security
Synchronization
DHIS2 Integration
```

### Patient

Represents the person receiving care.

Patient identity information should be relatively stable.

### Encounter

Represents a specific episode of care.

An encounter begins when a patient presents for care and ends when that episode is discharged/closed.

Conceptually:

```text
Patient
  │
  ├── Encounter 1
  ├── Encounter 2
  └── Encounter 3
```

An encounter is therefore not merely a property of the patient. It is a first-class domain concept.

Detailed encounter states and lifecycle rules require further domain design before implementation.

### Medical Records

Clinical information should be associated with the appropriate clinical context, often through an encounter.

Medical records should not turn the `Patient` or `Encounter` domain object into an enormous aggregate.

### Synchronization

Synchronization is responsible for moving data between local and remote contexts and handling issues such as:

* retries
* conflicts
* idempotency
* ordering
* partial connectivity

### DHIS2

DHIS2 integration is an external-system concern.

DHIS2-specific models and communication should remain outside the core domain.

---

## 13. API Design Principle

Each feature should expose explicit API contracts.

For example:

```text
POST /api/patients
GET  /api/patients/{id}
```

Requests and responses should use DTOs.

Domain objects should not be directly exposed as API contracts.

---

## 14. Testing Strategy

Testing is a first-class concern.

The system should eventually contain:

```text
Unit tests
Integration tests
Repository tests
API tests
Security tests
Synchronization tests
External integration tests
```

The testing strategy should be established centrally so feature developers can implement against consistent definitions of correctness.

---

## 15. Architectural Principle

The most important architectural rule for the project is:

> Business concepts should not become dependent on implementation technology.

Technology can change.

The domain should remain stable.

```text
SQLite → PostgreSQL
Spring implementation → another implementation
HTTP → another interface
DHIS2 API → another integration mechanism
```

None of these changes should require redesigning the core healthcare domain.
