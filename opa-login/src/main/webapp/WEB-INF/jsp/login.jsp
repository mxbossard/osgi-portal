<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!doctype html>
<html lang="fr">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
	
	<script type="text/javascript">
		var osgiPortal = window.parent.OsgiPortal.getInstance();
		window.appClient = osgiPortal.registerPortalApplication("${app.signature}");
	</script>
	
	<c:url var="appClientScriptUrl" value="/resources/js/portalAppClient.js" />
	
	<script type="text/javascript" src="${appClientScriptUrl}"></script>
	
	<style type="text/css">
		.error {
			color: #ff0000;
		}
		 
		.errorblock {
			color: #000;
			background-color: #ffEEEE;
			border: 3px solid #ff0000;
			padding: 8px;
			margin: 16px;
		}
	</style>
</head>
<body>
	<h2>Login</h2>
	
	<form:form method="POST" commandName="loginForm" action="login">
		<form:errors path="*" cssClass="errorblock" element="div" />
		<table>
			<tr>
				<td>username :</td>
				<td><form:input path="username" /></td>
				<td><form:errors path="username" cssClass="error" /></td>
			</tr>
			<tr>
				<td>password :</td>
				<td><form:input path="password" /></td>
				<td><form:errors path="password" cssClass="error" /></td>
			</tr>
			<tr>
				<td colspan="3"><input type="submit" /></td>
			</tr>
		</table>
	</form:form>

</body>
</html>