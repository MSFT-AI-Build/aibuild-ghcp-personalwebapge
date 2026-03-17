# Step 4. Prompt Files

> ⏱️ 20분 | 난이도 ⭐⭐
>
> 🎯 **핵심 학습: `.github/prompts/*.prompt.md`**
>
> **체감: "/ 하나로 반복 작업이 끝난다!"**

---

## 코드 폴더

| 폴더 | 설명 |
|------|------|
| `starter/` | Step 3 완성 코드 (기본 CRUD + Instructions) — 여기서 시작하세요 |
| `complete/` | 이번 스텝 완성 코드 (+ Prompt Files 추가) — 막힐 때 참고하세요 |

---

## 왜 Prompt Files인가?

Step 3의 **Instructions**가 "항상 적용되는 규칙"이라면,
**Prompt Files**는 "필요할 때 꺼내 쓰는 매크로"입니다.

| 구분 | Instructions | Prompt Files |
|------|-------------|--------------|
| 적용 시점 | 항상 자동 | `/명령어`로 수동 호출 |
| 용도 | 프로젝트 규칙 | 반복 작업 자동화 |
| 비유 | 교칙 | 도구 상자 |

---

## Prompt Files란?

`.github/prompts/` 폴더에 `.prompt.md` 파일을 생성하면,
Chat에서 `/파일명`으로 호출할 수 있습니다.

> ⚠️ **IDE별 차이**: `${input:변수명}` 문법은 **VS Code에서만 지원**됩니다.
> IntelliJ에서는 변수 입력 팝업이 나타나지 않으므로, 프롬프트 호출 시 **채팅 메시지로 대상을 직접 전달**하는 방식을 사용합니다.
> 아래 프롬프트 파일들은 두 가지 방식을 모두 지원하도록 작성되어 있습니다.

```
.github/
├── copilot-instructions.md              ← Step 3에서 생성 (항상 적용)
├── instructions/
│   ├── testing.instructions.md          ← Step 3에서 생성 (테스트 파일에 적용)
│   └── api.instructions.md              ← Step 3에서 생성 (Controller에 적용)
└── prompts/                             ← 이번 스텝에서 생성!
    ├── test.prompt.md                   → /test 로 호출
    ├── add-endpoint.prompt.md           → /add-endpoint 로 호출
    └── refactor.prompt.md               → /refactor 로 호출
```

![Prompt Files 폴더 구조](../screenshot/step04-prompts-folder.png)

---

## 태스크 1: 테스트 생성 프롬프트 (5분)

`.github/prompts/test.prompt.md` 생성:

```markdown
---
agent: "agent"
description: "선택한 모듈에 대한 테스트를 자동 생성합니다"
---

#file:TodoController.java 의 코드 패턴을 참조하여,
채팅에서 언급하거나 선택된 '대상'에 대한 테스트를 작성해주세요.

대상: ${input:testTarget}
(변수 입력이 없으면 채팅 메시지에서 언급된 대상을 사용하세요)

## 규칙
- JUnit 5 + MockMvc 사용
- @Nested 클래스로 그룹핑
- Given-When-Then 주석 패턴
- 메서드명: test_동작_조건_결과()
- 정상 케이스 / 에러 케이스 / 경계값 각각 최소 1개
- 한국어 주석과 설명
```

### 사용법

**VS Code:**
1. Chat에서 `/test` 입력
2. `testTarget` 변수 입력 팝업에 `PUT /todos/{id}` 입력
3. Copilot이 해당 엔드포인트의 테스트를 자동 생성

**IntelliJ:**
1. Chat에서 `/test PUT /todos/{id}` 입력 (프롬프트와 대상을 함께 전달)
2. Copilot이 채팅 메시지에서 대상을 인식하여 테스트를 자동 생성


---

## 태스크 2: 엔드포인트 추가 프롬프트 (5분)

`.github/prompts/add-endpoint.prompt.md` 생성:

```markdown
---
agent: "agent"
description: "기존 패턴에 맞춰 새 API 엔드포인트를 추가합니다"
---

채팅에서 언급하거나 선택된 '기능'에 대한 API 엔드포인트를 추가해주세요.

기능: ${input:featureDescription}
(변수 입력이 없으면 채팅 메시지에서 언급된 기능을 사용하세요)

## 순서 (반드시 이 순서대로!)
1. **Controller**: TodoController에 엔드포인트 메서드 추가
2. **TEST**: test/에 새 엔드포인트에 대한 테스트 작성
3. **VERIFY**: ./gradlew test로 전체 테스트 통과 확인

## 참고 파일
#file:TodoController.java
```

### 사용법

**VS Code:**
1. Chat에서 `/add-endpoint` 입력
2. `featureDescription` 변수 입력 팝업에 `PATCH /todos/{id} 부분 수정 엔드포인트 추가` 입력
3. Copilot이 기존 코드 패턴에 맞춰 Controller → 테스트 순서로 자동 구현

**IntelliJ:**
1. Chat에서 `/add-endpoint PATCH /todos/{id} 부분 수정 엔드포인트 추가` 입력
2. Copilot이 채팅 메시지에서 기능을 인식하여 Controller → 테스트 순서로 자동 구현

---

## 태스크 3: 리팩토링 프롬프트 (5분)

`.github/prompts/refactor.prompt.md` 생성:

```markdown
---
agent: "agent"
description: "선택한 코드를 리팩토링합니다"
---

채팅에서 언급하거나 선택된 '대상'을 리팩토링해주세요.

대상: ${input:refactorTarget}
(변수 입력이 없으면 채팅 메시지에서 언급된 대상을 사용하세요)

## 규칙
- 동작 변경 없이 구조만 개선
- 리팩토링 전후 테스트 통과 필수 (./gradlew test)
- 변경 사항을 한국어로 요약해주세요

## 리팩토링 관점
- 코드 중복 제거
- 메서드/클래스 분리
- 네이밍 개선
- 타입 안전성 향상
```

### 사용법

**VS Code:**
1. Chat에서 `/refactor` 입력
2. `refactorTarget` 변수 입력 팝업에 `TodoController의 findById 중복 로직` 입력
3. Copilot이 동작 변경 없이 구조를 개선하고 테스트 통과 확인

**IntelliJ:**
1. Chat에서 `/refactor TodoController의 findById 중복 로직` 입력 (프롬프트와 대상을 함께 전달)
2. Copilot이 채팅 메시지에서 대상을 인식하여 리팩토링 수행

---

## 태스크 4: 프롬프트 파일 활용 실습 (5분)

### 1. 테스트 생성 프롬프트 실행

Chat에 입력:

> `/test GET /todos/{id} 단건 조회`

→ Copilot이 단건 조회 엔드포인트 관련 테스트를 자동 생성합니다.

### 2. 엔드포인트 추가 프롬프트 실행

Chat에 입력:

> `/add-endpoint PATCH /todos/{id} 부분 수정 엔드포인트`

→ Copilot이 Controller → 테스트 순서로 PATCH 엔드포인트를 자동 구현합니다.

---

## ✅ 검증 체크리스트

- [ ] `.github/prompts/test.prompt.md` 생성
- [ ] `.github/prompts/add-endpoint.prompt.md` 생성
- [ ] `.github/prompts/refactor.prompt.md` 생성
- [ ] `/test` 입력 시 프롬프트 파일이 로드됨
- [ ] `/add-endpoint`로 새 엔드포인트 자동 생성

---

## 핵심 인사이트

> **"자주 하는 요청은 Prompt File로 만들어라"**
>
> Chat 히스토리에 묻히는 반복 프롬프트를 파일로 저장하면:
> - 팀원 누구나 동일한 품질로 사용 가능
> - Git에 커밋하여 버전 관리
> - `${input:변수}`로 유연하게 재사용 (VS Code)
> - 채팅 메시지로 대상을 직접 전달하여 재사용 (IntelliJ)

---

## 다음 단계

→ [Step 5. Agent 모드](../step-05-agent/README.md)

> 💡 Prompt File과 함께 더 체계적인 개발 방법론이 궁금하다면 → [Step 9. SDD (보너스)](../step-09-spec-driven/README.md)
