package com.example.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * TODO 항목 생성 요청 DTO.
 *
 * <p>클라이언트가 새로운 TODO 항목을 생성할 때 사용하는 요청 본문입니다.
 * 제목은 필수이며, 우선순위를 지정하지 않으면 기본값 {@link Priority#MEDIUM}이 적용됩니다.</p>
 *
 * @param title       TODO 항목의 제목 (필수, 1~200자)
 * @param description TODO 항목의 상세 설명 (선택, 최대 1000자)
 * @param priority    TODO 항목의 우선순위 (선택, 기본값: MEDIUM)
 */
public record TodoCreateRequest(
        @NotBlank(message = "제목은 필수입니다")
        @Size(min = 1, max = 200, message = "제목은 1자 이상 200자 이하여야 합니다")
        String title,

        @Size(max = 1000, message = "설명은 1000자 이하여야 합니다")
        String description,

        Priority priority
) {
    /**
     * 컴팩트 생성자.
     * 우선순위가 지정되지 않은 경우 기본값 {@link Priority#MEDIUM}을 설정합니다.
     */
    public TodoCreateRequest {
        if (priority == null) {
            priority = Priority.MEDIUM;
        }
    }
}
