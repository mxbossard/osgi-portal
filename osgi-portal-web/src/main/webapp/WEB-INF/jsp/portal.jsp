<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

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

	<fieldset>
		<legend>Apps to render</legend>
	
		<c:forEach items="${appsToRender}" var="app">
			<c:set var="appConfig" value="${app.config}" />
			<c:set var="appContext" value="${appConfig.context}" />
			
			<div id="${app.namespace}" class="portalApp">
				<h2>${appConfig.symbolicName} - ${appConfig.version}</h2>
				
				<p>path: ${appContext.webContextPath}</p>
				
				<iframe src="${app.webPath}" style="width: ${app.width}; height: ${app.height};"></iframe>
			</div>	
		
		</c:forEach>
	
	</fieldset>

	<fieldset>
		<legend>Rendered Apps</legend>
	
		<c:forEach items="${renderedApps}" var="appEntry">
			<c:set var="app" value="${appEntry.key}" />
			<c:set var="appContent" value="${appEntry.value}" />
			<c:set var="appConfig" value="${app.config}" />
			<c:set var="appContext" value="${appConfig.context}" />
		
			<div id="${app.namespace}" class="portalApp">
				<h2>${appConfig.symbolicName} - ${appConfig.version}</h2>
				
				<p>path: ${appContext.webContextPath}</p>
				
				<div class="appContent">
					${appContent}
				</div>
			</div>	
		</c:forEach>
	
	</fieldset>

</body>
</html>