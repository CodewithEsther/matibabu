import { ReactNode } from "react";

interface CardProps {
  children: ReactNode;
  className?: string;
  title?: string;
}

export default function Card({ children, className = "", title }: CardProps) {
  return (
    <section
      className={[
        "rounded-lg border border-border bg-white p-6 shadow-sm",
        className,
      ].join(" ")}
    >
      {title && (
        <h2 className="mb-4 text-lg font-medium text-foreground">{title}</h2>
      )}
      {children}
    </section>
  );
}
