<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<c:set var="appConfig" value="${app.config}" />
<c:set var="appContext" value="${appConfig.context}" />

<div id="${app.namespace}" class="portalApp">
	<h2>${appConfig.symbolicName} - ${appConfig.version}</h2>
	
	<p>path: ${appContext.webContextPath}</p>
	
	<c:if test="${'iframed' eq displayMode}">
		<tiles:insertAttribute name="iframedContent" />
	</c:if>
	
	<c:if test="${'rendered' eq displayMode}">
		<tiles:insertAttribute name="renderedContent" />
	</c:if>
	
</div>	