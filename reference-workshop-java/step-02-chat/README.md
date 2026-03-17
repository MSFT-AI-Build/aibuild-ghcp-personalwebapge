# Step 2. Copilot Chat

> ⏱️ 20분 | 난이도 ⭐
>
> 🎯 **핵심 학습: Chat 패널 + `@`, `#`, `/` 명령어**
>
> **체감: "대화로 코드를 다룰 수 있다!"**

---

## 코드 폴더

| 폴더 | 설명 |
|------|------|
| `starter/` | Step 1 완성 코드 (인메모리 CRUD TODO API) — 여기서 시작하세요 |
| `complete/` | 이번 스텝 완성 코드 — 막힐 때 참고하세요 |

---

## 왜 두 번째인가?

Step 1에서 수동으로 코드를 작성했으니, 이제 **대화**로 코드를 다루는 경험을 합니다.
"검색 + 코딩 + 질문"이 한 곳에서 해결됩니다.

---

## Chat 열기

**우측 사이드바 -> GitHub Copilot Chat 아이콘**을 클릭하여 Chat 패널을 엽니다.


![Chat 패널 열기](../screenshot/step02-open-chat.png)

---

## Chat 패널 UI 구성

### 입력창 구성

Chat 입력창 주변에는 다음 요소들이 있습니다:

| 요소 | 설명 |
|------|------|
| 📎 (클립 아이콘) | 파일을 드래그하거나 첨부하여 컨텍스트로 전달합니다 |
| 파일 태그 (예: `TodoController.java`) | 현재 참조 중인 파일. "끄기"를 클릭하면 제외할 수 있습니다 |
| 입력창 | `#`, `@`, `/` 키를 눌러 컨텍스트 추가, 확장 기능, 명령어를 사용할 수 있습니다 |
| 모드 선택기 (예: `Agent`) | 좌측 하단 드롭다운. Ask, Edit, Agent, Plan 중 선택합니다 |
| 모델 선택기 (예: `Claude Sonnet 4.6`) | 응답에 사용할 AI 모델을 변경할 수 있습니다 |
| 🔧 (도구 아이콘) | MCP 서버 등 외부 도구(Tool)를 연결하고 관리합니다. Agent 모드에서 활용됩니다 |


![Chat 입력창 구성](../screenshot/step02-chat-input-ui.png)

### 모드 선택 (Ask 드롭다운)

입력창 좌측 하단의 **모드 선택 드롭다운**을 클릭하면 4가지 모드를 선택할 수 있습니다:

| 모드 | 설명 | 
|------|------|
| **Ask** | 질문/답변 모드. 코드를 직접 수정하지 않고 답변만 합니다 |
| **Edit** | 지정한 파일들을 직접 편집합니다. 변경할 파일을 선택할 수 있습니다 |
| **Agent** | 자율적으로 파일 생성/수정/삭제 + 터미널 실행까지 수행합니다 |
| **Plan** | 구현 계획을 먼저 세우고, 승인 후 실행합니다 |

![모드 선택 드롭다운](../screenshot/step02-mode-dropdown.png)

---

## `#`, `@`, `/` — Chat 명령어 기본 개념

Chat 입력창에는 `Add context (#), extensions (@), commands (/)` 힌트가 표시됩니다.
이 세 가지 특수 기호를 조합하여 Copilot과 대화합니다.

| 기호 | 역할 | 설명 | 예시 |
|------|------|------|------|
| `#` | **컨텍스트 추가** (context) | 특정 파일, 폴더, 또는 Tool을 선택하여 전달합니다 | `#file:TodoController.java` |
| `@` | **확장 기능** (extensions) | Copilot 확장 기능을 호출합니다 | `@project`, `@github` |
| `/` | **명령어** (commands) | Copilot에게 수행할 작업을 지시합니다 | `/explain`, `/fix`, `/tests` |

### `@` 확장 기능 (Extensions)

| 확장 | 설명 |
|------|------|
| `@project` | **프로젝트 컨텍스트** — 현재 프로젝트의 파일 구조, 의존성, 코드를 분석하여 맞춤 답변을 제공합니다 (VS Code의 `@workspace`에 해당) |
| `@github` | **GitHub 스킬** — 웹 검색, 코드 검색 등 GitHub 관련 기능을 활용합니다. `@github #web 최신 Node.js LTS 버전은?` 처럼 웹 검색도 가능합니다 |
| `@github-copilot-coding-agent` | **코딩 에이전트** — GitHub 원격 환경에서 코딩 작업을 수행하는 에이전트를 호출합니다 |

### `/` 명령어 (Commands)

| 명령어 | 설명 |
|--------|------|
| `/explain` | 선택한 코드 또는 현재 파일의 동작을 설명합니다 |
| `/fix` | 선택한 코드의 문제를 분석하고 수정을 제안합니다 |
| `/tests` | 선택한 코드에 대한 단위 테스트를 생성합니다 |
| `/help` | GitHub Copilot 사용법에 대한 간단한 가이드를 보여줍니다 |

> 💡 `/`를 입력하면 사용 가능한 모든 명령어 목록이 표시됩니다.

![Chat 입력 힌트](../screenshot/step02-chat-hint.png)

### 조합 예시

```
@project /explain #file:TodoController.java
```
→ "**프로젝트**를 참고하여, **TodoController.java**를 **설명해줘**"

> 💡 세 기호를 모두 쓸 필요는 없습니다. 필요한 것만 조합하면 됩니다.

---

## 태스크 A: `@` — 프로젝트 맥락 질문 (3분)

> 💡 이 태스크는 **Ask 모드**에서 진행합니다. 좌측 하단 모드 드롭다운이 `Ask`인지 확인하세요.

Chat에 입력:

```
@project 이 프로젝트에 Spring Data JPA를 적용하려면 어떻게 해야 하나요?
```

> 💡 IntelliJ에서는 `@project`를 사용합니다 (VS Code의 `@workspace`에 해당)

**관찰**: `@project`가 프로젝트의 파일 구조와 의존성을 분석하여 맞춤 답변을 합니다.

---

## 태스크 B: `#` — 코드 설명 받기 (3분)

> 💡 이 태스크도 **Ask 모드**에서 진행합니다.

Chat에 입력:

```
/explain #file:TodoController.java 한국어로 설명해줘
```

**관찰**: `#file:`로 파일을 지정하면 Copilot이 해당 파일의 구조와 각 엔드포인트의 역할을 한국어로 설명합니다.

---

## 태스크 C: `/fix` — 버그 수정 (5분)

> ⚠️ 이 태스크는 **Agent 모드**로 전환해야 합니다. 좌측 하단의 모드 드롭다운에서 `Agent`를 선택하세요.

1. `TodoController.java`의 `createTodo` 메서드에서 **의도적 버그** 삽입:

```java
// 이 부분을 일부러 잘못 고치세요:
Todo newTodo = new Todo();
newTodo.setId(nextId++);
newTodo.setTitle(request.getDescription());  // ← 버그! title이 아니라 description
```

2. 버그가 있는 코드를 선택하고 Agent 모드 Chat에 입력:

```
/fix
```

> 📸 **[IntelliJ 스크린샷]** 버그가 있는 코드를 선택한 상태에서 `/fix` 명령을 실행하고 Copilot이 수정 제안을 하는 화면
>
> ![/fix 명령](../screenshot/step02-fix-command.png)

**관찰**: Copilot이 잘못된 필드 매핑과 기본값을 정확히 찾아냅니다.

---

## 태스크 D: `/tests` — 테스트 코드 생성 (5분)

> ⚠️ 이 태스크도 **Agent 모드**에서 진행합니다. (태스크 C에서 이미 전환했다면 그대로 유지)

Agent 모드 Chat에 입력:

```
/tests #file:TodoController.java
```
![/tests 명령](../screenshot/step02-tests-command.png)

### 생성된 테스트 실행

**방법 1: IntelliJ에서 실행 (권장)**

생성된 테스트 파일을 열고 클래스명 옆의 ▶️ 실행 버튼을 클릭합니다.

![/tests 완료](../screenshot/step02-tests-completed.png)

**방법 2: 터미널에서 실행**

```bash
./gradlew test
```

![/tests 터미널](../screenshot/step02-tests-completed-terminal.png)

---

## ✅ 검증 체크리스트

- [ ] `@project` 질문에 프로젝트 맥락이 반영된 답변 확인
- [ ] `#file:`로 코드 설명 받음
- [ ] `/fix`로 의도적 버그 수정
- [ ] `/tests`로 테스트 파일 생성 후 `./gradlew test` 통과

---

## 핵심 인사이트

- **`#file:`로 범위를 좁혀라** — 프로젝트 전체를 보여주면 답이 흐려집니다. 관련 파일만 지정하면 정확도가 올라갑니다
- **모드를 구분하라** — 질문은 Ask, 코드 수정은 Agent. 목적에 맞는 모드를 선택하세요
- **`@`, `#`, `/`를 조합하라** — 컨텍스트(`#`) + 확장(`@`) + 명령(`/`)을 조합하면 한 줄로 복잡한 작업을 지시할 수 있습니다
- **에러도 Chat으로 해결하라** — `/fix`와 에러 메시지 붙여넣기만으로 대부분의 문제가 해결됩니다

---

## 트러블슈팅

<details>
<summary><strong>🔧 에러가 나면? — 이 단계에서 배운 기능으로 해결!</strong></summary>

### `/fix` 사용하기

에러가 난 파일을 열고 Chat에 입력:
```
/fix #file:TodoController.java
```

### 에러 메시지를 직접 붙여넣기

```
이 에러 수정해줘:
JUnit에서 AssertionError가 나는데, 응답 필드가 expected와 다르대
```

> 💡 `#file:`로 관련 파일을 지정하면 더 정확한 답을 얻습니다.
> 그래도 안 되면 `complete/` 폴더의 코드와 비교해 보세요.

</details>

---


## 다음 단계

→ [Step 3. Instructions](../step-03-instructions/README.md)
