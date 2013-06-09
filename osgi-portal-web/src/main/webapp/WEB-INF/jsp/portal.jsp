<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>OSGi Portal</title>

	<link rel="stylesheet" href="<c:url value="/resources/css/portal.css" />" />

	<script type="text/javascript" src="<c:url value="/resources/js/osgi-portal-min.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/portal.js" />"></script>
	
</head>
<body>
	<h1>OSGi Portal</h1>

	<c:forEach items="${appsToRender}" var="app">
		<c:set var="appConfig" value="${app.config}" />
		<c:set var="appContext" value="${appConfig.context}" />

		<div id="${app.namespace}" class="portalApp" style="width: ${app.width}; height: ${app.height};">
			<h2>${appConfig.symbolicName} - ${appConfig.version}</h2>
			
			<p>path: ${appContext.webContextPath}</p>
			
			<iframe src="${appContext.webContextPath}"></iframe>
		</div>
	</c:forEach>

</body>
</html>