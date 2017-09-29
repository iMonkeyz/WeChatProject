<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="${pageContext.request.contextPath}/js_css/custom/css/admin.css" rel="stylesheet">

<div class="login ui-flex justify-center center">
	<div class="login_box">
		<div class="qrcode">
			<img class="img">
			<div class="waiting-scan">
				<p class="sub_title">请使用微信进行扫码登录</p>
				<p class="sub_desc">本网站关联微信号作为管理用户</p>
			</div>
			<div class="need-refresh">
				<div class="refresh_qrcode_mask ui-flex justify-center center">
					<div class="refresh">
						<i class="icon-refresh"></i>
					</div>
				</div>
				<p class="sub_desc">二维码失效，点击刷新</p>
			</div>
		</div>
		<div class="logo text-center">
			<img class="icon" src="${pageContext.request.contextPath}/js_css/custom/img/wechat.png">
		</div>
	</div>
</div>

<script src="${pageContext.request.contextPath}/js_css/custom/js/admin.js"></script>