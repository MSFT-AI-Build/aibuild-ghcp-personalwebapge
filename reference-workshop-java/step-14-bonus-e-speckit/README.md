# Bonus E. GitHub Spec Kit — Spec-Driven Development 자동화

> ⏱️ 30분 | 난이도 ⭐⭐ | **체감: "스펙만 쓰면 코드가 나온다!"**
>
> 🎯 **목표**: GitHub Spec Kit을 사용해 Spec-Driven Development 워크플로우를 체계적으로 자동화하는 방법 체험

---

## 📚 사전 준비

- **메인 트랙 Step 04 이상 완료** (Spec-Driven Development 개념 숙지)
- IntelliJ IDEA + GitHub Copilot 플러그인 설치 완료
- Python 3.11+ 설치 (Specify CLI 실행용)
- [uv](https://docs.astral.sh/uv/) 패키지 매니저 설치

---

## 📖 배경: GitHub Spec Kit이란?

**Step 04**에서 우리는 수동으로 SDD를 실습했습니다:
> DTO record(스펙) → JUnit 테스트 → Controller/Service 구현

**GitHub Spec Kit**은 이 과정을 **체계적인 워크플로우**로 자동화하는 오픈소스 툴킷입니다.

| 수동 SDD (Step 04) | Spec Kit SDD |
|-------------------|-------------|
| 직접 DTO 작성 | `/speckit.specify`로 요구사항 정의 |
| 직접 테스트 작성 | `/speckit.plan`으로 기술 설계 |
| Copilot에게 구현 요청 | `/speckit.tasks` → `/speckit.implement`로 자동 구현 |
| 개별 파일 관리 | 체계적인 아티팩트 관리 (spec, plan, tasks) |

> 💡 Spec Kit은 "Vibe Coding" 대신 **예측 가능한 결과**에 집중합니다. 스펙이 실행 가능한 산출물이 되는 것이 핵심입니다.

---

## ⚡ 실습 1: Spec Kit 설치 및 프로젝트 초기화 (5분)

### 1-1. Specify CLI 설치

```bash
# uv가 없다면 먼저 설치
curl -LsSf https://astral.sh/uv/install.sh | sh

# Specify CLI 설치 (persistent 설치 — 권장)
uv tool install specify-cli --from git+https://github.com/github/spec-kit.git

# 설치 확인
specify check
```

### 1-2. 프로젝트 초기화

```bash
# 새 프로젝트로 시작하는 경우
specify init todo-speckit --ai copilot

# 또는 기존 프로젝트 디렉토리에서
cd my-existing-project
specify init . --ai copilot
```

> 💡 `--ai copilot` 옵션으로 GitHub Copilot용 슬래시 명령어가 자동 구성됩니다.

### 관찰 포인트
- [ ] `specify init` 후 `.github/` 아래에 어떤 파일이 생성되는가?
- [ ] 슬래시 명령어 프롬프트 파일들이 생성되었는가?

> 📸 **[IntelliJ 스크린샷]** `specify init` 실행 후 `.github/` 폴더 아래에 생성된 Spec Kit 관련 파일들이 IntelliJ Project 탐색기에 표시된 모습
>
> ![Spec Kit 초기화 결과](./images/step14-speckit-init.png)

---

## 🏗️ 실습 2: Constitution → Specify → Plan (10분)

### 2-1. 프로젝트 원칙 수립 (Constitution)

Copilot Chat(Agent 모드)에서:

```
/speckit.constitution 다음 원칙으로 프로젝트 거버넌스를 만들어줘:
- 코드 품질: Java 17+, Spring Boot 3.x, Javadoc 필수
- 테스트: JUnit 5 + MockMvc, 커버리지 80% 이상
- 아키텍처: Controller → Service → Repository 레이어
- API 설계: RESTful, Jakarta Validation, DTO record 사용
- 언어: 모든 주석과 문서는 한국어
```

**관찰**: Constitution 파일이 생성되어 이후 모든 개발의 **가이드라인** 역할을 합니다.

### 2-2. 스펙 정의 (Specify)

```
/speckit.specify TODO 앱을 만들고 싶어.
사용자는 할일을 생성, 조회, 수정, 삭제할 수 있어야 해.
각 할일에는 제목, 설명, 우선순위(LOW/MEDIUM/HIGH), 완료 여부가 있어.
할일 목록은 우선순위별로 필터링하고 페이지네이션을 지원해야 해.
```

> 💡 **핵심**: 기술 스택을 지정하지 않고 **"무엇을"**, **"왜"** 에 집중합니다. "어떻게"는 다음 단계에서!

### 2-3. 기술 설계 (Plan)

```
/speckit.plan Java 17 + Spring Boot 3.x 프레임워크를 사용하고,
H2 인메모리 데이터베이스 + Spring Data JPA로 데이터를 저장해.
DTO는 Java record로 정의하고, Jakarta Validation으로 검증해.
JUnit 5 + MockMvc로 테스트하고, Gradle(Kotlin DSL)로 빌드해.
```

### 관찰 포인트
- [ ] Constitution → Specify → Plan 순서로 아티팩트가 쌓이는 과정
- [ ] Plan에서 Spring Boot 기술 스택이 반영된 구체적인 설계가 나오는가?
- [ ] Step 04에서 수동으로 했던 것과 비교해 어떤 차이가 있는가?

> 📸 **[IntelliJ 스크린샷]** Chat에서 `/speckit.specify` → `/speckit.plan` 순서로 실행하여 스펙과 기술 설계가 생성된 Chat 패널 화면
>
> ![Spec Kit 워크플로우](./images/step14-speckit-workflow.png)

---

## 🔨 실습 3: Tasks → Implement (10분)

### 3-1. 태스크 분해 (Tasks)

```
/speckit.tasks
```

Spec Kit이 Plan을 기반으로 **실행 가능한 태스크 목록**을 자동 생성합니다.

### 3-2. 구현 실행 (Implement)

```
/speckit.implement
```

Copilot Agent가 태스크를 순서대로 실행하여 전체 애플리케이션을 구현합니다.

### 3-3. 검증

```bash
# 테스트 실행
./gradlew test

# 서버 실행
./gradlew bootRun

# API 동작 확인
curl http://localhost:8080/todos
# 또는 H2 콘솔: http://localhost:8080/h2-console
```

### 관찰 포인트
- [ ] Tasks가 적절한 순서와 크기로 분해되었는가?
- [ ] Implement가 Constitution의 원칙을 따르며 코드를 생성하는가?
- [ ] 수동 SDD(Step 04)와 비교해 결과물 품질은 어떠한가?

---

## 🔍 실습 4: 선택적 고급 명령어 (5분)

### Clarify — 모호한 부분 확인

Plan 전에 실행하면 스펙의 모호한 부분을 질문으로 정리해 줍니다:

```
/speckit.clarify
```

### Analyze — 일관성 검증

Tasks 후, Implement 전에 실행하면 아티팩트 간 일관성을 검증합니다:

```
/speckit.analyze
```

### Checklist — 품질 체크리스트

요구사항의 완전성, 명확성, 일관성을 검증하는 체크리스트를 생성합니다:

```
/speckit.checklist
```

---

## 📊 수동 SDD vs Spec Kit SDD 비교

| 항목 | Step 04 (수동 SDD) | Spec Kit SDD |
|------|-------------------|-------------|
| **스펙 작성** | DTO record 직접 작성 | `/speckit.specify`로 자연어 정의 |
| **설계** | 개발자가 아키텍처 결정 | `/speckit.plan`으로 체계적 설계 |
| **태스크** | Chat에서 요청 | `/speckit.tasks`로 자동 분해 |
| **구현** | Copilot에게 개별 요청 | `/speckit.implement`로 일괄 실행 |
| **검증** | ./gradlew test 수동 실행 | `/speckit.analyze` + ./gradlew test |
| **재현성** | 매번 프롬프트 작성 | 아티팩트가 Git에 영구 보존 |
| **적합한 상황** | 작은 기능 추가, 학습 | 신규 프로젝트, 대규모 기능 개발 |

---

## ✅ 전체 체크리스트

- [ ] Specify CLI 설치 및 `specify check` 통과
- [ ] `specify init` 으로 프로젝트 초기화
- [ ] `/speckit.constitution`으로 프로젝트 원칙 수립
- [ ] `/speckit.specify`로 요구사항 정의
- [ ] `/speckit.plan`으로 기술 설계
- [ ] `/speckit.tasks`로 태스크 분해
- [ ] `/speckit.implement`로 구현 실행
- [ ] ./gradlew test 통과 및 API 동작 확인
- [ ] (선택) `/speckit.clarify`, `/speckit.analyze` 체험

---

## 💡 핵심 인사이트

- **스펙이 코드가 된다**: 전통적 개발에서 스펙은 코딩 후 버려지는 문서였지만, Spec Kit에서는 스펙이 **실행 가능한 산출물**로 직접 구현을 생성합니다.
- **단계별 정제**: 한 번의 프롬프트로 모든 것을 생성하는 대신, Constitution → Specify → Plan → Tasks → Implement로 **다단계 정제**를 거칩니다.
- **아티팩트 보존**: 모든 중간 산출물(스펙, 설계, 태스크)이 파일로 저장되어 Git에 커밋할 수 있습니다. 왜 이렇게 만들었는지 나중에 추적할 수 있습니다.
- **Step 04와의 관계**: Step 04에서 배운 SDD 개념은 Spec Kit의 기반 철학과 동일합니다. Spec Kit은 이를 **도구로 체계화**한 것입니다.

---

## 🔗 참고 링크

- [GitHub Spec Kit 저장소](https://github.com/github/spec-kit)
- [Spec-Driven Development 방법론](https://github.com/github/spec-kit/blob/main/spec-driven.md)
- [Spec Kit 영상 소개](https://www.youtube.com/watch?v=a9eR1xsfvHg)
- [Community Walkthrough: Spring Boot + React](https://github.com/mnriem/spec-kit-spring-react-demo)

---

## 다음 단계

🎉 모든 워크숍 과정을 완료했습니다! 수고하셨습니다!
