# Step 04. Custom Agent

## 1. 목표

이제부터 `output/`에 실제 발표자료를 만들기 시작합니다. custom agent를 써서 역할별로 결과물을 다듬습니다.

## 2. 배워볼 것

- custom agent 고르기
- 역할에 맞게 요청하기
- 리뷰 agent와 편집 agent 나눠 쓰기

## 3. Step by Step로 산출물 만들기

### Step 1. 발표자료 기본 골격 만들기

`output/`에 최소 발표자료 구조를 만듭니다.

필요한 결과:
- `output/index.html`
- `output/assets/css/`
- `output/assets/js/`
- 표지
- 아젠다
- step overview

### Step 2. 흐름 다듬기

기본 골격이 생기면 `story-editor` 관점으로 순서와 문구를 다듬습니다.

### Step 3. 접근성 점검 받기

`accessibility-reviewer`에게 먼저 리뷰를 받고, 필요한 수정만 반영합니다.

## 4. Copilot에 바로 넣을 프롬프트

### 기본 골격 생성

```text
`output/` 폴더에 HTML 발표자료의 최소 골격을 만들어줘.

조건:
- static HTML/CSS/JS만 사용
- 표지, 아젠다, step overview 섹션 포함
- 상대 경로 유지
- assets/css, assets/js와 연결
- 설명은 한국어

가능하면 deck-designer 관점으로 구조를 단정하게 만들어줘.
```

### 흐름 다듬기

```text
현재 output/ 발표자료 구조를 읽고,
story-editor 관점에서 흐름이 더 자연스럽게 이어지도록 섹션 순서와 문구를 다듬어줘.

조건:
- 주제는 유지
- 중복 제거
- 발표 흐름 강화
```

### 접근성 리뷰

```text
output/ 발표자료를 accessibility-reviewer 관점에서 리뷰해줘.

다음 항목을 봐줘:
- 제목 구조
- 시맨틱 HTML
- 키보드 이동 준비 상태
- 링크와 버튼의 의미

수정은 하지 말고 이슈만 한국어로 정리해줘.
```

## 5. 체크리스트

- [ ] `output/index.html`이 생성되었는가
- [ ] CSS와 JS 연결이 준비되었는가
- [ ] 표지와 아젠다가 있는가
- [ ] 리뷰와 편집 역할을 나눠서 써봤는가

## 6. 다음 step로 가져갈 것

- 발표자료 기본 골격
- agent 리뷰 결과