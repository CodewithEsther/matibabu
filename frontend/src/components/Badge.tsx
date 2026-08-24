import { ReactNode } from "react";

type BadgeVariant = "neutral" | "success" | "error" | "warning";

interface BadgeProps {
  children: ReactNode;
  variant?: BadgeVariant;
  className?: string;
}

const variantClasses: Record<BadgeVariant, string> = {
  neutral: "bg-surface-secondary text-foreground",
  success: "bg-success-bg text-success",
  error: "bg-error-bg text-error",
  warning: "bg-yellow-50 text-warning",
};

export default function Badge({
  children,
  variant = "neutral",
  className = "",
}: BadgeProps) {
  return (
    <span
      className={[
        "inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium",
        variantClasses[variant],
        className,
      ].join(" ")}
    >
      {children}
    </span>
  );
}
