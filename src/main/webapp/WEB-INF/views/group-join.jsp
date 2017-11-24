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
	<style>
		.mask {
			position: absolute;
			width: 100%;
			height: 70px;
			background-color: #FFFFFF;
		}
		.mask-header {
			top: 0;
		}
		.mask-footer {
			height: 50px;
			bottom: 0;
		}
		.mask-footer.desc-text {
			color: #adadad;
		}
	</style>
</head>
<body style="overflow: hidden;">
<div class="container text-center">
	<div class="row">
		<div class="col-xs-offset-1 col-xs-10">
			<div class="row" style="position:relative;">
				<div class="mask mask-header"></div>
				<img class="img-responsive center-block" src="${qrData}" <%--style="width: 200px; height: 200px;"--%>>
				<div class="mask mask-footer desc-text text-center">- 请长按二维码进行识别 -</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>
