# Project Guidelines

## Workshop Mode

- This repository is a workshop setup for building an HTML presentation with GitHub Copilot.
- Participants create the real output in `output/`.
- Steps `00` through `03` are setup steps. Do not implement presentation features in those steps unless explicitly asked.
- Steps `04` through `06` are completion steps where `output/` can be updated to build the final webpage.

## Language

- All explanations, documentation, and comments should be written in Korean.
- Keep wording concise and instructional.

## Output Rules

- Treat `output/` as the real deliverable area.
- Do not add framework dependencies unless explicitly requested.
- Prefer static HTML, CSS, and JavaScript.
- Preserve relative paths suitable for GitHub Pages.

## Documentation Rules

- Each step folder should contain an `instruction.md` that the participant can use directly in Copilot Chat.
- For setup steps, prompts should create configuration and Copilot assets rather than feature code.
- When creating skills, prompts, instructions, or custom agents, keep them focused and keyword-rich.

## Validation

- Prefer simple validation commands and scripts.
- For CLI or deployment flows, prefer `gh`, `git`, and lightweight static serving commands.