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
	
	<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
    <link href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet">
    
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
	<c:url var="rotatePictureUrl" value="/rotate/{:pictureId}/{:angle}" />
	<script type="text/javascript">
		var findAllAlbumsJsonUrl = '${findAllAlbumsJsonUrl}';
		var findAllPicturesOfAlbumJsonUrl = '${findAllPicturesOfAlbumJsonUrl}';
		var getImageUrl = '${getImageUrl}';
		var getPictureOfAlbumUrl = '${getPictureOfAlbumUrl}';
		var rotatePictureUrl = '${rotatePictureUrl}';
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
            <span data-ng-click="selectAlbum(album)">{{album.name}}</span>
            <span data-ng-click="deleteAlbum(album)">[x]</span>
          </span>
        </li>
      </ul>
    </div>
    <p>
      Stash width : 
      <input type="text" name="stashWidth" id="stashWidth" data-ng-model="stashWidth" /> px
      | Scale : 
      <input type="number" name="scale" id="scale" min="10" step="10" max="300" data-ng-model="scale" />
      % | Reechantillonating page size 
      <input type="number" name="scale" id="scale" min="10" step="10" max="1000" data-ng-model="reechantillonatingPageSize" />
      |
      <button type="button" class="btn btn-large btn-danger" data-ng-click="reechantillonateStash($event)">
        <i class="icon-time"></i><span>Reechantillonate</span>
      </button>
    </p>
    
    <h3>
      Pictures Stash ({{stash.size}}) ({{selectedPictures.length}})
      <button type="button" class="btn btn-large btn-primary"  data-ng-click="addRandomPicture()" data-ng-mousedown="startAddRandomPicture()" data-ng-mouseup="stopAddRandomPicture()">
        Add random picture
      </button>
    </h3>

    <h3>Stash keeping flux</h3>
    <div id="stash" data-infinite-scroll="nextPictures()" data-infinite-scroll-distance="2" data-infinite-scroll-immediate-check="false">
      <div>
		<div class="stashPicture" data-ng-repeat="picture in stash.pictures" 
            data-ng-class="{'selected' : picture.selected, 'removed' : picture.removed,
            'overlayed' : picture.overlayed,
            'rotatingLeft' : picture.rotatingLeft,'rotatingRight' : picture.rotatingRight}"
            data-ng-click="selectPicture($event, picture)" 
            style="height: {{picture.height}}px; width: {{picture.width}}px; font-size: {{scale/100}}em;">
          <div class="stashThumbnail" style="
              height: {{picture.height * hoverZoom}}px; 
              width: {{picture.width * hoverZoom}}px; 
              margin-left: {{-(hoverZoom - 1) * picture.width / 2}}px; 
              margin-top: {{-(hoverZoom - 1) * picture.height / 2}}px;">
            
            <%-- 
            <img src="http://lorempixel.com/{{picture.thumbnailWidth}}/{{picture.thumbnailHeight}}"
                class="img-rounded" />
            --%>
            <img data-ng-src="{{picture.thumbnailUrl}}" class="img-rounded" />
                
            <div class="overlay" data-ng-click="$event.stopPropagation();">
              <p>{{picture.waitingMsg}}</p>
              <i class="icon-spin icon-spinner icon-large"></i>
              <button type="button" class="btn btn-large btn-primary btn-action" data-ng-click="overlayAction($event, picture)">
                <i class="icon-action"></i>
              </button>
            </div>
            
            <div class="title" data-ng-click="$event.stopPropagation();">
              
            </div>
            
            <div class="buttonBar" data-ng-click="$event.stopPropagation();">
              <span class="btn-group">
                <button type="button" class="icon newShoot btn btn-mini btn-info" title="new shoot"
                    >
                  <i class="icon-camera icon-large"></i>
                </button>
                <button type="button" class="icon newSession btn btn-mini btn-primary" title="new session">
                  <i class="icon-folder-open icon-large"></i>
                </button>
              </span>
              <span class="btn-group" data-ng-style="font-size: {{scale/100}}em">
                <button type="button" class="icon rotateLeft btn btn-mini btn-warning" title="rotate left"
                    data-ng-click="rotatePictureLeft($event, picture)">
                  <i class="icon-rotate-left icon-large"></i>
                </button>
                <button type="button" class="icon rotateRight btn btn-mini btn-warning" title="rotate right"
                    data-ng-click="rotatePictureRight($event, picture)">
                  <i class="icon-rotate-right icon-large"></i>
                </button>
              </span>
              <span class="btn-group" data-ng-style="font-size: {{scale/100}}em">
                <button type="button" class="icon tags btn btn-mini btn-success" title="add tags">
                  <i class="icon-tags icon-large"></i>
                </button>
              </span>
              <span class="btn-group" data-ng-style="font-size: {{scale/100}}em">
                <button type="button" class="icon remove btn btn-mini btn-danger" title="remove"
                    data-ng-click="removePicture($event, picture)">
                  <i class="icon-remove-sign icon-large"></i>
                </button>
              </span>
            </div>
          </div>
        </div>
        
      </div>
      <div class="clearfix"></div>
    </div>
</body>
</html>