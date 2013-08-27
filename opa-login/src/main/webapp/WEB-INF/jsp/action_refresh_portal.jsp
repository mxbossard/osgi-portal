<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<script type="text/javascript">
		var osgiPortal = window.parent.OsgiPortal.getInstance();
		window.appClient = osgiPortal.loadPortalApplication("${app.signature}");
		
		appClient.doAction('refresh_portal');
	</script>
	
</head>
<body>
	<h1>Please wait ...</h1>
</body>
</html>