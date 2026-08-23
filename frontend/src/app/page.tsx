import RegisterPatientForm from "@/components/RegisterPatientForm";

export default function Home() {
  return (
    <main className="mx-auto max-w-xl p-6">
      <h1 className="mb-6 text-2xl font-semibold">Matibabu</h1>
      <section className="rounded-lg border border-zinc-200 bg-white p-6 shadow-sm">
        <h2 className="mb-4 text-lg font-medium">Register patient</h2>
        <RegisterPatientForm />
      </section>
    </main>
  );
}
