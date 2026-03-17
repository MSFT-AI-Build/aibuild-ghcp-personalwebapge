# Bonus C. 레거시 마이그레이션 — 클래식 ASP를 Java로 전환

> ⏱️ 30분 | 난이도 ⭐⭐ | **체감: "Copilot이 레거시 코드 전환도 도와준다!"**
>
> 🎯 **목표**: 클래식 ASP로 작성된 레거시 코드를 Java(Spring Boot)로 마이그레이션하며 Copilot의 언어 전환 능력 체험

---

## 📚 사전 준비

- **메인 트랙 Step 06 이상 완료**
- 클래식 ASP 코드 샘플 (아래 제공)

---

## 📖 배경: 왜 레거시 마이그레이션인가?

현업에서는 **클래식 ASP(VBScript)**, **PHP 4/5**, **JSP + Servlet** 등으로 작성된 레거시 코드를 현대적인 프레임워크로 전환하는 작업이 빈번합니다. Copilot은 이런 레거시 언어도 이해하고 현대적인 코드로 변환할 수 있습니다.

| 레거시 | 현대화 |
|--------|--------|
| 클래식 ASP (VBScript) | Spring Boot (Java) |
| 인라인 SQL | Spring Data JPA |
| Response.Write | REST API + JSON |
| Session/Application 상태 관리 | 서비스 레이어 + DB |

---

## 실습 개요

1. 샘플 클래식 ASP 코드를 `#file`로 참조하여 Java 변환 요청
2. Spring Boot + Spring Data JPA로 현대적 구조로 전환
3. 인라인 SQL → JPA Repository 변환
4. JUnit 5로 테스트 작성

---

## 🧩 샘플 레거시 코드 (클래식 ASP)

아래와 같은 클래식 ASP 코드가 있다고 가정합니다. 실습 전 이 코드를 파일로 저장해두세요.

### legacy/todo_list.asp

```asp
<%@ Language="VBScript" %>
<%
' TODO 목록 조회 및 생성 페이지
Dim conn, rs, sql

Set conn = Server.CreateObject("ADODB.Connection")
conn.Open "Provider=Microsoft.Jet.OLEDB.4.0;Data Source=" & Server.MapPath("todo.mdb")

' TODO 생성 처리
If Request.Form("action") = "create" Then
    sql = "INSERT INTO todos (title, description, priority, completed) VALUES ('" & _
          Replace(Request.Form("title"), "'", "''") & "', '" & _
          Replace(Request.Form("description"), "'", "''") & "', '" & _
          Request.Form("priority") & "', 0)"
    conn.Execute sql
    Response.Redirect "todo_list.asp"
End If

' TODO 목록 조회
sql = "SELECT * FROM todos ORDER BY id DESC"
Set rs = conn.Execute(sql)
%>
<html>
<body>
<h1>할일 목록</h1>
<form method="post">
    <input type="hidden" name="action" value="create">
    제목: <input type="text" name="title"><br>
    설명: <input type="text" name="description"><br>
    우선순위: <select name="priority">
        <option>LOW</option>
        <option selected>MEDIUM</option>
        <option>HIGH</option>
    </select><br>
    <input type="submit" value="추가">
</form>
<table border="1">
<tr><th>ID</th><th>제목</th><th>설명</th><th>우선순위</th><th>완료</th></tr>
<%
Do While Not rs.EOF
    Response.Write "<tr>"
    Response.Write "<td>" & rs("id") & "</td>"
    Response.Write "<td>" & Server.HTMLEncode(rs("title")) & "</td>"
    Response.Write "<td>" & Server.HTMLEncode(rs("description")) & "</td>"
    Response.Write "<td>" & rs("priority") & "</td>"
    Response.Write "<td>" & rs("completed") & "</td>"
    Response.Write "</tr>"
    rs.MoveNext
Loop
%>
</table>
</body>
</html>
<%
rs.Close
conn.Close
Set rs = Nothing
Set conn = Nothing
%>
```

---

## 💬 Copilot Agent 프롬프트 예시

```
#file:legacy/todo_list.asp

위 클래식 ASP(VBScript) 레거시 코드를 Java Spring Boot로 마이그레이션해줘.

조건:
- REST API로 변환 (JSON 응답, 뷰 렌더링 대신)
- Spring Data JPA + H2 사용 (@Entity, JpaRepository)
- Controller → Service → Repository 레이어 구조
- 인라인 SQL을 JPA 메서드 쿼리로 변환
- SQL Injection 취약점 제거
- Jakarta Validation으로 입력 검증 추가
- 기존 기능 유지: 목록 조회, 생성, 우선순위 필터링
- JUnit 5 + MockMvc로 테스트 코드 포함
```

---

## 관찰 포인트

- [ ] Copilot이 VBScript → Java 패턴 차이를 이해하는가?
- [ ] 인라인 SQL이 JPA Repository 메서드로 올바르게 변환되는가?
- [ ] SQL Injection 등 보안 취약점이 제거되었는가?
- [ ] Response.Write 패턴이 REST API JSON 응답으로 변환되는가?
- [ ] Session/Application 상태 관리가 적절히 대체되는가?
- [ ] 어디서 수동 수정이 필요한가?

---

## 💡 핵심 인사이트

- **레거시 코드 이해**: Copilot은 클래식 ASP, VBScript 같은 오래된 언어도 잘 이해합니다. `#file`로 레거시 코드를 제공하면 현대적 패턴으로 전환해 줍니다.
- **보안 개선**: 마이그레이션 과정에서 SQL Injection, XSS 등 보안 취약점이 자연스럽게 제거됩니다.
- **구조 개선**: 모놀리식 ASP 페이지가 Controller/Service/Repository 레이어로 분리됩니다.
- **반드시 검토**: 비즈니스 로직이 정확히 보존되었는지 테스트로 확인하세요.

---

## ✅ 체크리스트

- [ ] 클래식 ASP 코드를 Spring Boot로 변환 완료
- [ ] REST API 엔드포인트 동작 확인 (curl 또는 Swagger)
- [ ] SQL Injection 취약점 제거 확인
- [ ] JUnit 테스트 통과
- [ ] 기존 기능(목록 조회, 생성, 우선순위)이 유지되는지 확인

---

## 다음 단계

→ [Step 14. Spec Kit](../step-14-bonus-e-speckit/README.md)

---

## 다음 단계

→ [Step 14. Spec Kit](../step-14-bonus-e-speckit/README.md)