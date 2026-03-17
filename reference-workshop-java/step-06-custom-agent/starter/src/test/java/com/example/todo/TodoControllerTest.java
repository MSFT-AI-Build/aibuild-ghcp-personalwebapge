package com.example.todo;

import com.example.todo.dto.Priority;
import com.example.todo.entity.Todo;
import com.example.todo.repository.TodoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TODO 컨트롤러에 대한 통합 테스트.
 *
 * <p>MockMvc를 사용하여 HTTP 요청/응답을 검증합니다.
 * Spring Data JPA + H2 인메모리 데이터베이스를 사용하며,
 * {@code @Transactional}로 각 테스트 후 데이터를 자동 롤백합니다.</p>
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TodoRepository todoRepository;

    /**
     * 테스트용 TODO 항목을 데이터베이스에 직접 저장하는 헬퍼 메서드.
     *
     * @param title       제목
     * @param description 설명
     * @param priority    우선순위
     * @return 저장된 TODO 엔티티
     */
    private Todo createTestTodo(String title, String description, Priority priority) {
        Todo todo = new Todo();
        todo.setTitle(title);
        todo.setDescription(description);
        todo.setPriority(priority);
        todo.setCompleted(false);
        return todoRepository.save(todo);
    }

    // ========== 생성 (POST /todos) ==========

    /**
     * 할일 생성 성공 테스트.
     * POST /todos 요청 시 201 상태 코드와 생성된 TODO 항목을 반환하는지 검증합니다.
     */
    @Test
    void test_할일_생성_성공() throws Exception {
        String json = """
                {
                    "title": "테스트 할일"
                }
                """;

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("테스트 할일"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    /**
     * 설명과 우선순위가 포함된 할일 생성 성공 테스트.
     * POST /todos 요청 시 모든 필드가 올바르게 설정되는지 검증합니다.
     */
    @Test
    void test_할일_생성_설명_우선순위_포함() throws Exception {
        String json = """
                {
                    "title": "중요한 할일",
                    "description": "상세한 설명입니다",
                    "priority": "HIGH"
                }
                """;

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("중요한 할일"))
                .andExpect(jsonPath("$.description").value("상세한 설명입니다"))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }

    /**
     * 제목 없이 할일 생성 시 유효성 검증 실패 테스트.
     * POST /todos 요청 시 제목이 빈 문자열이면 400을 반환하는지 검증합니다.
     */
    @Test
    void test_할일_생성_제목_누락_시_400() throws Exception {
        String json = """
                {
                    "title": ""
                }
                """;

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    /**
     * 제목이 200자를 초과할 때 유효성 검증 실패 테스트.
     */
    @Test
    void test_할일_생성_제목_길이_초과_시_400() throws Exception {
        String longTitle = "가".repeat(201);
        String json = """
                {
                    "title": "%s"
                }
                """.formatted(longTitle);

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    // ========== 목록 조회 (GET /todos) ==========

    /**
     * 빈 목록 조회 테스트.
     * GET /todos 요청 시 TODO 항목이 없으면 빈 리스트와 메타 정보를 반환하는지 검증합니다.
     */
    @Test
    void test_할일_목록_조회_빈_리스트() throws Exception {
        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(0)))
                .andExpect(jsonPath("$.total").value(0))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(10));
    }

    /**
     * 생성 후 목록 조회 테스트.
     * TODO 항목 생성 후 GET /todos 요청 시 목록에 포함되는지 검증합니다.
     */
    @Test
    void test_할일_목록_조회_생성_후() throws Exception {
        createTestTodo("테스트 할일", null, Priority.MEDIUM);

        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].title").value("테스트 할일"))
                .andExpect(jsonPath("$.total").value(1));
    }

    /**
     * 페이지네이션 테스트.
     * 여러 항목 생성 후 page/size 파라미터로 페이지네이션이 동작하는지 검증합니다.
     */
    @Test
    void test_할일_목록_페이지네이션() throws Exception {
        // 3개의 할일을 데이터베이스에 직접 저장합니다
        for (int i = 1; i <= 3; i++) {
            createTestTodo("할일 " + i, null, Priority.MEDIUM);
        }

        // 페이지 크기 2로 첫 번째 페이지 조회
        mockMvc.perform(get("/todos")
                        .param("page", "1")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.total").value(3))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(2));

        // 두 번째 페이지 조회
        mockMvc.perform(get("/todos")
                        .param("page", "2")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.total").value(3))
                .andExpect(jsonPath("$.page").value(2));
    }

    /**
     * 우선순위 필터링 테스트.
     * priority 쿼리 파라미터로 특정 우선순위의 항목만 조회되는지 검증합니다.
     */
    @Test
    void test_할일_목록_우선순위_필터링() throws Exception {
        createTestTodo("긴급 할일", null, Priority.HIGH);
        createTestTodo("나중에 할 일", null, Priority.LOW);

        // HIGH 필터링 조회
        mockMvc.perform(get("/todos")
                        .param("priority", "HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].title").value("긴급 할일"))
                .andExpect(jsonPath("$.items[0].priority").value("HIGH"))
                .andExpect(jsonPath("$.total").value(1));
    }

    // ========== 단건 조회 (GET /todos/{id}) ==========

    /**
     * 특정 할일 조회 성공 테스트.
     */
    @Test
    void test_할일_단건_조회_성공() throws Exception {
        Todo saved = createTestTodo("조회할 할일", null, Priority.HIGH);

        mockMvc.perform(get("/todos/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.title").value("조회할 할일"))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }

    /**
     * 존재하지 않는 할일 조회 시 404 테스트.
     */
    @Test
    void test_할일_단건_조회_존재하지_않는_항목() throws Exception {
        mockMvc.perform(get("/todos/999"))
                .andExpect(status().isNotFound());
    }

    // ========== 전체 수정 (PUT /todos/{id}) ==========

    /**
     * 할일 전체 수정 성공 테스트.
     * PUT /todos/{id} 요청 시 200 상태 코드와 수정된 TODO 항목을 반환하는지 검증합니다.
     */
    @Test
    void test_할일_전체_수정_성공() throws Exception {
        Todo saved = createTestTodo("원래 제목", null, Priority.MEDIUM);

        String updateJson = """
                {
                    "title": "수정된 제목",
                    "description": "수정된 설명",
                    "priority": "HIGH",
                    "completed": true
                }
                """;

        mockMvc.perform(put("/todos/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정된 제목"))
                .andExpect(jsonPath("$.description").value("수정된 설명"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.completed").value(true))
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    /**
     * 존재하지 않는 할일 수정 시 404 테스트.
     */
    @Test
    void test_할일_전체_수정_존재하지_않는_항목() throws Exception {
        String updateJson = """
                {
                    "title": "수정된 제목",
                    "completed": true
                }
                """;

        mockMvc.perform(put("/todos/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNotFound());
    }

    // ========== 부분 수정 (PATCH /todos/{id}) ==========

    /**
     * 할일 부분 수정 성공 테스트 — 제목만 수정.
     * PATCH /todos/{id} 요청 시 지정한 필드만 업데이트되는지 검증합니다.
     */
    @Test
    void test_할일_부분_수정_제목만() throws Exception {
        Todo saved = createTestTodo("원래 제목", "원래 설명", Priority.LOW);

        String patchJson = """
                {
                    "title": "수정된 제목"
                }
                """;

        mockMvc.perform(patch("/todos/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정된 제목"))
                .andExpect(jsonPath("$.description").value("원래 설명"))
                .andExpect(jsonPath("$.priority").value("LOW"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    /**
     * 할일 부분 수정 — 완료 상태만 변경 테스트.
     */
    @Test
    void test_할일_부분_수정_완료_상태만() throws Exception {
        Todo saved = createTestTodo("테스트 할일", null, Priority.MEDIUM);

        String patchJson = """
                {
                    "completed": true
                }
                """;

        mockMvc.perform(patch("/todos/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("테스트 할일"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    /**
     * 존재하지 않는 할일 부분 수정 시 404 테스트.
     */
    @Test
    void test_할일_부분_수정_존재하지_않는_항목() throws Exception {
        String patchJson = """
                {
                    "title": "수정된 제목"
                }
                """;

        mockMvc.perform(patch("/todos/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isNotFound());
    }

    // ========== 삭제 (DELETE /todos/{id}) ==========

    /**
     * 할일 삭제 성공 테스트.
     * DELETE /todos/{id} 요청 시 204 상태 코드를 반환하는지 검증합니다.
     */
    @Test
    void test_할일_삭제_성공() throws Exception {
        Todo saved = createTestTodo("삭제할 할일", null, Priority.MEDIUM);

        // 할일을 삭제합니다
        mockMvc.perform(delete("/todos/" + saved.getId()))
                .andExpect(status().isNoContent());

        // 삭제 후 목록이 비어 있는지 확인합니다
        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(0)))
                .andExpect(jsonPath("$.total").value(0));
    }

    /**
     * 존재하지 않는 할일 삭제 시 404 테스트.
     */
    @Test
    void test_할일_삭제_존재하지_않는_항목() throws Exception {
        mockMvc.perform(delete("/todos/999"))
                .andExpect(status().isNotFound());
    }
}
