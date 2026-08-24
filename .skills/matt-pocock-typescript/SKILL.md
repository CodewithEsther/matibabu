> Scope: Advanced TypeScript conventions for the Matibabu frontend, inspired by Matt Pocock's Total TypeScript approach.
> Applies to: `frontend/**/*.ts` and `frontend/**/*.tsx`

# Matibabu TypeScript Mastery Skill

## Philosophy

TypeScript is a tool for making impossible states unrepresentable. We do not use it to document JavaScript; we use it to prevent bugs at compile time.

- Prefer compile-time safety over runtime convenience.
- Avoid `any` and `as` casts. They are escape hatches that hide bugs.
- Use `unknown` when the type is genuinely unknown, then narrow it.
- Let inference do the work, but be explicit at API boundaries.

## 1. No `any`

`any` disables the type checker. There is almost always a better alternative.

### Bad

```typescript
function parseData(data: any) {
  return data.patient;
}
```

### Good

```typescript
function parseData(data: unknown) {
  if (typeof data === "object" && data !== null && "patient" in data) {
    return (data as { patient: Patient }).patient;
  }
  throw new Error("Invalid data");
}
```

Even better: use Zod (see section 6).

## 2. Avoid `as` casts

`as` tells TypeScript to stop checking. Use it only when you genuinely know more than the compiler, and never to silence an error you do not understand.

### Bad

```typescript
const patient = response.json() as Patient;
```

### Good

```typescript
const patient = patientSchema.parse(await response.json());
```

If you must cast, prefer `satisfies` to preserve inference:

```typescript
const config = {
  apiBaseUrl: "http://localhost:8080/api",
} satisfies Config;
```

## 3. Use branded types for IDs

String IDs are easy to confuse. Brand them so the compiler catches mix-ups.

```typescript
declare const brand: unique symbol;

type Brand<T, TBrand> = T & { [brand]: TBrand };

export type PatientId = Brand<string, "PatientId">;
export type EncounterId = Brand<string, "EncounterId">;
export type UserDid = Brand<string, "UserDid">;

function toPatientId(id: string): PatientId {
  return id as PatientId;
}
```

Use them in functions and components:

```typescript
function getPatient(id: PatientId): Promise<Patient> { ... }

getPatient(encounterId); // Type error
```

## 4. Discriminated unions for state

Model async state as a discriminated union. Do not use booleans like `isLoading` + `error`.

### Bad

```typescript
const [data, setData] = useState<Patient | null>(null);
const [isLoading, setIsLoading] = useState(false);
const [error, setError] = useState<string | null>(null);
```

### Good

```typescript
type AsyncState<T> =
  | { status: "idle" }
  | { status: "loading" }
  | { status: "success"; data: T }
  | { status: "error"; error: Error };

const [state, setState] = useState<AsyncState<Patient>>({ status: "idle" });
```

Render exhaustively:

```typescript
switch (state.status) {
  case "idle": return <p>Enter a patient ID</p>;
  case "loading": return <p>Loading...</p>;
  case "success": return <PatientDetail patient={state.data} />;
  case "error": return <p>Error: {state.error.message}</p>;
}
```

## 5. Type React components correctly

### Function components

```typescript
interface PatientCardProps {
  patient: Patient;
  onSelect?: (id: PatientId) => void;
}

export function PatientCard({ patient, onSelect }: PatientCardProps) {
  // ...
}
```

Do not use `React.FC`.

### `forwardRef`

```typescript
import { forwardRef, ComponentPropsWithoutRef } from "react";

interface ButtonProps extends ComponentPropsWithoutRef<"button"> {
  variant?: "primary" | "secondary";
}

export const Button = forwardRef<HTMLButtonElement, ButtonProps>(
  ({ variant = "primary", ...props }, ref) => {
    return <button ref={ref} data-variant={variant} {...props} />;
  }
);

Button.displayName = "Button";
```

### Generic components

```typescript
interface DataTableProps<T> {
  rows: T[];
  columns: Array<{ key: keyof T; header: string }>;
}

export function DataTable<T>({ rows, columns }: DataTableProps<T>) {
  // ...
}
```

## 6. Validate all external data with Zod

Anything that crosses the runtime boundary — API responses, localStorage, query params — must be validated.

```typescript
import { z } from "zod";

export const genderSchema = z.enum(["MALE", "FEMALE", "OTHER", "UNKNOWN"]);

export const patientSchema = z.object({
  id: z.string().uuid(),
  firstName: z.string().min(1),
  lastName: z.string().min(1),
  dateOfBirth: z.string().date(),
  phoneNumber: z.string().min(1),
  gender: genderSchema,
  createdAt: z.string().datetime(),
});

export type Patient = z.infer<typeof patientSchema>;
```

Use it in API clients:

```typescript
export async function getPatient(id: PatientId): Promise<Patient> {
  const response = await fetch(`${API_BASE_URL}/patients/${id}`);
  if (!response.ok) {
    throw new Error(`Failed to fetch patient: ${response.status}`);
  }
  return patientSchema.parse(await response.json());
}
```

## 7. Derive types, do not duplicate them

If the backend contract is already defined in Zod, derive frontend types from it. Do not maintain parallel TypeScript interfaces.

```typescript
// Good
export type RegisterPatientRequest = z.infer<typeof registerPatientRequestSchema>;

// Bad: manually writing this and hoping it stays in sync
export interface RegisterPatientRequest {
  firstName: string;
  // ...
}
```

## 8. Environment variables

Never trust `process.env` values at runtime. Validate them with Zod.

```typescript
import { z } from "zod";

const envSchema = z.object({
  NEXT_PUBLIC_API_BASE_URL: z.string().url(),
});

export const env = envSchema.parse(process.env);
```

## 9. Function return types

Be explicit about return types at module boundaries. Inside pure functions, let TypeScript infer.

```typescript
// Explicit: this is a public API
export async function registerPatient(data: RegisterPatientRequest): Promise<Patient> { ... }

// Inferred: internal helper
function formatPhoneNumber(phone: string) {
  return phone.replace(/\s/g, "");
}
```

## 10. Type narrowing over type assertions

Always narrow before asserting.

```typescript
function handleEvent(event: PatientEvent | EncounterEvent) {
  if (event.type === "patient_registered") {
    // TypeScript knows this is PatientEvent here
    return event.patientId;
  }
  // TypeScript knows this is EncounterEvent here
  return event.encounterId;
}
```

## Summary

- ❌ No `any`
- ❌ No `as` to silence errors
- ✅ Branded IDs
- ✅ Discriminated unions for state
- ✅ Zod for external data
- ✅ Derive types from schemas
- ✅ Explicit return types at boundaries
- ✅ Narrow, don't assert
