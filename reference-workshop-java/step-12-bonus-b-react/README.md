# Bonus B. React 프론트엔드 — TODO 앱 UI 만들기

> ⏱️ 40분 | 난이도 ⭐⭐ | **체감: "Copilot이 프론트엔드도 척척!"**
>
> 🎯 **목표**: Copilot Agent로 React + TypeScript 프론트엔드를 빠르게 구현하고 Spring Boot API와 연동하기

---

## 📚 사전 준비

- **메인 트랙 Step 06 이상 완료** (Agent 모드 사용법 숙지)
- IntelliJ IDEA + GitHub Copilot 플러그인 설치 완료
- Node.js 18+ 설치
- Step 06 `complete/` 백엔드 서버 실행 가능 (`./gradlew bootRun`)

---

## 🛠️ 기술 스택

| 도구 | 버전 | 용도 |
|------|------|------|
| React | 18+ | UI 프레임워크 |
| TypeScript | 5+ | 타입 안전성 |
| Vite | 5+ | 빌드 도구 |
| Tailwind CSS | 3+ | 스타일링 |

---

## 📁 폴더 구조

```
todo-frontend/
├── src/
│   ├── api/           ← (실습 중 Copilot으로 생성)
│   ├── components/    ← (실습 중 Copilot으로 생성)
│   ├── App.tsx
│   └── main.tsx
├── package.json
├── tsconfig.json
└── vite.config.ts
```

---

## 🔧 프로젝트 초기화

터미널에서 실행:

```bash
# 프로젝트 폴더 생성 및 이동
mkdir todo-frontend && cd todo-frontend

# Git 초기화
git init

# Vite + React + TypeScript 프로젝트 생성
npm create vite@latest . -- --template react-ts

# 의존성 설치
npm install

# Tailwind CSS 설치
npm install -D tailwindcss @tailwindcss/vite

# 컴포넌트 디렉터리 생성
mkdir -p src/api src/components
```

### 검증

```bash
npm run dev
```

브라우저에서 `http://localhost:5173` 접속 시 Vite + React 기본 페이지가 보이면 성공! ✅

---

## 🚀 실습 1: 프로젝트 설정 (5분)

### 1-1. Tailwind CSS 설정

**💬 Copilot Agent에게 요청:**
```
이 React + TypeScript + Vite 프로젝트에 Tailwind CSS 설정을 완료해줘.
vite.config.ts에 @tailwindcss/vite 플러그인을 추가하고,
src/index.css에 @import "tailwindcss" 를 추가해줘.
```

### 1-2. 프록시 설정

**💬 Copilot Agent:**
```
vite.config.ts에 프록시를 설정해줘.
/todos 로 시작하는 요청은 http://localhost:8080 으로 프록시해야 합니다.
```

**예상 결과 (vite.config.ts):**
```typescript
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    proxy: {
      '/todos': 'http://localhost:8080',
      '/health': 'http://localhost:8080',
    },
  },
});
```

---

## 🚀 실습 2: API 클라이언트 생성 (10분)

**💬 Copilot Agent:**
```
백엔드 Spring Boot TODO API에 대한 TypeScript API 클라이언트를 만들어줘.

API 엔드포인트:
- GET /todos?page=0&size=10&priority= → Page<TodoResponse>
- POST /todos → TodoResponse (201)
- GET /todos/:id → TodoResponse
- PUT /todos/:id → TodoResponse
- PATCH /todos/:id → TodoResponse
- DELETE /todos/:id → 204

TodoResponse 타입:
- id: number
- title: string
- description: string | null
- priority: "LOW" | "MEDIUM" | "HIGH"
- completed: boolean
- createdAt: string
- updatedAt: string | null

타입 정의와 fetch 함수를 분리해서 만들어줘.
src/api/types.ts 와 src/api/todoApi.ts 로 나눠줘.
```

### 관찰 포인트
- [ ] Copilot이 Spring Boot 백엔드 스키마와 일치하는 타입을 생성하는가?
- [ ] 에러 핸들링을 포함하는가?
- [ ] Spring Data JPA의 `Page` 응답 구조를 올바르게 파싱하는가?

---

## 🚀 실습 3: 컴포넌트 구현 (15분)

### 3-1. TODO 목록 컴포넌트

**💬 Copilot Agent:**
```
TODO 목록을 보여주는 TodoList 컴포넌트를 만들어줘.

기능:
- 할 일 목록 표시 (제목, 우선순위 뱃지, 완료 체크박스)
- 우선순위별 색상 (HIGH: 빨강, MEDIUM: 노랑, LOW: 초록)
- 완료 토글 (체크박스 클릭 시 PATCH 호출)
- 삭제 버튼
- 페이지네이션 (Spring Data의 page/size/totalPages 기반)
- 우선순위 필터 셀렉트 박스

Tailwind CSS로 스타일링하고 반응형으로 만들어줘.
```

### 3-2. TODO 생성 폼

**💬 Copilot Agent:**
```
새 TODO를 추가하는 AddTodoForm 컴포넌트를 만들어줘.

필드:
- 제목 (필수, 텍스트 input)
- 설명 (선택, textarea)
- 우선순위 (select: LOW/MEDIUM/HIGH, 기본값 MEDIUM)

폼 제출 시 POST /todos 호출하고 목록을 새로고침해줘.
유효성 검사: 제목이 비어있으면 제출 비활성화.
Tailwind CSS로 스타일링.
```

### 3-3. 통계 대시보드 (선택)

**💬 Copilot Agent:**
```
Stats 컴포넌트를 만들어줘.
전체 TODO 목록에서 다음 통계를 계산해서 표시해:

- 전체 TODO 수
- 완료된 TODO 수와 완료율 (프로그레스 바)
- 우선순위별 개수 (HIGH/MEDIUM/LOW)

Chart 라이브러리 없이 Tailwind CSS 만으로 구현해줘.
```

---

## 🚀 실습 4: 페이지 조합 (5분)

**💬 Copilot Agent:**
```
App.tsx를 수정해서 다음 레이아웃을 만들어줘:

1. 상단: 앱 제목 "📝 TODO 앱" + 통계 요약
2. 중앙 상단: 새 TODO 추가 폼
3. 중앙: 우선순위 필터 + TODO 목록 + 페이지네이션
4. 하단: 푸터

반응형으로 모바일에서도 깔끔하게 보이도록.
Spring Boot 백엔드(http://localhost:8080)가 실행 중이어야 합니다.
```

---

## 🔧 백엔드 CORS 설정

프론트엔드(`:5173`)에서 백엔드(`:8080`)로 직접 요청할 경우 CORS 설정이 필요합니다.
Vite 프록시를 사용하면 CORS 없이도 동작하지만, 직접 연동하려면:

**💬 Copilot Agent (Spring Boot 프로젝트에서):**
```
Spring Boot에 CORS 설정을 추가해줘.
localhost:5173 과 localhost:3000 에서의 요청을 허용해야 해.
WebMvcConfigurer를 사용해줘.
```

**예상 결과:**
```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173", "http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                .allowedHeaders("*");
    }
}
```

---

## 🚀 실습 5: 통합 테스트 (5분)

### 5-1. 백엔드 실행

```bash
# Spring Boot 프로젝트 디렉터리에서
./gradlew bootRun
```

### 5-2. 프론트엔드 실행

```bash
# todo-frontend 디렉터리에서
npm run dev
```

### 5-3. 동작 확인

브라우저에서 `http://localhost:5173` 접속:

| 확인 항목 | 동작 |
|----------|------|
| 목록 조회 | TODO 목록이 표시되는가? |
| TODO 생성 | 폼에서 제출 시 목록에 추가되는가? |
| 완료 토글 | 체크박스 클릭 시 상태가 변경되는가? |
| 삭제 | 삭제 버튼 클릭 시 목록에서 제거되는가? |
| 우선순위 필터 | 필터 선택 시 해당 우선순위만 표시되는가? |
| 페이지네이션 | 10개 이상일 때 페이지 이동이 동작하는가? |

> 📸 **[스크린샷]** 완성된 React TODO 앱 UI — TODO 목록, 생성 폼, 우선순위 필터, 페이지네이션이 보이는 브라우저 화면
>
> ![React TODO 앱 완성](./images/step11-react-app-complete.png)

---

## ✅ 전체 체크리스트

- [ ] Vite + React + TypeScript 프로젝트 초기화
- [ ] Tailwind CSS 설정 완료
- [ ] 프록시 설정 (`vite.config.ts`)
- [ ] API 클라이언트 생성 (`src/api/`)
- [ ] TodoList 컴포넌트 구현
- [ ] AddTodoForm 컴포넌트 구현
- [ ] App.tsx에서 전체 레이아웃 조합
- [ ] 백엔드 + 프론트엔드 통합 테스트 성공
- [ ] CRUD 전체 동작 확인

---

## 💡 핵심 인사이트

| 관찰 항목 | 체크 |
|----------|------|
| Copilot이 TypeScript 타입을 정확히 생성하는가? | |
| React 컴포넌트 분리를 적절히 하는가? | |
| Tailwind 클래스를 올바르게 사용하는가? | |
| API 에러 핸들링을 포함하는가? | |
| Spring Boot API 응답 구조를 정확히 파싱하는가? | |

- **풀스택 개발**: Copilot은 백엔드(Spring Boot)와 프론트엔드(React) 모두에서 효과적입니다. 백엔드 API 스펙을 `#file`로 참조하면 프론트엔드 코드의 정확도가 올라갑니다.
- **타입 일관성**: TypeScript 타입 정의를 먼저 만들면 Copilot이 컴포넌트에서 일관된 타입을 사용합니다.
- **프록시 활용**: 개발 중에는 Vite 프록시로 CORS 문제를 우회하면 편리합니다.

---

## 🔗 참고

- [Vite 공식 문서](https://vitejs.dev/)
- [Tailwind CSS 공식 문서](https://tailwindcss.com/)
- [React TypeScript Cheatsheet](https://react-typescript-cheatsheet.netlify.app/)

---

## 다음 단계

→ [Step 13. 레거시 마이그레이션](../step-13-bonus-c-migration/README.md)

---

## 다음 단계

→ [Step 13. 레거시 마이그레이션](../step-13-bonus-c-migration/README.md)