package com.example.todo.controller;

import com.example.todo.dto.*;
import com.example.todo.service.TodoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * TODO 항목에 대한 CRUD REST 컨트롤러.
 *
 * <p>{@link TodoService}에 비즈니스 로직을 위임하며,
 * HTTP 요청/응답 처리와 유효성 검증을 담당합니다.</p>
 *
 * <p>기본 경로: {@code /todos}</p>
 */
@RestController
@RequestMapping("/todos")
@Validated
public class TodoController {

    private final TodoService todoService;

    /**
     * TodoController 생성자.
     *
     * @param todoService TODO 서비스
     */
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    /**
     * TODO 항목 목록을 조회합니다.
     *
     * <p>우선순위 필터링과 페이지네이션을 지원합니다.
     * 페이지 번호는 1부터 시작하며, 페이지 크기는 1~100 범위입니다.</p>
     *
     * @param priority 필터링할 우선순위 (선택, null이면 전체 조회)
     * @param page     페이지 번호 (기본값: 1, 최소: 1)
     * @param size     페이지 크기 (기본값: 10, 최소: 1, 최대: 100)
     * @return 페이지네이션된 TODO 항목 목록 응답
     */
    @GetMapping
    public TodoListResponse getAllTodos(
            @RequestParam(required = false) Priority priority,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return todoService.getTodos(priority, page, size);
    }

    /**
     * 특정 ID의 TODO 항목을 조회합니다.
     *
     * @param id 조회할 TODO 항목의 ID
     * @return 해당 ID의 TODO 항목 응답
     */
    @GetMapping("/{id}")
    public TodoResponse getTodoById(@PathVariable Long id) {
        return todoService.getTodoById(id);
    }

    /**
     * 새로운 TODO 항목을 생성합니다.
     *
     * @param request 생성할 TODO 항목의 데이터
     * @return 생성된 TODO 항목 응답 (ID, 생성 시각 포함)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoResponse createTodo(@Valid @RequestBody TodoCreateRequest request) {
        return todoService.createTodo(request);
    }

    /**
     * 기존 TODO 항목을 전체 수정합니다.
     *
     * @param id      수정할 TODO 항목의 ID
     * @param request 수정할 데이터
     * @return 수정된 TODO 항목 응답
     */
    @PutMapping("/{id}")
    public TodoResponse updateTodo(@PathVariable Long id,
                                   @Valid @RequestBody TodoUpdateRequest request) {
        return todoService.updateTodo(id, request);
    }

    /**
     * 기존 TODO 항목을 부분 수정합니다.
     *
     * @param id      수정할 TODO 항목의 ID
     * @param request 부분 수정할 데이터 (null이 아닌 필드만 적용)
     * @return 수정된 TODO 항목 응답
     */
    @PatchMapping("/{id}")
    public TodoResponse patchTodo(@PathVariable Long id,
                                  @RequestBody TodoUpdateRequest request) {
        return todoService.patchTodo(id, request);
    }

    /**
     * 특정 ID의 TODO 항목을 삭제합니다.
     *
     * @param id 삭제할 TODO 항목의 ID
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
    }
}
