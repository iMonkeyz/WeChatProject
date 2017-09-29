<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>授权结果</title>
	<link href="${pageContext.request.contextPath}/js_css/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/js_css/custom/css/common.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/js_css/custom/css/authorization.css" rel="stylesheet">
	<script src="${pageContext.request.contextPath}/js_css/bootstrap/js/jquery-1.9.1.min.js"></script>
	<style>

	</style>
</head>
<body>
	<div class="ui-flex justify-center center full">
		<div class="info-box text-center">
			<c:choose>
				<c:when test="${isPass}">
					<div><i class="icon-success"></i></div>
					<div class="text-box">
						<p class="sub_title">授权成功</p>
						<p class="sub_desc">- 微信内置浏览器授权请<span style="text-decoration:underline;"><a href="/wx/">点击这里</a></span> -</p>
					</div>
				</c:when>
				<c:otherwise>
					<div><i class="icon-fail"></i></div>
					<div class="text-box">
						<p class="sub_title">授权失败</p>
						<p class="sub_desc">二维码已过期, 请刷新后重试!</p>
					</div>
				</c:otherwise>
			</c:choose>

		</div>
	</div>
</body>
</html>
