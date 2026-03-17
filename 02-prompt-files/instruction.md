# Step 02. Prompt Files

## 1. 목표

반복해서 쓰는 요청을 slash command로 정리합니다.

## 2. 배워볼 것

- `.prompt.md` 구조
- description과 argument-hint 쓰는 법
- 반복 프롬프트 자산화

## 3. Step by Step로 산출물 만들기

### Step 1. 기존 prompt files 다듬기

```text
`.github/prompts/` 아래의 prompt files를 검토하고,
이 워크샵에서 바로 쓰기 좋게 더 짧고 분명하게 다듬어줘.

대상:
- add-slide.prompt.md
- polish-slide.prompt.md
- add-speaker-notes.prompt.md
- rewrite-for-audience.prompt.md

아직 output/ 구현 파일은 만들지 마.
```

### Step 2. 언제 쓰는지 정리하기

```text
각 prompt file을 언제 쓰는지 한국어로 짧게 설명해줘.

형식:
- 프롬프트 이름
- 언제 쓰는가
- 예시 입력 한 줄
```

### Step 3. 추가 prompt 아이디어 받기

```text
이 워크샵에 어울리는 prompt file 2개를 제안해줘.

조건:
- 발표자료 작업에 도움돼야 함
- output/ 수정과 연결돼야 함
- description과 용도를 같이 적어줘

지금은 제안만 하고 파일은 만들지 마.
```

## 4. 체크리스트

- [ ] 각 `.prompt.md`에 description이 있는가
- [ ] 각 `.prompt.md`가 한 작업에 집중하는가
- [ ] argument-hint가 이해하기 쉬운가
- [ ] `output/` 구현 파일이 아직 없는가

## 5. 다음 step로 가져갈 것

- 재사용 가능한 prompt files 세트