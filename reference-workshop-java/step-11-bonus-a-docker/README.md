# Bonus A. Docker — Copilot으로 컨테이너화하기

> ⏱️ 30분 | 난이도 ⭐⭐ | **체감: "Copilot이 Docker 설정도 척척!"**
>
> 🎯 **목표**: Copilot Agent를 활용해 TODO 앱을 Docker로 컨테이너화하고 docker-compose로 실행하기

---

## 📚 사전 준비

- **메인 트랙 Step 06 이상 완료** (Agent 모드 사용법 숙지)
- IntelliJ IDEA + GitHub Copilot 플러그인 설치 완료
- Docker Desktop 설치 및 실행 중
- Step 06 `complete/` 코드 사용 (또는 본인이 작업 중인 코드)

---

## 🐳 실습 1: Dockerfile 생성 (10분)

### 1-1. Copilot Agent에게 요청

**💬 Copilot Agent:**
```
#file:build.gradle.kts

현재 Spring Boot TODO 앱을 위한 Dockerfile을 만들어줘.

요구사항:
- Eclipse Temurin 베이스 이미지 (build.gradle.kts의 Java 버전에 맞춰서)
- 멀티스테이지 빌드 (Gradle 빌드 → 실행 이미지 분리)
- Spring Boot Layered Jar 활용
- 비루트 사용자로 실행
- 헬스 체크 포함 (/actuator/health)
- .dockerignore 도 만들어줘
```

### 1-2. 생성된 Dockerfile 확인

| 확인 항목 | 기대 내용 |
|----------|----------|
| 빌드 스테이지 | `eclipse-temurin:{버전}-jdk` + Gradle 빌드 (프로젝트 Java 버전과 일치) |
| 실행 스테이지 | `eclipse-temurin:{버전}-jre` (JRE만) |
| Layered Jar | `java -Djarmode=layertools -jar app.jar extract` |
| 비루트 사용자 | `USER` 지시어로 루트가 아닌 사용자 설정 |
| HEALTHCHECK | `/actuator/health` 엔드포인트 활용 |

> 📸 **[IntelliJ 스크린샷]** Agent가 생성한 Dockerfile이 IntelliJ 에디터에 열리고, 멀티스테이지 빌드 구조가 보이는 모습
>
> ![Dockerfile 생성 결과](./images/step10-dockerfile-result.png)

### 1-3. 이미지 빌드 및 실행 테스트

Copilot Agent에게 빌드부터 실행, 동작 확인까지 요청합니다:

**💬 Copilot Agent:**
```
생성된 Dockerfile로 Docker 이미지를 빌드하고, 컨테이너를 실행한 다음,
curl로 API가 정상 동작하는지 확인해줘.

확인할 엔드포인트:
- GET /actuator/health (헬스 체크)
- POST /todos (TODO 생성)
- GET /todos (TODO 목록 조회)

확인이 끝나면 컨테이너를 정리해줘.
```

> 💡 Agent 모드에서는 터미널 명령어 실행까지 자동으로 수행합니다.
> `docker build`, `docker run`, `curl` 테스트, `docker stop/rm`까지 한 번에 처리됩니다.

직접 터미널에서 수동으로 테스트하려면:

```bash
# Docker 이미지 빌드
docker build -t todo-api .

# 컨테이너 실행
docker run -d -p 8080:8080 --name todo-app todo-api

# 동작 확인
curl http://localhost:8080/actuator/health
curl http://localhost:8080/todos

# 정리
docker stop todo-app && docker rm todo-app
```

---

## 🏗️ 실습 2: docker-compose.yml 생성 (10분)

### 2-1. Copilot Agent에게 요청

**💬 Copilot Agent:**
```
docker-compose.yml을 만들어줘.

서비스:
1. api: Spring Boot TODO 앱 (포트 8080)
2. postgres: PostgreSQL DB (H2 대신)

설정:
- 환경 변수로 DB URL 주입 (spring.datasource.url)
- 볼륨 마운트로 데이터 영속화
- 네트워크 설정
- depends_on + healthcheck
```

### 2-2. 실행 및 확인

```bash
docker-compose up --build
curl http://localhost:8080/actuator/health
curl http://localhost:8080/todos
docker-compose down
```

---

## 🔧 실습 3: Docker 설정 개선 (10분)

### 3-1. 개발환경용 docker-compose 오버라이드

**💬 Copilot Agent:**
```
개발환경용 docker-compose.override.yml을 만들어줘.

요구사항:
- Spring DevTools 활성화
- 디버그 포트(5005) 노출 (JDWP)
- H2 콘솔 활성화
- .env 파일 로드
```

### 3-2. 프로덕션용 최적화 요청

**💬 Copilot Agent:**
```
프로덕션 배포를 위해 Dockerfile을 최적화해줘.

요구사항:
- 이미지 크기 최소화 (JRE + Layered Jar)
- Spring Boot Actuator 보안 설정
- JVM 메모리 최적화 (-XX:MaxRAMPercentage)
- 적절한 레이블(LABEL) 추가
```

---

## ✅ 전체 체크리스트

- [ ] Dockerfile 생성 및 이미지 빌드 성공
- [ ] .dockerignore 파일 생성
- [ ] docker-compose.yml로 멀티 서비스 실행
- [ ] 헬스 체크가 정상 동작 (/actuator/health)
- [ ] 개발/프로덕션 환경 분리 설정
- [ ] Layered Jar로 효율적인 이미지 레이어 구성

---

## 💡 핵심 인사이트

- **Spring Boot + Docker**: Layered Jar를 활용하면 의존성 레이어가 캐시되어 빌드 속도가 크게 향상됩니다.
- **보안은 반드시 검토**: 비루트 사용자 실행, Actuator 엔드포인트 보안 설정을 항상 확인하세요.
- **JVM 최적화**: 컨테이너 환경에서는 `-XX:MaxRAMPercentage`로 메모리 제한을 설정하는 것이 중요합니다.

---

## 다음 단계

→ [Step 12. React 프론트엔드](../step-12-bonus-b-react/README.md)
