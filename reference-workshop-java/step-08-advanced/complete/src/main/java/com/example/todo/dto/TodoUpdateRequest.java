package com.example.todo.dto;

import jakarta.validation.constraints.Size;

/**
 * TODO 항목 수정 요청 DTO.
 *
 * <p>기존 TODO 항목을 전체 수정(PUT) 또는 부분 수정(PATCH)할 때 사용합니다.
 * 모든 필드는 nullable이며, {@code null}인 필드는 부분 수정 시 기존 값을 유지합니다.</p>
 *
 * @param title       수정할 제목 (1~200자)
 * @param description 수정할 상세 설명 (최대 1000자)
 * @param priority    수정할 우선순위
 * @param completed   수정할 완료 여부
 */
public record TodoUpdateRequest(
        @Size(min = 1, max = 200, message = "제목은 1자 이상 200자 이하여야 합니다")
        String title,

        @Size(max = 1000, message = "설명은 1000자 이하여야 합니다")
        String description,

        Priority priority,

        Boolean completed
) {
}
