<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ taglib prefix="opa" uri="http://www.mby.fr/osgi-portal/tags/opa"%>

<!doctype html>
<html lang="fr" data-ng-app="pics">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Pics</title>
	
	<script type="text/javascript" src="http://code.jquery.com/jquery-1.10.2.js"></script>
	<script type="text/javascript" src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
	
	<script data-require="angular.js@1.0.x" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.8/angular.min.js" data-semver="1.0.8"></script>
	
	<script type="text/javascript" src="resources/js/ng-infinite-scroll.min.js"></script>
	
	<opa:meta/>
	
	<c:url var="appClientScriptUrl" value="/resources/js/portalAppClient.js" />
	<script type="text/javascript" src="${appClientScriptUrl}"></script>
	
	<c:url var="picsAppScriptUrl" value="/resources/js/picsApp.js" />
	<script type="text/javascript" src="${picsAppScriptUrl}"></script>
	
	<c:url var="picsAppCssUrl" value="/resources/css/picsApp.css" />
	<link rel="stylesheet" href="${picsAppCssUrl}" />

	<c:url var="findAllAlbumsJsonUrl" value="/album" />
	<c:url var="findAllPicturesOfAlbumJsonUrl" value="/album/{:albumId}/pictures" />
	<c:url var="getImageUrl" value="/image/{:imageId}" />
	<c:url var="getPictureOfAlbumUrl" value="/album/{:albumId}/pictures/{:pictureId}" />
	<script type="text/javascript">
		var findAllAlbumsJsonUrl = '${findAllAlbumsJsonUrl}';
		var findAllPicturesOfAlbumJsonUrl = '${findAllPicturesOfAlbumJsonUrl}';
		var getImageUrl = '${getImageUrl}';
		var getPictureOfAlbumUrl = '${getPictureOfAlbumUrl}';
	</script>

</head>
<body data-ng-controller="PicsCtrl">
	<h2>Pics</h2>	
		
	<spring:url value="/upload" var="uploadFormUrl" />
	<a href="${uploadFormUrl}">Upload pics form</a>
	
	<h3>Albums</h3>
    <div id="albums">
      	<ul>
        	<li data-ng-repeat="album in albums" data-ng-class="{'selected': album.id == selectedAlbum.id}">
          		<span>
            		<span data-ng-click="selectAlbum(album)">{{album.id}} - {{album.name}} ({{album.size}})</span>
            		<span data-ng-click="deleteAlbum(album)">[x]</span>
          		</span>
        	</li>
      	</ul>
    </div>
    
    <h3>Thumbnails</h3>
    <div id="thumbnails" infinite-scroll='loadMore()' infinite-scroll-distance='2'>
    	<div class="thumbnailsRow" data-ng-repeat="row in thumbnailRows" style="height: {{row.height}}px;">
			<span class="thumbnail" data-ng-repeat="picture in row.pictures" data-ng-click="selectPicture(picture)"
				style="width: {{picture.thumbnailWidth}}px; height: {{picture.thumbnailHeight}}px;">
				<img data-ng-src="{{picture.thumbnailUrl}}" width="{{picture.thumbnailWidth}}px" height="{{picture.thumbnailHeight}}px" />
			</span>
		</div>
    </div>

</body>
</html>