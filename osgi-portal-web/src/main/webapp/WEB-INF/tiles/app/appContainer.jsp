<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<c:set var="appConfig" value="${app.config}" />
<c:set var="appContext" value="${appConfig.context}" />

<tiles:useAttribute id="displayMode" name="displayMode" />
<tiles:useAttribute id="app" name="app" />
<tiles:useAttribute id="appContent" name="appContent" />

<div id="${app.namespace}" class="portalApp">
	<div class="title">${app.title}</div>

	<c:if test="${'iframed' eq displayMode}">
		<tiles:insertAttribute name="iframedContent">
			<tiles:putAttribute name="app" value="${app}" />
		</tiles:insertAttribute>
	</c:if>
	
	<c:if test="${'rendered' eq displayMode}">
		<tiles:insertAttribute name="renderedContent">
			<tiles:putAttribute name="app" value="${app}" />
			<tiles:putAttribute name="appContent" value="${appContent}" />
		</tiles:insertAttribute>
	</c:if>
	
</div>	