import { InputHTMLAttributes, SelectHTMLAttributes, ReactNode } from "react";
import Label from "./Label";

type InputType = "text" | "tel" | "date" | "email" | "number" | "select";

interface BaseInputProps {
  label: string;
  name: string;
  error?: string;
  helperText?: ReactNode;
  required?: boolean;
  className?: string;
}

interface TextInputProps
  extends BaseInputProps,
    Omit<InputHTMLAttributes<HTMLInputElement>, "name" | "type" | "required"> {
  type?: Exclude<InputType, "select">;
  options?: never;
}

interface SelectInputProps
  extends BaseInputProps,
    Omit<SelectHTMLAttributes<HTMLSelectElement>, "name" | "required"> {
  type: "select";
  options: Array<{ value: string; label: string }>;
}

type InputProps = TextInputProps | SelectInputProps;

export default function Input(props: InputProps) {
  const {
    label,
    name,
    error,
    helperText,
    required,
    className = "",
    type = "text",
  } = props;

  const inputClasses = [
    "mt-1 block w-full rounded-md border px-3 py-2 text-sm text-foreground bg-white",
    "focus:border-foreground focus:outline-none focus:ring-1 focus:ring-foreground",
    error
      ? "border-error focus:border-error focus:ring-error"
      : "border-border",
    className,
  ].join(" ");

  return (
    <div className="w-full">
      <Label htmlFor={name} required={required}>
        {label}
      </Label>

      {type === "select" ? (
        <select
          id={name}
          className={inputClasses}
          {...(props as SelectInputProps)}
        >
          {(props as SelectInputProps).options.map((option) => (
            <option key={option.value} value={option.value}>
              {option.label}
            </option>
          ))}
        </select>
      ) : (
        <input
          id={name}
          className={inputClasses}
          {...(props as TextInputProps)}
        />
      )}

      {helperText && !error && (
        <p className="mt-1 text-xs text-muted">{helperText}</p>
      )}

      {error && (
        <p className="mt-1 text-xs text-error" role="alert">
          {error}
        </p>
      )}
    </div>
  );
}
