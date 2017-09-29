<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>演示导航页</title>
	<link href="${pageContext.request.contextPath}/js_css/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<script src="${pageContext.request.contextPath}/js_css/bootstrap/js/jquery-1.9.1.min.js"></script>
</head>
<body>
<div class="container">
	<div class="row">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">功能 1: 群信息发布</h3>
			</div>
			<div class="panel-body text-center">
				<a class="btn btn-primary" href="${pageContext.request.contextPath}/group-setting">点我跳转</a>
			</div>
			<div class="panel-heading">
				<h3 class="panel-title">功能 2: 二维码访问加群</h3>
			</div>
			<div class="panel-body text-center">
				<img class="center-block" src="${pageContext.request.contextPath}/group/qr/share/1506311181905" style="width: 200px;"/>
				<p style="margin-top: 5px;">扫我</p>
			</div>
			<div class="panel-heading">
				<h3 class="panel-title">功能 3: 群信息发布后台管理</h3>
			</div>
			<div class="panel-body text-center">
				<a class="btn btn-primary" href="${pageContext.request.contextPath}/group/admin/list">点我跳转</a>
			</div>
		</div>
	</div>
</div>

</body>
</html>
