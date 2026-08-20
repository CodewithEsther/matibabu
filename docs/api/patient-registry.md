# Patient Registry API Contract

## Base Path

/api/patients

## Patient

A patient contains:

- id: UUID
- firstName: String
- lastName: String
- dateOfBirth: LocalDate
- gender: Gender
- phoneNumber: String
- address: String
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

## Get Patient

GET /api/patients/{id}

Response:

200 OK

Returns a Patient.

If the patient does not exist:

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

## Delete Patient

Not implemented in the initial version.

The deletion/archival strategy must be agreed upon before implementation.