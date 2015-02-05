<%--
  Created by IntelliJ IDEA.
  User: lihao
  Date: 1/28/15
  Time: 10:34 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title></title>
  
</head>
<body>
<ul style="list-style-type: none">
  <c:forEach var="d" items="${data}" varStatus="status">
    <li style="list-style-type: none">${status.index+1}、<a href="${d.httpPath}">${d.name}</a></li><br>
    ----------------------------------------------------------------------------------------------
  </c:forEach>
</ul>
</body>
</html>
