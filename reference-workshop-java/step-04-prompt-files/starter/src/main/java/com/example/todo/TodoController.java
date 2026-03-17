package com.example.todo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO 항목에 대한 CRUD REST 컨트롤러.
 *
 * <p>인메모리 {@link ArrayList}를 사용하여 TODO 데이터를 저장하며,
 * 생성, 조회, 수정, 삭제 기능을 REST API로 제공합니다.</p>
 *
 * <p>기본 경로: {@code /todos}</p>
 */
@RestController
@RequestMapping("/todos")
public class TodoController {

    /** TODO 항목을 저장하는 인메모리 리스트 */
    private final List<Todo> todos = new ArrayList<>();

    /** 다음에 생성될 TODO 항목의 ID (자동 증가) */
    private long nextId = 1;

    /**
     * 모든 TODO 항목을 조회합니다.
     *
     * @return 저장된 모든 TODO 항목의 리스트 (비어 있을 수 있음)
     */
    @GetMapping
    public List<Todo> getAllTodos() {
        return todos;
    }

    /**
     * 특정 ID의 TODO 항목을 조회합니다.
     *
     * @param id 조회할 TODO 항목의 ID
     * @return 해당 ID의 TODO 항목
     * @throws ResponseStatusException 해당 ID의 TODO 항목이 존재하지 않는 경우 (HTTP 404)
     */
    @GetMapping("/{id}")
    public Todo getTodoById(@PathVariable Long id) {
        return todos.stream()
                .filter(todo -> todo.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 TODO 항목을 찾을 수 없습니다: " + id));
    }

    /**
     * 새로운 TODO 항목을 생성합니다.
     *
     * <p>ID와 생성 시각은 서버에서 자동으로 설정되며,
     * 완료 여부는 {@code false}로 초기화됩니다.</p>
     *
     * @param todo 생성할 TODO 항목의 데이터 (제목, 설명 포함)
     * @return 생성된 TODO 항목 (ID, 생성 시각 포함)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo createTodo(@RequestBody Todo todo) {
        todo.setId(nextId++);
        todo.setCreatedAt(LocalDateTime.now());
        todo.setCompleted(false);
        todos.add(todo);
        return todo;
    }

    /**
     * 기존 TODO 항목을 수정합니다.
     *
     * <p>제목, 설명, 완료 여부를 업데이트합니다.
     * ID와 생성 시각은 변경되지 않습니다.</p>
     *
     * @param id   수정할 TODO 항목의 ID
     * @param todo 수정할 데이터 (제목, 설명, 완료 여부)
     * @return 수정된 TODO 항목
     * @throws ResponseStatusException 해당 ID의 TODO 항목이 존재하지 않는 경우 (HTTP 404)
     */
    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        Todo existingTodo = todos.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 TODO 항목을 찾을 수 없습니다: " + id));

        // 기존 TODO 항목의 필드를 업데이트합니다
        existingTodo.setTitle(todo.getTitle());
        existingTodo.setDescription(todo.getDescription());
        existingTodo.setCompleted(todo.isCompleted());

        return existingTodo;
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
}
