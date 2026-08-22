# Patient Registry API Contract

## Base Path

/api/patients

## Patient

A patient contains:

- id: UUID
- firstName: String (non-blank)
- lastName: String (non-blank)
- dateOfBirth: LocalDate (past or present)
- gender: Gender (MALE, FEMALE, OTHER, UNKNOWN)
- phoneNumber: String (unique, valid phone format: 7-15 digits with optional `+` prefix, e.g. `+254712345678`)
- address: String (non-blank)
- createdAt: Instant
- updatedAt: Instant

## Create Patient

POST /api/patients

Request:

{
"firstName": "John",
"lastName": "Kamau",
"dateOfBirth": "1995-06-15",
"gender": "MALE",
"phoneNumber": "+254712345678",
"address": "Nairobi"
}

Response:

201 Created

If validation fails:

400 Bad Request

If a patient with the same phone number already exists:

409 Conflict

## Get Patient

GET /api/patients/{id}

Response:

200 OK

Returns a Patient.

If the patient does not exist:

404 Not Found

## Search Patient by Phone Number

GET /api/patients/search?phoneNumber={phoneNumber}

Response:

200 OK

Returns a Patient.

If the phone number parameter is missing:

400 Bad Request

If no patient exists with the provided phone number:

404 Not Found

## List Patients

GET /api/patients?page=0&size=20

Response:

200 OK

Returns a paginated collection of patients.

## Update Patient

PUT /api/patients/{id}

Request:

{
"firstName": "John",
"lastName": "Kamau",
"dateOfBirth": "1995-06-15",
"gender": "MALE",
"phoneNumber": "+254700000000",
"address": "Mombasa"
}

Response:

200 OK

If validation fails:

400 Bad Request

If the patient does not exist:

404 Not Found

If the phone number is already registered to another patient:

409 Conflict

## Delete Patient

DELETE /api/patients/{id}

Response:

204 No Content

If the patient does not exist:

404 Not Found