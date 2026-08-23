import Link from "next/link";
import { getPatient } from "@/lib/api/patients";
import Card from "@/components/Card";
import Badge from "@/components/Badge";

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
      <main className="mx-auto w-full max-w-3xl p-6">
        <h1 className="mb-4 text-3xl font-semibold tracking-tight text-foreground">
          Patient not found
        </h1>
        <Link href="/" className="text-link hover:underline">
          Back to registration
        </Link>
      </main>
    );
  }

  return (
    <main className="mx-auto w-full max-w-3xl p-6">
      <h1 className="mb-6 text-3xl font-semibold tracking-tight text-foreground">
        Patient details
      </h1>

      <Card>
        <div className="mb-4 flex items-center justify-between">
          <h2 className="text-lg font-medium text-foreground">
            {patient.firstName} {patient.lastName}
          </h2>
          <Badge>Registered</Badge>
        </div>

        <dl className="divide-y divide-border">
          <DetailRow label="Patient ID" value={patient.id} isMonospace />
          <DetailRow label="Date of birth" value={patient.dateOfBirth} />
          <DetailRow label="Phone" value={patient.phoneNumber} />
          <DetailRow
            label="Gender"
            value={
              patient.gender.charAt(0) +
              patient.gender.slice(1).toLowerCase()
            }
          />
          <DetailRow
            label="Registered"
            value={new Date(patient.createdAt).toLocaleString("en-GB")}
          />
        </dl>
      </Card>

      <div className="mt-6">
        <Link href="/" className="text-link hover:underline">
          Back to registration
        </Link>
      </div>
    </main>
  );
}

function DetailRow({
  label,
  value,
  isMonospace,
}: {
  label: string;
  value: string;
  isMonospace?: boolean;
}) {
  return (
    <div className="flex justify-between py-3">
      <dt className="text-sm text-muted">{label}</dt>
      <dd
        className={[
          "text-sm text-foreground",
          isMonospace ? "font-mono" : "",
        ].join(" ")}
      >
        {value}
      </dd>
    </div>
  );
}
