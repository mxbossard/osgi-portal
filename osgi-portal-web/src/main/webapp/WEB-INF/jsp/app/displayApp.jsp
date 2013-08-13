<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- Params passed with jsp inclusion --%>
<c:set var="displayMode" value="${param.displayMode}" />
<c:set var="app" value="${param.app}" />
<c:set var="appContent" value="${param.appContent}" />

<c:set var="appConfig" value="${app.config}" />
<c:set var="appContext" value="${appConfig.context}" />

<div id="${app.namespace}" class="portalApp">
	<h2>${appConfig.symbolicName} - ${appConfig.version}</h2>
	
	<p>path: ${appContext.webContextPath}</p>
	
	<c:if test="${'iframed' eq displayMode}">
		<jsp:include page="iframedContent.jsp">
			<jsp:param value="${app}" name="app" />
		</jsp:include>
	</c:if>
	
	<c:if test="${'rendered' eq displayMode}">
		<jsp:include page="renderedContent.jsp">
			<jsp:param value="${appContent}" name="appContent" />
		</jsp:include>
	</c:if>
	
</div>	