# ADR-02: Decentralised Identity and Authentication with did:web and utxos.dev

## Status

Proposed

## Date

2026-08-23

## Context

Matibabu needs an authentication mechanism for the web frontend and future mobile clients. Because the project is exploring decentralised identity, the system should allow users (patients and providers) to authenticate without relying on a centralised username/password database.

After evaluating several options, the team wants to use:

- **utxos.dev** as a Wallet-as-a-Service for non-custodial Cardano wallets.
- **did:web** as the decentralised identifier method.

This decision separates key management (utxos.dev) from identifier resolution (did:web).

## Decision

Matibabu will use `did:web` identifiers and Cardano wallet signatures for authentication.

### Why did:web?

- It resolves over HTTPS, so the backend does not need a blockchain node or Sidetree infrastructure.
- Identifiers can be anchored to domains the project already controls.
- It is simpler to operate than blockchain-based DID methods such as `did:ada`.

## Authentication flow

```text
User
  ↓ clicks "Sign In"
Next.js frontend
  ↓ utxos.dev SDK creates/loads Cardano wallet
  ↓ frontend derives or registers did:web for the user
  ↓ POST /api/auth/challenge { did }
Spring Boot backend
  ↓ generates nonce, stores it temporarily
  ↓ returns { nonce }
Next.js frontend
  ↓ asks wallet to sign nonce
  ↓ POST /api/auth/verify { did, nonce, signature }
Spring Boot backend
  ↓ resolves did:web to DID document over HTTPS
  ↓ extracts public key from DID document
  ↓ verifies signature against nonce
  ↓ issues JWT access token (and optional refresh token)
```

## Backend requirements

The backend must provide the following capabilities.

### 1. Spring Security

Add Spring Security to the project and configure a security filter chain that:

- Permits public access to `/api/auth/**`.
- Requires a valid JWT for all other `/api/**` endpoints.
- Disables default form login.

### 2. DID resolver service

Create a service that resolves a `did:web` identifier to a DID document.

Example:

```text
did:web:matibabu.co:patients:alice
```

resolves to:

```text
https://matibabu.co/.well-known/did.json
```

or, for paths:

```text
https://matibabu.co/patients/alice/did.json
```

The service must:

- Fetch the DID document over HTTPS.
- Cache it briefly to avoid repeated network calls.
- Handle resolution failures cleanly.

### 3. Signature verifier

Create a service that verifies Cardano wallet signatures.

The verifier must:

- Accept a signature, a message (the nonce), and a public key.
- Return a boolean indicating whether the signature is valid.
- Be implemented using a well-maintained Cardano or Ed25519 library.

Open decision: which library or service to use. Options include:

- `cardano-client-lib` (Java) — pure local verification, no network call.
- Bouncy Castle with manual CIP-8 parsing — pure local verification, no network call.
- A small verification microservice in another language — requires a network call to the service.
- An external API such as Blockfrost or Koios — requires a network call to a third party.

Note: signature verification is a pure cryptographic operation. A network call is only required if the verification is outsourced to an external service or microservice.

### 4. Auth controller

Add an auth controller with two endpoints:

#### POST /api/auth/challenge

Request:

```json
{
  "did": "did:web:matibabu.co:patients:alice"
}
```

Response:

```json
{
  "nonce": "random-string"
}
```

The backend stores the nonce temporarily, associated with the DID, with a short expiry (e.g., 5 minutes).

#### POST /api/auth/verify

Request:

```json
{
  "did": "did:web:matibabu.co:patients:alice",
  "nonce": "random-string",
  "signature": "signature-bytes-in-hex-or-base64"
}
```

Response on success:

```json
{
  "accessToken": "jwt",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

Response on failure:

```json
{
  "error": "Invalid signature"
}
```

### 5. JWT issuer and verifier

The backend must issue and verify JWTs.

- Access tokens should be short-lived (open decision: 15 minutes or 1 hour).
- Consider refresh tokens for better user experience.
- Store the signing key securely (not in source control).

### 6. DID-to-user mapping

Create a table that maps a DID to an internal user record.

```text
user_identities
  id
  did
  user_type (PATIENT, PROVIDER, ADMIN)
  created_at
  updated_at
```

When a user authenticates for the first time, the system creates a new identity record. On subsequent logins, it links the DID to the existing record.

## Frontend requirements

- Integrate the utxos.dev SDK.
- Derive or register a `did:web` for the authenticated wallet.
- Send the DID to `/api/auth/challenge`.
- Sign the returned nonce with the wallet.
- Send the signature to `/api/auth/verify`.
- Store the access token and send it in the `Authorization` header for subsequent requests.

## Consequences

### Positive

- No centralised password database.
- Users control their own keys.
- Easier to operate than blockchain-anchored DID methods.
- Aligns with the project's decentralised identity goals.

### Negative

- The backend must resolve and verify DIDs, which adds complexity.
- Cardano signature verification in Java is not trivial.
- Users must have a wallet; this may create friction for non-technical users.
- If the `did:web` host is unavailable, authentication fails.

## Open decisions

1. Which Java or external library will verify Cardano signatures?
2. What should the JWT access token expiry be?
3. Should the system issue refresh tokens, or require wallet re-signing?
4. Will providers use `did:web` anchored to their own domains, or to a Matibabu subdomain?

## Related decisions

- ADR-001: Clean Architecture with Feature-Oriented Packaging
- ADR-004: UUID v7 for Entity Identity
- ADR-005: DTOs at the API Boundary
- ADR-007: Global API Exception Translation
