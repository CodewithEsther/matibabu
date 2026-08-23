import { LabelHTMLAttributes, ReactNode } from "react";

interface LabelProps extends LabelHTMLAttributes<HTMLLabelElement> {
  children: ReactNode;
  required?: boolean;
}

export default function Label({
  children,
  required,
  className = "",
  ...props
}: LabelProps) {
  return (
    <label
      className={["block text-sm font-medium text-foreground", className].join(
        " "
      )}
      {...props}
    >
      {children}
      {required && <span className="ml-1 text-error">*</span>}
    </label>
  );
}
