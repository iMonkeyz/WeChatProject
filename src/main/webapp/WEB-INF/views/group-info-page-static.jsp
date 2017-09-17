<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>群信息介绍页</title>
	<link href="${pageContext.request.contextPath}/js_css/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/js_css/custom/css/group-info.css" rel="stylesheet">
	<script src="${pageContext.request.contextPath}/js_css/bootstrap/js/jquery-1.9.1.min.js"></script>
</head>
<body style="font-size: 12px;">
	<div class="container">
		<div class="row group-banner">
			<img src="${pageContext.request.contextPath}/img/banner.jpg" class="img-responsive center-block">
		</div>
		<div class="row group-title">
			<div class="col-xs-12">
				<span class="h5">这个片儿有毒</span>
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
				<span>2017-09-07 15:30</span>
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
							<img class="media-object img-circle avatar" src="${pageContext.request.contextPath}/img/icon.jpg">
						</div>
						<div class="media-body">
							<div>▍这个片儿有毒 ▍影视资源分享群 </div>
							<div>&nbsp;</div>
							<div>禁言! 只发片, 不说话</div>
							<div>拯救被洗劫一空的A站/B站</div>
							<div>&nbsp;</div>
							<div>周一至周五中午12:30, 群内准时更新电影资源</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-xs-12 info-panel">
				<div class="info-panel-header">
					<div class="symbol"></div>
					<h5 class="header-text">群介绍</h5>
				</div>
				<div class="info-panel-content desc-text">
					<div>▍这个片儿有毒 ▍影视资源分享群 </div>
					<div>&nbsp;</div>
					<div>▲不说话只发片, 365日影视资源共享</div>
					<div>&nbsp;</div>
					<div>▲看完不想说话, 只想装B片</div>
					<div>▲看完不想工作, 只想打人片</div>
					<div>▲看完不想学习, 只想抖腿片</div>
					<div>▲看完不想恋爱, 只想结婚片</div>
					<div>&nbsp;</div>
					<div>★报名方式: 原价65元, 限时免费, 过时不候, 截止9月8号晚24点!</div>
				</div>
			</div>
		</div>
		<div class="row navbar-fixed-bottom">
			<div class="col-xs-12 text-center group-join">
				<a class="btn btn-success join-button" href="${pageContext.request.contextPath}/group-setting">我要入群</a>
			</div>
		</div>
	</div>
</body>
</html>
