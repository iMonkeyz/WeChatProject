<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>群信息发布页</title>
	<link href="${pageContext.request.contextPath}/js_css/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/js_css/datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/js_css/custom/css/group-info.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/js_css/custom/css/group-edit.css" rel="stylesheet">
	<script src="${pageContext.request.contextPath}/js_css/bootstrap/js/jquery-1.9.1.min.js"></script>
	<script src="${pageContext.request.contextPath}/js_css/bootstrap/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath}/js_css/datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
	<script src="${pageContext.request.contextPath}/js_css/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
	<script language="JavaScript">
		$(function () {
			$(".group-info-container").on("click", ".btn-new-panel", function () {
				var panel = $(".info-panel-template").clone().removeClass("info-panel-template");
				$(panel).find("input, textarea").val("");
				$(".group-info-container").append(panel);
			}).on("click", ".btn-remove-panel", function () {
				$(this).parents(".info-panel").remove();
			});
			$(".btn-banner-upload").on("click", function () {
				$(".input-file[name='banner']").click();
			});
			$(".btn-avatar-upload").on("click", function () {
				$(".input-file[name='avatar']").click();
			});

			$(".input-file[name='banner']").on("change", function () {
				if ( this.files || this.files[0] ) {
					var file = this.files[0];
					var kb = (file.size / 1024).toFixed(2);
					if ( kb > 500 ) {
						alert("请上传小于500K的图片.")
						return;
					}
					var reader = new FileReader();
					reader.onloadend = function () {
						var dataURL = reader.result;
						$(".banner").attr("src", dataURL);
					};
					reader.readAsDataURL(file);
				}
			});

			$(".navbar").on("click", ".btn-preview", function () {

			}).on("click", ".btn-save", function () {
				$(".bs-example-modal-sm").modal("toggle");
				var infos = [];
				$(".info-panel:not(.intro-panel)").each(function () {
					infos.push({
						title: $(this).find("input[name='title']").val(),
						content: $(this).find("textarea[name='content']").val()
					});
				});

				var data = {
					id: null,
					name: $("input[name='name']").val(),
					datetime: $("input[name='datetime']").val(),
					intro: $("textarea[name='intro']").val(),
					banner: $(".banner").attr("src"),
					avatar: $(".avatar").attr("src"),
					infos: infos
				}

				$.ajax({
					url: "/wx/group/save",
					type: "POST",
					contentType: "application/json",
					data: JSON.stringify(data),
					dataType: "json",
					success: function (data) {
						$(".btn-save").addClass("disabled");
						$(".btn-preview").attr("href", "/wx/group/share/" + data).removeClass("disabled");
						console.log(data);
						$(".bs-example-modal-sm").modal("toggle");
						alert("创建成功, 您现在可以点击预览查看结果!");
					},
					error: function (data) {
						console.log(data);
					}
				})
			});

			$(".input-file[name='avatar']").on("change", function () {
				if ( this.files || this.files[0] ) {
					var file = this.files[0];
					var kb = (file.size / 1024).toFixed(2);
					if ( kb > 500 ) {
						alert("请上传小于500K的图片.")
						return;
					}
					var reader = new FileReader();
					reader.onloadend = function () {
						var dataURL = reader.result;
						$(".avatar").attr("src", dataURL);
					};
					reader.readAsDataURL(file);
				}
			});

			$(".input-file").on("change", function () {
				return;
				var file = this.files[0];
				var reader = new FileReader();
				reader.onloadend = function () {
					var dataURL = reader.result;
					$(".avatar").attr("src", dataURL);
					$.ajax({
						url: "/wx/img/save",
						type: "POST",
						data: {base64: dataURL},
						dataType: "json",
						success: function (data) {
							console.log(data);
						},
						error: function (data) {
							console.error(data);
						}
					});
				}
				reader.readAsDataURL(file);
			});

			$(".valid_datetime").datetimepicker({
				language:  'zh-CN',
				format: 'yyyy-mm-dd hh:ii',
				weekStart: 0,
				todayBtn:  1,
				autoclose: 1,
				todayHighlight: 1,
				startView: 2,
				forceParse: 0,
				showMeridian: 1
			});
		});
	</script>
</head>
<body style="padding-top: 50px;">
<div class="navbar navbar-fixed-top navbar-inverse">
	<div class="container">
		<div class="row" style="padding-top: 10px;">
			<div class="col-xs-6" style="padding-top: 5px;">
				<div style="color: #ffffff; font-size: large;"><b>创建群分享内容页</b></div>
			</div>
			<div class="col-xs-6 text-right">
				<a class="btn btn-sm btn-info btn-preview disabled">预&nbsp;览</a>
				<a class="btn btn-sm btn-success btn-save" data-target=".bs-example-modal-sm">保&nbsp;存</a>
			</div>

		</div>
	</div>
</div>
<div class="container">
	<div class="row">
		<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
			<div class="row group-banner">
				<div class="banner-upload-panel text-center">
					<button class="btn btn-xs btn-primary btn-banner-upload">图片上传</button>
				</div>
				<img src="${pageContext.request.contextPath}/img/default_banner.png" class="img-responsive center-block banner">
				<input type="file" class="input-file" name="banner"/>
			</div>

			<div class="row group-title">
				<div class="col-xs-12">
					<input type="text" class="input-field" placeholder="请在此处输入 群名称" name="name"/>
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
					<div class="input-group input-group-sm date valid_datetime" data-date-format="dd MM yyyy" data-link-format="yyyy-mm-dd">
						<input class="form-control input-sm" size="16" type="text" placeholder="请选择时间" name="datetime">
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
				</div>
			</div>
			<div class="row group-info-container">
				<div class="new-panel">
					<button class="btn btn-xs btn-primary btn-new-panel">新增信息模块</button>
				</div>
				<div class="col-xs-12 info-panel intro-panel">
					<div class="info-panel-header">
						<div class="symbol"></div>
						<h5 class="header-text">创建人介绍</h5>
					</div>
					<div class="info-panel-content desc-text">
						<div class="media">
							<div class="media-left text-center">
								<img class="media-object img-circle avatar" src="${pageContext.request.contextPath}/img/avatar.png">
								<input type="file" class="input-file" name="avatar"/>
								<button type="" class="btn btn-xs btn-primary btn-avatar-upload">头像上传</button>
							</div>
							<div class="media-body">
								<textarea class="input-field" name="intro"></textarea>
							</div>
						</div>
					</div>
				</div>
				<div class="col-xs-12 info-panel info-panel-template">
					<div class="remove-panel">
						<button class="btn btn-xs btn-danger btn-remove-panel glyphicon glyphicon-trash"></button>
					</div>
					<div class="info-panel-header">
						<div class="symbol"></div>
						<h5 class="header-text"><input type="text" class="input-field" value="群介绍" placeholder="请在此处输入标题" name="title"/></h5>
					</div>
					<div class="info-panel-content desc-text">
						<textarea class="input-field" placeholder="请在此处输内容..." name="content">请在此输入群介绍内容...</textarea>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
	<div class="modal-dialog modal-sm" role="document">
		<div class="modal-content">
			数据正在处理中...
		</div>
	</div>
</div>
</body>
</html>
