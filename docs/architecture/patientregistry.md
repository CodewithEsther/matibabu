# Patient Registry Contract

## 1. Purpose

The Patient Registry is responsible for creating, retrieving, updating,
and managing patient records within the Matibabu backend.

The registry is the authoritative backend component for patient identity
and demographic information.

---

## 2. Patient Identity

Every patient must have a unique identifier.

The backend must generate the patient identifier.

Clients must not be allowed to choose or overwrite the primary patient ID.

---

## 3. Patient Information

The initial patient record contains:

- Patient ID
- First name
- Last name
- Date of birth
- Sex
- Phone number
- Address
- Created timestamp
- Updated timestamp

The exact database representation must remain an infrastructure concern.

The domain model must not depend directly on PostgreSQL.

---

## 4. Required Operations

The Patient Registry must support:

### Create patient

Create a new patient record.

Input:

- First name
- Last name
- Date of birth
- Sex
- Phone number
- Address

Output:

- Created patient
- Generated patient ID
- Creation timestamp

---

### Get patient

Retrieve a patient using the patient ID.

Input:

- Patient ID

Output:

- Patient record

If the patient does not exist, the API must return an appropriate
not-found response.

---

### List patients

Retrieve a collection of patients.

The implementation should support pagination rather than returning an
unbounded number of records.

---

### Update patient

Update an existing patient's demographic information.

The patient ID must not change during an update.

The updated timestamp must change when the patient is successfully updated.

---

### Delete patient

Patient deletion must be treated as a controlled operation.

The implementation must not physically delete clinical history that may
depend on the patient.

The final deletion/archival strategy must be agreed upon before implementing
this operation.

---

## 5. Layer Responsibilities

### API

Responsible for:

- HTTP endpoints
- Request/response DTOs
- HTTP status codes
- Request validation
- API-level error responses

API classes belong in:

`com.matibabu.backend.api`

---

### Application

Responsible for:

- Patient use cases
- Application services
- Coordinating domain and persistence operations

Application classes belong in:

`com.matibabu.backend.application`

---

### Domain

Responsible for:

- Patient entity/model
- Patient business rules
- Domain validation
- Domain-level concepts

Domain classes belong in:

`com.matibabu.backend.domain`

The domain must not depend on Spring MVC, PostgreSQL, or HTTP.

---

### Infrastructure

Responsible for:

- JPA entities/adapters where required
- PostgreSQL persistence
- Repository implementations
- Database-specific configuration

Infrastructure classes belong in:

`com.matibabu.backend.infrastructure`

---

## 6. Repository Boundary

Application/domain code must depend on an abstraction rather than directly
using a PostgreSQL-specific repository.

Conceptually:

    API
     |
     v
    Application
     |
     v
    Domain / Repository Port
     |
     v
    Infrastructure
     |
     v
    PostgreSQL

The PostgreSQL implementation must remain replaceable.

---

## 7. API Boundary

The initial REST API is:

    POST   /api/patients
    GET    /api/patients/{id}
    GET    /api/patients
    PUT    /api/patients/{id}

The DELETE endpoint will be finalized after the patient archival strategy
has been agreed upon.

---

## 8. Validation

The API must reject invalid patient data.

At minimum:

- Required fields must not be empty.
- Date of birth must be a valid date.
- Date of birth must not be in the future.
- Patient ID must be valid when supplied in a path.
- Phone number must follow the project's agreed format.

Validation rules must be consistent across the backend.

---

## 9. Error Handling

The API should provide consistent error responses.

Errors should include enough information for a client to understand the
failure without exposing internal implementation details.

Examples:

- 400 Bad Request
- 404 Not Found
- 409 Conflict
- 500 Internal Server Error

---

## 10. Timestamps

Patient records must contain:

- `createdAt`
- `updatedAt`

`createdAt` must not change after creation.

`updatedAt` must change after a successful update.

---

## 11. Security

Patient information is sensitive application data.

Authentication and authorization requirements will be implemented through
the security layer.

Patient endpoints must not bypass the application's security architecture.

Security implementation belongs in:

`com.matibabu.backend.security`

---

## 12. Synchronization

Patient synchronization with external systems such as DHIS2 must not be
implemented directly inside the patient controller or domain model.

Synchronization belongs in:

`com.matibabu.backend.synchronization`

External-system integration belongs under the project's integration
structure.

---

## 13. Testing

The Patient Registry must have:

- Domain tests
- Application/service tests
- API/controller tests
- Persistence/integration tests

Tests must not require a developer's personal database credentials.

---

## 14. Ownership

The Patient Registry implementation will be divided among the backend team.

Before implementation begins, each developer must have a clearly defined
responsibility and must avoid modifying another developer's owned area
without agreement.

---

## 15. Contract Changes

Changes to this contract must be discussed by the team before implementation.

Breaking changes to:

- Patient fields
- API endpoints
- API request/response formats
- Repository interfaces
- Domain boundaries

must be communicated to all affected developers.

---

## 16. Package Structure

The Patient Registry follows the project's layered architecture.

```text
com.matibabu.backend
│
├── api
│   └── patient
│       ├── PatientController.java
│       ├── CreatePatientRequest.java
│       ├── UpdatePatientRequest.java
│       └── PatientResponse.java
│
├── application
│   └── patient
│       ├── CreatePatientUseCase.java
│       ├── GetPatientUseCase.java
│       ├── ListPatientsUseCase.java
│       └── UpdatePatientUseCase.java
│
├── domain
│   └── patient
│       ├── Patient.java
│       ├── Sex.java
│       └── PatientRepository.java
│
├── infrastructure
│   └── persistence
│       └── patient
│           ├── PatientEntity.java
│           ├── PatientJpaRepository.java
│           └── PatientRepositoryAdapter.java
│
├── security
│
└── synchronization