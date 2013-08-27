<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<fieldset>
	<legend>Apps</legend>

	<c:forEach items="${appsToDisplay}" var="appEntry">

		<tiles:insertAttribute name="appContainer">
			<tiles:putAttribute name="app" value="${appEntry.key}" />
			<tiles:putAttribute name="appContent" value="${appEntry.value}" />
		</tiles:insertAttribute>
		
	</c:forEach>
</fieldset>
