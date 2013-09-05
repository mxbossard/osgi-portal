<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!doctype html>
<html lang="fr">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Pics</title>
	
	<script type="text/javascript">
		var osgiPortal = window.parent.OsgiPortal.getInstance();
		window.appClient = osgiPortal.registerPortalApplication("${app.signature}");
	</script>
	
	<c:url var="appClientScriptUrl" value="/resources/js/portalAppClient.js" />
	
	<script type="text/javascript" src="${appClientScriptUrl}"></script>
</head>
<body>
	<h2>Pics</h2>	
	
	<spring:url value="/upload" var="uploadFormUrl" />
	<a href="${uploadFormUrl}">Upload pics form</a>
	
	<div>
		Uploaded pics (thumbnails) :
		<c:forEach items="${pictures}" var="picture">
			<spring:url value="/thumbnail/${picture.id}" var="picUrl" />
			<img src="${picUrl}" />
		</c:forEach>
	</div>
	
</body>
</html>