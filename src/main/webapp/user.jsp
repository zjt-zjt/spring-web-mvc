<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 2019/10/19
  Time: 11:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

用户列表： <br />
<c:forEach items="${userList}" var="user">
    <c:out value="${user.name}" /> <br />
</c:forEach>

</body>
</html>
