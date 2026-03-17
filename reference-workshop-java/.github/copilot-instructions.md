# Copilot Instructions — GitHub Copilot Workshop (Java Edition)

## Project Overview

This is a **progressive tutorial workshop** that teaches GitHub Copilot features by building a TODO API with Spring Boot. Each `step-XX-*` folder is a self-contained lesson with `starter/` (starting code) and `complete/` (reference answer). Steps 00–08 are the main track; 09–12 are bonus tracks (README-only guides).

**This is educational content, not a production application.** When modifying code, preserve the progressive learning design — each step must only use Copilot features introduced up to that point.

## Language & Locale

- All comments, Javadoc, commit messages, error messages, and exception messages must be in **Korean**.
- Javadoc follows standard Javadoc style in Korean.

## Tech Stack

- Java 17+, Spring Boot 3.x, Spring Data JPA, H2 Database (local file / in-memory)
- Build: Gradle (Kotlin DSL)
- Testing: JUnit 5 + MockMvc + Spring Boot Test
- No external services required — everything runs locally

## Architecture Patterns

### File Organization (steps 06–08)

```
src/main/java/com/example/todo/
├── config/          — DB 설정, WebConfig 등
├── controller/      — @RestController 클래스 (API 엔드포인트)
├── dto/             — Request/Response DTO (record 클래스)
├── entity/          — @Entity JPA 엔티티 클래스 (DB 스키마)
├── repository/      — Spring Data JPA Repository 인터페이스
├── service/         — @Service 비즈니스 로직 클래스
└── TodoApplication.java — @SpringBootApplication 메인 클래스

src/test/java/com/example/todo/
├── controller/      — MockMvc 기반 컨트롤러 테스트
└── service/         — 서비스 단위 테스트
```

**Key separation**: JPA Entity(`@Entity`) lives in `entity/`; API DTOs(`record`) live in `dto/`. Never mix them.

### CRUD Endpoint Pattern

```java
@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoResponse createTodo(@Valid @RequestBody TodoCreateRequest request) {
        return todoService.createTodo(request);
    }
}
```

- Use `@Valid` for request validation
- Status codes: 201 create, 204 delete, 404 not found (`ResponseStatusException`)
- Partial updates via `PATCH` with null-check on each field
- Pagination: `page`/`size` query params with `@RequestParam` + `@Min(1)` validation

### Testing Pattern

- Tests use **H2 in-memory DB** via `@SpringBootTest` / `@DataJpaTest`
- Controller tests use `@AutoConfigureMockMvc` + `MockMvc`
- Group tests by endpoint in nested `@Nested` classes: `CreateTodo`, `ListTodos`, etc.
- Use `@BeforeEach` for test data setup
- Cover: happy path, validation errors, edge cases, 404 scenarios

## Step Progression & Constraints

| Steps | Code style | DB | Key constraint |
|-------|-----------|-----|----------------|
| 01–05 | In-memory `List<Map>` or `List<Todo>` | None | No database, no JPA |
| 06–08 | Spring Data JPA + Entity | H2 | Full ORM with Repository pattern |

When editing a specific step's code, use only patterns available at that step level. For example, step-03 code should not use `dto/` package (introduced in step-04).

## Developer Commands

```bash
# Run the API server (from any step's starter/ or complete/ directory)
./gradlew bootRun

# Run tests
./gradlew test

# Build
./gradlew build
```

## Copilot Config Files Inside Steps

Steps contain `.github/` directories as **tutorial artifacts** (copilot-instructions.md, instructions/, prompts/, agents/, skills/). These demonstrate Copilot configuration to workshop participants — treat them as lesson content, not as active project config.
