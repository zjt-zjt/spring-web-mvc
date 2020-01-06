<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 2019/10/18
  Time: 20:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>

    <title>hello</title>
</head>
<body>

   <a href="/hello">欢迎进入</a><br />
   <a href="/hello_world">Hello World</a> <br />
   <a href="/user">用户列表</a> <br />
   <a href="/test">测试路径</a> <br />
   <a href="/save?name=老大">保存用户</a><br/>


   <form action="/user/" method="post">
      <input type="hidden" name="_method" value="put" />
      <input type="number" name="id" />
      <input type="text" name="name" />
      <input type="submit" />
   </form>

</body>
</html>
