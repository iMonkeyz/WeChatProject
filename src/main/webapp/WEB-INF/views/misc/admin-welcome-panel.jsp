<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="${pageContext.request.contextPath}/js_css/custom/css/welcome.css" rel="stylesheet">
<c:set var="userInfo" value="${sessionScope.ADMIN}"/>

<div class="container">
	<div class="row">
		<div class="page-header">
			<h1> 欢迎, <small>${userInfo.nickname} !</small><a class="btn btn-sm btn-warning pull-right btn-exit" href="admin/exit">退出</a></h1>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">功能 1: 群信息发布</h3>
			</div>
			<div class="panel-body text-center">
				<a class="btn btn-primary" href="admin/edit/0">点我跳转</a>
			</div>
			<div class="panel-heading">
				<h3 class="panel-title">功能 2: 二维码访问加群 [示例]</h3>
			</div>
			<div class="panel-body text-center">
				<img class="center-block" src="${pageContext.request.contextPath}/group/admin/qr/stream/1506676873128" style="width: 200px;"/>
				<p class="desc-text"> - 用手机扫我 - </p>
			</div>
			<div class="panel-heading">
				<h3 class="panel-title">功能 3: 已创建群信息管理</h3>
			</div>
			<div class="panel-body text-center">
				<div class="row">
					<c:forEach items="${groupInfos}" var="groupInfo">
						<div class="col-sm-6 col-md-4 info-block">
							<div class="thumbnail">
								<a class="btn btn-xs btn-danger glyphicon glyphicon-trash"></a>
								<img src="${groupInfo.banner}">
								<div class="caption">
									<h3>${groupInfo.name }</h3>
									<p>活动时间: ${groupInfo.datetime}</p>
									<p>
										<a href="admin/edit/${groupInfo.id}" class="btn btn-info" role="button">编辑</a>
										<a href="admin/view/${groupInfo.id}" class="btn btn-primary" role="button">查看</a>
										<a href="admin/qr/stream/${groupInfo.id}" class="btn btn-default" role="button">生成二维码</a>
									</p>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
</div>

<script src="${pageContext.request.contextPath}/js_css/custom/js/welcome.js"></script>
