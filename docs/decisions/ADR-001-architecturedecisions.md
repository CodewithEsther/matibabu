ADR — Matibabu Backend Architecture
Status: Accepted
Date: 2026-08-15
Project: Matibabu
Scope: Backend architecture and persistence
1. Context

The Matibabu backend is being developed as a Spring Boot application with multiple backend developers working on separate clinical and infrastructure features.

The architecture needs to:

Keep business/domain logic independent from Spring and persistence technology.
Allow SQLite to be used as the current database.
Keep the application independent from the database implementation.
Support multiple developers working on different modules without constantly modifying the same classes.
Make persistence concerns separate from domain concerns.
Provide clear places for HTTP, application, domain, and infrastructure logic.
Avoid creating interfaces and classes without a concrete responsibility.


2. Decision: Use layered Clean Architecture boundaries

The backend is organized around four primary concerns:

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

The practical dependency flow for a patient operation is:

PatientController
│
▼
RegisterPatientUseCase
│
▼
RegisterPatientService
│
▼
PatientRepository
▲
│
PatientRepositoryAdapter
│
├── PatientMapper
│
└── SpringDataPatientRepository
│
▼
PatientEntity
│
▼
SQLite

The application and domain layers should not depend directly on JPA, Hibernate, SQLite, or Spring Data.

Infrastructure implements the abstractions required by the application/domain layers.



3. Decision: Separate Patient from PatientEntity

We intentionally maintain two representations of a patient.

Patient

Located in:

domain/patient/Patient.java

Patient represents the domain concept.

It contains domain-level state such as:

ID
first name
last name
date of birth
phone number
creation timestamp
gender

It generates its own UUID and controls its construction.

PatientEntity

Located in:

infrastructure/persistence/patient/PatientEntity.java

PatientEntity represents how a patient is persisted.

It is annotated with JPA annotations such as:

@Entity
@Table(name = "patients")

and therefore knows about persistence technology.

This separation prevents JPA concerns from leaking into the domain model.

The distinction is therefore:

Patient
→ domain representation

PatientEntity
→ database representation

The existence of both classes is justified because they belong to different architectural boundaries and have different responsibilities.



4. Decision: Use UUIDs for patient identity

Patients use:

UUID

as their identifier.

A new Patient generates its identifier using:

UuidCreator.getTimeOrderedEpoch();

The UUID is persisted as the entity's ID.

The use of UUIDs provides application-level identifiers without requiring the database to generate sequential IDs.

UUID generation does not imply a linear retrieval operation. Patient lookup is performed through the repository/database using the indexed primary key.

The domain therefore remains responsible for patient identity while persistence stores that identity.



5. Decision: Use a reconstitution method for persisted domain objects

Patient has a normal constructor for creating a new patient:

new Patient(...)

It also has:

Patient.reconstitute(...)

for reconstructing an existing patient from persisted data.

The distinction is:

New patient
↓
Patient constructor
↓
new UUID + createdAt

Existing patient
↓
Patient.reconstitute(...)
↓
restore persisted UUID + createdAt

This prevents persistence code from requiring unrestricted setters on the domain object merely to restore database state.

6. Decision: Use Gender as a domain enum

Gender is represented in the domain using:

Gender

rather than an unrestricted string.

Persistence stores the enum using:

@Enumerated(EnumType.STRING)

This keeps the domain representation explicit while storing readable enum values in the database.



7. Decision: Use repository abstraction in the domain/application boundary

The application depends on:

PatientRepository

rather than directly depending on Spring Data or JPA.

The repository represents the operations required by the application.

For example:

Patient save(Patient patient);

Optional<Patient> findById(UUID id);

The application therefore depends on the abstraction:

PatientRepository

rather than:

SpringDataPatientRepository

This keeps persistence technology outside the application boundary.



8. Decision: Use Spring Data JPA as the persistence mechanism

The infrastructure layer contains:

SpringDataPatientRepository

which extends:

JpaRepository<PatientEntity, UUID>

Its responsibility is database-oriented persistence of PatientEntity.

It is intentionally separate from the domain repository.

Therefore:

PatientRepository
│
│ domain/application abstraction
▼
PatientRepositoryAdapter
│
▼
SpringDataPatientRepository
│
▼
PatientEntity

This prevents Spring Data types from leaking into the domain/application layers.



9. Decision: Use a persistence adapter

PatientRepositoryAdapter implements the domain repository:

PatientRepository

Its responsibilities are:

Receive a domain Patient.
Convert it into PatientEntity.
Persist the entity through Spring Data.
Convert the persisted entity back into Patient.

Conceptually:

Patient
↓
PatientMapper
↓
PatientEntity
↓
SpringDataPatientRepository
↓
PatientEntity
↓
PatientMapper
↓
Patient

The adapter is marked with:

@Repository

because it is a persistence component.



10. Decision: Use MapStruct for mapping

The project uses MapStruct for mapping between domain and persistence representations.

The mapper is defined as an interface:

@Mapper(componentModel = "spring")
public interface PatientMapper

The interface defines the mapping boundary while MapStruct generates the implementation.



PatientMapper
│
├── Patient → PatientEntity
│
└── PatientEntity → Patient

The entity-to-domain conversion uses Patient.reconstitute(...) so that persisted identity and creation time are restored correctly.



11. Decision: Use application use cases

Application operations are represented through use-case interfaces.

For patient registration:

RegisterPatientUseCase

is implemented by:

RegisterPatientService

The service:

Creates a Patient.
Delegates persistence to PatientRepository.
Returns the persisted domain object.

The service does not know about:

SQLite
JPA
Hibernate
PatientEntity
Spring Data

Its dependency is:

PatientRepository

This keeps the application logic independent from infrastructure.



12. Decision: Use Spring-managed application services

RegisterPatientService is annotated with:

@Service

Spring therefore manages the service as a bean.

Dependencies are supplied through constructor injection:

public RegisterPatientService(
PatientRepository patientRepository
)



13. Decision: Keep controllers thin

The API layer contains:

PatientController

The controller depends on:

RegisterPatientUseCase

rather than directly depending on:

RegisterPatientService

The controller is responsible for HTTP concerns.

It should not contain:

database operations
JPA code
domain persistence logic
business rules

The intended flow is:

HTTP request
↓
PatientController
↓
RegisterPatientUseCase
↓
RegisterPatientService


14. Decision: Use SQLite as the current database

The backend is configured to use SQLite for local persistence.

The datasource configuration uses:

spring.datasource.url=${DB_URL:jdbc:sqlite:../database/matibabu.db}
spring.datasource.driver-class-name=org.sqlite.JDBC

This allows:

an explicit DB_URL environment variable to override the database URL;
SQLite to be used by default when DB_URL is not supplied.

The project includes the Xerial SQLite JDBC driver and Hibernate's community dialect support.

The database is therefore externalized from the application code rather than being hardcoded into Java classes.



15. Decision: Use environment variables for database configuration

Database configuration should not require credentials or connection details to be hardcoded into application source code.

The application supports environment-based configuration such as:

DB_URL

This allows the database configuration to vary between environments.

The principle is:

Application code
│
▼
Configuration
│
▼
Environment

rather than embedding environment-specific database configuration in source code.



16. Decision: Organize development by bounded feature responsibility

Because the backend has multiple developers, developers should work primarily on independent feature boundaries rather than all modifying the same files.

The backend can evolve into modules such as:

Patients
Encounters
Medical Records
Authentication
DHIS2 Integration
Infrastructure
Testing

Each feature should follow the established architectural pattern.

For example:

appointment/
├── domain/
├── application/
├── infrastructure/
└── api/

Developers should not introduce an alternative architectural pattern for individual features without an explicit architectural decision.

17. Consequences
    Positive consequences
    Domain logic remains independent of persistence technology.
    SQLite can be replaced without rewriting the domain.
    HTTP concerns remain separate from application logic.
    Persistence concerns remain isolated.
    MapStruct reduces repetitive mapping code.
    Repository abstractions make application logic easier to test.
    Multiple developers can work on different features with less coupling.
    Architectural responsibilities are explicit.
    The codebase has a consistent pattern for future modules.
    Negative consequences
    There are more types than in a simple CRUD application.
    A single patient operation crosses several layers.
    Mapping introduces additional code and build-time generation.
    Developers need to understand the architectural boundaries.
    Small features may sometimes require several files.

These costs are accepted because the project is intended to support multiple developers and multiple clinical modules rather than remain a single simple CRUD application.



18. Current Architecture

The current patient implementation is approximately:

backend/
└── src/main/java/com/matibabu/backend/
│
├── api/
│   └── patient/
│       └── PatientController
│
├── application/
│   └── patient/
│       ├── RegisterPatientUseCase
│       └── RegisterPatientService
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

The current dependency direction is:

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

with infrastructure implementing the persistence contracts required by the application/domain boundary.



20. Future Decisions

The following decisions remain to be made as development continues:

API request and response DTO conventions.
Validation strategy and exact patient validation rules.
Error/exception handling and HTTP error responses.
Database migration strategy.
Authentication and authorization architecture.
Appointment domain model.
Medical-record domain model.
DHIS2 integration boundary and synchronization strategy.
Testing strategy for unit, integration, and API tests.
Production database configuration.

These should be recorded as additional ADRs when the relevant decisions are made rather than prematurely specifying them here.

Summary

The Matibabu backend adopts a Clean Architecture-inspired separation of concerns.

The central principle is:

Domain and application logic depend on abstractions; infrastructure implements those abstractions.

For the patient feature, this results in:

HTTP
↓
Controller
↓
Use Case
↓
Application Service
↓
Domain Repository
↓
Persistence Adapter
↓
Spring Data JPA
↓
Entity
↓
SQLite

Each class, interface, and layer exists because it has a distinct responsibility rather than simply because the architecture requires additional files.
