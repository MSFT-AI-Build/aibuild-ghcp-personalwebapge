# GitHub Copilot HTML 발표자료 워크샵

GitHub Copilot의 핵심 기능을 순서대로 실습하면서, 참가자가 직접 HTML 발표자료 웹페이지를 완성해가는 워크샵입니다.

이 저장소는 완성 결과물을 대신 제공하는 저장소가 아니라, 참가자가 결과물을 만들어갈 수 있도록 환경과 Copilot 자산을 준비해두는 워크샵 저장소입니다.

## 핵심 원칙

- 실제 결과물은 `output/` 폴더에서 참가자가 직접 만든다.
- `Step 00`부터 `Step 03`까지는 코딩 단계가 아니라 세팅 단계다.
- `Step 04`부터 `Step 06`까지는 앞에서 만든 자산을 활용해 결과물을 완성하는 단계다.
- 각 step 폴더의 `instruction.md`는 설명 문서이면서 동시에 Copilot Chat에 직접 입력할 프롬프트 모음이다.
- `starter/complete` 복제 구조는 사용하지 않는다.

## 폴더 구조

```text
.
├── plan.md
├── output/
├── .specs/
├── context/
├── .github/
├── scripts/
├── 00-spec-driven-development/
├── 01-instructions/
├── 02-prompt-files/
├── 03-skills/
├── 04-custom-agent/
├── 05-cli/
└── 06-coding-agent/
```

## 학습 흐름

1. `00-spec-driven-development`
스펙과 구현 계획을 만든다.
2. `01-instructions`
Copilot의 기본 규칙을 고정한다.
3. `02-prompt-files`
반복 프롬프트를 slash command 자산으로 만든다.
4. `03-skills`
환경 점검, QA, 배포용 skills를 만든다.
5. `04-custom-agent`
결과물 개선용 custom agent를 만든다.
6. `05-cli`
로컬 실행, 점검, 배포 흐름을 CLI로 연결한다.
7. `06-coding-agent`
GitHub Issue 기반 비동기 위임 흐름을 체험한다.

## 시작 방법

1. [plan.md](./plan.md)를 읽고 전체 워크샵 구조를 확인한다.
2. `00-spec-driven-development/instruction.md`부터 순서대로 진행한다.
3. 각 step의 프롬프트를 Copilot Chat에 직접 입력한다.
4. `output/` 폴더는 `Step 04`부터 실제로 수정하기 시작한다.

## 참고

- GitHub Pages를 기본 배포 경로로 가정한다.
- GitHub CLI `gh`를 사용할 수 있으면 CLI와 Coding Agent 단계 실습이 더 자연스럽다.
- Coding Agent 단계는 GitHub 상의 권한과 플랜이 필요하다.