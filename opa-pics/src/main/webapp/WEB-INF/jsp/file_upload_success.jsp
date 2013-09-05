<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
    <title>Pics Uploaded</title>
</head>
<body>
    <h1>Pics successfully uploaded</h1>
    <p>Following pictures were upload successfully.</p>
    <ol>
        <c:forEach items="${picturesNames}" var="pictureName">
            <li>${pictureName}</li>
        </c:forEach>
    </ol>
    
    <spring:url value="/" var="backUrl" />
	<a href="${backUrl}">Back to index</a>
</body>
</html>