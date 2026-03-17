# 데이터 모델

## Todo
| 필드 | 타입 | 필수 | 설명 |
|------|------|------|------|
| id | Long | 자동 | 고유 식별자 |
| title | String (1~200자) | ✅ | 할일 제목 |
| description | String (최대 1000자) | ❌ | 상세 설명 |
| priority | enum (LOW, MEDIUM, HIGH) | ❌ | 우선순위 (기본: MEDIUM) |
| completed | boolean | ❌ | 완료 여부 (기본: false) |
| createdAt | LocalDateTime | 자동 | 생성 시각 |
| updatedAt | LocalDateTime | 자동 | 수정 시각 (수정 시 갱신) |

## 저장소
- 현재 단계: 인메모리 ArrayList (Step 6에서 H2 DB + JPA로 전환 예정)
