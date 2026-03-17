package com.example.todo.dto;

import java.util.List;

/**
 * TODO 항목 목록 응답 DTO.
 *
 * <p>페이지네이션된 TODO 항목 목록과 메타 정보를 클라이언트에 반환할 때 사용합니다.</p>
 *
 * @param items 현재 페이지의 TODO 항목 리스트
 * @param total 전체 TODO 항목 수 (필터 적용 후)
 * @param page  현재 페이지 번호 (1부터 시작)
 * @param size  페이지당 항목 수
 */
public record TodoListResponse(
        List<TodoResponse> items,
        long total,
        int page,
        int size
) {
}
