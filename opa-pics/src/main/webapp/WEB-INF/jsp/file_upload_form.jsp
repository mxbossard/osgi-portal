<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
    <title>Pics Upload</title>

	<script type="text/javascript" src="http://code.jquery.com/jquery-1.10.2.js"></script>

	<script type="text/javascript">
	$(document).ready(function() {

	});
	</script>
</head>
<body>
	<h1>Pics Upload</h1>
	 
	 <spring:url value="upload/save" var="uploadActionUrl" />
	 
	<form:form method="post" action="${uploadActionUrl}"
	        modelAttribute="uploadForm" enctype="multipart/form-data">
	 
	    <p>Select pictures to upload. Press Add button to add more file inputs.</p>
	 
	    <input name="pictures" type="file" multiple="multiple" />
	    <br/>
	    <input type="submit" value="Upload" />
	</form:form>
	
	<spring:url value="/" var="backUrl" />
	<a href="${backUrl}">Back to index</a>
</body>
</html>