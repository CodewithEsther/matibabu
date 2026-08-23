> Scope: TypeScript and React conventions for the Matibabu Next.js frontend.

# Matibabu Frontend TypeScript Skill

## Project context

- The frontend is a Next.js 16 application using the App Router.
- It lives in `frontend/` and consumes the Spring Boot backend at `http://localhost:8080/api`.
- Backend API contracts use DTOs; the frontend should mirror those types explicitly.

## TypeScript conventions

- Keep `strict: true` enabled in `tsconfig.json`.
- Avoid `any`. Use `unknown` when the type is genuinely unknown, then narrow it.
- Prefer explicit return types on public API client functions.
- Use string literal unions and `as const` for small enumerations that map to backend values.
- Do not expose internal domain types directly to components; use DTO types from `src/types/`.

## File and naming conventions

- Components: `PascalCase.tsx` under `src/components/`.
- Pages: `page.tsx` inside route directories under `src/app/`.
- API clients: `src/lib/api/<resource>.ts` with camelCase file names.
- Types: `src/types/<domain>.ts` with PascalCase exported interfaces.
- Environment variables that must be available in the browser must use the `NEXT_PUBLIC_` prefix.

## API client rules

- Centralize fetch calls in `src/lib/api/`.
- Throw descriptive errors on non-OK responses.
- Do not build URLs manually in components; use the shared API functions.
- Keep `process.env.NEXT_PUBLIC_API_BASE_URL` as the default base URL with a localhost fallback.

## Component rules

- Use Server Components by default.
- Mark interactive components with `"use client"` only when needed.
- Keep forms as client components; data display pages can be server components.
- Pass IDs and primitives as props; do not pass entire objects between server and client boundaries unless serializable.

## Error handling

- Surface API errors to users with clear messages.
- Use Next.js `notFound()` for 404-style missing resources on server components when appropriate.
- Do not swallow exceptions in async handlers.

## Backend contract awareness

- The backend uses UUID v7 string identifiers.
- Dates are returned as ISO-8601 strings (`createdAt`, `dateOfBirth`).
- `gender` values are uppercase strings: `MALE`, `FEMALE`, `OTHER`, `UNKNOWN`.
- Some database fields (national ID, insurance, emergency contacts) exist in migrations but are not yet exposed by the API. Do not build UI for them until the backend contract includes them.
