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
	
	<opa:meta/>

	<jsp:include page="parts/head.jsp" />
	
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
            <span data-ng-click="selectAlbum(album)">{{album.name}} ({{album.size}})</span>
            <span data-ng-click="deleteAlbum(album)">[x]</span>
          </span>
        </li>
      </ul>
      
      <div class="input-append">
	      <input type="text" class="span2" name="newAlbumName" id="newAlbumName" data-ng-model="newAlbumName" 
	      		placeholder="Add new Album" pattern="[A-Za-z0-9 _./\-]+" required />
	      <button class="btn" type="button" data-ng-click="createAlbum();$event.stopPropagation();">Add</button>
      </div>
    </div>
    <p>
      Stash width : 
      <input type="text" class="input-small" name="stashWidth" id="stashWidth" data-ng-model="stashWidth" /> px
      | Scale : 
      <input type="number" class="input-small" name="scale" id="scale" min="10" step="10" max="300" data-ng-model="scale" />
      % | Reechantillonating page size 
      <input type="number" class="input-small" name="scale" id="scale" min="10" step="10" max="1000" 
      		data-ng-model="reechantillonatingPageSize" />
      |
      <button type="button" class="btn btn-danger" data-ng-click="reechantillonateStash($event)">
        <i class="icon-time"></i><span> Reechantillonate</span>
      </button>
    </p>
    
    <h3>
      Pictures Stash ({{stash.size}}) ({{selectedPictures.length}})
      <button type="button" class="btn btn-primary"  data-ng-click="addRandomPicture()" data-ng-mousedown="startAddRandomPicture()" data-ng-mouseup="stopAddRandomPicture()">
        Add random picture
      </button>
    </h3>

	<jsp:include page="parts/stash.jsp" />

	<jsp:include page="parts/bottom.jsp" />
	
</body>
</html>