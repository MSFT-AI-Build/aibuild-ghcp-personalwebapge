# 빠른 시작 가이드: 개발자 포트폴리오 생성기

## 사전 요구사항

- **Node.js** (v14 이상) — `node --version`으로 확인
- 다른 설치는 필요 없음 (npm install 불필요)

## 1단계: 데이터 준비

프로젝트 루트의 `data/` 폴더에 자료를 넣는다:

```
data/
├── photos/          ← 사진 파일 (JPEG, PNG 등)
└── resume/          ← 이력서 마크다운 파일 (.md)
```

## 2단계: 포트폴리오 생성

```bash
node generate.js
```

## 3단계: 결과 확인

`output/index.html` 파일을 더블클릭하면 브라우저에서 포트폴리오가 열린다.

```
output/
├── index.html       ← 이 파일을 더블클릭
└── photos/          ← 자동 복사된 이미지
```

## 데이터 수정 후

`data/` 폴더의 내용을 변경한 후 다시 `node generate.js`를 실행하면 `output/`이 갱신된다.

## 문제 해결

| 증상 | 원인 | 해결 |
|------|------|------|
| `node: command not found` | Node.js 미설치 | [nodejs.org](https://nodejs.org)에서 설치 |
| 이미지가 표시되지 않음 | `data/photos/`에 이미지 없음 | 이미지 파일(jpg, png 등)을 추가 |
| 이력서가 표시되지 않음 | `data/resume/`에 .md 파일 없음 | 마크다운 이력서 파일을 추가 |
| 페이지가 빈 화면 | generate.js 미실행 | `node generate.js` 실행 후 다시 열기 |
