import RegisterPatientForm from "@/components/RegisterPatientForm";
import Card from "@/components/Card";

export default function Home() {
  return (
    <main className="mx-auto w-full max-w-3xl p-6">
      <h1 className="mb-6 text-3xl font-semibold tracking-tight text-foreground">
        Matibabu
      </h1>
      <Card title="Register patient">
        <RegisterPatientForm />
      </Card>
    </main>
  );
}
