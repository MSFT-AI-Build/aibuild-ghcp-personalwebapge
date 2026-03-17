# Step 01. Instructions

## 1. 목표

Copilot이 이 워크샵에서 같은 규칙으로 답하게 만듭니다.

## 2. 배워볼 것

- 저장소 전체 규칙 만들기
- HTML, CSS, JS별 규칙 나누기
- `output/`에만 규칙이 붙게 설정하기

## 3. Step by Step로 산출물 만들기

### Step 1. 저장소 전체 규칙 정리

```text
`.github/copilot-instructions.md`를 읽고 이 워크샵에 맞게 더 구체화해줘.

반드시 반영:
- Step 00부터 Step 03까지는 코딩 금지
- 실제 결과물은 output/에서만 만든다
- 문서와 설명은 한국어
- GitHub Pages 상대 경로 유지

output/ 구현 파일은 만들지 말고 규칙 파일만 수정해줘.
```

### Step 2. HTML/CSS/JS 규칙 점검

```text
`.github/instructions/html.instructions.md`, `.github/instructions/css.instructions.md`, `.github/instructions/js.instructions.md`를 검토하고
더 분명하고 짧게 다듬어줘.

조건:
- output/ 구현 코드는 아직 작성하지 마
- 규칙 파일만 다듬어줘
```

### Step 3. 적용 방식 이해하기

```text
현재 instructions들이 언제 적용되는지 한국어로 설명해줘.

구분:
- 저장소 전체 규칙
- HTML 규칙
- CSS 규칙
- JS 규칙
```

## 4. 체크리스트

- [ ] `.github/copilot-instructions.md`가 워크샵 흐름을 설명하는가
- [ ] HTML/CSS/JS용 `.instructions.md`가 각각 나뉘어 있는가
- [ ] `applyTo`가 `output/` 하위 파일로 잡혀 있는가
- [ ] `output/` 구현 파일이 아직 없는가

## 5. 다음 step로 가져갈 것

- 저장소 전체 규칙
- 파일별 규칙