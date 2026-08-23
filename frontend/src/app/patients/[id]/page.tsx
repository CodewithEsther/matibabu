import Link from "next/link";
import { getPatient } from "@/lib/api/patients";

interface PageProps {
  params: Promise<{ id: string }>;
}

export default async function PatientDetailPage({ params }: PageProps) {
  const { id } = await params;

  let patient;
  try {
    patient = await getPatient(id);
  } catch {
    return (
      <main className="mx-auto max-w-xl p-6">
        <h1 className="mb-4 text-2xl font-semibold">Patient not found</h1>
        <Link href="/" className="text-blue-600 hover:underline">
          Back to registration
        </Link>
      </main>
    );
  }

  return (
    <main className="mx-auto max-w-xl p-6">
      <h1 className="mb-6 text-2xl font-semibold">Patient details</h1>

      <section className="rounded-lg border border-zinc-200 bg-white p-6 shadow-sm">
        <dl className="space-y-3">
          <div className="flex justify-between">
            <dt className="text-sm font-medium text-zinc-500">Name</dt>
            <dd className="text-sm">
              {patient.firstName} {patient.lastName}
            </dd>
          </div>

          <div className="flex justify-between">
            <dt className="text-sm font-medium text-zinc-500">Date of birth</dt>
            <dd className="text-sm">{patient.dateOfBirth}</dd>
          </div>

          <div className="flex justify-between">
            <dt className="text-sm font-medium text-zinc-500">Phone</dt>
            <dd className="text-sm">{patient.phoneNumber}</dd>
          </div>

          <div className="flex justify-between">
            <dt className="text-sm font-medium text-zinc-500">Gender</dt>
            <dd className="text-sm">
              {patient.gender.charAt(0) + patient.gender.slice(1).toLowerCase()}
            </dd>
          </div>
        </dl>
      </section>

      <div className="mt-6">
        <Link href="/" className="text-blue-600 hover:underline">
          Back to registration
        </Link>
      </div>
    </main>
  );
}
