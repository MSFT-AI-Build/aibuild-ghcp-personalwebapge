# Step 5. Agent에게 복잡한 작업 위임하기

> ⏱️ 25분 | 난이도 ⭐⭐⭐
>
> 🎯 **핵심 학습: Agent에게 아키텍처 변경을 통째로 위임**
>
> **체감: "한마디에 파일 10개 이상이 동시에 바뀌고, 테스트까지 알아서 돌린다!"

---

## 코드 폴더

| 폴더 | 설명 |
|------|------|
| `starter/` | Step 4 완성 코드 (in-memory CRUD) — 여기서 시작하세요 |
| `complete/` | 이번 스텝 완성 코드 — 막힐 때 참고하세요 |

---

## Agent 모드 확인

이미 Agent 모드로 작업하고 있다면 이 단계는 건너뛰세요.
아직 전환하지 않았다면:

- **IntelliJ**: Copilot Chat 탭에서 Agent 모드 활성화

---

## 태스크: Agent에게 DB 연동 전체 위임 (20분)

지금까지는 Agent에게 "메서드 하나 만들어줘", "테스트 작성해줘" 같은 **단위 작업**을 시켰습니다.
이번에는 **"in-memory → Spring Data JPA + H2로 전환해줘"** 라는 **아키텍처 레벨의 요청**을 한 번에 던집니다.

### 프롬프트

Agent 모드 Chat에 입력:

```
TODO 앱에 H2 데이터베이스를 연동해줘.

요구사항:
1. Spring Data JPA 사용
2. 기존 in-memory List를 JPA Repository로 완전히 교체
3. entity/ 패키지에 JPA Entity 정의 (@Entity, @Id, @GeneratedValue)
4. repository/ 패키지에 JpaRepository 인터페이스 생성
5. service/ 패키지에 Service 클래스 생성 (비즈니스 로직 분리)
6. dto/ 패키지에 요청/응답 DTO 클래스 생성 (record 사용)
7. Controller를 controller/ 패키지로 이동하고 Service를 주입받아 사용
8. application.properties에 H2 설정 (인메모리 모드)
9. 기존 테스트 모두 통과하도록 수정 (@SpringBootTest + @Transactional)

참고:
#file:Todo.java
#file:TodoController.java
#file:TodoControllerTest.java
```

### Agent의 작업 관찰 — 이전 단계와 뭐가 다른가?

이전 단계에서 Agent는 파일 1~2개를 수정했습니다.
이번에는 Agent가 **스스로 계획을 세우고 연쇄적으로 작업**하는 것을 관찰하세요:

1. **`build.gradle.kts` 수정** — JPA, H2, Validation 의존성 추가
2. **`dto/` 패키지 생성** — Priority, TodoCreateRequest, TodoResponse 등 DTO 클래스
3. **`entity/Todo.java` 생성** — JPA Entity 클래스
4. **`repository/TodoRepository.java` 생성** — JpaRepository 인터페이스
5. **`service/TodoService.java` 생성** — 비즈니스 로직
6. **`controller/TodoController.java` 수정** — controller 패키지로 이동, Service 주입으로 변경
7. **`application.properties` 수정** — H2 DB 설정
8. **테스트 수정** — @SpringBootTest + @Transactional
9. **터미널에서 `./gradlew test` 실행** — 스스로 검증하고 실패하면 수정까지

> 💡 단일 파일 수정이 아닌 **9단계 연쇄 작업**을 하나의 프롬프트로 수행합니다.

![Agent 연쇄 작업](../screenshot/step05-agent-chain-work.png)

### ⚠️ 중요: 승인/거부

Agent가 파일을 변경할 때마다 **diff를 확인**할 수 있습니다:
- ✅ **Accept** — 변경 적용
- ❌ **Reject** — 변경 거부
- 📝 **수정 요청** — "이 부분은 다르게 해줘"

> **팁**: 한 번에 모든 변경을 수락하지 말고, 파일별로 리뷰하세요!

---

## 생성되는 핵심 코드 미리보기

### entity/Todo.java

```java
package com.example.todo.entity;

import com.example.todo.dto.Priority;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "todos")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority = Priority.MEDIUM;

    @Column(nullable = false)
    private boolean completed = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() { this.updatedAt = LocalDateTime.now(); }
}
```

### repository/TodoRepository.java

```java
package com.example.todo.repository;

import com.example.todo.entity.Todo;
import com.example.todo.dto.Priority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    Page<Todo> findByPriority(Priority priority, Pageable pageable);
}
```

### application.properties

```properties
spring.datasource.url=jdbc:h2:mem:tododb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
spring.jpa.show-sql=true
```

---

## 검증

```bash
# 테스트 실행
./gradlew test

# 서버 실행
./gradlew bootRun

# H2 콘솔 확인
# http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:tododb)

# TODO 생성
curl -X POST http://localhost:8080/todos \
  -H "Content-Type: application/json" \
  -d '{"title": "DB 연동 테스트", "priority": "HIGH"}'

# 목록 조회
curl http://localhost:8080/todos
```

---

## ✅ 검증 체크리스트

- [ ] `build.gradle.kts`에 JPA, H2, Validation 의존성 추가
- [ ] `dto/` 패키지에 Priority, TodoCreateRequest, TodoResponse 등 DTO 생성
- [ ] Agent가 `entity/Todo.java`를 생성함
- [ ] `repository/TodoRepository.java` JPA Repository 생성
- [ ] `service/TodoService.java` 비즈니스 로직 분리
- [ ] Controller를 `controller/` 패키지로 이동하고 Service 주입 사용
- [ ] `application.properties`에 H2 설정 추가
- [ ] `./gradlew test` 전체 통과
- [ ] H2 콘솔에서 데이터 확인 가능

---

## 트러블슈팅

<details>
<summary><strong>🔧 에러가 나면? — Agent에게 다시 시키기</strong></summary>

### 방법 1: Agent에게 직접 요청

```
터미널에 에러가 났어. 분석해서 수정해줘
```
> Agent가 터미널 출력을 읽고, 파일을 수정하고, 다시 테스트까지 실행합니다.

### 방법 2: 구체적으로 지시

```
./gradlew test가 실패하고 있어. Entity와 Repository 설정을 확인해서 고쳐줘
```

### 방법 3: 처음부터 다시

Agent가 만든 코드가 꼬였다면 `complete/` 폴더에서 시작하는 것도 방법입니다.

> 💡 Agent 모드의 장점: "이 부분 다시 해줘"라고만 하면 알아서 수정합니다.

</details>

---

## 핵심 인사이트

> **"Agent에게는 큰 그림을, 승인은 작은 단위로"**
>
> - **이전 단계와의 차이**: 단위 작업 → 아키텍처 변경 위임
> - 요구사항은 구체적이고 상세하게 전달 (번호 매긴 목록이 효과적)
> - 하지만 변경은 파일 하나씩 리뷰
> - Agent가 실수하면 "이 부분 다시 해줘"로 즉시 수정
> - Agent가 **스스로 테스트를 실행하고 오류를 수정**하게 하면 생산성이 극대화됩니다

---

## 다음 단계

→ [Step 6. Custom Agent 제작](../step-06-custom-agent/README.md)
