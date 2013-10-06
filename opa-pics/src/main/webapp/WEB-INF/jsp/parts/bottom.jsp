<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:url var="appClientScriptUrl" value="/resources/js/portalAppClient.js" />
<script type="text/javascript" src="${appClientScriptUrl}"></script>

<c:url var="findAllAlbumsJsonUrl" value="/album" />
<c:url var="createAlbumJsonUrl" value="/album" />
<c:url var="findAllPicturesOfAlbumJsonUrl" value="/album/{:albumId}/pictures" />
<c:url var="getPictureOfAlbumUrl" value="/album/{:albumId}/pictures/{:pictureId}" />
<c:url var="getImageUrl" value="/image/{:imageId}" />
<c:url var="rotatePictureUrl" value="/picture/{:pictureId}/rotate/{:angle}" />
<script type="text/javascript">
	var findAllAlbumsJsonUrl = '${findAllAlbumsJsonUrl}';
	var createAlbumJsonUrl = '${createAlbumJsonUrl}';
	var findAllPicturesOfAlbumJsonUrl = '${findAllPicturesOfAlbumJsonUrl}';
	var getImageUrl = '${getImageUrl}';
	var getPictureOfAlbumUrl = '${getPictureOfAlbumUrl}';
	var rotatePictureUrl = '${rotatePictureUrl}';
</script>

<!-- Mby-utils.js -->
<script type="text/javascript" src="resources/js/mby-utils-min.js"></script>

<!-- Jquery for Infinite-scroll -->
<script type="text/javascript" src="http://code.jquery.com/jquery-1.10.2.js"></script>
	
<!-- AngularJs -->
<script data-require="angular.js@1.0.x" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.8/angular.min.js" data-semver="1.0.8"></script>

<!-- Infinite-scroll -->
<script type="text/javascript" src="resources/js/ng-infinite-scroll.min.js"></script>


<script type="text/javascript" src="resources/js/picsApp.js"></script>
