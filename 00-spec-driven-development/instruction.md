# Step 00. Spec-Driven Development

## 1. 목표

무엇을 만들지 먼저 정합니다. 이 step에서는 코드를 만들지 않고, 기준 문서만 정리합니다.

## 2. 배워볼 것

- 스펙 문서 먼저 만들기
- Copilot에게 요구사항 정리 맡기기
- 구현 계획 문서 만들기

## 3. Step by Step로 산출물 만들기

### Step 1. 요구사항 문서 정리

Copilot Chat에 아래처럼 입력합니다.

```text
이 저장소는 GitHub Copilot 워크샵용 HTML 발표자료 프로젝트야.

`.specs/workshop-requirements.md`를 더 구체적으로 정리해줘.

조건:
- 실제 결과물은 output/에서 참가자가 직접 만든다
- Step 00부터 Step 03까지는 세팅 단계다
- Step 04부터 Step 06까지 output/을 본격적으로 만든다
- GitHub Pages 배포를 고려한다
- 의존성은 최소화한다

HTML/CSS/JS 코드는 만들지 말고 문서만 수정해줘.
```

### Step 2. 슬라이드 구조 문서 정리

```text
`.specs/slide-structure.md`를 바탕으로 발표자료 흐름을 더 구체적으로 정리해줘.

포함:
- 워크샵 소개
- Copilot 기능 흐름
- step별 학습 포인트
- 데모 또는 before/after
- 배포 흐름
- Q&A

output/ 코드는 만들지 말고 문서만 정리해줘.
```

### Step 3. 구현 계획 문서 만들기

```text
`.specs/workshop-requirements.md`, `.specs/slide-structure.md`, `.specs/ux-rules.md`를 읽고
`context/implementation-plan.md`를 채워줘.

반드시 포함:
1. 단계별 파일 계획
2. Step 00부터 Step 03까지는 세팅만 한다는 점
3. Step 04부터 output/을 수정한다는 점
4. GitHub Pages 배포 전 체크 포인트

아직 코드는 작성하지 마.
```

## 4. 체크리스트

- [ ] `.specs/workshop-requirements.md`가 구체화되었는가
- [ ] `.specs/slide-structure.md`가 발표 흐름을 설명하는가
- [ ] `context/implementation-plan.md`가 채워졌는가
- [ ] `output/` 안에 구현 파일이 아직 없는가

## 5. 다음 step로 가져갈 것

- 요구사항 문서
- 슬라이드 구조 문서
- 구현 계획 문서