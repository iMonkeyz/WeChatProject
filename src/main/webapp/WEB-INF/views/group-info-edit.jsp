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
		var Control = {
			init: function () {
				this.Buttons.init();
				this.Events.init();
			},
			Buttons: {
				init: function () {
					this.fileUpload();
					this.addPanel();
					this.removePanel();
					this.removeQr();
					this.saveAll();
				},
				fileUpload: function () {
					$(".btn-file-upload").on("click", function () {
						var target = $(this).data("trigger-of");
						$(target).trigger("click");
					});
				},
				addPanel: function () {
					$(".group-info-container").on("click", ".btn-new-panel", function () {
						var panel = $(".info-panel-template").clone().removeClass("info-panel-template");
						$(panel).find("input, textarea").val("");
						$(".group-info-container").append(panel);
					});
				},
				removePanel: function () {
					$(".group-info-container").on("click", ".btn-remove-panel", function () {
						$(this).parents(".info-panel").remove();
					});
				},
				removeQr: function () {
					$(".panel-qr").on("click", ".btn-remove-qr", function () {
						$(this).parents(".item-qr").animate({opacity: 0, transform: 'scale(0.001)'}, 'fast', function () {
							$(this).remove();
						});
					});
				},
				saveAll: function () {
					$(".navbar").on("click", ".btn-save", function () {
						//do validation
						var msgs = [];
						if ( $(".img-banner").attr("src") == "/wx/img/default_banner.png" ) {
							msgs.push("宣传大图未上传");
						}
						if ( $(".img-avatar").attr("src") == "/wx/img/avatar.png" ) {
							msgs.push("头像未上传");
						}
						if ( $(".img-qr").attr("src") == "/wx/img/default_qr.png" ) {
							msgs.push("二维码未上传");
						}
						if ( $.trim( $("[name='name']").val() ) == "" ) {
							msgs.push("群名称为空");
						}
						var datetime = $.trim( $("[name='datetime']").val() );
						if ( datetime == "" ) {
							msgs.push("活动日期为空");
						}
						if ( !/^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\s{1}(20|21|22|23|[0-1]\d):[0-5]\d$/.test(datetime) ) {
							msgs.push("活动日期不正确");
						}
						if ( $.trim( $("textarea[name='intro']").val() ) == "" ) {
							msgs.push("创建人介绍为空");
						}
						var group_intro = $.trim( $(".info-panel.info-panel-template").find("[name='content']").val() );
						if ( group_intro == "" ) {
							msgs.push("群介绍为空");
						}
						if ( msgs.length > 0 ) {
							alert(msgs);
							return;
						}

						//do submit
						$(".bs-example-modal-sm").modal("toggle");

						//panel infos
						var infos = [];
						$(".info-panel:not(.intro-panel)").each(function () {
							infos.push({
								title: $(this).find("input[name='title']").val(),
								content: $(this).find("textarea[name='content']").val()
							});
						});

						//QRs
						var qrs = [];
						$(".item-qr:not(.template-qr)").each(function () {
							qrs.push($(this).find(".img-qr").attr("src"));
						});

						var data = {
							id: null,
							name: $("input[name='name']").val(),
							datetime: $("input[name='datetime']").val(),
							intro: $("textarea[name='intro']").val(),
							banner: $(".img-banner").attr("src"),
							avatar: $(".img-avatar").attr("src"),
							infos: infos,
							qrs: qrs
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
				}
			},
			Events: {
				init: function () {
					this.dateTimePlugin();
					this.fileUpload();
					this.qrUpload();
				},
				dateTimePlugin: function () {
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
				},
				fileUpload: function () {
					$(".input-file:not(.file-qr)").on("change", function () {
						var img = $(this).data("file-for");
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
								$(img).attr("src", dataURL);
							};
							reader.readAsDataURL(file);
						}
					});
				},
				qrUpload: function () {
					$(".img-qr").on("click", function () {
						$(".file-qr").trigger("click");
					});
					$(".file-qr").on("change", function () {
						if ( this.files || this.files[0] ) {
							var qr = $(".template-qr").clone().removeClass("template-qr");
							var file = this.files[0];
							var kb = (file.size / 1024).toFixed(2);
							if ( kb > 500 ) {
								alert("请上传小于500K的图片.")
								return;
							}
							var reader = new FileReader();
							reader.onloadend = function () {
								var dataURL = reader.result;
								$(qr).find("img").attr("src", dataURL);
							};
							reader.readAsDataURL(file);
							$(".panel-qr").prepend(qr);
							$(this).val("");
						}

					});
				}
			}
		};

		$(()=>{
			Control.init();
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
	<div class="row step-1">
		<div class="col-xs-12">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">第 1 步: 上传宣传大图</h3>
				</div>
				<div class="panel-body">
					<div class="row group-banner">
						<div class="banner-upload-panel text-center">
							<button class="btn btn-xs btn-primary btn-file-upload" data-trigger-of=".file-banner">图片上传</button>
						</div>
						<img class="img-responsive center-block img-banner" src="${pageContext.request.contextPath}/img/default_banner.png">
						<input type="file" class="input-file file-banner" data-file-for=".img-banner"/>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="row step-2">
		<div class="col-xs-12">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">第 2 步: 设定群分享信息</h3>
				</div>
				<div class="panel-body">
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
										<img class="media-object img-circle img-avatar" src="${pageContext.request.contextPath}/img/avatar.png">
										<input type="file" class="input-file file-avatar" data-file-for=".img-avatar"/>
										<button class="btn btn-xs btn-primary btn-file-upload" data-trigger-of=".file-avatar">头像上传</button>
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
	</div>
	<div class="row step-3">
		<div class="col-xs-12">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">第 3 步: 设定群二维码</h3>
				</div>
				<div class="panel-body text-center panel-qr">
					<div class="col-xs-6 col-sm-4 col-md-3 col-lg-2 item-qr template-qr">
						<div class="ui-flex justify-center center content-qr">
							<div class="btn-remove-qr">
								<a class="btn btn-xs btn-danger glyphicon glyphicon-trash"></a>
							</div>
							<img src="${pageContext.request.contextPath}/img/add.png" class="img-responsive center-block img-qr">
						</div>
					</div>
					<div class="col-xs-12">
						<input type="file" class="input-file file-qr"/>
						<div class="input-group input-group-sm">
							<span class="input-group-addon">限制分享次数</span>
							<input class="form-control input-sm text-center" maxlength="3" type="text" placeholder="100">
							<span class="input-group-addon">(最大100)</span>
						</div>
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
