# Copilot Instructions — TODO API (Java/Spring Boot)

## 프로젝트 개요

Java 17 + Spring Boot 3.x 기반의 TODO REST API 프로젝트입니다.
인메모리 저장소(List)를 사용하여 TODO 항목의 CRUD 기능을 제공합니다.

## 기술 스택

- **언어**: Java 17
- **프레임워크**: Spring Boot 3.x (Spring Web)
- **빌드 도구**: Gradle (Kotlin DSL)
- **테스트**: JUnit 5 + MockMvc + @SpringBootTest

## 코딩 컨벤션

### 네이밍 규칙
- **클래스**: PascalCase (예: `TodoController`, `TodoResponse`)
- **메서드/변수**: camelCase (예: `getAllTodos`, `nextId`)
- **상수**: UPPER_SNAKE_CASE (예: `MAX_PAGE_SIZE`)
- **패키지**: 소문자 (예: `com.example.todo`)

### DTO 네이밍 패턴
- 생성 요청: `XxxCreateRequest` (예: `TodoCreateRequest`)
- 수정 요청: `XxxUpdateRequest` (예: `TodoUpdateRequest`)
- 응답: `XxxResponse` (예: `TodoResponse`)
- 목록 응답: `XxxListResponse` (예: `TodoListResponse`)

### 언어
- 모든 주석, Javadoc, 커밋 메시지, 에러 메시지는 **한국어**로 작성합니다.
- Javadoc은 표준 Javadoc 스타일을 따릅니다.

### REST API 패턴
- 경로: `/todos` (복수형)
- 상태 코드: 201(생성), 204(삭제), 404(미존재)
- 전체 수정: `PUT`, 부분 수정: `PATCH`
- 페이지네이션: `page`/`size` 쿼리 파라미터 사용

### 테스트 패턴
- `@SpringBootTest` + `@AutoConfigureMockMvc` 사용
- `@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)`로 상태 격리
- 테스트 메서드명: `test_한국어_설명` 형식 (예: `test_할일_생성_성공`)
- Happy path, 유효성 검증 오류, 엣지 케이스, 404 시나리오 커버
