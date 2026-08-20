# ADR-01: Encounter as a First-Class Domain Concept

## Status

Accepted

## Date

2026-08-19

## Context

Matibabu needs to distinguish between a patient's long-lived identity and an individual episode of care.

A patient may interact with the healthcare facility multiple times. Each interaction may represent a separate episode of care with its own start, clinical activity, and conclusion.

Therefore, Patient alone is insufficient to provide the clinical context required by the system.

An Encounter represents the period from the formal opening of an episode of care until that episode is discharged or cancelled.

## Decision

Encounter will be modeled as a **first-class domain concept** with its own identity, lifecycle, and repository abstraction.

The relationship is:

```text
Patient
   │
   │ 1
   │
   │ *
Encounter
```

An Encounter belongs to exactly one Patient, while a Patient may have multiple Encounters.

## Encounter Identity

Encounter identifiers will use UUID v7, consistent with the Patient identity strategy.

The Encounter UUID is the stable identity of the encounter across:

* Local persistence
* Synchronization
* Remote persistence
* External integration boundaries

The local and remote systems must not generate different identifiers for the same Encounter.

## Initial Domain Model

The initial Encounter model contains:

```text
id
patientId
status
startedAt
endedAt
createdAt
```

### Identifier

`id` is a UUID v7.

### Patient Relationship

`patientId` identifies the Patient associated with the Encounter.

The Encounter does not contain a full Patient aggregate.

### Status

The initial lifecycle states are:

```text
ACTIVE
DISCHARGED
CANCELLED
```

## Lifecycle

The initial lifecycle is:

```text
                 ┌────────────┐
                 │   ACTIVE   │
                 └─────┬──────┘
                      /   \
                     /     \
              discharge   cancel
                   /         \
                  ▼           ▼
           DISCHARGED      CANCELLED
```

Valid transitions:

```text
ACTIVE → DISCHARGED
ACTIVE → CANCELLED
```

Terminal states:

```text
DISCHARGED
CANCELLED
```

Terminal states cannot transition back to ACTIVE.

## Starting an Encounter

An Encounter begins when the healthcare workflow formally opens an episode of care.

A patient's physical arrival at the facility does not automatically create an Encounter.

The system should create the Encounter when the patient is formally registered/opened for care.

When created:

```text
status = ACTIVE
startedAt = current time
endedAt = null
```

## Discharge

Discharge is a formal clinical/workflow operation.

Physical departure from the facility does not automatically imply that an Encounter has been discharged.

When discharged:

```text
status = DISCHARGED
endedAt = current time
```

An already discharged Encounter cannot be discharged again.

## Cancellation

Cancellation represents an Encounter that does not complete through normal discharge.

When cancelled:

```text
status = CANCELLED
endedAt = current time
```

The detailed business rules determining when cancellation is permitted may be expanded as the clinical workflow becomes better defined.

## Domain Invariants

The domain must prevent invalid states.

Examples:

```text
ACTIVE       → endedAt must be null
DISCHARGED   → endedAt must not be null
CANCELLED    → endedAt must not be null
endedAt      → cannot be before startedAt
```

The domain should also prevent invalid state transitions.

State should not be changed through unrestricted setters.

Instead, the domain exposes meaningful operations such as:

```text
discharge()
cancel()
```

## Aggregate Boundary

Encounter should remain a relatively small aggregate.

It should not contain every clinical object associated with the episode of care.

The following should remain separate domain concepts:

* Observations
* Diagnoses
* Procedures
* Medications
* Clinical notes
* Laboratory results
* Other medical records

These concepts may reference the Encounter using its identifier.

Conceptually:

```text
Encounter
   │
   ├── Observation
   ├── Diagnosis
   ├── Procedure
   ├── Medication
   └── Clinical Record
```

This prevents Encounter from becoming a large aggregate with excessive coupling and synchronization complexity.

## Medical Records

Clinical records that belong to a specific episode of care should be associated with the Encounter.

Medical Records must not require the Patient aggregate to contain the complete clinical history.

The Encounter provides the clinical context:

```text
Patient
   │
   └── Encounter
          │
          ├── Diagnosis
          ├── Observation
          ├── Procedure
          └── Clinical Record
```

## Synchronization

Encounter UUIDs are stable synchronization identities.

The same UUID must be preserved when an Encounter moves between local and remote persistence.

Synchronization metadata must not be added to the core Encounter domain model unless a future architectural decision explicitly requires it.

## DHIS2 Integration

The Encounter domain must remain independent of DHIS2.

DHIS2-specific representations must be handled by an integration boundary.

Conceptually:

```text
Matibabu Encounter
       │
       ▼
DHIS2 Adapter / Mapper
       │
       ▼
DHIS2 Representation
```

DHIS2 identifiers and concepts should not be added to the core Encounter entity merely for integration purposes.

## Concurrent Encounters

The initial domain model does not enforce a rule that a Patient can have only one ACTIVE Encounter.

Whether multiple active Encounters are permitted is considered a workflow/application concern until clinical requirements establish otherwise.

This avoids unnecessarily constraining the domain model.

## Consequences

### Positive

* Patient identity is separated from episode-of-care identity.
* Clinical records have an explicit context.
* Synchronization has a stable identity.
* DHIS2 remains isolated from the core domain.
* Encounter lifecycle rules are enforced centrally.
* The model remains small and testable.
* Other developers can build against a stable Encounter contract.

### Negative

* More domain concepts and tables are introduced.
* Developers must explicitly associate clinical records with an Encounter.
* Additional application services and API endpoints are required.

## Implementation Order

Encounter will be implemented incrementally:

```text
1. Domain
2. Domain tests
3. Application/use cases
4. Application tests
5. Persistence
6. API
7. Integration tests
8. Apidog verification
```

The domain layer will be implemented before Spring, JPA, or HTTP concerns are introduced.

## Related Decisions

* ADR-001: Clean Architecture with Feature-Oriented Packaging
* ADR-002: Keep the Domain Independent of Frameworks
* ADR-003: Repository Abstractions
* ADR-004: UUID v7 for Entity Identity
* ADR-005: DTOs at the API Boundary
* ADR-008: Encounter as a First-Class Domain Concept
* ADR-009: Avoid Large Patient or Encounter Aggregates
