package com.example.todo.entity;

import com.example.todo.dto.Priority;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * TODO 항목을 나타내는 JPA 엔티티 클래스.
 *
 * <p>H2 데이터베이스의 {@code todos} 테이블과 매핑됩니다.
 * 각 TODO 항목은 고유 ID, 제목, 설명, 우선순위, 완료 여부, 생성 시각, 수정 시각을 가집니다.</p>
 *
 * <p>생성 시각과 수정 시각은 JPA 라이프사이클 콜백({@code @PrePersist}, {@code @PreUpdate})으로
 * 자동 관리됩니다.</p>
 */
@Entity
@Table(name = "todos")
public class Todo {

    /** TODO 항목의 고유 식별자 (자동 생성) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** TODO 항목의 제목 (필수, 최대 200자) */
    @Column(nullable = false, length = 200)
    private String title;

    /** TODO 항목의 상세 설명 (선택, 최대 1000자) */
    @Column(length = 1000)
    private String description;

    /** TODO 항목의 우선순위 (기본값: MEDIUM) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority = Priority.MEDIUM;

    /** TODO 항목의 완료 여부 (기본값: false) */
    @Column(nullable = false)
    private boolean completed = false;

    /** TODO 항목이 생성된 시각 (자동 설정, 수정 불가) */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** TODO 항목이 마지막으로 수정된 시각 (자동 설정) */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 엔티티가 처음 저장되기 전에 생성 시각과 수정 시각을 현재 시각으로 설정합니다.
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * 엔티티가 수정되기 전에 수정 시각을 현재 시각으로 갱신합니다.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 기본 생성자.
     * JPA에서 엔티티 인스턴스를 생성하기 위해 필요합니다.
     */
    public Todo() {
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
