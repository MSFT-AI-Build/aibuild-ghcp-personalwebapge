package com.example.todo.dto;

/**
 * TODO 항목의 우선순위를 나타내는 열거형.
 *
 * <p>LOW(낮음), MEDIUM(보통), HIGH(높음) 세 가지 수준을 제공합니다.</p>
 */
public enum Priority {

    /** 낮은 우선순위 */
    LOW,

    /** 보통 우선순위 (기본값) */
    MEDIUM,

    /** 높은 우선순위 */
    HIGH
}
