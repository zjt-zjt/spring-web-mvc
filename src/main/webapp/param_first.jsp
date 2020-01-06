<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 2019/10/22
  Time: 20:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>

    <title>hello</title>
</head>
<body>

<a href="/param/requestParam?name=李嘉诚">传递请求参数</a> <br />
    <form action="/param/requestParam" method="post">
        <input type="hidden" name = "name" value="李嘉诚"/>
        <input type="submit">
    </form><br />

  <%
    request.setAttribute("name","李嘉诚");
    /*request.getRequestDispatcher("/param/sessionscope").forward(request,response);*/
%>

会话作用域参数传递：<br />
<%
    session.setAttribute("name", "李嘉诚");
%>
<a href="/param/sessionscope">发起请求</a>

<hr />

cookie参数传递：<br />
<%
    response.addCookie(new Cookie("name", "张三"));
%>
<a href="/param/cookie">发起请求</a>


<form action="/param/checkbox_param" method="post">
    <input type="hidden" name="name" value="李四" />
    爱好：<br />
    <input type="checkbox" name="hobby" value="编程" /> 编程
    <input type="checkbox" name="hobby" value="爬山" /> 爬山
    <input type="checkbox" name="hobby" value="上天" /> 上天
    <input type="submit" />
</form>
</body>
</html>
