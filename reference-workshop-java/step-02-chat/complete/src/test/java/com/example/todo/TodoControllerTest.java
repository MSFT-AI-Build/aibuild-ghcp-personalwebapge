package com.example.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * {@link TodoController}에 대한 통합 테스트.
 *
 * <p>MockMvc를 사용하여 HTTP 요청/응답을 검증합니다.
 * 각 테스트 메서드 실행 후 컨텍스트를 초기화하여 테스트 간 상태 격리를 보장합니다.</p>
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("테스트 할일"))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    /**
     * 설명이 포함된 할일 생성 성공 테스트.
     * POST /todos 요청 시 제목과 설명이 모두 포함된 TODO 항목을 생성하는지 검증합니다.
     */
    @Test
    void test_할일_생성_설명_포함() throws Exception {
        String json = """
                {
                    "title": "테스트 할일",
                    "description": "상세한 설명입니다"
                }
                """;

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("테스트 할일"))
                .andExpect(jsonPath("$.description").value("상세한 설명입니다"));
    }

    /**
     * 빈 목록 조회 테스트.
     * GET /todos 요청 시 TODO 항목이 없으면 빈 리스트를 반환하는지 검증합니다.
     */
    @Test
    void test_할일_목록_조회_빈_리스트() throws Exception {
        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /**
     * 생성 후 목록 조회 테스트.
     * TODO 항목 생성 후 GET /todos 요청 시 생성된 항목이 포함된 리스트를 반환하는지 검증합니다.
     */
    @Test
    void test_할일_목록_조회_생성_후() throws Exception {
        // 먼저 할일을 생성합니다
        String json = """
                {
                    "title": "테스트 할일"
                }
                """;

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        // 목록을 조회합니다
        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("테스트 할일"));
    }

    /**
     * 할일 수정 성공 테스트.
     * PUT /todos/{id} 요청 시 200 상태 코드와 수정된 TODO 항목을 반환하는지 검증합니다.
     */
    @Test
    void test_할일_수정_성공() throws Exception {
        // 먼저 할일을 생성합니다
        String createJson = """
                {
                    "title": "원래 제목"
                }
                """;

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated());

        // 할일을 수정합니다
        String updateJson = """
                {
                    "title": "수정된 제목",
                    "description": "수정된 설명",
                    "completed": true
                }
                """;

        mockMvc.perform(put("/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정된 제목"))
                .andExpect(jsonPath("$.description").value("수정된 설명"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    /**
     * 존재하지 않는 할일 수정 시 404 테스트.
     * PUT /todos/{id} 요청 시 해당 ID의 TODO 항목이 없으면 404를 반환하는지 검증합니다.
     */
    @Test
    void test_할일_수정_존재하지_않는_항목() throws Exception {
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

    /**
     * 할일 삭제 성공 테스트.
     * DELETE /todos/{id} 요청 시 204 상태 코드를 반환하는지 검증합니다.
     */
    @Test
    void test_할일_삭제_성공() throws Exception {
        // 먼저 할일을 생성합니다
        String json = """
                {
                    "title": "삭제할 할일"
                }
                """;

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        // 할일을 삭제합니다
        mockMvc.perform(delete("/todos/1"))
                .andExpect(status().isNoContent());

        // 삭제 후 목록이 비어 있는지 확인합니다
        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /**
     * 존재하지 않는 할일 삭제 시 404 테스트.
     * DELETE /todos/{id} 요청 시 해당 ID의 TODO 항목이 없으면 404를 반환하는지 검증합니다.
     */
    @Test
    void test_할일_삭제_존재하지_않는_항목() throws Exception {
        mockMvc.perform(delete("/todos/999"))
                .andExpect(status().isNotFound());
    }
}
