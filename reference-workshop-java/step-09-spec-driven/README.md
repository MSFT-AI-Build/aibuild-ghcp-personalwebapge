# Step 9. Spec-Driven Development (SDD)

> ⏱️ 25분 | 난이도 ⭐⭐
>
> 🎯 **핵심 학습: 문서 기반 스펙 → 테스트 → 구현 패턴**
>
> **체감: "스펙 문서를 먼저 쓰니 AI가 놀락도록 정확하다!"**

---

## 코드 폴더

| 폴더 | 설명 |
|------|------|
| `starter/` | **빈 Spring Boot 프로젝트** (build.gradle.kts, Application 클래스만 존재) — 여기서 시작하세요 |
| `complete/` | 이번 스텝 완성 코드 — 막힐 때 참고하세요 |

> 💡 **왜 처음부터?**
> 앞 스텝에서 만든 TODO API 코드를 **모두 지우고** 빈 프로젝트에서 시작합니다.
> SDD로 스펙 문서 작성 → 테스트 → 구현을 **처음부터 끝까지** 자동으로 수행하는 과정을 체험하기 위해서입니다.

---

## SDD란?

Instructions로 **스타일**을 잡았다면, SDD로 **정확도**를 극대화합니다.

기존 접근: "TODO 앱에 우선순위 기능 추가해줘" → Copilot이 추측하며 구현
SDD 접근: **문서로 스펙 정의 → 분석/계획 → 테스트 → 구현** → Copilot이 정확히 구현

---

## SDD (Spec-Driven Development)란?

코드를 먼저 작성하는 대신, **마크다운 문서로 요구사항과 설계를 먼저 정의**합니다.
AI가 이 문서를 읽고 정확한 코드를 생성하는 것이 핵심입니다.

```
1. SPEC     — 요구사항/API/데이터 모델을 문서로 정의 (.specs/)
2. ANALYZE  — Agent가 스펙을 읽고 전체 요구사항을 파악
3. PLAN     — 파악한 내용을 바탕으로 구현 계획 작성 (context/)
4. TEST     — 구현 전에 스펙을 만족하는 테스트 코드 작성
5. EXECUTE  — 테스트를 통과할 때까지 코드 작성/수정
```

### 왜 "문서 기반" 스펙인가?

| 코드 기반 스펙 | 문서 기반 스펙 |
|-------------|------------|
| DTO record에 타입 정의 | `.specs/requirements.md`에 자연어로 정의 |
| 구현 언어에 종속 | 언어/프레임워크 독립적 |
| "어떻게" 중심 | **"무엇을" + "왜"** 중심 |
| 개발자만 이해 | 기획자/디자이너도 이해 가능 |

> 💡 문서 스펙은 **의도를 전달**하고, 코드 스펙(DTO record)은 Agent가 문서를 읽고 **자동 생성**합니다.

---

## 사전 준비: 프로젝트 초기화

`starter/` 폴더에는 다음만 포함되어 있습니다:

```
starter/
├── build.gradle.kts          ← 의존성만 정의
├── settings.gradle.kts
├── src/main/java/.../
│   └── TodoApplication.java  ← @SpringBootApplication만 존재
├── src/main/resources/
│   └── application.properties  ← 빈 설정 파일
└── src/test/java/.../
    └── TodoApplicationTests.java ← contextLoads() 테스트만 존재
```

> ⚠️ **entity, repository, service, controller, dto 패키지는 비어 있거나 존재하지 않습니다.**
> 이 모든 것을 Agent가 스펙 문서를 기반으로 생성합니다.

## 최종 폴더 구조 (완성 후)

```
.specs/
├── requirements.md        ← 기능 요구사항 (무엇을 & 왜)
├── api-design.md          ← API 엔드포인트 설계
└── data-model.md          ← 데이터 모델 정의
context/
└── todo.md                ← Agent가 분석 후 작성하는 구현 계획
src/main/java/com/example/todo/
├── dto/                   ← DTO record (Agent가 스펙에서 생성)
├── TodoController.java    ← 구현 코드 (Agent가 생성)
└── Todo.java              ← 모델 (Agent가 생성)
src/test/java/com/example/todo/
└── TodoControllerTest.java ← 테스트 (Agent가 스펙에서 생성)
```

---

## 태스크 1: 스펙 문서 작성 (7분)

### 1-1. 요구사항 정의

`.specs/requirements.md` 파일 생성:

```markdown
# TODO 앱 요구사항

## 개요
사용자가 할일을 생성, 조회, 수정, 삭제할 수 있는 REST API.

## 기능 요구사항

### 우선순위 시스템
- 각 TODO에 우선순위를 지정할 수 있다 (LOW, MEDIUM, HIGH)
- 기본 우선순위는 MEDIUM이다
- 목록 조회 시 우선순위로 필터링할 수 있다

### 페이지네이션
- TODO 목록은 페이지네이션을 지원한다
- 기본 페이지 크기는 10이다
- 응답에 전체 개수, 현재 페이지, 페이지 크기를 포함한다

### 유효성 검사
- 제목은 필수이며 1~200자
- 설명은 선택이며 최대 1000자
- 빈 제목으로 생성 시 400 에러를 반환한다

### 수정
- PUT: 전체 수정
- PATCH: 부분 수정 (전달된 필드만 변경)
- 수정 시 updatedAt 타임스탬프가 갱신된다
```

### 1-2. API 설계

`.specs/api-design.md` 파일 생성:

````markdown
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
- 응답 (200):
  ```json
  { "items": [...], "total": 25, "page": 1, "size": 10 }
  ```

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
````

### 1-3. 데이터 모델

`.specs/data-model.md` 파일 생성:

```markdown
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
- 현재 단계: 인메모리 ArrayList (Step 5에서 H2 DB + JPA로 전환 예정)
```

### 관찰 포인트
- 코드가 아닌 **자연어 문서**로 스펙을 정의했습니다
- "무엇을 만들지"와 "왜 필요한지"에 집중합니다
- 기술 스택에 종속되지 않은 순수한 요구사항입니다

---

## 태스크 2: Agent에게 분석 → 계획 요청 (5분)

### 2-1. 스펙 분석 (Analyze)

Copilot Chat(**Ask 모드**)에서 스펙을 분석합니다:

```
#file:.specs/requirements.md
#file:.specs/api-design.md
#file:.specs/data-model.md

위 스펙 문서를 읽고 전체 요구사항을 파악해줘.
빠진 부분이나 모호한 부분이 있으면 알려줘.
```

> 💡 분석 단계에서는 파일 생성이 필요 없으므로 **Ask 모드**로 충분합니다.
> Agent가 지적하는 모호한 부분이 있으면 `.specs/` 문서를 보완하세요.

### 2-2. 구현 계획 작성 (Plan)

Copilot Chat을 **Agent 모드로 전환**한 후 구현 계획 파일을 생성합니다:

```
#file:.specs/requirements.md
#file:.specs/api-design.md
#file:.specs/data-model.md

위 스펙 문서를 바탕으로 context/todo.md에 다음을 포함한 구현 계획을 작성해줘:

1. 생성할 파일 목록
2. 각 파일의 역할
3. 구현 순서
4. 주의사항

기술 스택: Java 17+ / Spring Boot 3.x / Jakarta Validation
저장소: 인메모리 ArrayList
```

> ⚠️ **모드 주의**: `context/todo.md` 파일을 자동 생성하려면 **Agent 모드**가 필요합니다.
> Plan 모드에서는 채팅 응답으로만 계획이 출력되며, 파일이 생성되지 않습니다.
>
> 💡 Plan 모드를 사용한 경우, Copilot의 응답을 직접 `context/todo.md`에 복사하거나 Agent 모드로 변경해서 파일 저장을 요청합니다.

### 관찰 포인트
- [ ] Agent가 스펙의 모호한 부분을 지적하는가?
- [ ] `context/todo.md`에 체계적인 구현 계획이 작성되었는가?
- [ ] DTO record, Controller 등 파일별 역할이 명확히 정리되었는가?

> 📸 **[IntelliJ 스크린샷]** Agent가 `.specs/` 문서를 읽고 분석한 후 `context/todo.md`에 구현 계획을 작성한 결과물이 IntelliJ 에디터에 표시된 모습
>
> ![Agent 구현 계획](../screenshot/step09-context-plan.png)

---

## 태스크 3: 테스트 먼저 작성 (Test First) (5분)

Copilot Chat(**Agent 모드**)에서 스펙과 구현 계획을 기반으로 테스트를 생성합니다:

```
#file:.specs/requirements.md
#file:.specs/api-design.md
#file:.specs/data-model.md
#file:context/todo.md

위 스펙과 구현 계획을 기반으로, 구현 전에 TodoControllerTest.java에 테스트 코드를 먼저 작성해줘.

다음 케이스를 반드시 포함:
- 정상 생성 (201 + 응답 검증)
- 빈 제목으로 생성 시 400 에러
- 우선순위별 필터링 (GET /todos?priority=HIGH)
- 페이지네이션 동작 (GET /todos?page=1&size=5)
- 존재하지 않는 TODO 수정/삭제 시 404
- PATCH로 부분 수정 시 updatedAt 갱신

스펙 문서의 요구사항을 빠짐없이 검증하는 테스트를 작성해줘.
```

> 💡 `context/todo.md` 파일이 없는 경우, `#file:context/todo.md` 행을 제거하고 스펙 문서만으로도 테스트를 생성할 수 있습니다.

> ✨ **Agent 모드의 핵심 장점**: Agent 모드에서 이 프롬프트를 실행하면, Copilot이 테스트 파일만 생성하는 것이 아니라 **테스트를 실행하고, 실패를 확인한 후, 테스트를 통과시키기 위한 실제 소스코드(DTO, Controller, Model 등)까지 자동으로 구현**될 수 있습니다.


---

## 태스크 4: 구현 (Execute) (5분)

```
#file:.specs/requirements.md
#file:.specs/api-design.md
#file:.specs/data-model.md
#file:TodoControllerTest.java

위 스펙 문서와 테스트를 참고하여,
테스트를 통과할 때까지 DTO record, Todo 모델, TodoController를 처음부터 작성해줘.

인메모리 ArrayList 저장소를 사용하고:
- Priority enum 지원
- 페이지네이션 지원
- 필터링 지원
을 포함해야 합니다.
```

---

## 태스크 5: 테스트로 검증 (3분)

```bash
./gradlew test
```

**모든 테스트가 통과해야 합니다!**

만약 실패하면:

```
터미널에 테스트 에러가 났어. 스펙 문서를 다시 읽고 구현을 수정해줘

#file:.specs/requirements.md
#file:TodoControllerTest.java
```

> 💡 **핵심**: 테스트를 고치지 말고 구현을 고치세요! 테스트는 스펙의 검증 수단입니다.

---

## ✅ 검증 체크리스트

- [ ] `.specs/` 폴더에 요구사항 + API 설계 + 데이터 모델 문서 작성
- [ ] `context/todo.md`에 Agent가 구현 계획 작성 (Agent 모드 필요)
- [ ] 테스트가 먼저 작성됨 (Red 단계)
- [ ] Copilot이 구현을 완성 (Green 단계)
- [ ] `./gradlew test` 전체 통과
- [ ] 우선순위 필터링 동작 확인
- [ ] 페이지네이션 동작 확인

---

## 🔧 테스트가 실패하면? — Copilot으로 Red → Green

SDD에서 테스트 실패는 **정상**입니다. Red → Green이 목표니까요!

### 방법 1: 스펙을 포함해서 수정 요청

```
#file:.specs/requirements.md
#file:TodoControllerTest.java
테스트가 실패해. 스펙을 참고해서 구현을 수정해줘
```

### 방법 2: 구현과 테스트 함께 보여주기

```
#file:TodoControllerTest.java 의 테스트가 실패해.
#file:TodoController.java 에서 뭘 고쳐야 하는지 알려줘
```

> 💡 테스트 자체를 고치지 말고 구현을 고치세요!
> 그래도 안 되면 `complete/` 폴더의 코드와 비교해 보세요.

---

## 핵심 인사이트

> **"코드가 아닌 문서로 AI에게 의도를 전달하라"**
>
> - **문서 스펙** = "무엇을", "왜" → AI가 맥락을 정확히 파악
> - **테스트** = 스펙의 검증 수단 → 구현이 스펙을 만족하는지 자동 확인
> - **구현** = AI가 스펙 + 테스트를 보고 생성 → 예측 가능한 결과
>
> "우선순위 기능 추가해줘" vs "이 스펙 문서를 읽고 테스트를 통과하는 코드를 작성해줘"
> → 후자가 항상 더 정확합니다.

---

## 다음 단계

→ [Step 10. README 문서화](../step-10-bonus-readme/README.md)
