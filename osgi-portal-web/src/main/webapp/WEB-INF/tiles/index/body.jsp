<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<fieldset>
	<legend>Apps to render (in iframe)</legend>

	<c:forEach items="${appsToRender}" var="app">

		<tiles:insertAttribute name="appContainer">
			<tiles:putAttribute name="displayMode" value="iframed" />
		</tiles:insertAttribute>

	</c:forEach>

</fieldset>

<fieldset>
	<legend>Rendered Apps (without iframe)</legend>

	<c:forEach items="${renderedApps}" var="appEntry">
		<c:set var="app" value="${appEntry.key}" />
		<c:set var="appContent" value="${appEntry.value}" />
	
		<tiles:insertAttribute name="appContainer">
			<tiles:putAttribute name="displayMode" value="rendered" />
		</tiles:insertAttribute>
		
	</c:forEach>

</fieldset>
