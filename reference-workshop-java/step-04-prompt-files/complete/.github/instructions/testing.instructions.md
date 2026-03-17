---
applyTo: "**/test/**"
---
# 테스트 규칙

## 프레임워크
- JUnit 5 + @SpringBootTest + MockMvc

## 네이밍
- 메서드명: test_동작_조건_결과()
  - 예: test_createTodo_withValidData_returns201()
  - 예: test_getTodo_withInvalidId_returns404()

## 구조
- @Nested 클래스로 엔드포인트별 그룹핑
- Given-When-Then 주석 패턴 사용
  - 예시:
    ```java
    @Test
    void test_createTodo_withValidData_returns201() throws Exception {
        // Given: 유효한 TODO 데이터
        String json = """
            {"title": "테스트", "description": "설명"}
            """;

        // When: POST /todos 요청
        var result = mockMvc.perform(post("/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json));

        // Then: 201 상태 코드와 생성된 TODO 반환
        result.andExpect(status().isCreated())
              .andExpect(jsonPath("$.title").value("테스트"));
    }
    ```
- 각 테스트는 독립적 (@Transactional 사용)

## 커버리지
- 정상 케이스 + 에러 케이스 + 경계값 각각 최소 1개
- 모든 HTTP 상태 코드 테스트
