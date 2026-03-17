---
agent: "agent"
description: "기존 패턴에 맞춰 새 API 엔드포인트를 추가합니다"
---

채팅에서 언급하거나 선택된 '기능'에 대한 API 엔드포인트를 추가해주세요.

기능: ${input:featureDescription}
(변수 입력이 없으면 채팅 메시지에서 언급된 기능을 사용하세요)

## 순서 (반드시 이 순서대로!)
1. **Controller**: TodoController에 엔드포인트 메서드 추가
2. **TEST**: test/에 새 엔드포인트에 대한 테스트 작성
3. **VERIFY**: ./gradlew test로 전체 테스트 통과 확인

## 참고 파일
#file:TodoController.java
