package com.example.todo;

import com.example.todo.dto.Priority;

import java.time.LocalDateTime;

/**
 * TODO 항목을 나타내는 모델 클래스.
 *
 * <p>인메모리 저장소에서 사용되는 간단한 POJO입니다.
 * 각 TODO 항목은 고유 ID, 제목, 설명, 우선순위, 완료 여부, 생성 시각, 수정 시각을 가집니다.</p>
 *
 * <p>이 클래스는 내부 데이터 모델로 사용되며, API 요청/응답에는 DTO를 사용합니다.</p>
 */
public class Todo {

    /** TODO 항목의 고유 식별자 (자동 생성) */
    private Long id;

    /** TODO 항목의 제목 (필수) */
    private String title;

    /** TODO 항목의 상세 설명 (선택) */
    private String description;

    /** TODO 항목의 우선순위 (기본값: MEDIUM) */
    private Priority priority = Priority.MEDIUM;

    /** TODO 항목의 완료 여부 (기본값: false) */
    private boolean completed;

    /** TODO 항목이 생성된 시각 (자동 설정) */
    private LocalDateTime createdAt;

    /** TODO 항목이 마지막으로 수정된 시각 (자동 설정) */
    private LocalDateTime updatedAt;

    /**
     * 기본 생성자.
     * JSON 역직렬화를 위해 필요합니다.
     */
    public Todo() {
    }

    /**
     * 모든 필드를 초기화하는 생성자.
     *
     * @param id          고유 식별자
     * @param title       제목
     * @param description 상세 설명
     * @param priority    우선순위
     * @param completed   완료 여부
     * @param createdAt   생성 시각
     * @param updatedAt   수정 시각
     */
    public Todo(Long id, String title, String description, Priority priority,
                boolean completed, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.completed = completed;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 고유 식별자를 반환합니다.
     *
     * @return TODO 항목의 ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 고유 식별자를 설정합니다.
     *
     * @param id 설정할 ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 제목을 반환합니다.
     *
     * @return TODO 항목의 제목
     */
    public String getTitle() {
        return title;
    }

    /**
     * 제목을 설정합니다.
     *
     * @param title 설정할 제목
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 상세 설명을 반환합니다.
     *
     * @return TODO 항목의 상세 설명
     */
    public String getDescription() {
        return description;
    }

    /**
     * 상세 설명을 설정합니다.
     *
     * @param description 설정할 상세 설명
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 우선순위를 반환합니다.
     *
     * @return TODO 항목의 우선순위
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * 우선순위를 설정합니다.
     *
     * @param priority 설정할 우선순위
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * 완료 여부를 반환합니다.
     *
     * @return 완료되었으면 {@code true}, 아니면 {@code false}
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * 완료 여부를 설정합니다.
     *
     * @param completed 설정할 완료 여부
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * 생성 시각을 반환합니다.
     *
     * @return TODO 항목의 생성 시각
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 생성 시각을 설정합니다.
     *
     * @param createdAt 설정할 생성 시각
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 수정 시각을 반환합니다.
     *
     * @return TODO 항목의 최종 수정 시각
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 수정 시각을 설정합니다.
     *
     * @param updatedAt 설정할 수정 시각
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
