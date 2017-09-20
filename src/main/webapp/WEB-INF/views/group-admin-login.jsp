<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>后台管理页面</title>
	<link href="${pageContext.request.contextPath}/js_css/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/js_css/custom/css/group-info.css" rel="stylesheet">
	<script src="${pageContext.request.contextPath}/js_css/bootstrap/js/jquery-1.9.1.min.js"></script>
	<script language="JavaScript">
		$(function () {
			var uuid = $.trim($("#uuid").text());
			setInterval(function () {
				$.get(`/wx/group/admin/validation/${uuid}`, function (data) {
					var d = new Date();
					$("#result").prepend("<div>" + d.toLocaleDateString() + " " + d.toLocaleTimeString() + "授权结果: " + data + "</div>");
				});
			}, 2000);
		});
	</script>
</head>
<body>
授权地址: https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxac058681c6aff3a8&redirect_uri=http%3A%2F%2Fimonkeyz.ngrok.cc%2Fwx%2Fgroup%2Fadmin%2Fauthorization&response_type=code&scope=snsapi_userinfo&state=${uuid}#wechat_redirect
<br>
UUID: <span id="uuid">${uuid}</span>
<br>
<div style="display: table;">
	<div style="display: table-cell;">
		授权登录二维码: <img src="${pageContext.request.contextPath}/group/qr/auth/${uuid}" style="width: 256px;">
	</div>
	<div style="display: table-cell;">
		群1505901650830分享二维码: <img src="${pageContext.request.contextPath}/group/qr/share/1505901650830" style="width: 256px;">
	</div>
</div>
<hr>
<div id="result"></div>
</body>
</html>
