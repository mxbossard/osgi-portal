<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:useAttribute id="app" name="app" />

<iframe src="${app.webPath}" style="width: ${app.width}; height: ${app.height};"></iframe>