# ADR-002 — Division of Automated Testing and CI/CD Responsibilities

* **Status:** Pending Approval
* **Date:** 2026-08-26
* **Decision:** Divide automated testing and CI/CD responsibilities between two developers while maintaining shared ownership of overall software quality.

## Context

The Matibabu Offline-First EMR Lite backend is being developed collaboratively by multiple developers. As the codebase grows, automated testing and CI/CD need dedicated ownership to ensure that:

* New functionality is covered by tests.
* Existing functionality is protected against regressions.
* Tests execute automatically on GitHub.
* Pull requests cannot silently introduce broken code.
* The team has a predictable process for validating and integrating changes.
* Testing responsibilities do not become concentrated on a single developer.

Two developers will therefore take primary responsibility for the testing and CI/CD workstream.

## Decision

Testing and CI/CD responsibilities will be divided into two complementary roles.

### Jack — Automated Testing Lead

 will primarily own the **test suite and test strategy**.

Responsibilities include:

#### 1. Unit tests

Maintain and expand unit tests for:

* Domain entities
* Domain business rules
* Application services
* Exception behavior
* State transitions
* Validation logic

Examples include testing:

```text
ACTIVE → DISCHARGED
ACTIVE → CANCELLED
DISCHARGED → DISCHARGED → rejected
CANCELLED → CANCELLED → rejected
CANCELLED → DISCHARGED → rejected
```

#### 2. Repository/integration tests

Maintain tests for:

* Repository adapters
* JPA mappings
* Database persistence
* Entity/domain mapping
* Flyway migration compatibility where applicable

Tests should use an isolated test database configuration rather than depending on a developer's local production-like database.

#### 3. API/integration tests

Add automated tests for important REST flows where appropriate.

Examples:

```text
POST /api/patients
GET  /api/patients/{id}

POST /api/encounters
GET  /api/encounters/{id}

POST /api/encounters/{id}/discharge
POST /api/encounters/{id}/cancel

POST /api/encounters/{id}/medical-record
GET  /api/medical-records/{id}
```

Tests should verify both successful and unsuccessful requests.

#### 4. Negative testing

Ensure that business-rule failures are tested explicitly.

Examples:

* Missing patient
* Missing encounter
* Duplicate phone number
* Invalid encounter state
* Invalid patient/encounter relationship
* Duplicate medical record
* Invalid request data

#### 5. Test quality

will review tests for:

* Meaningful test names
* Appropriate assertions
* Independent tests
* Repeatable results
* No dependence on developer-specific local state
* Adequate coverage of business rules

---

# Fidel — CI/CD and Quality Pipeline Lead

 will primarily own the **automation and delivery pipeline**.

Responsibilities include:

### 1. GitHub Actions

Create and maintain workflows for:

```text
Pull Request
    ↓
Checkout
    ↓
Set up Java
    ↓
Install dependencies
    ↓
Compile
    ↓
Run tests
    ↓
Build application
```

### 2. Continuous Integration

CI should automatically execute when relevant changes are pushed or pull requests are opened/updated.

At minimum, CI should verify:

* Project compilation
* Automated tests
* Maven build
* Database/test configuration
* Generated artifacts where applicable

### 3. Pull-request quality gates

CI should provide a clear pass/fail result.

A pull request containing broken code or failing tests should not be considered ready for merging.

Where appropriate, GitHub branch protection should require the CI check to pass before merging.

### 4. Build verification

Ensure the project can be built from a clean environment.

The pipeline should not depend on:

* A developer's local database
* IntelliJ-specific configuration
* Uncommitted files
* Local environment variables that are not documented
* Previously generated build artifacts

### 5. CI troubleshooting

Developer B will investigate:

* Failed GitHub Actions jobs
* Maven build failures
* Environment differences between local and CI execution
* Test failures caused by CI configuration
* Dependency/cache problems
* Migration/configuration issues

### 6. Future deployment automation

As the project progresses , he will own the CI/CD path toward deployment, including:

```text
Code
 ↓
Pull Request
 ↓
CI
 ↓
Tests
 ↓
Build
 ↓
Container/package
 ↓
Deployment
```

Deployment should only be expanded once the CI stage is reliable.

---

# Shared Responsibilities

Although the work is divided, neither developer owns quality in isolation.

Both developers are responsible for:

* Reviewing test-related pull requests.
* Investigating failures that affect their changes.
* Keeping tests synchronized with business rules.
* Maintaining readable CI configuration.
* Documenting important changes.
* Communicating breaking changes.
* Reviewing failures before modifying or weakening tests.

A test should **not** be changed merely to make CI pass if the underlying production behavior is incorrect.

Similarly, CI should **not** be weakened simply because an existing test exposes a legitimate defect.

---

# Recommended Repository Structure

Testing responsibilities should follow the existing project organization.

```text
src/
├── main/
│   └── java/
│       └── com/matibabu/backend/
│
└── test/
    └── java/
        └── com/matibabu/backend/
            ├── domain/
            ├── application/
            ├── infrastructure/
            └── api/

.github/
└── workflows/
    └── ci.yml
```

The exact test structure may evolve as the project grows.

---

# Definition of Done for a Feature

A feature should not be considered complete until:

* [ ] Domain behavior is implemented where applicable.
* [ ] Application use case is implemented where applicable.
* [ ] Persistence is implemented where applicable.
* [ ] API endpoint is implemented where applicable.
* [ ] Unit tests cover the relevant business rules.
* [ ] Integration/repository tests cover persistence behavior where applicable.
* [ ] API behavior has been manually verified when appropriate.
* [ ] Error cases are tested.
* [ ] `mvn test` passes locally.
* [ ] CI passes.
* [ ] The pull request has been reviewed.

---

# Collaboration Workflow

The recommended workflow is:

```text
Developer implements feature
          ↓
Developer writes/updates tests
          ↓
Pull Request
          ↓
CI automatically runs
          ↓
Testing Lead reviews test coverage
          ↓
CI/CD Lead verifies pipeline/build
          ↓
Code review
          ↓
All required checks pass
          ↓
Merge
```

If a CI failure is caused by application code, the feature developer and Testing Lead should address the code/test issue.

If the failure is caused by workflow configuration or the CI environment,  CI should take primary ownership.

If the failure is caused by an ambiguous business requirement, the developers should resolve the requirement before modifying the tests.

## Consequences

### Positive consequences

* Testing receives dedicated ownership.
* CI/CD receives dedicated ownership.
* Responsibilities are clear without creating isolated silos.
* Business rules receive stronger regression protection.
* Pull requests are automatically validated.
* The project becomes less dependent on individual developers' local environments.
* Future deployment automation has a clear owner.
* The team gains a repeatable quality-control process.

### Trade-offs

* Two developers must coordinate closely when tests and CI configuration change together.
* Both roles require knowledge of the overall architecture.
* CI maintenance introduces additional configuration work.
* Some responsibilities intentionally overlap to avoid creating a single point of failure.

## Future evolution

As the project matures, the testing and CI/CD process may be expanded to include:

* Code coverage reporting
* Static analysis
* Formatting/lint checks
* Security/dependency scanning
* Test containers or dedicated integration databases
* Docker image builds
* Staging deployments
* Automated release workflows
* Production deployment gates

These additions should be introduced incrementally rather than making the initial CI pipeline unnecessarily complex.
