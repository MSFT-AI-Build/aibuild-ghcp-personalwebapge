# 데이터 모델: 개발자 포트폴리오 웹사이트 자동 생성기

**Branch**: `002-portfolio-site-gen`
**Date**: 2026-03-18

---

## 엔티티 정의

### 1. ResumeData (이력서 전체 데이터)

이력서 마크다운 파일을 파싱한 최상위 구조.

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| name | string | 개발자 이름 (첫 행 첫 셀) | ✅ |
| phone | string | 전화번호 | ❌ |
| email | string | 이메일 주소 | ❌ |
| education | Education[] | 학력 항목 목록 | ❌ |
| work | WorkExperience[] | 경력 항목 목록 | ❌ |
| activities | Activity[] | 과외활동 항목 목록 | ❌ |
| skills | Skill[] | 스킬/정보 항목 목록 | ❌ |

**추출 규칙**:
- `name`: 마크다운 첫 번째 테이블 행의 첫 번째 비어있지 않은 셀
- `phone`/`email`: 두 번째 행에서 `+82` 패턴(전화), `@` 패턴(이메일)으로 추출
- 나머지 필드: 섹션 키워드로 구분하여 추출

---

### 2. Education (학력)

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| degree | string | 학위명 (예: "BA Convergence Software") | ✅ |
| school | string | 학교명 (예: "Sungkyunkwan University") | ✅ |
| location | string | 소재지 (예: "Suwon, Korea") | ❌ |
| period | string | 기간 (예: "Mar 2020 - Present") | ❌ |
| gpa | string | 학점 정보 (예: "Cumulative GPA 3.98 / 4.5") | ❌ |

**추출 규칙**:
- `**학위명 - 학교명**` 패턴에서 `-`를 기준으로 분리
- `period`: 같은 행의 3번째 셀에서 `**기간**` 추출
- `gpa`: 다음 행의 첫 셀에서 "GPA" 포함 텍스트 추출

---

### 3. WorkExperience (경력)

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| company | string | 회사명 (예: "Microsoft") | ✅ |
| role | string | 직책 (예: "Solution Engineer Intern") | ❌ |
| location | string | 소재지 (예: "Seoul, Korea") | ❌ |
| period | string | 기간 (예: "Jan 2025 - Apr 2025") | ❌ |
| description | string | 업무 상세 설명 | ❌ |

**추출 규칙**:
- `**회사명**`: 볼드로 감싼 첫 번째 텍스트
- `*직책*`: 이탤릭으로 감싼 텍스트 (세미콜론 제거)
- `location`/`period`: 같은 행 3번째 셀에서 볼드/이탤릭 분리
- `description`: 다음 행의 첫 셀 전체 텍스트 (마크다운 서식 정리)

---

### 4. Activity (과외활동)

WorkExperience와 동일한 구조.

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| title | string | 활동명 | ✅ |
| role | string | 역할 | ❌ |
| location | string | 장소 | ❌ |
| period | string | 기간 | ❌ |
| description | string | 활동 상세 설명 | ❌ |

---

### 5. Skill (기술/정보)

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| category | string | 카테고리 (예: "Language / Skills", "Military Service") | ✅ |
| value | string | 내용 (예: "Korean (Native), English (Fluent)") | ✅ |

**추출 규칙**:
- 1열: 볼드 텍스트 → `category`
- 2열: 텍스트 → `value`

---

### 6. PhotoCollection (사진 컬렉션)

| 필드 | 타입 | 설명 | 필수 |
|------|------|------|------|
| profilePhoto | string | 대표 프로필 사진 파일 경로 (첫 번째 이미지) | ❌ |
| galleryPhotos | string[] | 갤러리에 표시할 전체 이미지 경로 목록 | ❌ |

**추출 규칙**:
- `data/photos/` 디렉토리 스캔
- 확장자 필터: `.jpg`, `.jpeg`, `.png`, `.gif`, `.webp` (대소문자 무관)
- 파일명 기준 정렬
- 첫 번째 파일 = `profilePhoto`, 전체 = `galleryPhotos`

---

### 7. PortfolioPage (최종 출력)

| 필드 | 타입 | 설명 |
|------|------|------|
| htmlContent | string | 완성된 HTML 문자열 (CSS + JS 인라인 포함) |
| outputPath | string | 출력 경로: `output/index.html` |
| photosDir | string | 이미지 복사 경로: `output/photos/` |

---

## 엔티티 관계

```
ResumeData (1)
├── Education (0..N)
├── WorkExperience (0..N)
├── Activity (0..N)
└── Skill (0..N)

PhotoCollection (1)
├── profilePhoto (0..1)
└── galleryPhotos (0..N)

PortfolioPage (1)
├── uses ResumeData
└── uses PhotoCollection
```

---

## 상태 전이: 파싱 상태 머신

이력서 마크다운을 줄 단위로 처리할 때의 상태 전이:

```
[시작] → header
header → (EDUCATION 감지) → education
education → (WORK EXPERIENCE 감지) → work
work → (EXTRA-CURRICULAR ACTIVITIES 감지) → activities
activities → (SKILLS AND INFORMATION 감지) → skills
skills → [종료]
```

각 상태에서 행 데이터를 해당 엔티티 배열에 추가한다.

---

## 유효성 검증 규칙

| 규칙 | 조건 | 결과 |
|------|------|------|
| 이름 필수 | `ResumeData.name`이 빈 문자열 | 기본값 "Portfolio" 사용 |
| 빈 섹션 처리 | education/work/skills 배열이 비어있음 | 해당 섹션을 HTML에서 생략 |
| 사진 없음 | `PhotoCollection.galleryPhotos`가 비어있음 | 갤러리 섹션 생략 |
| 이력서 없음 | `data/resume/`에 .md 파일 없음 | 기본 안내 메시지 표시 |
| 비이미지 파일 | 지원하지 않는 확장자 | 자동 필터링 (무시) |
