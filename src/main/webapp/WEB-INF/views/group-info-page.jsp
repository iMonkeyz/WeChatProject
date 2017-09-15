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
	<script src="${pageContext.request.contextPath}/js_css/bootstrap/js/jquery-1.9.1.min.js"></script>
	<style>
		.content-body {
			color: #9B9B9B;
		}
	</style>
</head>
<body style="font-size: 12px;">
	<div class="container">
		<div class="row">
			<div style="padding-bottom: 10px; background-color: #EFEFF4;">
				<img src="${pageContext.request.contextPath}/img/banner.jpg" class="img-responsive center-block">
			</div>
		</div>
		<div class="row" style="padding: 10px 0;">
			<div class="col-xs-12">
				<span class="h5">这个片儿有毒</span>
			</div>
		</div>
		<div class="row" style="padding: 0 15px;">
			<div style="border-bottom: 1px solid #eee;"></div>
		</div>
		<div class="row content-body" style="padding: 10px 0;">
			<div class="col-xs-6 text-left">
				<span class="glyphicon glyphicon-time"></span> 活动开始时间
			</div>
			<div class="col-xs-6 text-right">
				<span>2017-09-07 15:30</span>
			</div>
		</div>
		<div class="row" style="background-color: #EFEFF4; padding: 10px 12px 0 12px;">
			<div class="col-xs-12" style="padding: 10px 15px; background-color: #FFFFFF; border-radius: 4px;">
				<div class="content-header">
					<div style="position: relative; margin-bottom: 10px;">
						<div style="width: 2px; height: 16px; background-color: green; position: absolute; top: 0px;"></div>
						<h5 style="margin-left: 8px;">创建人介绍</h5>
					</div>
				</div>
				<div class="content-body">
					<div class="media">
						<div class="media-left">
							<a href="#">
								<img class="media-object img-circle" src="${pageContext.request.contextPath}/img/icon.jpg" style="width: 64px; height: 64px;">
							</a>
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
		</div>
		<div class="row" style="background-color: #EFEFF4; padding: 10px 12px 50px 12px;">
			<div class="col-xs-12" style="padding: 10px 15px; background-color: #FFFFFF; border-radius: 4px;">
				<div class="content-header">
					<div style="position: relative; margin-bottom: 10px;">
						<div style="width: 2px; height: 16px; background-color: green; position: absolute; top: 0px;"></div>
						<h5 style="margin-left: 8px;">群介绍</h5>
					</div>
				</div>
				<div class="content-body">
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
			<div class="col-xs-12 text-center" style="height: 45px;">
				<button class="btn btn-success" style="width: 98%; height: 95%; background-color: #09BB07;">我要入群</button>
			</div>
		</div>
	</div>
</body>
</html>
