package com.example.todo.dto;

import java.time.LocalDateTime;

/**
 * TODO 항목 응답 DTO.
 *
 * <p>단일 TODO 항목의 모든 정보를 클라이언트에 반환할 때 사용합니다.</p>
 *
 * @param id          TODO 항목의 고유 식별자
 * @param title       TODO 항목의 제목
 * @param description TODO 항목의 상세 설명
 * @param priority    TODO 항목의 우선순위
 * @param completed   TODO 항목의 완료 여부
 * @param createdAt   TODO 항목의 생성 시각
 * @param updatedAt   TODO 항목의 최종 수정 시각
 */
public record TodoResponse(
        Long id,
        String title,
        String description,
        Priority priority,
        boolean completed,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
