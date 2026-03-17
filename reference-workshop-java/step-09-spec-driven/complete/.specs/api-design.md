# API 설계

## 기본 정보
- Base Path: `/todos`
- Content-Type: application/json

## 엔드포인트

### GET /todos
- 설명: TODO 목록 조회 (필터링 + 페이지네이션)
- 쿼리 파라미터:
  - priority: LOW | MEDIUM | HIGH (선택)
  - page: 정수, 1 이상 (기본값: 1)
  - size: 정수, 1~100 (기본값: 10)
- 응답 (200): { "items": [...], "total": 25, "page": 1, "size": 10 }

### POST /todos
- 설명: 새 TODO 생성
- 요청 본문: { "title": "...", "description": "...", "priority": "MEDIUM" }
- 응답 (201): 생성된 TODO 객체
- 에러 (400): 유효성 검사 실패

### PUT /todos/{id}
- 설명: TODO 전체 수정
- 응답 (200): 수정된 TODO 객체
- 에러 (404): 존재하지 않는 TODO

### PATCH /todos/{id}
- 설명: TODO 부분 수정
- 응답 (200): 수정된 TODO 객체
- 에러 (404): 존재하지 않는 TODO

### DELETE /todos/{id}
- 설명: TODO 삭제
- 응답 (204): 본문 없음
- 에러 (404): 존재하지 않는 TODO
