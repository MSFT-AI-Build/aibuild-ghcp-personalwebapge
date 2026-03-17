package com.example.todo.repository;

import com.example.todo.dto.Priority;
import com.example.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TODO 항목에 대한 데이터 접근 레포지토리.
 *
 * <p>Spring Data JPA의 {@link JpaRepository}를 상속하여
 * 기본 CRUD 및 페이지네이션 기능을 자동으로 제공합니다.</p>
 *
 * <p>우선순위별 필터링을 위한 커스텀 쿼리 메서드를 추가로 정의합니다.</p>
 */
public interface TodoRepository extends JpaRepository<Todo, Long> {

    /**
     * 특정 우선순위의 TODO 항목을 페이지네이션하여 조회합니다.
     *
     * @param priority 필터링할 우선순위
     * @param pageable 페이지네이션 정보
     * @return 페이지네이션된 TODO 항목 목록
     */
    Page<Todo> findByPriority(Priority priority, Pageable pageable);

    /**
     * 특정 우선순위의 TODO 항목 수를 반환합니다.
     *
     * @param priority 카운트할 우선순위
     * @return 해당 우선순위의 TODO 항목 수
     */
    long countByPriority(Priority priority);
}
