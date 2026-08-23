import { Patient, RegisterPatientRequest } from "@/types/patient";

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080/api";

export async function registerPatient(data: RegisterPatientRequest): Promise<Patient> {
  const response = await fetch(`${API_BASE_URL}/patients`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    const error = await response.text();
    throw new Error(error || `Failed to register patient: ${response.status}`);
  }

  return response.json();
}

export async function getPatient(id: string): Promise<Patient> {
  const response = await fetch(`${API_BASE_URL}/patients/${id}`, {
    cache: "no-store",
  });

  if (!response.ok) {
    const error = await response.text();
    throw new Error(error || `Failed to fetch patient: ${response.status}`);
  }

  return response.json();
}
