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

## Private keys, public keys, and wallets

### What is a private key?

A private key is a large random number. In Cardano, it is a 256-bit value. Whoever knows the private key can prove ownership of the corresponding identity and sign messages on its behalf.

### How is a private key created?

Private keys are generated from randomness. When a wallet is created:

1. A random seed is generated.
2. That seed is converted into a master private key using a deterministic key derivation algorithm.
3. The wallet derives one or more child private keys for different accounts or addresses.

The randomness is designed to be unpredictable. The chance of two people ever generating the same private key is astronomically low — lower than the chance of guessing a specific atom in the observable universe.

### Why does everyone have a unique private key?

Because the key space is enormous. A 256-bit key has approximately 1.15 × 10^77 possible values. For context, there are roughly 10^80 atoms in the observable universe. Even if billions of wallets were created every second for billions of years, collisions would still be practically impossible.

This uniqueness is what makes public-key cryptography viable for identity. We do not need a central registry to guarantee that your key is different from everyone else's.

### How does utxos.dev fit in?

utxos.dev provides a non-custodial wallet. This means:

- The private key is generated inside the user's device or browser.
- utxos.dev does not store the private key on its servers.
- The user controls the key, usually protected by a password, PIN, biometrics, or social-login-backed key recovery.

When the user authenticates to Matibabu, the frontend asks the utxos.dev wallet to sign the backend's nonce. The private key never leaves the wallet.

### Public keys and DIDs

From a private key, a public key is derived mathematically. It is safe to share the public key — it cannot be used to discover the private key.

The `did:web` document publishes the public key. When the backend resolves the DID, it gets the public key and uses it to verify the signature. In this way:

- The private key stays with the user.
- The public key is public.
- The DID document links the identifier to the public key.
- The signature proves the user controls the private key.

### Security implications

- If a user loses their private key, they may lose access to their identity. Recovery mechanisms are the responsibility of the wallet provider.
- If a private key is stolen, the attacker can impersonate the user. This is why the wallet protects the key with authentication.
- Matibabu never sees or stores the private key. It only sees signatures and public keys.

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

The user signs the auth challenge with their Cardano wallet from utxos.dev. That signature is not a generic Ed25519 signature — it follows Cardano message signing standards (CIP-8 / CIP-30).

The verifier must:

- Accept a signature, a message (the nonce), and a public key.
- Return a boolean indicating whether the signature is valid.
- Be implemented using a well-maintained Cardano or Ed25519 library.

Signature verification is a pure cryptographic operation: the backend has the public key from the DID document, the message (nonce), and the signature from the wallet. A local library can verify this combination without any network call.

Open decision: which local library to use. Options include:

| Option | Pros | Cons |
|---|---|---|
| `cardano-client-lib` (Java) | Native Java, no external service | Adds dependency, must keep updated |
| Bouncy Castle for raw Ed25519 | Lightweight | You must manually parse the CIP-8 structure |

**Recommendation:** use `cardano-client-lib` if it supports CIP-8/CIP-30 message signing. Fall back to Bouncy Castle only if the Cardano library proves too heavy or incomplete.

Network-based verification (external APIs like Blockfrost or Koios, or a separate microservice) is intentionally out of scope. Healthcare authentication should not depend on a third-party network call every time someone logs in.

#### Purpose

The signature verifier proves that the user owns the private key associated with their `did:web` identifier. The backend knows the user's public key from the resolved DID document. By signing the backend's nonce with their private key, the user demonstrates control of that key. The verifier checks whether the signature, message, and public key are cryptographically consistent.

This replaces the password check in traditional authentication.

#### Performance

Local signature verification is fast and should not make signup or login slow. It is a pure cryptographic operation, typically measured in milliseconds.

| Step | Typical speed | Notes |
|---|---|---|
| Resolve `did:web` document | 50–300 ms | HTTPS network call; the only network step |
| Verify signature | 1–10 ms | Local cryptographic operation |
| Create or look up user identity | 5–20 ms | Database write or read |
| Issue JWT | <1 ms | Local signing |

The DID resolution is the only network-dependent step. To reduce repeated network calls, the backend may cache resolved DID documents for a short period (e.g., 5 minutes).

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

## Decisions made

1. **Signature verification** — use a local Java library. Prefer `cardano-client-lib` if it supports CIP-8/CIP-30; fall back to Bouncy Castle with manual CIP-8 parsing. Network-based verification is out of scope.

## Open decisions

1. What should the JWT access token expiry be?

   Options:

   - 15 minutes — secure, but users re-authenticate often.
   - 1 hour — common for healthcare dashboards.
   - 24 hours — convenient, but risky if stolen.

2. Should the system issue refresh tokens, or require wallet re-signing?

   Options:

   - **No refresh tokens** — user must sign a new challenge with their wallet. Most decentralised, but annoying.
   - **Long-lived refresh token** — backend issues a refresh token; frontend silently gets new access tokens. Better UX, more like Clerk/Auth0.
   - **Session cookie** — traditional web session; less "decentralised" but simple.

   For a healthcare app, wallet signing every 15 minutes is probably unacceptable. Refresh tokens are likely the right choice.

3. Will providers use `did:web` anchored to their own domains, or to a Matibabu subdomain?

## Related decisions

- ADR-001: Clean Architecture with Feature-Oriented Packaging
- ADR-004: UUID v7 for Entity Identity
- ADR-005: DTOs at the API Boundary
- ADR-007: Global API Exception Translation
