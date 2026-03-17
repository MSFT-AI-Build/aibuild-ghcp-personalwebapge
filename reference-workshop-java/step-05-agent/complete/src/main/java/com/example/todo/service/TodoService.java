package com.example.todo.service;

import com.example.todo.dto.*;
import com.example.todo.entity.Todo;
import com.example.todo.repository.TodoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * TODO 항목에 대한 비즈니스 로직을 처리하는 서비스 클래스.
 *
 * <p>{@link TodoRepository}를 통해 데이터베이스에 접근하며,
 * 컨트롤러와 데이터 접근 계층 사이의 중간 계층 역할을 합니다.</p>
 *
 * <p>엔티티와 DTO 간의 변환도 이 계층에서 수행합니다.</p>
 */
@Service
public class TodoService {

    private final TodoRepository todoRepository;

    /**
     * TodoService 생성자.
     *
     * @param todoRepository TODO 항목 레포지토리
     */
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    /**
     * TODO 항목 목록을 페이지네이션하여 조회합니다.
     *
     * <p>우선순위 필터가 지정된 경우 해당 우선순위의 항목만 조회합니다.
     * 페이지 번호는 1부터 시작하며, 내부적으로 0 기반 인덱스로 변환됩니다.</p>
     *
     * @param priority 필터링할 우선순위 (null이면 전체 조회)
     * @param page     페이지 번호 (1부터 시작)
     * @param size     페이지당 항목 수
     * @return 페이지네이션된 TODO 항목 목록 응답
     */
    public TodoListResponse getTodos(Priority priority, int page, int size) {
        // 1 기반 페이지 번호를 0 기반으로 변환
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Todo> todoPage;
        if (priority != null) {
            todoPage = todoRepository.findByPriority(priority, pageable);
        } else {
            todoPage = todoRepository.findAll(pageable);
        }

        List<TodoResponse> items = todoPage.getContent().stream()
                .map(this::toResponse)
                .toList();

        return new TodoListResponse(items, todoPage.getTotalElements(), page, size);
    }

    /**
     * 특정 ID의 TODO 항목을 조회합니다.
     *
     * @param id 조회할 TODO 항목의 ID
     * @return 해당 ID의 TODO 항목 응답
     * @throws ResponseStatusException 해당 ID의 TODO 항목이 존재하지 않는 경우 (HTTP 404)
     */
    public TodoResponse getTodoById(Long id) {
        Todo todo = findTodoOrThrow(id);
        return toResponse(todo);
    }

    /**
     * 새로운 TODO 항목을 생성합니다.
     *
     * <p>요청 데이터를 엔티티로 변환하여 데이터베이스에 저장합니다.
     * ID, 생성 시각, 수정 시각은 자동으로 설정됩니다.</p>
     *
     * @param request 생성할 TODO 항목의 데이터
     * @return 생성된 TODO 항목 응답
     */
    public TodoResponse createTodo(TodoCreateRequest request) {
        Todo todo = new Todo();
        todo.setTitle(request.title());
        todo.setDescription(request.description());
        todo.setPriority(request.priority());
        todo.setCompleted(false);

        Todo savedTodo = todoRepository.save(todo);
        return toResponse(savedTodo);
    }

    /**
     * 기존 TODO 항목을 전체 수정합니다.
     *
     * <p>요청 본문의 모든 필드로 기존 항목을 덮어씁니다.
     * ID와 생성 시각은 변경되지 않으며, 수정 시각은 자동으로 갱신됩니다.</p>
     *
     * @param id      수정할 TODO 항목의 ID
     * @param request 수정할 데이터
     * @return 수정된 TODO 항목 응답
     * @throws ResponseStatusException 해당 ID의 TODO 항목이 존재하지 않는 경우 (HTTP 404)
     */
    public TodoResponse updateTodo(Long id, TodoUpdateRequest request) {
        Todo existingTodo = findTodoOrThrow(id);

        // 전체 수정: 모든 필드를 업데이트합니다
        existingTodo.setTitle(request.title());
        existingTodo.setDescription(request.description());
        existingTodo.setPriority(request.priority());
        if (request.completed() != null) {
            existingTodo.setCompleted(request.completed());
        }

        Todo savedTodo = todoRepository.save(existingTodo);
        return toResponse(savedTodo);
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
    public TodoResponse patchTodo(Long id, TodoUpdateRequest request) {
        Todo existingTodo = findTodoOrThrow(id);

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

        Todo savedTodo = todoRepository.save(existingTodo);
        return toResponse(savedTodo);
    }

    /**
     * 특정 ID의 TODO 항목을 삭제합니다.
     *
     * @param id 삭제할 TODO 항목의 ID
     * @throws ResponseStatusException 해당 ID의 TODO 항목이 존재하지 않는 경우 (HTTP 404)
     */
    public void deleteTodo(Long id) {
        Todo todo = findTodoOrThrow(id);
        todoRepository.delete(todo);
    }

    /**
     * ID로 TODO 항목을 검색합니다. 존재하지 않으면 404 예외를 발생시킵니다.
     *
     * @param id 검색할 TODO 항목의 ID
     * @return 해당 ID의 TODO 항목
     * @throws ResponseStatusException 해당 ID의 TODO 항목이 존재하지 않는 경우 (HTTP 404)
     */
    private Todo findTodoOrThrow(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "해당 TODO 항목을 찾을 수 없습니다: " + id));
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
