"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { registerPatient } from "@/lib/api/patients";
import { Gender, RegisterPatientRequest } from "@/types/patient";
import Button from "./Button";
import Input from "./Input";

const GENDERS: Array<{ value: Gender; label: string }> = [
  { value: "MALE", label: "Male" },
  { value: "FEMALE", label: "Female" },
  { value: "OTHER", label: "Other" },
  { value: "UNKNOWN", label: "Unknown" },
];

export default function RegisterPatientForm() {
  const router = useRouter();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleSubmit(formData: FormData) {
    setIsSubmitting(true);
    setError(null);

    const request: RegisterPatientRequest = {
      firstName: formData.get("firstName") as string,
      lastName: formData.get("lastName") as string,
      dateOfBirth: formData.get("dateOfBirth") as string,
      phoneNumber: formData.get("phoneNumber") as string,
      gender: formData.get("gender") as Gender,
    };

    try {
      const patient = await registerPatient(request);
      router.push(`/patients/${patient.id}`);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Something went wrong");
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <form action={handleSubmit} className="space-y-4">
      {error && (
        <div
          className="rounded-md border border-error bg-error-bg p-3 text-sm text-error"
          role="alert"
        >
          {error}
        </div>
      )}

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <Input
          label="First name"
          name="firstName"
          type="text"
          required
          autoComplete="given-name"
        />

        <Input
          label="Last name"
          name="lastName"
          type="text"
          required
          autoComplete="family-name"
        />
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <Input
          label="Date of birth"
          name="dateOfBirth"
          type="date"
          required
        />

        <Input
          label="Phone number"
          name="phoneNumber"
          type="tel"
          required
          placeholder="+254712345678"
          autoComplete="tel"
        />
      </div>

      <Input
        label="Gender"
        name="gender"
        type="select"
        required
        options={[
          { value: "", label: "Select gender" },
          ...GENDERS,
        ]}
      />

      <Button type="submit" disabled={isSubmitting}>
        {isSubmitting ? "Registering..." : "Register patient"}
      </Button>
    </form>
  );
}
