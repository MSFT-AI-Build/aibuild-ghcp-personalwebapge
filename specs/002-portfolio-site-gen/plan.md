# 구현 계획: 개발자 포트폴리오 웹사이트 자동 생성기

**Branch**: `002-portfolio-site-gen` | **Date**: 2026-03-18 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/002-portfolio-site-gen/spec.md`

## 요약

`data/resume/`의 마크다운 이력서와 `data/photos/`의 이미지 파일을 읽어,
개발자 소개·이력서 구조적 표시·사진 갤러리를 포함하는 미니멀 디자인의
단일 페이지 포트폴리오를 `output/index.html`로 생성한다.
HTML + CSS + Vanilla JavaScript만 사용하며, 서버·빌드·프레임워크 없이
로컬 파일 시스템(`file://`)에서 바로 동작해야 한다.

## 기술 컨텍스트

**언어/버전**: HTML5, CSS3, Vanilla JavaScript (ES2020+)
**주요 의존성**: 없음 (CDN 라이브러리 불필요 — 이력서가 테이블 기반 비표준 마크다운이므로 커스텀 파서가 범용 마크다운 라이브러리보다 적합)
**저장소**: 파일 시스템만 사용 (`data/` → `output/`)
**테스트**: 브라우저 수동 테스트 (output/index.html 더블클릭)
**대상 플랫폼**: 모던 웹 브라우저 (Chrome, Safari, Firefox), 로컬 file:// 프로토콜
**프로젝트 유형**: 정적 웹 페이지 생성 스크립트
**성능 목표**: 페이지 로드 3초 이내
**제약**: 서버 금지, 프레임워크 금지, 빌드 도구 금지, 패키지 매니저 금지
**규모/범위**: 단일 페이지, 파일 3개 이내 (HTML, CSS, JS)

## Constitution Check

*GATE: Phase 0 연구 진행 전 통과 필수. Phase 1 설계 완료 후 재검증.*

| 원칙 | 게이트 | 상태 |
|------|--------|------|
| I. 단순함이 최우선 | 파일 개수 최소화, 불필요한 추상화 금지, 단일 페이지 구조 | ✅ 통과 — 파일 3개 (index.html, style.css, script.js) |
| II. 비개발자 이해 | 변수명·파일명 직관적, 전문 용어 최소화 | ✅ 통과 — 파일명이 기능을 직접 설명 |
| III. 구조 직관성 | data/ → output/ 워크플로우, index.html 클릭으로 동작 | ✅ 통과 — Constitution의 입출력 경로 그대로 사용 |
| IV. 읽기 쉬움 | 위→아래 순서 읽기, 복잡한 제어 흐름 금지 | ✅ 통과 — 순차적 실행 흐름 |
| 기술 제약 | HTML/CSS/JS만 사용, 백엔드·프레임워크·빌드·패키지매니저 금지 | ✅ 통과 — CDN도 미사용 |

**결과**: 모든 게이트 통과. Phase 0 진행 허가.

## 프로젝트 구조

### 문서 (이 피처)

```text
specs/002-portfolio-site-gen/
├── plan.md              # 이 파일 (/speckit.plan 출력)
├── research.md          # Phase 0 출력
├── data-model.md        # Phase 1 출력
├── quickstart.md        # Phase 1 출력
└── tasks.md             # Phase 2 출력 (/speckit.tasks — 이 명령에서 생성하지 않음)
```

### 소스 코드 (저장소 루트)

```text
data/
├── photos/              # 입력: 개발자 사진 (JPEG, PNG 등)
└── resume/              # 입력: 이력서 마크다운 파일

output/
├── index.html           # 최종 포트폴리오 페이지 (인라인 CSS + JS 포함)
└── photos/              # data/photos에서 복사된 이미지

generate.js              # Node.js 생성 스크립트 (data/ 읽기 → output/ 생성)
```

**구조 결정**: output/ 폴더에 단일 `index.html` 파일을 생성한다.
CSS와 JS는 index.html 안에 인라인으로 포함하여 파일 개수를 최소화한다.
이미지는 `output/photos/`에 복사하여 상대 경로(`photos/파일명`)로 참조한다.
생성 스크립트(`generate.js`)는 Node.js로 실행하며, 이는 "빌드 도구"가 아닌
"일회성 파일 생성 스크립트"로서 Constitution의 빌드 과정 금지 원칙과
구분된다 (npm install 불필요, node generate.js 한 줄로 실행).

## 복잡성 추적

> Constitution Check 위반 없음. 이 섹션은 비어 있음.
