<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>微信群测试</title>
	<link href="${pageContext.request.contextPath}/js_css/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<script src="${pageContext.request.contextPath}/js_css/bootstrap/js/jquery-1.9.1.min.js"></script>
</head>
<body>
<div class="container">
	<div class="row">
		<div class="col-sm-10 col-sm-offset-1">
			<div class="thumbnail">
				<img src="${userInfo.headimgurl}"/>
				<div class="caption">
					<p>
						<span class="h3">${userInfo.nickname}</span>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<span class="h5">${userInfo.sex == 1 ? '男' : '女'}</span>
					</p>
					<p>${userInfo.country} ${userInfo.province} ${userInfo.city}</p>
					<p><a href="${pageContext.request.contextPath}/join/${userInfo.openId}" class="btn btn-primary"
					      role="button">Join Group</a></p>
				</div>
			</div>
		</div>
	</div>
	<%-- <h2>Hello World!</h2>
	 <p>Code: ${code} , State: ${state}</p>
	 <h2>User Info</h2>
	 <p>
		 <ul>
			 <li>OpenID: ${userInfo.openId}</li>
			 <li>NickName: ${userInfo.nickname}</li>
			 <li>Sex: ${userInfo.sex}</li>
			 <li>Province: ${userInfo.province}</li>
			 <li>City: ${userInfo.city}</li>
			 <li>Country: ${userInfo.country}</li>
			 <li>Headimgurl:</li>
			 <li><img src="${userInfo.headimgurl}"></li>
			 <li>Privilege: ${userInfo.privilege}</li>
			 <li>UnionID: ${userInfo.unionid}</li>
		 </ul>
	 </p>
	 <h2>Error</h2>
	 <p>${userInfo.errCode} , ${userInfo.errMsg}</p>
	 <h2>Join Group</h2>
	 <form action="${pageContext.request.contextPath}/join/${userInfo.openId}" method="post">
		 <input type="hidden" name="openid" value="${userInfo.openId}">
		 <button id="join" type="submit" style="width:600px; height: 120px;">Click Me to Join Group</button>
	 </form>--%>
</body>
</html>
