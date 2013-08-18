<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<fieldset>
	<legend>Apps to render (in iframe)</legend>

	<c:forEach items="${appsToRender}" var="appValue">

		<tiles:insertAttribute name="appContainer">
			<tiles:putAttribute name="displayMode" value="iframed" />
			<tiles:putAttribute name="app" value="${appValue}" />
			<tiles:putAttribute name="appContent" value="" />
		</tiles:insertAttribute>

	</c:forEach>

</fieldset>

<fieldset>
	<legend>Rendered Apps (without iframe)</legend>

	<c:forEach items="${renderedApps}" var="appEntry">

		<tiles:insertAttribute name="appContainer">
			<tiles:putAttribute name="displayMode" value="rendered" />
			<tiles:putAttribute name="app" value="${appEntry.key}" />
			<tiles:putAttribute name="appContent" value="${appEntry.value}" />
		</tiles:insertAttribute>
		
	</c:forEach>

</fieldset>
