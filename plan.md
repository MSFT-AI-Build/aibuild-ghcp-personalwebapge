# GitHub Copilot HTML 발표자료 워크샵 플랜

## 1. 목표

이 워크샵의 목표는 GitHub Copilot의 주요 기능을 순서대로 실습할 수 있도록, 바로 시작 가능한 워크샵 세팅을 만드는 것입니다.

- 실습 산출물은 실제로 동작하는 HTML 발표자료 웹페이지다.
- 발표자료 결과물은 참가자가 각 step을 따라가며 직접 만들어간다.
- 내 역할은 그 결과물을 대신 만드는 것이 아니라, 참가자가 결과물을 만들 수 있도록 공통 파일, 규칙, 템플릿, 스크립트를 세팅하는 것이다.
- 실습 중 참가자가 수정할 실제 작업 대상은 `output/` 폴더 아래의 웹페이지다.
- `reference-workshop-java`처럼 `starter/complete`를 단계마다 복제하지 않는다.
- 각 step 폴더의 `instruction.md`는 설명 문서이면서 동시에 "채팅에 그대로 넣을 수 있는 프롬프트 모음" 역할을 해야 한다.
- 가능하면 GitHub CLI와 GitHub Pages를 활용해 로컬 작업과 배포 흐름까지 바로 실습 가능하게 한다.

---

## 2. 레퍼런스에서 가져올 핵심 원칙

`reference-workshop-java`를 기준으로 유지할 원칙은 아래와 같다.

1. 단계별 학습 곡선이 분명해야 한다.
2. 각 step은 "왜 이 기능을 지금 배우는가"를 설명해야 한다.
3. 각 step은 파일 생성, 실행 명령, 검증 체크리스트까지 포함해야 한다.
4. 이전 step의 산출물이 다음 step의 입력이 되어야 한다.
5. 기능 소개에서 끝내지 않고 다음 step에서 바로 재사용 가능한 세팅이 남아야 한다.

이 워크샵에서는 위 원칙을 유지하되, step마다 완성본을 복제하는 대신 단일 루트 워크샵 환경과 프롬프트 자산을 누적하는 형태로 바꾼다.

---

## 3. 워크샵 컨셉

### 프로젝트 주제

Copilot 기능 소개용 HTML 발표자료를 만드는 워크샵을 설계한다.

### 워크샵 결과물 형태

- 참가자가 직접 완성해갈 정적 HTML/CSS/JS 발표자료 웹페이지
- Copilot이 참고할 스펙 문서, instructions, prompt files, skills, agents, CLI 스크립트
- step별 프롬프트 가이드 문서
- GitHub Pages 배포까지 이어지는 운영 가이드

### 진행 원칙

- 워크샵은 `세팅 단계`와 `완성 단계`로 나눈다.
- `Step 00`부터 `Step 03`까지는 코드를 구현하는 단계가 아니라, Copilot이 잘 동작하도록 환경과 자산을 세팅하는 단계다.
- 즉 `SDD`, `Instructions`, `Prompt Files`, `Skills` 단계에서는 참가자가 HTML/CSS/JS 기능 구현 코드를 직접 작성하지 않는다.
- `Step 04`부터 `Step 06`에서부터 비로소 앞에서 만든 자산을 활용해 `output/` 웹페이지의 기능과 품질을 본격적으로 완성해간다.
- 이 저장소는 워크샵 설계와 세팅 관점에서는 완성형으로 관리할 수 있지만, 실제 참가자 경험은 각 step의 프롬프트를 직접 치고 결과를 확인하는 방식으로 설계한다.

### 기술 방향

- 우선순위는 "의존성 최소화"다.
- 기본안은 바닐라 HTML/CSS/JS다.
- 배포는 GitHub Pages를 기본안으로 잡는다.
- CLI 실습은 `git`, `gh`, 필요 시 `python -m http.server` 수준으로 단순하게 간다.
- 별도 빌드 도구 없이도 바로 실습을 시작할 수 있는 구조를 우선 채택한다.
- `output/`에는 참가자가 step마다 계속 수정할 실제 결과물 웹페이지를 둔다.
- 루트에는 결과물 제작을 돕는 최소 골격과 템플릿, Copilot 자산을 둔다.

---

## 4. 제안하는 루트 구조

```text
/
├── plan.md
├── README.MD
├── .specs/
│   ├── workshop-requirements.md
│   ├── slide-structure.md
│   └── ux-rules.md
├── output/
│   ├── index.html
│   └── assets/
│       ├── css/
│       ├── js/
│       └── img/
├── .github/
│   ├── copilot-instructions.md
│   ├── instructions/
│   ├── prompts/
│   ├── skills/
│   ├── agents/
│   ├── ISSUE_TEMPLATE/
│   └── workflows/
├── scripts/
│   ├── serve.sh
│   ├── check.sh
│   └── publish.sh
├── context/
│   └── implementation-plan.md
├── 00-spec-driven-development/
│   └── instruction.md
├── 01-instructions/
│   └── instruction.md
├── 02-prompt-files/
│   └── instruction.md
├── 03-skills/
│   └── instruction.md
├── 04-custom-agent/
│   └── instruction.md
├── 05-cli/
│   └── instruction.md
└── 06-coding-agent/
    └── instruction.md
```

### 구조 원칙

- 실제 작업 대상은 항상 루트 아래의 단일 워크샵 프로젝트다.
- `output/`은 참가자가 실습 중 직접 수정할 실제 결과물 웹페이지 작업 공간이다.
- 루트는 워크샵 운영 파일과 공통 세팅을 제공한다.
- step 폴더는 코드 복사본이 아니라 진행 가이드와 채팅 프롬프트만 가진다.
- 실습자는 각 step의 `instruction.md`에서 프롬프트를 복사해 Copilot Chat에 입력한다.
- step이 끝날수록 `output/`의 발표자료가 실제로 좋아지고, 동시에 Copilot 워크플로우 자산도 함께 쌓이도록 설계한다.

---

## 5. 단계별 학습 흐름

### 단계 구분

#### Phase A. 세팅 단계

- 범위: `Step 00` ~ `Step 03`
- 목표: Copilot이 일관되게 일하도록 스펙, 규칙, 프롬프트, skill을 준비한다.
- 원칙: 기능 구현 코드를 직접 쓰지 않는다.
- 참가자 경험: 채팅에 프롬프트를 넣고, 파일과 설정이 만들어지는 과정을 체험한다.

#### Phase B. 완성 단계

- 범위: `Step 04` ~ `Step 06`
- 목표: 앞 단계에서 만든 자산을 활용해 `output/` 웹페이지를 실제로 개선하고 완성한다.
- 원칙: 기능 추가, 품질 개선, 운영 자동화, GitHub 위임 흐름까지 이어간다.
- 참가자 경험: Custom Agent, CLI, Coding Agent를 써서 결과물을 다듬고 확장한다.

### Step 00. Spec-Driven Development

#### 학습 목표

- 문서 기반으로 발표자료 요구사항을 먼저 정의한다.
- Copilot에게 스펙 분석과 구현 계획 작성을 맡긴다.
- 참가자가 이후 step에서 무엇을 만들게 될지 기준 문서를 세운다.

#### 실습 산출물

- `.specs/workshop-requirements.md`
- `.specs/slide-structure.md`
- `.specs/ux-rules.md`
- `context/implementation-plan.md`
- `output/` 작업 공간의 최소 골격
- `00-spec-driven-development/instruction.md` 안의 분석/계획 프롬프트

#### 이 step의 역할

- 이후 step에서 참조할 요구사항 문서를 만든다.
- 참가자가 이후 step에서 `output/` 웹페이지를 구현할 때 AI가 흔들리지 않게 기준을 먼저 세운다.
- 이 단계에서는 기능 구현 코드를 쓰지 않고, 무엇을 만들지 정의하는 문서만 만든다.

#### 이 step에서 사용할 자산

- 스펙 템플릿
- 요구사항 분석 프롬프트
- 구현 계획 생성 프롬프트

#### 핵심 메시지

"AI에게 바로 코드를 시키기 전에, 스펙을 먼저 주면 결과가 훨씬 안정적이다."

---

### Step 01. Instructions

#### 학습 목표

- 저장소 공통 규칙과 경로별 규칙을 설정한다.
- HTML/CSS/JS 작업에서 Copilot의 출력 일관성을 높인다.
- 참가자가 어떤 채팅을 쳐도 출력 방향이 유지되도록 세팅한다.

#### 실습 산출물

- `.github/copilot-instructions.md`
- `.github/instructions/html.instructions.md`
- `.github/instructions/css.instructions.md`
- `.github/instructions/js.instructions.md`
- `01-instructions/instruction.md` 안의 적용 확인용 프롬프트

#### 권장 규칙 예시

- 모든 설명과 문서는 한국어
- 시맨틱 HTML 우선
- CSS 변수 기반 디자인 토큰 사용
- 외부 의존성 최소화
- 애니메이션은 과하지 않게, 접근성 고려
- 키보드 조작과 반응형 우선

#### 이 step의 역할

- 이후 step의 모든 채팅 결과가 같은 룰을 따르게 만든다.
- 참가자가 `output/` 웹페이지를 수정할 때 매번 스타일, 언어, 접근성 규칙을 다시 설명하지 않게 한다.
- 이 단계에서도 기능 구현보다 규칙 파일 세팅에 집중한다.

#### 이 step에서 사용할 자산

- 저장소 공통 instructions
- HTML/CSS/JS 경로별 instructions
- instructions 적용 확인 프롬프트

#### 핵심 메시지

"반복해서 설명할 팀 규칙은 Instructions로 고정한다."

---

### Step 02. Prompt Files

#### 학습 목표

- 반복 요청을 프롬프트 파일로 캡슐화한다.
- 발표자료 제작 작업을 명령형 워크플로우로 바꾼다.
- 참가자가 실습 도중 바로 재사용할 프롬프트 세트를 만든다.

#### 실습 산출물

- `.github/prompts/add-slide.prompt.md`
- `.github/prompts/polish-slide.prompt.md`
- `.github/prompts/add-speaker-notes.prompt.md`
- `.github/prompts/rewrite-for-audience.prompt.md`
- `02-prompt-files/instruction.md` 안의 호출 예시

#### 프롬프트 역할 예시

- `/add-slide`: 새 슬라이드 1장을 기존 구조에 맞게 추가
- `/polish-slide`: 텍스트 밀도를 줄이고 시각 요소를 보강
- `/add-speaker-notes`: 발표자 노트 초안 생성
- `/rewrite-for-audience`: 개발자용 설명을 비개발자용으로 재작성

#### 이 step의 역할

- 참가자가 자주 쓰는 요청을 slash prompt 자산으로 만든다.
- 워크샵 중 `output/` 웹페이지를 수정할 때 반복되는 요청 문구를 통일한다.
- 이 단계 역시 기능 구현보다 프롬프트 인터페이스를 설계하는 단계다.

#### 이 step에서 사용할 자산

- 슬라이드 추가 프롬프트
- 슬라이드 다듬기 프롬프트
- 발표자 노트 생성 프롬프트
- 청중별 재작성 프롬프트

#### 핵심 메시지

"반복 프롬프트는 채팅 기록에 묻히게 두지 말고 자산화한다."

---

### Step 03. Skills

#### 학습 목표

- 특정 작업을 잘하는 재사용 가능한 skill을 만든다.
- 로컬 개발, 점검, 배포 준비 같은 실무 루틴을 구조화한다.
- 참가자가 실습 후 바로 배포하거나 환경을 점검할 수 있게 한다.

#### 실습 산출물

- `.github/skills/fast-deploy/SKILL.md`
- `.github/skills/local-setup/SKILL.md`
- `.github/skills/slide-qa/SKILL.md`
- `03-skills/instruction.md` 안의 skill 실행 프롬프트

#### 설계할 skills

`local-setup`

- 목표: 워크샵 시작 전 로컬 환경과 GitHub CLI 상태를 점검한다.
- 범위: `git`, `gh`, 브라우저 확인, 정적 파일 확인, 로컬 미리보기 방법 안내

`slide-qa`

- 목표: 발표자료 구조, 접근성, 링크, 파일 경로, 콘텐츠 밀도를 점검한다.
- 범위: 슬라이드 수, 제목 구조, 상대 경로, 키보드 흐름, 이미지 누락 여부 점검

`fast-deploy`

- 목표: 의존성 없이 정적 사이트를 빠르게 배포하는 방법 안내
- 범위: GitHub Pages 기준 배포 절차, 필수 파일 점검, 상대 경로 검증, 404/경로 문제 방지
- CLI 중심으로 설명

#### 이 step의 역할

- 배포나 점검처럼 실무성 높은 반복 작업을 skill로 정리한다.
- 참가자가 `output/` 웹페이지를 만든 뒤 "어떻게 배포하지?"에서 막히지 않게 한다.
- `Step 03`까지는 여전히 코드를 직접 쓰지 않고, 이후 단계를 위한 능력 팩을 준비하는 단계다.

#### 이 step에서 사용할 자산

- `local-setup` skill
- `slide-qa` skill
- `fast-deploy` skill

#### 핵심 메시지

"Skill은 기능 소개가 아니라, 특정 작업을 잘 수행하게 만드는 실행 패키지다."

---

### Step 04. Custom Agent

#### 학습 목표

- 발표자료 제작에 특화된 Agent를 직접 만든다.
- 단순 코드 생성이 아니라 역할 기반 협업 방식을 체험한다.
- 참가자가 목적별로 다른 Copilot 페르소나를 써보게 한다.

#### 실습 산출물

- `.github/agents/deck-designer.agent.md`
- `.github/agents/story-editor.agent.md`
- 선택: `.github/agents/accessibility-reviewer.agent.md`
- `04-custom-agent/instruction.md` 안의 agent 호출 프롬프트

#### Agent 역할 예시

`deck-designer`

- 시각 리듬, 타이포, 색상, 섹션 전환 개선
- 슬라이드당 메시지 밀도 조정
- 발표 흐름상 약한 장면 보강

`story-editor`

- 슬라이드 순서 점검
- 중복 메시지 제거
- 발표 시간 기준 분량 조정

#### 이 step의 역할

- 같은 작업도 역할에 따라 결과가 달라지는 점을 체험하게 한다.
- `output/` 발표자료를 다듬는 팀원 역할을 Agent로 모델링한다.

#### 이 step에서 사용할 자산

- `deck-designer` agent
- `story-editor` agent
- 선택: `accessibility-reviewer` agent

#### 핵심 메시지

"좋은 Agent는 모델에게 역할, 기준, 금지사항을 함께 준다."

---

### Step 05. CLI

#### 학습 목표

- 터미널과 GitHub CLI를 중심으로 발표자료 운영 흐름을 자동화한다.
- 로컬 확인, 저장소 설정, 이슈 생성, 배포 점검을 명령 기반으로 다룬다.
- 채팅 중심 실습을 실제 운영 흐름과 연결한다.

#### 실습 산출물

- `scripts/serve.sh`
- `scripts/check.sh`
- `scripts/publish.sh`
- `README.MD` 내 CLI 사용 섹션
- 필요 시 `.github/ISSUE_TEMPLATE/feature-request.md`
- `05-cli/instruction.md` 안의 CLI 명령 프롬프트와 예시 커맨드

#### CLI 실습 후보

- `python -m http.server` 또는 간단한 정적 서버로 로컬 확인
- `gh repo create`, `gh issue create`, `gh pr view` 흐름 소개
- GitHub Pages 설정 보조 명령 정리
- 배포 전 체크 자동화

#### 이 step의 역할

- 실습으로 만든 자산을 로컬 실행, 검증, 배포 루틴에 연결한다.
- GitHub CLI를 써서 `output/` 결과물을 관리하고 다음 step의 Coding Agent 흐름으로 넘어갈 발판을 만든다.

#### 이 step에서 사용할 자산

- `serve.sh`
- `check.sh`
- `publish.sh`
- `gh` 명령 예시

#### 핵심 메시지

"Copilot을 잘 쓰는 팀은 채팅만 잘하는 팀이 아니라, CLI까지 포함해 흐름을 짠 팀이다."

---

### Step 06. Coding Agent

#### 학습 목표

- IDE의 Agent Mode와 GitHub의 Coding Agent가 다르다는 점을 이해한다.
- GitHub Issue를 만들어 Copilot coding agent에 작업을 위임하고, Draft PR로 결과를 받는 흐름을 체험한다.

#### 공식 문서 기반 반영 사항

- Coding Agent는 GitHub 상에서 백그라운드로 동작한다.
- 보통 Issue 할당 또는 PR 코멘트 기반으로 작업을 맡긴다.
- 작업 결과는 브랜치 + Draft PR 형태로 돌아온다.
- 사람은 리뷰어로서 PR 코멘트로 재지시한다.
- IDE의 Agent Mode와 달리, GitHub Actions 기반의 별도 환경에서 실행된다.

#### 실습 산출물

- Coding Agent용 이슈 템플릿
- 샘플 이슈 1~2개
- `06-coding-agent/instruction.md`에 실제 위임 절차 문서화
- 실제 이슈 본문에 바로 붙여넣을 프롬프트 템플릿

#### 이 step의 역할

- 참가자가 로컬 채팅이 아닌 GitHub 상의 비동기 위임 흐름을 이해하게 한다.
- `output/` 웹페이지 개선 작업도 "이슈 작성 방식"에 따라 결과 품질이 크게 달라진다는 점을 보여준다.

#### 이 step에서 사용할 자산

- Coding Agent용 이슈 템플릿
- PR 코멘트 재지시 프롬프트
- 샘플 이슈 본문 템플릿

#### 이 step에서 맡기면 좋은 작업 예시

아래 중 하나를 Coding Agent에 맡기는 방식이 적합하다.

1. 인쇄용 프린트 모드 추가
2. 발표 종료 후 Q&A appendix 슬라이드 추가
3. 접근성 개선 작업 수행
4. 모바일 뷰 최적화

가장 현실적인 기본안은 `인쇄용 프린트 모드 추가` 또는 `Q&A appendix 추가`다. 범위가 명확하고 PR 리뷰 포인트도 분명하다.

#### 운영 전제조건

- 저장소가 GitHub에 올라가 있어야 함
- Copilot coding agent 사용 가능한 플랜/권한 필요
- 리포지토리에서 coding agent가 활성화되어 있어야 함
- 브랜치 보호 규칙과 워크플로우 실행 정책을 사전에 점검해야 함

#### 핵심 메시지

"Coding Agent는 로컬 페어 프로그래밍이 아니라, 이슈를 받아 PR을 올리는 비동기 팀원이다."

---

## 6. 단계별 누적 세팅 제안

워크샵이 끝났을 때는 두 가지가 함께 남아야 한다. 하나는 참가자가 완성해온 `output/` 웹페이지이고, 다른 하나는 그 결과물을 만들 수 있게 해준 Copilot 세팅 자산이다. 그래서 아래처럼 누적시키는 구성이 좋다.

1. Step 00: 스펙 문서와 구현 계획 템플릿
2. Step 01: 저장소 공통 규칙과 경로별 instructions
3. Step 02: 재사용 가능한 prompt files
4. Step 03: `local-setup`, `slide-qa`, `fast-deploy` skills
5. Step 04: 발표자료 제작 특화 custom agents
6. Step 05: 로컬 실행, 점검, 배포, 이슈 생성용 CLI 스크립트
7. Step 06: Coding Agent 위임용 이슈 템플릿과 PR 리뷰 흐름

`Step 00`부터 `Step 03`까지는 위 자산을 세팅하는 데 집중하고, `Step 04`부터 `Step 06`까지는 그 자산을 활용해 `output/` 폴더의 웹페이지를 실제로 개선한다.

---

## 7. 각 step 문서 템플릿 제안

모든 `instruction.md`는 아래 흐름을 통일한다.

1. 이 step에서 배우는 기능
2. 왜 지금 이 기능이 필요한가
3. 사전 준비물
4. Copilot Chat에 넣을 프롬프트
5. 기대 결과
6. 실패 시 다시 넣을 보정 프롬프트
7. 검증 체크리스트
8. 다음 step로 넘길 산출물

즉, 각 `instruction.md`는 설명 문서이면서 동시에 참가자가 Copilot Chat에 복붙해 `output/` 웹페이지를 실제로 수정하게 만드는 실행 스크립트여야 한다.

추가로 `Step 00`부터 `Step 03`의 `instruction.md`는 코드 구현 프롬프트가 아니라, 세팅 파일과 Copilot 자산을 생성하게 만드는 프롬프트여야 한다.

---

## 8. 우선 구현 순서

워크샵 자체를 만드는 작업은 아래 순서가 적절하다.

1. 루트 워크샵 골격 생성
2. `output/`, `.specs/`, `context/`, `.github/`, `scripts/` 기본 구조 생성
3. `00-spec-driven-development/instruction.md` 작성
4. `01-instructions`와 `.github/copilot-instructions.md` 작성
5. `02-prompt-files`와 `.github/prompts/` 작성
6. `03-skills`와 `local-setup`, `slide-qa`, `fast-deploy` skills 작성
7. `04-custom-agent`와 발표자료 특화 agent 작성
8. `05-cli` 문서와 스크립트 작성
9. `06-coding-agent` 문서, 이슈 템플릿, 샘플 이슈 작성
10. README를 워크샵 인덱스로 정리

---

## 9. 결정 사항

현재 기준으로 아래 방향을 기본안으로 확정하는 것이 좋다.

- 발표자료는 바닐라 HTML/CSS/JS 기반으로 진행
- step별 `starter/complete`는 만들지 않음
- 참가자는 `output/` 폴더의 실제 웹페이지를 step별 프롬프트로 직접 구현
- 나는 결과물을 대신 만들지 않고, 그 과정을 위한 세팅과 Copilot 자산을 준비
- `Step 00`부터 `Step 03`까지는 코딩 한 줄 없이 환경과 Copilot 자산만 세팅
- 실질적인 결과물 완성은 `Step 04`부터 시작
- Skills 단계의 대표 skill은 `fast-deploy`
- Skills 단계에서는 `local-setup`, `slide-qa`, `fast-deploy`를 세트로 설계
- 배포 대상은 GitHub Pages
- CLI 단계는 `gh` 중심으로 구성
- Coding Agent 단계는 GitHub Issue 할당 → Draft PR 리뷰 흐름으로 설계
- 각 step 폴더의 `instruction.md`는 Copilot Chat 입력 프롬프트 중심으로 작성

---

## 10. 다음 작업 기준

이 플랜 다음 단계에서는 아래 순서로 바로 세팅 작업에 들어가면 된다.

1. 워크샵 루트 구조 생성
2. `instruction.md` 템플릿 확정
3. `.github/copilot-instructions.md` 초안 작성
4. `fast-deploy` skill 초안 작성
5. Coding Agent용 이슈 템플릿 작성

즉, 다음 작업부터는 결과물 구현이 아니라 워크샵을 바로 진행할 수 있는 운영 파일들을 실제로 만들면 된다.