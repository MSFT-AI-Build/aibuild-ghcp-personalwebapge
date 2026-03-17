# Step 1. 코드 완성 (Inline Suggestions)

> ⏱️ 20분 | 난이도 ⭐
>
> 🎯 **핵심 학습: Tab 자동완성 (Ghost Text)**
>
> **체감: "타이핑이 빨라졌다!"**

---

## 코드 폴더

| 폴더 | 설명 |
|------|------|
| `starter/` | Step 0 결과 (빈 Spring Boot 프로젝트) — 여기서 시작하세요 |
| `complete/` | 이번 스텝 완성 코드 — 막힐 때 참고하세요 |

---

## 왜 이게 첫 번째인가?

가장 낮은 진입 장벽입니다.
코드를 치는 도중에 AI가 **회색 텍스트(Ghost Text)** 로 제안하는 것을 `Tab`으로 수락하면 됩니다.

---

## 태스크 1: TODO 모델 작성 (5분)

`src/main/java/com/example/todo/Todo.java` 파일을 생성하고, **아래 코드까지만 타이핑**한 뒤 멈추세요:

```java
package com.example.todo;

public class Todo {
    private Long id;
    private String title;
    // ← 여기서 멈추고 Tab! Copilot이 나머지 필드를 제안합니다
```

> ![Todo 모델 Ghost Text](../screenshot/step01-todo-ghost-text.png)

**Tab** 을 누르면 Copilot이 나머지 필드를 제안합니다/

**Copilot 조작법**

| 동작 | IntelliJ (Win/Linux) | IntelliJ (Mac) |
|------|---------------------|----------------|
| 제안 수락 | `Tab` | `Tab` |
| 제안 거절 | `Esc` | `Esc` |
| 다른 제안 | `Alt+]` / `Alt+[` | `Option+]` / `Option+[` |

---

## 태스크 2: CRUD 엔드포인트 작성 (10분)

`src/main/java/com/example/todo/TodoController.java` — **한글 주석을 먼저 쓰고** 코드가 따라오는 패턴:

```java
package com.example.todo;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final List<Todo> todos = new ArrayList<>();
    private long nextId = 1;

    // 모든 TODO 목록을 반환하는 엔드포인트
    // ← 주석을 쓰고 Enter 치면 메서드가 자동 생성됩니다!
```

> ![주석으로 코드 생성](../screenshot/step01-comment-to-code.png)

**만들어야 할 엔드포인트 4개**

| Method | Path | 설명 |
|--------|------|------|
| GET | `/todos` | 전체 목록 조회 |
| POST | `/todos` | 새 TODO 생성 |
| PUT | `/todos/{id}` | TODO 수정 |
| DELETE | `/todos/{id}` | TODO 삭제 |

---

## 태스크 3: 서버 실행 & 수동 테스트 (5분)

### 서버 실행

**방법 1: IntelliJ에서 실행 (권장)**

`TodoApplication.java` 파일을 열고 `main` 메서드 옆의 ▶️ 실행 버튼을 클릭합니다.

![서버 실행](../screenshot/step01-run-application.png)

**방법 2: 터미널에서 실행**

```bash
./gradlew bootRun
```

### 수동 테스트

```bash
# TODO 생성
curl -X POST http://localhost:8080/todos \
  -H "Content-Type: application/json" \
  -d '{"title": "첫 번째 할일", "description": "테스트"}'

# 목록 조회
curl http://localhost:8080/todos

# 수정
curl -X PUT http://localhost:8080/todos/1 \
  -H "Content-Type: application/json" \
  -d '{"title": "수정된 할일", "completed": true}'

# 삭제
curl -X DELETE http://localhost:8080/todos/1
```

---

## ✅ 검증 체크리스트

- [ ] Copilot이 모델 필드를 제안했고 수락함
- [ ] 주석 → 메서드 패턴으로 엔드포인트 4개 완성
- [ ] `./gradlew bootRun` 서버가 정상 실행
- [ ] curl로 CRUD 모두 동작

---

## 핵심 인사이트

> **"주석/Javadoc이 곧 프롬프트다"**
>
> 의도를 명확히 쓸수록 Copilot의 제안이 정확합니다.
> 주석 없이 빈 줄에서 `Tab`을 누르면 엉뚱한 코드가 나올 수 있습니다.

---

## 트러블슈팅

<details>
<summary><strong>🔧 에러가 나면? — Copilot으로 해결하기</strong></summary>

### 방법 1: Copilot Chat에 "에러 고쳐줘"

1. IntelliJ 우측 사이드바 > GitHub Copilot Chat
2. 에러 메시지를 붙여넣고:
   ```
   이 에러 수정해줘: [에러 메시지]
   ```

### 방법 2: 그래도 안 되면

- `complete/` 폴더의 코드와 비교해 보세요
- `complete/` 코드를 복사해서 진행해도 괜찮습니다 — 이번 단계의 목표는 **Inline Suggestion 체험**입니다

</details>

---


## 다음 단계

→ [Step 2. Copilot Chat](../step-02-chat/README.md)
