package com.example.todo;

import java.time.LocalDateTime;

/**
 * TODO 항목을 나타내는 모델 클래스.
 * 인메모리 저장소에서 사용되는 간단한 POJO입니다.
 */
public class Todo {

    /** TODO 항목의 고유 식별자 */
    private Long id;

    /** TODO 항목의 제목 */
    private String title;

    /** TODO 항목의 상세 설명 */
    private String description;

    /** TODO 항목의 완료 여부 */
    private boolean completed;

    /** TODO 항목의 생성 시각 */
    private LocalDateTime createdAt;

    /**
     * 기본 생성자.
     */
    public Todo() {
    }

    /**
     * 모든 필드를 초기화하는 생성자.
     *
     * @param id          고유 식별자
     * @param title       제목
     * @param description 상세 설명
     * @param completed   완료 여부
     * @param createdAt   생성 시각
     */
    public Todo(Long id, String title, String description, boolean completed, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.createdAt = createdAt;
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
     * 완료 여부를 반환합니다.
     *
     * @return 완료되었으면 true, 아니면 false
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
}
