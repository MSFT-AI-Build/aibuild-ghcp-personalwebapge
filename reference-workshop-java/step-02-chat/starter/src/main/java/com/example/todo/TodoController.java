package com.example.todo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// TODO 항목에 대한 CRUD REST 컨트롤러
@RestController
@RequestMapping("/todos")
public class TodoController {

    // TODO 항목을 저장하는 인메모리 리스트
    private final List<Todo> todos = new ArrayList<>();

    // 다음에 생성될 TODO 항목의 ID
    private long nextId = 1;

    // 모든 TODO 항목을 조회합니다
    @GetMapping
    public List<Todo> getAllTodos() {
        return todos;
    }

    // 특정 ID의 TODO 항목을 조회합니다
    @GetMapping("/{id}")
    public Todo getTodoById(@PathVariable Long id) {
        return todos.stream()
                .filter(todo -> todo.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 TODO 항목을 찾을 수 없습니다: " + id));
    }

    // 새로운 TODO 항목을 생성합니다
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo createTodo(@RequestBody Todo todo) {
        todo.setId(nextId++);
        todo.setCreatedAt(LocalDateTime.now());
        todo.setCompleted(false);
        todos.add(todo);
        return todo;
    }

    // 기존 TODO 항목을 수정합니다
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

    // 특정 ID의 TODO 항목을 삭제합니다
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable Long id) {
        boolean removed = todos.removeIf(todo -> todo.getId().equals(id));
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 TODO 항목을 찾을 수 없습니다: " + id);
        }
    }
}
