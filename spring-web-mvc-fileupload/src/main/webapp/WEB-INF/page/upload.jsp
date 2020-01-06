<%--
  Created by IntelliJ IDEA.
  User: John
  Date: 2019/10/23
  Time: 10:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>文件上传</title>
</head>
<body>

    <!-- ApacheCommonFileUpload方式上传 -->
    <form action="/file/upload" method="post" enctype="multipart/form-data">

        <input type="file" name="myfile">

        <input type="submit">
    </form>

<!-- 基于servlet3.0方式上文件传 -->
<%--<form action="/Servletfile/upload" method="post" enctype="multipart/form-data">

    <input type="file" name="myfile">

    <input type="submit">
</form>--%>



</body>
</html>
