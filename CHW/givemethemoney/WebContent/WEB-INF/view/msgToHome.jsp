<%@page import="poly.util.CmmUtil"%>
<%@ page import="poly.dto.EventDTO" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	String msg = CmmUtil.nvl((String)request.getAttribute("msg"));
%>
<%
%>
<script>
	alert("<%=msg%>");
	top.location.href="/home.do";
</script>
</body>
</html>