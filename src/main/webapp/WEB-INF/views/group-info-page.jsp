<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>${groupInfo.name}</title>
	<link href="${pageContext.request.contextPath}/js_css/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/js_css/custom/css/group-info.css" rel="stylesheet">
	<script src="${pageContext.request.contextPath}/js_css/bootstrap/js/jquery-1.9.1.min.js"></script>
</head>
<body style="font-size: 12px;">
	<div class="container">
		<div class="row group-banner">
			<img src="${groupInfo.banner }" class="img-responsive center-block">
		</div>
		<div class="row group-title">
			<div class="col-xs-12">
				<span class="h5">${groupInfo.name }</span>
			</div>
		</div>
		<div class="row group-split">
			<div class="line"></div>
		</div>
		<div class="row group-datetime desc-text">
			<div class="col-xs-6 text-left">
				<span class="glyphicon glyphicon-time"></span> 活动开始时间
			</div>
			<div class="col-xs-6 text-right">
				<span>${groupInfo.datetime }</span>
			</div>
		</div>
		<div class="row group-info-container">
			<div class="col-xs-12 info-panel">
				<div class="info-panel-header">
					<div class="symbol"></div>
					<h5 class="header-text">创建人介绍</h5>
				</div>
				<div class="info-panel-content desc-text">
					<div class="media">
						<div class="media-left">
							<img class="media-object img-circle img-avatar" src="${groupInfo.avatar }">
						</div>
						<div class="media-body">
							${groupInfo.formattedIntro }
						</div>
					</div>
				</div>
			</div>
			<c:forEach items="${groupInfo.infos }" var="info" varStatus="status">
				<div class="col-xs-12 info-panel">
					<div class="info-panel-header">
						<div class="symbol"></div>
						<h5 class="header-text">${info.title }</h5>
					</div>
					<div class="info-panel-content desc-text">
						${info.formattedConent}
					</div>
				</div>
			</c:forEach>
			<div class="col-xs-12 info-panel">
				<div class="info-panel-header">
					<div class="symbol"></div>
					<h5 class="header-text">用户信息</h5>
				</div>
				<div class="info-panel-content desc-text">
					<div>${sessionScope.GUEST.nickname}</div>
				</div>
			</div>
		</div>
		<div class="row navbar-fixed-bottom">
			<div class="col-xs-12 text-center group-join">
				<c:choose>
					<c:when test="${adminMode}">
						<a class="btn btn-success join-button" href="${pageContext.request.contextPath}/group/admin">返回</a>
					</c:when>
					<c:otherwise>
						<a class="btn btn-success join-button" href="${pageContext.request.contextPath}/group/share/${groupInfo.id}/join">我要入群</a>
					</c:otherwise>
				</c:choose>

			</div>
		</div>
	</div>
</body>
</html>
