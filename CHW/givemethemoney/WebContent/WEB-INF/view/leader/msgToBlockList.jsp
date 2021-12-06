<%@page import="poly.util.CmmUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%
	String msg= CmmUtil.nvl((String)request.getAttribute("msg"));
%>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
	alert("<%=msg%>");
	top.location.href="/leader/blockList.do";
</script>
</head>
<body>

</body>
</html>