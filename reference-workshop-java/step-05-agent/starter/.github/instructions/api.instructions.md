---
applyTo: "**/*Controller.java"
---
# API 코드 규칙

## 엔드포인트
- @ResponseStatus로 상태 코드 명시 (CREATED, NO_CONTENT 등)
- @Valid로 요청 검증
- 메서드 파라미터 타입 명확히 지정

## 에러 처리
- 404: ResponseStatusException(HttpStatus.NOT_FOUND, "메시지")
- 400: @Valid + MethodArgumentNotValidException 자동 처리
- 에러 메시지는 한국어

## 구조
- Controller는 비즈니스 로직 없이 Service 위임만
- 반환 타입은 DTO (Entity 직접 반환 금지)
