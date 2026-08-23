export type Gender = "MALE" | "FEMALE" | "OTHER" | "UNKNOWN";

export interface Patient {
  id: string;
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  phoneNumber: string;
  gender: Gender;
  createdAt: string;
}

export interface RegisterPatientRequest {
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  phoneNumber: string;
  gender: Gender;
}
