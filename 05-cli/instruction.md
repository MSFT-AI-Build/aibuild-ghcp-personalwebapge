# Step 05. CLI

## 1. 목표

만든 발표자료를 직접 실행하고 점검할 수 있게 만듭니다.

## 2. 배워볼 것

- 로컬 미리보기
- 기본 점검 스크립트
- GitHub CLI 흐름

## 3. Step by Step로 산출물 만들기

### Step 1. 로컬 실행 흐름 확인

`scripts/serve.sh`로 `output/`을 바로 띄울 수 있게 정리합니다.

### Step 2. 점검 흐름 확인

`scripts/check.sh`로 폴더 구조와 기본 상태를 확인합니다.

### Step 3. 배포 전 가이드 정리

`scripts/publish.sh`와 README를 보강해서 배포 직전 흐름을 정리합니다.

## 4. Copilot에 바로 넣을 프롬프트

### 스크립트 개선

```text
`scripts/serve.sh`, `scripts/check.sh`, `scripts/publish.sh`를 검토하고,
이 워크샵에 더 잘 맞게 개선해줘.

조건:
- 정적 사이트 기준
- output/ 폴더 기준
- gh 사용 가능하면 활용
- 설명과 주석은 한국어
```

### README CLI 가이드 보강

```text
README.MD에 CLI 사용 섹션을 보강해줘.

포함:
- 로컬 실행 방법
- 점검 방법
- gh를 활용한 저장소 작업 흐름
- 배포 전 체크
```

### 점검 항목 정리

```text
현재 output/ 발표자료를 기준으로,
CLI로 확인할 수 있는 점검 항목을 한국어로 정리해줘.

형식:
- 점검 항목
- 왜 필요한가
- 확인 명령
```

## 5. 체크리스트

- [ ] `scripts/serve.sh`로 로컬 미리보기가 되는가
- [ ] `scripts/check.sh`가 기본 상태를 확인하는가
- [ ] `scripts/publish.sh`가 배포 전 흐름을 안내하는가
- [ ] README에 CLI 사용 흐름이 들어갔는가

## 6. 다음 step로 가져갈 것

- 실행 스크립트
- 점검 스크립트
- 배포 가이드