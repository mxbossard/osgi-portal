<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:useAttribute id="app" name="app" />
<tiles:useAttribute id="appContent" name="appContent" />

<div id="${app.namespace}" class="portalApp" style="width: ${app.width}; height: ${app.height};">
	<div class="title">${app.title} (mode: ${app.config.renderingMode})</div>

	<c:if test="${'IFRAMED' == app.config.renderingMode}">
		<tiles:insertAttribute name="iframedContent">
			<tiles:putAttribute name="app" value="${app}" />
		</tiles:insertAttribute>
	</c:if>
	
	<c:if test="${'RENDERED' == app.config.renderingMode}">
		<tiles:insertAttribute name="renderedContent">
			<tiles:putAttribute name="app" value="${app}" />
			<tiles:putAttribute name="appContent" value="${appContent}" />
		</tiles:insertAttribute>
	</c:if>
	
</div>	