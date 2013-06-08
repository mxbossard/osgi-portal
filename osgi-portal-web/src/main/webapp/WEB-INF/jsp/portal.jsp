<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>OSGi Portal</title>
</head>
<body>
	<h1>OSGi Portal</h1>
	
	<c:forEach items="${appsToRender}" var="app">
		<c:set var="appConfig" value="${app.config}"  />
		<c:set var="appContext" value="${appConfig.context}" />
		
		<div id="${app.namespace}" style="float: left; width: ${app.width}; height: ${app.height}; border: 1px dashed green; margin: 5px;">
			<h2>${appConfig.symbolicName} - ${appConfig.version}</h2>
			<p>path: ${appContext.webContextPath}</p>
			<iframe src="${appContext.webContextPath}"></iframe>
		</div>
	</c:forEach>
	
</body>
</html>