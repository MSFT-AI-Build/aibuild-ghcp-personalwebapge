# 구현 계획

## 생성할 파일
1. `dto/Priority.java` — 우선순위 enum (LOW, MEDIUM, HIGH)
2. `dto/TodoCreateRequest.java` — 생성 요청 record (@NotBlank, @Size 검증)
3. `dto/TodoUpdateRequest.java` — 수정 요청 record (nullable 필드)
4. `dto/TodoResponse.java` — 응답 record
5. `dto/TodoListResponse.java` — 목록 응답 record (페이지네이션)
6. `Todo.java` — 모델 POJO (Priority, updatedAt 추가)
7. `TodoController.java` — REST 컨트롤러 (CRUD + 필터링 + 페이지네이션)
8. `TodoControllerTest.java` — JUnit 5 + MockMvc 테스트

## 구현 순서
1. dto/ 패키지에 record 정의 (스펙 문서 → 코드 변환)
2. TodoControllerTest.java에 테스트 작성 (스펙 기반)
3. Todo.java 모델 업데이트
4. TodoController.java에 엔드포인트 구현 (테스트 통과)

## 주의사항
- 인메모리 ArrayList 저장소 사용 (DB 없음)
- Priority는 enum으로 정의
- 페이지네이션: page는 1부터 시작, 0-based 변환 필요
- @Valid + Jakarta Validation으로 요청 검증
- @DirtiesContext로 테스트 간 상태 초기화
