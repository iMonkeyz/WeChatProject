<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>群分享发布系统</title>
	<link href="${pageContext.request.contextPath}/js_css/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<script src="${pageContext.request.contextPath}/js_css/bootstrap/js/jquery-1.9.1.min.js"></script>
</head>
<body>
	<c:choose>
		<c:when test="${empty sessionScope.ADMIN}">
			<c:import url="misc/admin-login-panel.jsp"/>
		</c:when>
		<c:otherwise>
			<c:import url="misc/admin-welcome-panel.jsp"/>
		</c:otherwise>
	</c:choose>
</body>
</html>
