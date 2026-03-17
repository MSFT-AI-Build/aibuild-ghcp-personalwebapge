# Tasks: 개발자 포트폴리오 웹사이트 자동 생성기

**Input**: `/specs/002-portfolio-site-gen/` 설계 문서
**Prerequisites**: plan.md (필수), spec.md (필수), research.md, data-model.md, quickstart.md

**테스트**: 스펙에서 별도로 자동 테스트를 요구하지 않음. 브라우저 수동 테스트로 검증.

**구성**: 태스크는 유저 스토리 기준으로 그룹화하여 각 스토리를 독립적으로 구현·검증 가능하도록 한다.

## 형식: `[ID] [P?] [Story] 설명`

- **[P]**: 병렬 실행 가능 (다른 파일, 의존성 없음)
- **[Story]**: 해당 유저 스토리 (예: US1, US2, US3)
- 모든 파일 경로는 저장소 루트 기준 절대 경로 표기

## 경로 규칙

- 생성 스크립트: `generate.js` (저장소 루트)
- 입력 데이터: `data/photos/`, `data/resume/`
- 출력 결과: `output/index.html`, `output/photos/`

---

## Phase 1: 셋업 (공통 인프라)

**목적**: 프로젝트 골격 생성 및 생성 스크립트 기본 구조 수립

- [ ] T001 `generate.js` 파일 생성 — Node.js 생성 스크립트 엔트리포인트. `fs`, `path` 내장 모듈만 사용. `output/` 디렉토리 생성(기존 시 삭제 후 재생성), `output/photos/` 디렉토리 생성 로직 포함.
- [ ] T002 [P] `data/photos/` → `output/photos/` 이미지 복사 함수 구현 — 확장자 필터링(jpg, jpeg, png, gif, webp, 대소문자 무관), 파일명 기준 정렬, data-model.md의 PhotoCollection 엔티티 구조대로 `{ profilePhoto, galleryPhotos }` 반환.

**체크포인트**: `node generate.js` 실행 시 `output/` 디렉토리가 생성되고 이미지가 복사됨

---

## Phase 2: 기반 (모든 유저 스토리의 전제 조건)

**목적**: 이력서 마크다운 파싱 엔진 — 모든 콘텐츠 유저 스토리(US1, US2)의 필수 전제

**⚠️ 중요**: 이 Phase가 완료되어야 유저 스토리 구현이 가능함

- [ ] T003 이력서 마크다운 파서 구현 — `generate.js` 내에 `parseResume(mdContent)` 함수 작성. data-model.md의 상태 머신(header → education → work → activities → skills) 흐름대로 줄 단위 파싱. 반환값: `ResumeData` 객체 `{ name, phone, email, education[], work[], activities[], skills[] }`.
- [ ] T004 테이블 행 셀 추출 유틸 함수 — `|`로 분리, 앞뒤 공백 제거, 빈 셀 처리. `parseTableRow(line)` → `string[]` 반환.
- [ ] T005 마크다운 서식 정리 함수 — 볼드(`**...**`), 이탤릭(`*...*`), 이스케이프(`\-`, `\+`) 제거/변환 정규식. `cleanMarkdown(text)` → 깨끗한 문자열 반환.
- [ ] T006 섹션별 파싱 규칙 구현:
  - **header 파싱**: 첫 행 첫 셀 → `name`, 둘째 행에서 `+82` 패턴 → `phone`, `@` 패턴 → `email`
  - **education 파싱**: `**학위 - 학교**` 패턴 분리 → `{ degree, school, location, period, gpa }`
  - **work 파싱**: `**회사명** *직책;*` + `**장소** *기간*` 패턴 → `{ company, role, location, period, description }`
  - **activities 파싱**: work과 동일 구조 → `{ title, role, location, period, description }`
  - **skills 파싱**: 1열 볼드 → `category`, 2열 → `value`
- [ ] T007 빈 데이터 처리 — `data/resume/`에 .md 파일이 없을 때 기본 `ResumeData` 반환 (name: "Portfolio", 나머지 빈 배열). data-model.md 유효성 검증 규칙 준수.

**체크포인트**: `parseResume()` 함수가 실제 `Hangwoo_Intern_Resume.docx.md`를 파싱하여 구조화된 ResumeData 객체를 반환함

---

## Phase 3: 유저 스토리 1 — 개발자 첫인상 전달 (Priority: P1) 🎯 MVP

**목표**: 페이지 최상단에 개발자 이름, 한 줄 소개(직책/학교 기반), 대표 프로필 사진 표시

**독립 테스트**: `output/index.html`을 브라우저에서 열었을 때, 스크롤 없이 이름·소개·프로필 사진이 보이는지 확인

### 구현

- [ ] T008 [US1] HTML 생성 기본 골격 — `generate.js`에 `generateHTML(resumeData, photoCollection)` 함수 작성. `<!DOCTYPE html>` 부터 `</html>` 까지의 전체 HTML 문자열을 반환. `<style>` 태그(인라인 CSS)와 `<script>` 태그(인라인 JS, 필요 시) 포함 구조.
- [ ] T009 [US1] 히어로 섹션 HTML 생성 — 개발자 이름(`ResumeData.name`), 한 줄 소개(첫 번째 work 항목의 `role` + `company` 조합), 대표 프로필 사진(`PhotoCollection.profilePhoto` → `<img src="photos/파일명">`) 포함. 프로필 사진 없을 시 이미지 영역 생략.
- [ ] T010 [US1] 히어로 섹션 CSS — 중앙 정렬, 프로필 사진 원형 크롭(`border-radius: 50%`), 이름 큰 글씨(2rem 이상), 한 줄 소개 서브텍스트 스타일. 미니멀 모던 디자인. 스크롤 없이 화면에 보이도록 적절한 여백.
- [ ] T011 [US1] `output/index.html` 파일 쓰기 — `generateHTML()` 결과를 `output/index.html`에 `fs.writeFileSync`로 저장. 메인 흐름: 이력서 읽기 → 파싱 → 사진 수집 → HTML 생성 → 파일 쓰기.

**체크포인트**: `node generate.js` 실행 후 `output/index.html`을 열면 이름, 소개, 프로필 사진이 화면 최상단에 보임

---

## Phase 4: 유저 스토리 2 — 이력서 구조적 표시 (Priority: P2)

**목표**: 학력, 경력, 과외활동, 기술을 포트폴리오 형태의 별도 섹션으로 재구성하여 표시

**독립 테스트**: 이력서 섹션이 마크다운 원본이 아닌 포트폴리오 UI로 재구성되어 표시되는지 확인

### 구현

- [ ] T012 [US2] 경력(Work Experience) 섹션 HTML 생성 — `ResumeData.work[]` 배열 순회. 각 항목: 회사명(큰 글씨), 직책(서브텍스트), 기간(우측 정렬 또는 태그), 상세 설명(본문). 타임라인 또는 카드 스타일 레이아웃. 빈 배열이면 섹션 생략.
- [ ] T013 [US2] 학력(Education) 섹션 HTML 생성 — `ResumeData.education[]` 배열 순회. 각 항목: 학위명, 학교명, 기간, GPA(선택적 표시). 카드 또는 리스트 스타일. 빈 배열이면 섹션 생략.
- [ ] T014 [US2] 과외활동(Activities) 섹션 HTML 생성 — `ResumeData.activities[]` 배열 순회. 경력 섹션과 유사한 카드 스타일. 빈 배열이면 섹션 생략.
- [ ] T015 [US2] 기술(Skills) 섹션 HTML 생성 — `ResumeData.skills[]` 배열 순회. 카테고리: 값 형태의 정돈된 레이아웃 (태그 스타일 또는 그리드). 빈 배열이면 섹션 생략.
- [ ] T016 [US2] 이력서 섹션 CSS — 각 섹션의 시각적 구분 (섹션 제목 스타일, 구분선 또는 여백), 카드/타임라인 스타일, 회사명·직책·기간의 타이포그래피 구분. 전체 일관성 유지.
- [ ] T017 [US2] `generateHTML()`에 이력서 섹션 통합 — 히어로 섹션 아래에 경력 → 학력 → 과외활동 → 기술 순서로 배치. 각 섹션은 데이터가 있을 때만 표시.

**체크포인트**: 페이지에서 경력, 학력, 활동, 기술이 포트폴리오 스타일 카드/타임라인으로 표시되며, 마크다운 원본이 그대로 노출되지 않음

---

## Phase 5: 유저 스토리 3 — 사진 갤러리 (Priority: P3)

**목표**: `data/photos/` 이미지를 2~3열 그리드 갤러리로 표시

**독립 테스트**: 갤러리 섹션에 이미지가 그리드로 표시되고, hover 효과가 동작하는지 확인

### 구현

- [ ] T018 [US3] 갤러리 섹션 HTML 생성 — `PhotoCollection.galleryPhotos[]` 배열 순회. 각 이미지를 `<img src="photos/파일명">` 태그로 생성. 카드 컨테이너로 감싸기. `galleryPhotos`가 비어있으면 섹션 생략 (FR-013).
- [ ] T019 [US3] 갤러리 CSS — CSS Grid 레이아웃 (`grid-template-columns: repeat(auto-fill, minmax(250px, 1fr))`로 2~3열 반응형), 이미지 카드 `border-radius: 12px`, `object-fit: cover`, hover 시 `transform: scale(1.03)` + `box-shadow` 효과. transition 부드럽게 (0.3s ease).
- [ ] T020 [US3] `generateHTML()`에 갤러리 섹션 통합 — 이력서 섹션 아래에 갤러리 배치.

**체크포인트**: 페이지에 사진들이 깔끔한 그리드 갤러리로 표시되며, hover 시 확대/그림자 효과가 동작

---

## Phase 6: 유저 스토리 4 — 직관적 UI와 세련된 디자인 (Priority: P4)

**목표**: 전체 페이지의 미니멀·모던 디자인 완성

**독립 테스트**: 중앙 정렬, 여백, 타이포그래피, 색상 체계가 스펙대로 적용되어 있는지 시각적 점검

### 구현

- [ ] T021 [US4] 전역 CSS 기반 스타일 — `body`: `font-family: system-ui, -apple-system, sans-serif`, `line-height: 1.6`, `color: #222`, `background: #f9f9f9`. `.container`: `max-width: 900px`, `margin: 0 auto`, `padding: 0 24px`. 섹션 간 `padding: 48px 0`. `box-sizing: border-box` 리셋.
- [ ] T022 [US4] 타이포그래피 세부 조정 — `h1`: 2.5rem, `h2`: 1.75rem (섹션 제목), `h3`: 1.25rem (항목 제목), `p`/`li`: 1rem. 포인트 컬러 1가지 (예: `#2563eb` — 차분한 파란색)를 섹션 제목, 링크, 주요 키워드에 적용. 제목과 본문 간 명확한 시각적 계층.
- [ ] T023 [US4] 반응형 기본 대응 — 모바일 화면(`max-width: 600px`)에서 갤러리 1열 전환, 여백 축소(`padding: 0 16px`), 폰트 크기 미세 조정. 미디어 쿼리 최소한으로.
- [ ] T024 [US4] 부드러운 스크롤 — `html { scroll-behavior: smooth; }` 적용. 네비게이션 요소를 추가한다면 앵커 링크 스크롤에 활용.

**체크포인트**: 기본 HTML 느낌 없이, 미니멀하고 모던한 포트폴리오로 인식됨. 타이포그래피·색상·여백이 일관적.

---

## Phase 7: 유저 스토리 5 — 로컬 파일 직접 실행 (Priority: P5)

**목표**: `output/index.html` 더블클릭만으로 완전한 포트폴리오 확인 가능

**독립 테스트**: Finder에서 `output/index.html`을 더블클릭하여 모든 콘텐츠가 정상 표시되는지 확인

### 구현

- [ ] T025 [US5] file:// 호환성 최종 점검 — 모든 리소스 경로가 상대 경로인지 확인 (`photos/파일명`, 절대 경로 `/` 시작 금지), 외부 CDN 의존성 없음 확인, `<script type="module">` 미사용 확인. `output/index.html`이 독립적으로 동작하는지 검증.
- [ ] T026 [US5] generate.js 완성 및 실행 흐름 정리 — 메인 함수에서: ① output/ 정리 → ② 이력서 읽기·파싱 → ③ 사진 수집·복사 → ④ HTML 생성 → ⑤ 파일 쓰기 → ⑥ 완료 메시지 콘솔 출력. 에러 처리: 누락 파일 시 경고만 출력하고 계속 진행.
- [ ] T027 [US5] quickstart.md 검증 — quickstart.md의 3단계(데이터 준비 → 생성 → 확인) 가이드대로 실행하여 end-to-end 동작 확인.

**체크포인트**: `node generate.js` → `output/index.html` 더블클릭 → 브라우저에서 완전한 포트폴리오 표시. 서버 없이 동작.

---

## Phase 8: 마무리 및 품질 개선

**목적**: 전체 스토리 통합 후 횡단 관심사 점검

- [ ] T028 [P] 엣지 케이스 처리 최종 점검 — data/photos/ 비어있을 때 갤러리 숨김, data/resume/ 비어있을 때 안내 메시지 표시, 비이미지 파일 필터링, 파싱 실패 시 원본 정리 표시. 스펙 Edge Cases 섹션 전항목 대조.
- [ ] T029 [P] 코드 가독성 정리 — generate.js 전체 코드가 위→아래로 읽히는지 확인. Constitution 원칙 IV(읽기 쉬움) 준수. 변수명·함수명 직관성 점검. 불필요한 추상화 제거.
- [ ] T030 quickstart.md 실행 검증 — 실제로 quickstart.md 가이드를 따라 처음부터 끝까지 실행하여 동작 확인.

---

## 의존성 및 실행 순서

### Phase 의존성

- **셋업 (Phase 1)**: 의존성 없음 — 즉시 시작 가능
- **기반 (Phase 2)**: Phase 1 완료 필요 — **모든 유저 스토리를 블로킹**
- **유저 스토리 (Phase 3~7)**: 모두 Phase 2 완료 후 진행
  - US1(Phase 3)을 먼저 완료해야 US2~5의 HTML 통합이 가능
  - US2(Phase 4)와 US3(Phase 5)는 US1 완료 후 병렬 진행 가능
  - US4(Phase 6)는 모든 콘텐츠 스토리(US1~US3) 완료 후 최종 조정
  - US5(Phase 7)는 모든 스토리 완료 후 최종 검증
- **마무리 (Phase 8)**: 모든 유저 스토리 완료 후 진행

### 유저 스토리 간 의존성

- **US1 (P1)**: Phase 2 완료 후 시작 가능 — 다른 스토리에 의존 없음 → **MVP**
- **US2 (P2)**: US1 완료 후 시작 (HTML 골격 필요) — US3와 병렬 가능
- **US3 (P3)**: US1 완료 후 시작 (HTML 골격 필요) — US2와 병렬 가능
- **US4 (P4)**: US1~US3 콘텐츠 있을 때 최종 디자인 조정 의미 있음
- **US5 (P5)**: 모든 스토리의 결과를 검증하므로 가장 마지막

### 각 유저 스토리 내부 순서

- HTML 생성 → CSS 스타일 → generateHTML() 통합 (순차)
- 모델(파서) → 뷰(HTML 생성) → 스타일(CSS) 순서

### 병렬 가능 구간

- Phase 1: T001과 T002 병렬 불가 (T002가 output/photos/ 디렉토리 필요)
- Phase 2: T004, T005 병렬 가능 (독립 유틸 함수)
- Phase 4: T012, T013, T014, T015 병렬 가능 (독립 섹션 생성)
- Phase 5: T018, T019 병렬 가능
- Phase 6: T021, T022, T023, T024 병렬 가능
- Phase 8: T028, T029 병렬 가능
