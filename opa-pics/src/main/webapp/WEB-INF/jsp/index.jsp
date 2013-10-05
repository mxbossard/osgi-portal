<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ taglib prefix="opa" uri="http://www.mby.fr/osgi-portal/tags/opa"%>

<!doctype html>
<html lang="fr" data-ng-app="pics">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Pics</title>
	
	<opa:meta/>

	<jsp:include page="parts/head.jsp" />
	
</head>
<!-- data-ng-controller="PicsCtrl" -->
<body>
	<h2>Pics</h2>	
		
	<spring:url value="/upload" var="uploadFormUrl" />
	<a href="${uploadFormUrl}">Upload pics form</a>
	
    <h3>Albums</h3>
	<jsp:include page="parts/album.jsp" />

	<jsp:include page="parts/stash.jsp" />

	<jsp:include page="parts/bottom.jsp" />
	
</body>
</html>