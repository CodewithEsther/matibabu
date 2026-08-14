Matibabu EMR Lite — Backend Architecture Contract

Version: 1.0
Status: Draft — Team Review
Project: Matibabu Offline-First EMR Lite

1. Purpose

This document defines the shared architectural rules for the Matibabu EMR Lite backend.

The backend is being developed by a team of eight developers. The architecture must therefore:

support parallel development;
minimize merge conflicts;
keep domain logic independent from infrastructure;
support offline operation;
support synchronization between local SQLite databases and the central PostgreSQL database;
provide consistent APIs and data models;
preserve clinical history;
make future maintenance easier.

No individual feature implementation should introduce architectural conventions that contradict this document.

If a developer believes an architectural change is necessary, it should be discussed with the team before implementation.

2. System Architecture

Matibabu follows an offline-first architecture.

The system consists of:

                    ┌──────────────────────┐
                    │      PostgreSQL      │
                    │   Central Database   │
                    └──────────┬───────────┘
                               │
                          Sync API
                               │
              ┌────────────────┴────────────────┐
              │                                 │
       ┌──────▼──────┐                   ┌──────▼──────┐
       │   Device A  │                   │   Device B  │
       │    SQLite   │                   │    SQLite   │
       └─────────────┘                   └─────────────┘
Local database

SQLite is responsible for:

local data storage;
offline reads;
offline writes;
local application operation;
maintaining pending synchronization changes.
Central database

PostgreSQL is responsible for:

centralized/shared data;
server-side persistence;
multi-user access;
synchronization;
centralized reporting and administration;
authoritative server state.


Important rule

SQLite and PostgreSQL are not two competing sources that users manually reconcile.

The synchronization subsystem is responsible for keeping local and central state coordinated.

3. Backend Package Architecture

The Java backend follows a layered/hexagonal-inspired structure:

com.matibabu.backend
│
├── api/
├── application/
├── domain/
├── infrastructure/
├── security/
└── synchronization/


api/

Responsible for external interfaces.

Examples:

REST controllers
request DTOs
response DTOs
HTTP validation
HTTP status handling

The API layer must not contain core business logic.

application/

Responsible for application use cases.

Examples:

RegisterPatient
UpdatePatient
CreateEncounter
RecordObservation
SyncChanges

Application services coordinate domain objects and repositories.

domain/

Contains the core EMR business model.

Examples:

Patient
Encounter
Observation
Diagnosis
Medication

The domain must remain independent of:

PostgreSQL;
SQLite;
HTTP;
Spring MVC;
synchronization transport;
infrastructure implementation details.


infrastructure/

Contains technical implementations.

Examples:

database access
repository implementations
PostgreSQL
SQLite
configuration
external integrations

Infrastructure implements interfaces defined by the appropriate higher-level layers.

security/

Responsible for:

authentication
authorization
roles
permissions
security configuration
synchronization/

Responsible for:

outbox
change tracking
sync cursors
server change feed
conflict detection
conflict resolution
synchronization API

Synchronization is treated as a first-class subsystem.

4. Project-Level Directory Structure

The project root is:

matibabu/
│
├── backend/
├── database/
├── docs/
├── infrastructure/
├── integration/
└── tests/


backend/

Java/Spring Boot application.

database/

Database-related resources such as:

migrations/
seed/
docs/

Project documentation.

Recommended:

docs/
├── architecture/
├── api/
└── synchronization/
infrastructure/

Deployment and environment configuration.

Examples:

docker/
deployment/
configuration/
integration/

Integration-level resources and scenarios.

tests/

Shared test resources, fixtures, and integration test support.

5. Dependency Rule

The most important dependency rule is:

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

The domain must not depend on infrastructure.

For example, domain code must not directly import:

org.postgresql.*

or:

org.springframework.jdbc.*

or other database-specific implementation classes.

The same principle applies to SQLite.

6. Entity Identification

All synchronizable domain entities use:

UUID

as their identifier.

Example:

private UUID id;

IDs are generated when the record is created, including when the device is offline.

Why?

A device must be capable of creating:

Patient
Encounter
Observation

without contacting the central server.

The server must not be responsible for assigning the ID after the fact.

7. Common Entity Metadata

Synchronizable entities will share a common lifecycle model.

The conceptual base entity is:

BaseEntity
├── id
├── createdAt
├── updatedAt
├── version
└── deleted


id

Unique UUID.

createdAt

Time at which the record was created.

updatedAt

Time of the most recent modification.

version

Used for detecting concurrent modifications and synchronization conflicts.

deleted

Indicates logical deletion.

8. Soft Deletion

Clinical records should not normally be physically deleted from the database.

Instead:

deleted = true

This is necessary because a deletion performed offline must itself become a synchronization event.

Example:

Device A


Patient
id = abc
deleted = true

The synchronization system can then communicate that deletion to PostgreSQL and other devices.

9. Clinical Data Principle

Clinical information should generally be modeled as historical events/observations rather than mutable snapshots.

For example, instead of:

Patient
└── bloodPressure = 120/80

we prefer:

Patient
│
├── Observation
│   └── BP 120/80
│
├── Observation
│   └── BP 125/82
│
└── Observation
└── BP 130/85

This preserves clinical history and reduces synchronization conflicts.

The exact implementation is determined by each domain module.

10. Offline Write Model

Every local write must be capable of being synchronized later.

Conceptually:

SQLite Transaction
│
├── Domain Record
│
└── Outbox Event

For example:

Patient
id = 123
name = Jane Doe

and:

OutboxEvent
operation = CREATE
entityType = Patient
entityId = 123
status = PENDING

The important principle is:

The domain change and its synchronization event must not be allowed to diverge.

If the patient is successfully written locally but the outbox event is lost, synchronization becomes unreliable.

Therefore these operations should eventually occur within the same local database transaction.

11. Outbox

The local outbox records changes that have not yet been successfully synchronized.

Conceptually:

OutboxEvent
├── id
├── entityType
├── entityId
├── operation
├── payload
├── createdAt
├── status
└── retry information

Possible operations:

CREATE
UPDATE
DELETE

Possible statuses:

PENDING
SYNCED
FAILED
CONFLICT

The exact schema will be finalized during implementation of the synchronization subsystem.

12. Synchronization Direction

Synchronization is bidirectional.

             PostgreSQL
              ▲     │
              │     │
          PUSH│     │PULL
              │     │
              │     ▼
             SQLite
Client → Server

The device sends pending local changes.

Server → Client

The device receives changes it has not yet seen.

13. Sync Cursor

Each device needs to know which server changes it has already processed.

We prefer a server-issued monotonically increasing synchronization position/cursor over relying exclusively on timestamps.

Conceptually:

Device cursor = 15420

Server changes:

15421 → Patient created
15422 → Encounter created
15423 → Observation created

Device requests:

changes after 15420

The server returns:

15421
15422
15423

The device then advances its cursor.

This reduces ambiguity caused by clock differences between devices.

14. Conflict Handling

The system must detect conflicts rather than silently overwrite data.

A conflict can occur when:

Device A edits version 4
Device B edits version 4
Device A synchronizes first
Server becomes version 5
Device B later submits an update based on version 4

The server can detect:

clientVersion != serverVersion

and identify the operation as a conflict.

15. Conflict Resolution Principle

We will not use "last write wins" indiscriminately.

For clinical information, preserving history takes priority over convenience.

Where appropriate:

append clinical event

rather than:

overwrite previous clinical event

For genuinely mutable administrative data, the appropriate conflict-resolution strategy will be defined by the relevant domain.

16. Auditability

EMR operations should be auditable.

The system should eventually maintain an audit trail containing information such as:

who
what
when
which record
what operation

Conceptually:

AuditLog
├── id
├── actor
├── action
├── entityType
├── entityId
└── timestamp

Audit requirements must be considered when implementing sensitive clinical operations.

17. API Conventions

REST endpoints should be resource-oriented.

Example:

GET    /api/patients
POST   /api/patients
GET    /api/patients/{id}
PUT    /api/patients/{id}
DELETE /api/patients/{id}

The API layer should use DTOs rather than exposing persistence entities directly.

For example:

HTTP Request
↓
Request DTO
↓
Application Use Case
↓
Domain

and:

Domain
↓
Response DTO
↓
HTTP Response

18. Error Handling

The API must return consistent error structures.

We should eventually standardize on something conceptually like:

{
"timestamp": "...",
"status": 400,
"code": "PATIENT_VALIDATION_ERROR",
"message": "Invalid patient data",
"details": []
}

Individual modules should not invent completely different error formats.

19. Database Rules

PostgreSQL and SQLite may have different technical implementations, but their relevant domain data must represent the same business concepts.

We must not design:

SQLite Patient

and:

PostgreSQL Patient

as unrelated models.

The databases represent different persistence environments for the same application.

Database schema changes should be managed through a controlled migration process.

Developers should not rely on manually changing production schemas.

20. Git Rules

Recommended branch structure:

main
└── develop
├── feature/patient-registry
├── feature/encounters
├── feature/observations
├── feature/medications
├── feature/auth
├── feature/synchronization
└── feature/infrastructure
Rules
Do not commit directly to main.
Features are developed on feature branches.
Changes are merged through pull requests.
PRs should be reviewed before merging.
Keep commits focused.
Do not modify another developer's module unnecessarily.
Architectural changes require team discussion.

21. Developer Ownership
    Developer	Responsibility
    Backend 1	Core architecture/shared components
    Backend 2	Patient Registry
    Backend 3	Encounters
    Backend 4	Observations/Vitals
    Backend 5	Diagnosis & Medication
    Backend 6	Authentication & Authorization
    Backend 7	Synchronization
    Backend 8	Persistence & Infrastructure

Ownership means primary responsibility, not isolation.

For example, synchronization touches every domain, so Backend 7 must coordinate with the developers responsible for those domains.

22. Frontend Contract

The two frontend developers should consume the backend through the API contract.

They should not directly access PostgreSQL or SQLite.

Frontend
│
▼
REST API
│
▼
Application

This allows frontend development to proceed using mocked API responses while backend modules are still being implemented.

23. What Developers Must NOT Do
    ❌ Don't access PostgreSQL directly from controllers.
    ❌ Don't put business logic inside controllers.
    ❌ Don't put database-specific code inside domain entities.
    ❌ Don't generate sequential IDs for synchronizable entities.
    ❌ Don't physically delete clinical records without an approved reason.
    ❌ Don't silently overwrite conflicting clinical data.
    ❌ Don't create a new synchronization mechanism for an individual module.
    ❌ Don't expose JPA entities directly as API responses.
    ❌ Don't introduce a new architectural pattern inside one module without discussing it.

24. Architecture Decision Summary

The current architectural decisions are:

Decision	                               Choice
Architecture	             Layered/hexagonal-inspired
Local DB	                 SQLite
Central DB	                 PostgreSQL
Entity IDs	                 UUID
Offline operation	         Required
Synchronization	             Bidirectional
Local changes	             Outbox
Server changes	             Change feed/cursor
Conflict detection	         Version-based
Clinical history	         Prefer append/event-oriented model
Deletion	                 Soft deletion
API	                         REST
API data	                 DTOs
Authentication	             Dedicated security layer
Synchronization	             Dedicated subsystem
Database changes	         Migrations
Git integration	             Feature branches → PR → develop

25. Definition of "Architecture Complete"

Before feature development begins, the team should have:

Project directory structure created
Java package structure created
Base entity convention agreed
UUID strategy agreed
Timestamp convention agreed
Soft-delete convention agreed
Audit requirements agreed
Outbox model agreed
Sync cursor model agreed
Conflict strategy agreed
API/error conventions agreed
Migration strategy agreed
Git workflow agreed
Developer ownership agreed

Once these are checked, Patient Registry can officially begin.