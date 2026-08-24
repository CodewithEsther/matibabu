import { ButtonHTMLAttributes, ReactNode } from "react";

type ButtonVariant = "primary" | "secondary" | "danger" | "ghost";

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: ButtonVariant;
  children: ReactNode;
}

const variantClasses: Record<ButtonVariant, string> = {
  primary:
    "bg-primary text-primary-text hover:bg-foreground disabled:bg-foreground",
  secondary:
    "bg-white text-foreground border border-border hover:bg-surface-secondary disabled:bg-white",
  danger:
    "bg-error text-white hover:opacity-90 disabled:opacity-50",
  ghost:
    "bg-transparent text-foreground hover:bg-surface-secondary disabled:bg-transparent",
};

export default function Button({
  variant = "primary",
  children,
  className = "",
  disabled,
  ...props
}: ButtonProps) {
  return (
    <button
      type={props.type ?? "button"}
      disabled={disabled}
      className={[
        "inline-flex items-center justify-center rounded-md px-6 py-2 text-sm font-medium transition-colors",
        "focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-foreground",
        "disabled:cursor-not-allowed disabled:opacity-50",
        variantClasses[variant],
        className,
      ].join(" ")}
      {...props}
    >
      {children}
    </button>
  );
}
