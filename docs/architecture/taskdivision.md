# Matibabu Backend Team Delegation and Ownership

## 1. Purpose

The backend currently has eight developers with separate technical responsibilities.

The purpose of this document is to establish:

* Ownership
* Boundaries
* Responsibilities
* Collaboration points
* Architectural constraints
* Definition of done

The architecture owner is responsible for maintaining consistency across all areas.

---

## 2. Ownership

| Developer | Primary Responsibility     |
|  | -------------------------- |
| Fidel       | Architecture / Integration |
| Mburia      | Patient Update             |
| Jak         | Security                   |
| Chaser      | Database                   |
| Esther      | Medical Records            |
| Rael        | Testing                    |
| Brian       | DHIS2 Integration          |
| Clement     | Synchronization            |

Encounter is currently an identified architectural gap and requires explicit ownership before implementation begins.

---

## 3. Fidel — Architecture

### Primary responsibility

Maintain the overall architecture and ensure that individual feature implementations remain compatible.

### Responsibilities

* Define package conventions
* Define architectural boundaries
* Review cross-module dependencies
* Define domain boundaries
* Review interfaces between modules
* Maintain ADRs
* Resolve architectural conflicts
* Coordinate integration between developers
* Review major pull requests
* Establish shared API conventions
* Establish common error-handling conventions

### Should not become

The architecture owner should not become the implementation bottleneck.

The goal is to establish patterns that other developers can independently follow.

---

## 4. Mburia — Patient Update

### Responsibility

Implement modification of existing patient information.

Likely scope:

```text
PATCH /api/patients/{id}
```

or another agreed update endpoint.

### Expected layers

```text
api/patient/
application/patient/
domain/patient/
infrastructure/persistence/patient/
```

### Responsibilities

* Update request DTO
* Update use case
* Update application service
* Patient update behavior
* Validation
* Not-found handling
* API integration
* Tests for the feature

### Must coordinate with

* Architecture
* Testing
* Security

---

## 5. Jack — Security

### Responsibility

Authentication and authorization.

### Expected scope

* Authentication
* Authorization
* Roles
* Permissions
* Security configuration
* Protected endpoints
* Token/session strategy
* Password handling where applicable

### Architectural constraint

Security should not leak into domain entities unnecessarily.

Avoid putting framework-specific security annotations throughout the domain.

### Must coordinate with

* Architecture
* API developers
* Testing
* Sync if synchronization requires authentication

---

## 6. Chaser — Database

### Responsibility

Persistence infrastructure and database operations.

### Scope

* PostgreSQL environment
* Database configuration
* Schema management
* Migrations
* Indexes
* Database constraints
* Connection configuration
* Local/remote database environments
* Dockerized database infrastructure where appropriate

### Architectural constraint

Database requirements must not drive inappropriate changes to the domain model.

The database implementation belongs in infrastructure.

---

## 7. Esther — Medical Records

### Responsibility

Medical record domain and related use cases.

### Expected structure

```text
domain/medicalrecord/
application/medicalrecord/
api/medicalrecord/
infrastructure/persistence/medicalrecord/
```

### Important architectural constraint

Medical records must be designed with Encounter context in mind.

Avoid attaching all medical information directly to `Patient`.

---

## 8. Rael — Testing

### Responsibility

Own the project's testing strategy.

### Scope

* Unit tests
* Application service tests
* Repository integration tests
* API tests
* Security tests
* Regression tests
* Test fixtures
* Test utilities
* Integration-test infrastructure

### Important principle

Testing should happen alongside feature development rather than after development has finished.

Developers should provide tests for their own features while Developer 6 establishes the overall testing strategy and shared infrastructure.

---

## 9. Brian — DHIS2

### Responsibility

External DHIS2 integration.

### Scope

* DHIS2 client
* Authentication
* External API communication
* Request/response mapping
* External DTOs
* Error handling
* Retry behavior where appropriate
* DHIS2 synchronization contracts

### Architectural constraint

DHIS2-specific models should not become the Matibabu domain model.

Use an integration boundary:

```text
Matibabu Domain
       ↓
DHIS2 Integration Layer
       ↓
DHIS2 API
```

---

## 10. Clement — Synchronization

### Responsibility

Data synchronization between local and remote environments.

### Scope

* Sync engine
* Outbound synchronization
* Inbound synchronization
* Retry mechanisms
* Idempotency
* Conflict detection
* Conflict resolution strategy
* Sync state
* Ordering
* Offline/online transitions

### Architectural constraint

Synchronization must not be tightly coupled to a single database implementation.

The synchronization design should work whether the local persistence implementation is SQLite or another local store.

---

## 11. Encounter Ownership

Encounter has been identified as a missing responsibility in the initial delegation.

Encounter should be treated as a first-class domain concept:

```text
Patient
   │
   └── Encounter
```

An Encounter represents an episode of care from presentation/admission through discharge/closure.

Before implementation begins, the following must be defined:

* Encounter identity
* Patient relationship
* Start time
* End time
* Lifecycle states
* Admission/presentation semantics
* Discharge semantics
* Cancellation semantics
* Relationship to Medical Records
* Relationship to synchronization
* Relationship to DHIS2
* Whether multiple active encounters are allowed
* Concurrency rules

No developer should independently implement Encounter before these decisions are documented.

---

## 12. Ownership Does Not Mean Isolation

Developers own implementation areas but do not own architectural rules independently.

For example:

```text
Developer 4 → Database
```

does not mean Developer 4 can independently change the domain model to satisfy a database requirement.

Similarly:

```text
Developer 7 → DHIS2
```

does not mean DHIS2 models become the application's domain model.

Ownership means:

> The developer is responsible for implementing and maintaining the area while following the project's architectural contracts.

---

## 13. Cross-Team Interfaces

Developers must explicitly communicate when their work crosses module boundaries.

Important current boundaries include:

```text
Patient ↔ Encounter
Encounter ↔ Medical Records
Encounter ↔ DHIS2
Encounter ↔ Sync
Security ↔ API
Database ↔ Infrastructure
Testing ↔ All modules
```

These boundaries should be agreed upon before implementation where the interaction is significant.

---

## 14. Pull Request Expectations

Each feature should provide:

1. Implementation
2. Tests
3. API contract if applicable
4. Database changes if applicable
5. Documentation for significant architectural decisions
6. Migration information where applicable

Large architectural changes should be discussed before implementation.

---

## 15. Definition of Done

A backend feature is not considered complete merely because it compiles.

A feature should generally have:

```text
Code
  +
Tests
  +
API contract
  +
Persistence where required
  +
Error handling
  +
Documentation where necessary
  +
Architectural review
```

---

## 16. Integration Strategy

Developers should work in feature branches.

The architecture owner coordinates integration and resolves conflicts between modules.

The goal is to prevent long-lived branches from diverging significantly from the main architecture.

---

## 17. Architectural Review Rule

The following changes require architectural discussion before implementation:

* New domain concepts
* Changes to aggregate boundaries
* Changes to repository interfaces
* New cross-module dependencies
* Changes to identity strategy
* Changes to synchronization semantics
* Changes to database ownership
* External integration contracts
* Security model changes
* Major API contract changes

Routine implementation within an established boundary does not require architectural redesign.

---

## 18. Current Development Principle

The team should optimize for:

> **Independent implementation within clearly defined boundaries, followed by controlled integration.**

The architecture exists to enable parallel development rather than to prevent developers from moving independently.
