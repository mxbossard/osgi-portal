<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!doctype html>
<html lang="fr">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
	
	<script type="text/javascript">
		var osgiPortal = window.parent.OsgiPortal.getInstance();
		window.appClient = osgiPortal.registerPortalApplication("${app.signature}");
	</script>
	
	<script type="text/javascript" src="<c:url value="/resources/js/portalAppClient.js" />"></script>
</head>
<body>
	<h1>Message : ${message}</h1>	
	<p>userActionDispatcherCount : ${userActionDispatcherCount}</p>
</body>
</html>