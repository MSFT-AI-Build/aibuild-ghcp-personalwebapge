# Step 06. Coding Agent

## 1. 목표

GitHub Issue로 Copilot coding agent에 일을 맡기는 흐름을 익힙니다.

## 2. 배워볼 것

- Coding Agent에 맡길 일 고르기
- Issue 본문 잘 쓰기
- Draft PR 리뷰하기
- IDE Agent Mode와 구분하기

## 3. Step by Step로 산출물 만들기

### Step 1. 맡길 작업 정하기

범위가 작고 분명한 작업 하나를 고릅니다.

예시:
- print mode 추가
- Q&A appendix 추가
- 모바일 뷰 개선

### Step 2. 이슈 초안 만들기

목표, 범위, 제외 범위, 검증 기준이 들어간 이슈를 만듭니다.

### Step 3. PR 리뷰 문장 준비하기

Copilot이 PR을 올리면, 코멘트로 다시 지시할 문장을 준비합니다.

## 4. Copilot에 바로 넣을 프롬프트

### Coding Agent용 이슈 초안

```text
현재 output/ 발표자료를 기준으로 Copilot coding agent에 맡길 이슈 초안을 작성해줘.

후보 작업:
- 인쇄용 print mode 추가
- Q&A appendix 섹션 추가
- 모바일 뷰 개선

형식:
- 목표
- 작업 범위
- 제외 범위
- 검증 기준
```

### PR 리뷰 코멘트 초안

```text
Copilot coding agent가 올린 Draft PR을 리뷰한다고 가정하고,
수정 요청 코멘트 예시를 3개 작성해줘.

조건:
- 구체적으로 지시
- output/ 웹페이지 기준
- 한국어로 작성
```

### Agent Mode와 Coding Agent 차이

```text
IDE의 Agent Mode와 GitHub Copilot coding agent의 차이를 한국어로 설명해줘.

반드시 포함:
- 실행 위치
- 작업 방식
- 결과물 형태
- 리뷰 방식
```

## 5. 체크리스트

- [ ] 작은 작업 하나를 맡길 수 있는가
- [ ] Issue 본문에 범위와 검증 기준이 들어갔는가
- [ ] PR 코멘트로 다시 지시할 수 있는가
- [ ] IDE Agent Mode와 구분해서 설명할 수 있는가

## 6. 다음 step로 가져갈 것

- Coding Agent용 이슈 초안
- PR 리뷰 코멘트 초안