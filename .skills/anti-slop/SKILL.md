> Scope: Detect and prevent AI-generated slop in code, documentation, and design for the Matibabu project.
> Applies to: All files in `backend/`, `frontend/`, `docs/`, `.skills/`, and `README.md`.
> Inspired by: [rand/cc-polymath anti-slop skill](https://github.com/rand/cc-polymath/tree/main/skills/anti-slop)

# Anti-Slop Skill

## What is slop?

Slop is low-quality, generic output that looks correct but adds noise, hides intent, or creates maintenance burden. In Matibabu, slop is especially dangerous because the codebase touches patient data and healthcare workflows.

## General principles

- **Clarity over cleverness.** If a simpler solution works, use it.
- **Specificity over generality.** Name things for what they are.
- **Substance over spectacle.** No fluff comments, no placeholder text, no trendy visuals.
- **Scope discipline.** Do not change files unrelated to the request.
- **Verify before declaring done.** Run tests, builds, and lint before finishing.

## Text and documentation slop

### Remove immediately

- "delve into" → use "examine", "explore", or delete
- "it's important to note that" → delete
- "in order to" → "to"
- "due to the fact that" → "because"
- "leverage" → "use"
- "empower", "synergistic", "paradigm shift", "holistic"
- Meta-commentary about the document itself ("this section will discuss...")
- Corporate filler ("moving forward", "going forward", "at this point in time")

### Keep it direct

- Lead with the point.
- Use British English spelling consistently (colour, centre, organisation).
- Use sentence case for headings.
- Skip preambles.

## Code slop

### Naming

Rename generic identifiers:

- `data` → `patient`, `request`, `payload`, depending on content
- `result` → `registeredPatient`, `savedEncounter`, etc.
- `temp` → what you are temporarily storing
- `item` → what kind of item
- `obj`, `val`, `num` → never use these
- `handleData()` → `registerPatient()`, `updateEncounter()`, etc.
- `processItems()` → `savePatients()`, `validateRequests()`, etc.

### Comments

Remove obvious comments:

```java
// Bad
// Create a patient
Patient patient = new Patient(...);

// Good
Patient patient = new Patient(...);
```

Comment only when the *why* is not obvious:

```java
// UUID v7 is time-ordered, which is better for database indexing than random UUIDs.
```

### Abstraction

Avoid speculative generality:

- Three similar lines are better than a premature abstraction.
- Do not create a `BaseService` or `AbstractHandler` until you have at least three concrete cases that justify it.
- Do not add configuration options for behaviour that does not exist yet.

### Frontend slop

- No `console.log` left in committed code. Use a logger or remove it.
- No unused imports or variables.
- No `any` or `as` casts to silence errors.
- No inline styles. Use the design tokens in `globals.css`.
- No magic strings for backend URLs. Use the API client.
- No speculative component props. Add props only when needed.

### Backend slop

- No `System.out.println`. Use a logger.
- No empty catch blocks.
- No `@Autowired` field injection. Use constructor injection.
- No domain logic leaking into controllers or repositories.
- No DTOs that mirror the domain model one-to-one unless the API contract requires it.
- No public setters on domain entities unless necessary.

## Design slop

- No generic gradient backgrounds.
- No glassmorphism or neumorphism.
- No stock illustrations or irrelevant 3D shapes.
- No center-aligning everything.
- No cards around content that does not need grouping.
- Use the colour, spacing, and typography tokens from `DESIGN.md` / `.skills/design/SKILL.md`.

## Git and commit slop

- Do not commit commented-out code.
- Do not commit `.env.local` or database files.
- Commit messages should state what changed and why:
  - Good: `feat(patient): add national ID to registration request`
  - Bad: `updates`
- Keep commits focused on one logical change.

## Verification rule

Before declaring any task complete:

1. Run the relevant tests: `./mvnw test` for backend changes, `npm test` or `npm run build` for frontend changes.
2. Run the linter: `npm run lint` in `frontend/`.
3. Check for unused imports, variables, and obvious comments.
4. Review the diff for scope creep.

## When this skill should not apply

Some patterns are acceptable in specific contexts:

- Generated migration files can be verbose.
- Legal or compliance documentation may need specific phrasing.
- Third-party code or vendored dependencies are out of scope.

## Summary

- Be direct in writing.
- Name things precisely.
- Comment the why, not the what.
- Keep abstractions small and justified.
- Verify before finishing.
