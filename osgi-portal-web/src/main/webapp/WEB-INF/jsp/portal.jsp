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

			<jsp:include page="app/displayApp.jsp">
				<jsp:param value="${app}" name="app" />
				<jsp:param value="iframed" name="displayMode" />
			</jsp:include>
		
		</c:forEach>
	
	</fieldset>

	<fieldset>
		<legend>Rendered Apps</legend>
	
		<c:forEach items="${renderedApps}" var="appEntry">
			<c:set var="app" value="${appEntry.key}" />
			<c:set var="appContent" value="${appEntry.value}" />
		
			<jsp:include page="app/displayApp.jsp">
				<jsp:param value="${app}" name="app" />
				<jsp:param value="rendered" name="displayMode" />
				<jsp:param value="${appContent}" name="appContent" />
			</jsp:include>
			
		</c:forEach>
	
	</fieldset>

</body>
</html>