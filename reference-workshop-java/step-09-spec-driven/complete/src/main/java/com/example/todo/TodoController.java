package com.example.todo;

import com.example.todo.dto.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TODO 항목에 대한 CRUD REST 컨트롤러.
 *
 * <p>인메모리 {@link ArrayList}를 사용하여 TODO 데이터를 저장하며,
 * 생성, 조회(페이지네이션/필터링), 전체 수정, 부분 수정, 삭제 기능을 REST API로 제공합니다.</p>
 *
 * <p>기본 경로: {@code /todos}</p>
 */
@RestController
@RequestMapping("/todos")
@Validated
public class TodoController {

    /** TODO 항목을 저장하는 인메모리 리스트 */
    private final List<Todo> todos = new ArrayList<>();

    /** 다음에 생성될 TODO 항목의 ID (자동 증가) */
    private long nextId = 1;

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

        // 우선순위 필터링 적용
        Stream<Todo> stream = todos.stream();
        if (priority != null) {
            stream = stream.filter(todo -> todo.getPriority() == priority);
        }

        List<Todo> filtered = stream.collect(Collectors.toList());
        long total = filtered.size();

        // 페이지네이션 적용
        int startIndex = (page - 1) * size;
        List<TodoResponse> items;
        if (startIndex >= filtered.size()) {
            items = List.of();
        } else {
            items = filtered.stream()
                    .skip(startIndex)
                    .limit(size)
                    .map(this::toResponse)
                    .collect(Collectors.toList());
        }

        return new TodoListResponse(items, total, page, size);
    }

    /**
     * 특정 ID의 TODO 항목을 조회합니다.
     *
     * @param id 조회할 TODO 항목의 ID
     * @return 해당 ID의 TODO 항목 응답
     * @throws ResponseStatusException 해당 ID의 TODO 항목이 존재하지 않는 경우 (HTTP 404)
     */
    @GetMapping("/{id}")
    public TodoResponse getTodoById(@PathVariable Long id) {
        Todo todo = findTodoById(id);
        return toResponse(todo);
    }

    /**
     * 새로운 TODO 항목을 생성합니다.
     *
     * <p>ID, 생성 시각, 수정 시각은 서버에서 자동으로 설정되며,
     * 완료 여부는 {@code false}로 초기화됩니다.</p>
     *
     * @param request 생성할 TODO 항목의 데이터
     * @return 생성된 TODO 항목 응답 (ID, 생성 시각 포함)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoResponse createTodo(@Valid @RequestBody TodoCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();

        Todo todo = new Todo();
        todo.setId(nextId++);
        todo.setTitle(request.title());
        todo.setDescription(request.description());
        todo.setPriority(request.priority());
        todo.setCompleted(false);
        todo.setCreatedAt(now);
        todo.setUpdatedAt(now);

        todos.add(todo);
        return toResponse(todo);
    }

    /**
     * 기존 TODO 항목을 전체 수정합니다.
     *
     * <p>요청 본문의 모든 필드로 기존 항목을 덮어씁니다.
     * ID와 생성 시각은 변경되지 않으며, 수정 시각이 갱신됩니다.</p>
     *
     * @param id      수정할 TODO 항목의 ID
     * @param request 수정할 데이터
     * @return 수정된 TODO 항목 응답
     * @throws ResponseStatusException 해당 ID의 TODO 항목이 존재하지 않는 경우 (HTTP 404)
     */
    @PutMapping("/{id}")
    public TodoResponse updateTodo(@PathVariable Long id,
                                   @Valid @RequestBody TodoUpdateRequest request) {
        Todo existingTodo = findTodoById(id);

        // 전체 수정: 모든 필드를 업데이트합니다
        existingTodo.setTitle(request.title());
        existingTodo.setDescription(request.description());
        existingTodo.setPriority(request.priority());
        if (request.completed() != null) {
            existingTodo.setCompleted(request.completed());
        }
        existingTodo.setUpdatedAt(LocalDateTime.now());

        return toResponse(existingTodo);
    }

    /**
     * 기존 TODO 항목을 부분 수정합니다.
     *
     * <p>요청 본문에 포함된 필드만 업데이트하며,
     * {@code null}인 필드는 기존 값을 유지합니다.</p>
     *
     * @param id      수정할 TODO 항목의 ID
     * @param request 부분 수정할 데이터 (null이 아닌 필드만 적용)
     * @return 수정된 TODO 항목 응답
     * @throws ResponseStatusException 해당 ID의 TODO 항목이 존재하지 않는 경우 (HTTP 404)
     */
    @PatchMapping("/{id}")
    public TodoResponse patchTodo(@PathVariable Long id,
                                  @RequestBody TodoUpdateRequest request) {
        Todo existingTodo = findTodoById(id);

        // 부분 수정: null이 아닌 필드만 업데이트합니다
        if (request.title() != null) {
            existingTodo.setTitle(request.title());
        }
        if (request.description() != null) {
            existingTodo.setDescription(request.description());
        }
        if (request.priority() != null) {
            existingTodo.setPriority(request.priority());
        }
        if (request.completed() != null) {
            existingTodo.setCompleted(request.completed());
        }
        existingTodo.setUpdatedAt(LocalDateTime.now());

        return toResponse(existingTodo);
    }

    /**
     * 특정 ID의 TODO 항목을 삭제합니다.
     *
     * @param id 삭제할 TODO 항목의 ID
     * @throws ResponseStatusException 해당 ID의 TODO 항목이 존재하지 않는 경우 (HTTP 404)
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable Long id) {
        boolean removed = todos.removeIf(todo -> todo.getId().equals(id));
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 TODO 항목을 찾을 수 없습니다: " + id);
        }
    }

    /**
     * ID로 TODO 항목을 검색합니다.
     *
     * @param id 검색할 TODO 항목의 ID
     * @return 해당 ID의 TODO 항목
     * @throws ResponseStatusException 해당 ID의 TODO 항목이 존재하지 않는 경우 (HTTP 404)
     */
    private Todo findTodoById(Long id) {
        return todos.stream()
                .filter(todo -> todo.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 TODO 항목을 찾을 수 없습니다: " + id));
    }

    /**
     * TODO 엔티티를 응답 DTO로 변환합니다.
     *
     * @param todo 변환할 TODO 엔티티
     * @return 변환된 응답 DTO
     */
    private TodoResponse toResponse(Todo todo) {
        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getPriority(),
                todo.isCompleted(),
                todo.getCreatedAt(),
                todo.getUpdatedAt()
        );
    }
}
