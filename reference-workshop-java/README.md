# 🤖 GitHub Copilot 점진적 체험 워크샵 — Java Edition

GitHub Copilot의 기능을 단계별로 익히는 핸즈온 워크샵입니다.
**Spring Boot**로 TODO API를 처음부터 만들어가며, 자동완성부터 Chat, Agent, Custom Agent까지 Copilot의 핵심 기능을 하나씩 체험합니다.

---

## 시작하기

[Step 00 — 환경 세팅](step-00-setup/)부터 따라가세요. 각 스텝의 README가 튜토리얼 가이드입니다.

> 이 저장소는 **튜토리얼 문서와 참고 코드**를 제공합니다.
> 특정 스텝만 실습하고 싶다면 해당 스텝의 `starter/` 코드를 복사해 시작할 수 있지만, 처음부터 순서대로 진행하는 것을 추천합니다.

---

## 기술 스택

| 항목 | 기술 |
|------|------|
| 언어 | Java 17+ |
| 프레임워크 | Spring Boot 3.x |
| ORM | Spring Data JPA |
| DB | H2 (로컬/인메모리) |
| 빌드 | Gradle (Kotlin DSL) |
| 테스트 | JUnit 5 + MockMvc |
| IDE | IntelliJ IDEA |

---

## 프로젝트 구조

각 스텝 폴더는 `README.md`(가이드) + `starter/`(시작 코드) + `complete/`(완성 코드)로 구성됩니다.
보너스 트랙(10~14)은 README 가이드만 제공하는 자율 실습입니다.

---

## 학습 로드맵

### 메인 트랙

| Step | 주제 | 무엇을 배우나요? |
|:----:|------|-----------------|
| [00](step-00-setup/) | 환경 세팅 | JDK 17+, IntelliJ IDEA, Copilot 플러그인 설치 및 동작 확인 |
| [01](step-01-inline/) | 코드 완성 | Tab 키로 Copilot 자동완성을 수락·거절하는 기본기 |
| [02](step-02-chat/) | Copilot Chat | Chat 패널에서 대화로 코드 생성·리팩터링하기 |
| [03](step-03-instructions/) | Instructions | 프로젝트 규칙 파일을 만들어 반복 설명 없이 일관된 응답 받기 |
| [04](step-04-prompt-files/) | Prompt Files | 반복 작업을 매크로처럼 한 번에 실행하는 프롬프트 파일 만들기 |
| [05](step-05-agent/) | Agent 모드 | 큰 작업(아키텍처 전환)을 Agent에게 통째로 맡기기 |
| [06](step-06-custom-agent/) | Custom Agent | 나만의 전문 Agent(코드 리뷰, DB 등) 직접 만들기 |
| [07](step-07-multi-agent/) | Multi-Agent | Agent 간 역할 분담 + 자동 연쇄 호출로 팀 협업 시뮬레이션 |
| [08](step-08-advanced/) | 고급 워크플로우 | Plan 기능 + Custom Agent 조합으로 Vibe Coding 실습 |
| [09](step-09-spec-driven/) | Spec-Driven Dev | 문서 기반 스펙 → 분석/계획 → 테스트 → 구현으로 전체 스킬 종합 실습 |

### 보너스 트랙

| Step | 주제 | 무엇을 배우나요? |
|:----:|------|-----------------|
| [10](step-10-bonus-readme/) | README 문서화 | Copilot으로 프로젝트 README + Mermaid 다이어그램 생성 |
| [11](step-11-bonus-a-docker/) | Docker | Copilot으로 Dockerfile, docker-compose 설정 생성 및 컨테이너화 |
| [12](step-12-bonus-b-react/) | React 프론트엔드 | Copilot과 함께 TODO 앱 UI 만들기 (Vite + TS + Tailwind) |
| [13](step-13-bonus-c-migration/) | 레거시 마이그레이션 | 클래식 ASP 레거시 코드를 Java(Spring Boot)로 전환하며 언어 마이그레이션 체험 |
| [14](step-14-bonus-e-speckit/) | Spec Kit | GitHub Spec Kit으로 Spec-Driven Development 워크플로우 자동화 |

---

## 참고

- 모든 주석·Javadoc·커밋 메시지는 **한국어**로 작성합니다.
- 각 스텝은 **해당 단계까지 배운 기능만** 사용하도록 설계되어 있습니다.
- 막히면 `complete/` 폴더의 참고 코드를 확인하세요.

## Python 버전

같은 커리큘럼의 Python(FastAPI + VS Code) 버전은 [python/](../python/README.md)에서 확인하세요.
