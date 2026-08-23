> This document adapts the principles from the [Logos Brand Guidelines](https://guide.logos.co/) to the Matibabu healthcare information system. It is not a Logos sub-brand document; it uses the Logos philosophy of clarity, function, and accessibility as a starting point for Matibabu's own design direction.

# Matibabu Design Direction

## 1. Design philosophy

Matibabu exists to help healthcare workers register patients, record encounters, and access clinical information with as little friction as possible. The interface is not the product — the care is. Design should therefore get out of the way.

We treat design as a tool for clarity and trust, not spectacle. Every visual choice should help a user complete a task more confidently and with fewer errors. Where the Logos guidelines describe design as activism, Matibabu's equivalent is **design as care**: the interface should be calm, predictable, and respectful of the people using it and the patients whose records it holds.

### Core principles

1. **Substance over spectacle**  
   Avoid decoration that does not serve a function. Colour, animation, and imagery should only appear when they improve understanding, reduce error, or signal state.

2. **Function first**  
   Components and layouts must be chosen for how well they support real workflows: registering a patient during a busy clinic, finding a record quickly, confirming identity before care.

3. **Clarity and honesty**  
   Use plain language. Avoid medical jargon where possible. State errors, empty states, and confirmations directly. Do not hide important information behind interactions.

4. **Inclusion and accessibility**  
   The interface must work on low-resolution screens, unstable networks, and in bright or dim clinical environments. Follow WCAG 2.2 AA contrast and keyboard-accessibility requirements by default.

5. **Modularity and restraint**  
   Build from a small set of reusable patterns. Prefer system defaults and browser-native behaviour where they are sufficient. Do not invent new components for one-off effects.

## 2. Voice and tone

Matibabu communicates in a voice that is:

- **Clear** — short sentences, plain words, no ambiguity.  
  *"Patient registered"* not *"The patient registration operation has been completed successfully."*
- **Sincere** — direct and without exaggeration.  
  *"No patients found"* not *"Oops, looks like nothing is here!"*
- **Respectful** — calm and professional. Healthcare workers may be under pressure; the interface should not add anxiety.
- **Confident** — decisive labels and actions.  
  *"Register patient"*, *"View record"*, *"Confirm discharge"*.

### Language conventions

- Use **British English** spelling (e.g., *colour*, *centre*, *organisation*).
- Follow **Oxford style**, including the Oxford comma.
- Avoid idioms, slang, or culturally specific humour.
- Use sentence case for headings and labels: *"Patient details"* not *"Patient Details"*.
- Write button labels as verbs: *"Save"*, *"Register"*, *"Cancel"*.

## 3. Colour

The palette is intentionally minimal. The default experience is dark text on a light surface. Colour is used only when it carries meaning.

### Base palette

| Token | Value | Usage |
|-------|-------|-------|
| `foreground` | `#171717` | Primary text, borders, icons |
| `background` | `#ffffff` | Page and card surfaces |
| `muted` | `#737373` | Secondary text, placeholders, disabled states |
| `surface-secondary` | `#f4f4f5` | Subtle backgrounds, alternating rows, input backgrounds |
| `border` | `#e4e4e7` | Dividers, input borders, card outlines |

### Semantic colours

| Token | Value | Usage |
|-------|-------|-------|
| `primary` | `#171717` | Primary actions, active states, emphasis |
| `primary-text` | `#ffffff` | Text on primary backgrounds |
| `error` | `#dc2626` | Errors, destructive actions, critical alerts |
| `error-bg` | `#fef2f2` | Error backgrounds and banners |
| `success` | `#16a34a` | Success confirmations |
| `success-bg` | `#f0fdf4` | Success backgrounds |
| `warning` | `#ca8a04` | Warnings that need attention but are not blocking |
| `link` | `#2563eb` | Hyperlinks only. This is the only permitted non-neutral colour by default. |

### Rules

- Use neutral colours for 95% of the interface.
- Reserve semantic colours for status, feedback, and actions.
- Never rely on colour alone to convey information. Pair it with text, icons, or labels.
- Maintain WCAG 2.2 AA contrast ratios for all text and interactive elements.
- Prefer light mode as the default. A dark mode may be added later if clinical users request it.

## 4. Typography

The type system is built for readability under pressure. Use a single sans-serif family across the product.

### Font

- **Primary**: system sans-serif stack (`ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif`)
- **Monospace**: system monospace stack for IDs, timestamps, and technical values (`ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace`)

Using system fonts reduces load time and respects platform conventions, consistent with the Logos preference for default platform systems.

### Scale

| Style | Size | Line height | Weight | Usage |
|-------|------|-------------|--------|-------|
| Display | 2.25rem (36px) | 2.5rem | 600 | Page titles |
| Heading 1 | 1.875rem (30px) | 2.25rem | 600 | Major section headings |
| Heading 2 | 1.5rem (24px) | 2rem | 600 | Section headings |
| Heading 3 | 1.25rem (20px) | 1.75rem | 500 | Subsection headings |
| Body | 1rem (16px) | 1.5rem | 400 | Paragraphs, labels, descriptions |
| Body small | 0.875rem (14px) | 1.25rem | 400 | Captions, metadata, helper text |
| Label | 0.75rem (12px) | 1rem | 500 | Badges, timestamps, tags |

### Rules

- Use a maximum of three weights in any view: regular (400), medium (500), and semibold (600).
- Avoid all-caps except for small, non-critical labels such as badges.
- Keep line lengths to roughly 60–75 characters for body text.
- Use monospace for patient IDs and UUIDs to improve scannability.

## 5. Spacing and layout

### Base unit

The base spacing unit is **8px**. All spacing values are multiples of 8.

| Token | Value |
|-------|-------|
| `space-1` | 4px |
| `space-2` | 8px |
| `space-3` | 16px |
| `space-4` | 24px |
| `space-5` | 32px |
| `space-6` | 48px |
| `space-7` | 64px |

### Layout grid

- **Desktop**: 12 columns, max content width 1280px, 16px gutters.
- **Tablet**: 8 columns.
- **Mobile**: 4 columns, single-column forms.

### Rules

- Use generous whitespace around primary actions and record summaries.
- Stack related fields closely; separate unrelated sections clearly.
- Forms should use a single column on mobile and up to two columns on desktop.
- Avoid full-width text blocks. Limit paragraph widths to improve readability.

## 6. Components

### Buttons

| Variant | Usage |
|---------|-------|
| Primary | The main action on a page or form (e.g., *Register patient*) |
| Secondary | Alternative actions that do not submit or create (e.g., *Cancel*) |
| Danger | Destructive actions that delete or remove data |
| Ghost | Low-emphasis actions inside lists or cards |

- Buttons use sentence-case labels.
- Primary buttons use the `primary` background with `primary-text` colour.
- Disabled buttons reduce opacity; do not change the label.

### Inputs

- Use clear, short labels above the input.
- Show helper text only when it prevents errors.
- Display validation errors below the input in `error` colour with a brief message.
- Required fields are marked; optional fields are not marked unless most fields are optional.

### Cards and surfaces

- Use cards to group related information, such as patient identity or encounter summary.
- Card borders use `border` colour; background uses `background`.
- Avoid shadows for elevation. Use borders and spacing to create hierarchy.

### Tables and lists

- Use tables for structured data with many rows (e.g., patient search results).
- Use lists or cards when the data is scannable and has fewer columns.
- Align numbers to the right; align text to the left.

## 7. Iconography and imagery

### Icons

- Use a single icon library, preferably a line-style set at 1.5px stroke.
- Icons should clarify, not decorate. Every icon needs a text label or accessible name.
- Avoid 3D illustration, gradients, or decorative photography in core workflows.

### Imagery

- Clinical photography of people is discouraged unless explicitly required and consent is documented.
- When illustration is needed, prefer simple geometric or diagrammatic styles that explain a concept.

## 8. Accessibility

Accessibility is not a separate consideration. It is a baseline requirement.

- Meet **WCAG 2.2 AA** for contrast, focus states, and keyboard navigation.
- All interactive elements must have visible focus indicators.
- Form inputs must have associated labels.
- Use ARIA landmarks and live regions for dynamic feedback.
- Support keyboard-only navigation for all primary workflows.
- Test with screen readers and at 200% zoom.

## 9. Frontend implementation

This section maps the design direction to the existing Next.js codebase.

### Files to follow

- `frontend/src/app/globals.css` — global styles and CSS variables.
- `frontend/src/components/` — reusable UI components.
- `frontend/tailwind.config.ts` or equivalent — theme tokens (colours, spacing, typography).

### Recommended next steps

1. Replace the default Geist font configuration with the system font stack if clinical users are on lower-end devices.
2. Define the colour tokens above as CSS custom properties in `globals.css` and map them to Tailwind theme values.
3. Create a small set of components: `Button`, `Input`, `Label`, `Card`, `Badge`.
4. Refactor the patient registration form to use the new components.
5. Add a dark mode only after the light mode design is stable and tested in clinical settings.

## 10. References

- [Logos Brand Guidelines](https://guide.logos.co/)
- [Logos Philosophy](https://guide.logos.co/philosophy/)
- [Logos Voice](https://guide.logos.co/voice/)
- [Logos Colour](https://guide.logos.co/visual-language/color)
- [Logos Logo](https://guide.logos.co/visual-language/logo)
- [Logos Grid and Layout — Web Environment](https://guide.logos.co/visual-language/grid-and-layout/web-environment)
- [WCAG 2.2](https://www.w3.org/WAI/WCAG22/quickref/)
