# 워크샵 요구사항

## 목적

GitHub Copilot의 핵심 기능을 단계적으로 체험하면서, 참가자가 직접 HTML 발표자료 웹페이지를 완성하도록 돕는 워크샵을 만든다.

## 워크샵 요구사항

### 진행 방식

- 참가자는 step을 순서대로 진행한다.
- 각 step은 이전 step의 결과를 재사용해야 한다.
- 각 step 폴더에는 `instruction.md`만 두고, 실제 작업은 루트 기준으로 진행한다.
- `starter/complete` 구조는 사용하지 않는다.

### 결과물 방식

- 실제 결과물은 `output/` 폴더에서 참가자가 직접 만든다.
- `Step 00`부터 `Step 03`까지는 결과물 구현보다 환경과 Copilot 자산 세팅에 집중한다.
- `Step 04`부터 `Step 06`까지는 앞 단계에서 만든 자산을 활용해 결과물을 완성한다.

### 필수 기능 범위

- Spec-Driven Development
- Repository Instructions
- Prompt Files
- Skills
- Custom Agent
- CLI
- Coding Agent

### 운영 요구사항

- GitHub Pages 배포를 염두에 둔다.
- 가능한 한 의존성을 최소화한다.
- GitHub CLI 기반 흐름을 지원한다.
- Coding Agent 단계는 GitHub Issue 기반 비동기 위임 흐름을 설명해야 한다.

## 비목표

- 이 저장소가 참가자용 최종 결과물을 대신 제공하지 않는다.
- 초반 단계에서 HTML/CSS/JS 기능 구현 자체를 목표로 하지 않는다.