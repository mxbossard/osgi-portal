<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:useAttribute id="app" name="app" />
<tiles:useAttribute id="appContent" name="appContent" />

<div class="appContent">
	${appContent}
</div>