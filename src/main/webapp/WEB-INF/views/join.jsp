<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  Created by IntelliJ IDEA.
  User: Jesse
  Date: 2017/8/12
  Time: 3:07
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>群二维码</title>
	<link href="${pageContext.request.contextPath}/js_css/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<script src="${pageContext.request.contextPath}/js_css/bootstrap/js/jquery-1.9.1.min.js"></script>
</head>
<body>
<div class="container">
	<div class="row">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">加入次数: ${joinCount}</h3>
			</div>
			<div class="panel-body">
				<img class="img-responsive"
				     src="${pageContext.request.contextPath}/js_css/Screenshot_20170811-220254.png">
			</div>
		</div>
	</div>

</div>

</body>
</html>
