<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="module-album" class="" data-ng-controller="AlbumCtrl" data-ng-init="init()">
	<h3>Albums</h3>

	<div class="">
		<ul class="list-unstyled list-inline">
			<li data-ng-repeat="album in albums" data-ng-class="{'selected': album.id == selectedAlbum.id}">
				<span>
					<span data-ng-click="selectAlbum(album)">{{album.label}}</span>
					<span data-ng-click="deleteAlbum(album)">[x]</span>
				</span>
			</li>
		</ul>
	</div>
	
	<div class="row">
		<div class="col-lg-3 col-md-3 col-sm-4 col-xs-10">
			<div class="input-group">
				<input type="text" class="span2 form-control" name="newAlbumName" id="newAlbumName" data-ng-model="newAlbumName" 
						placeholder="Add new Album" pattern="[A-Za-z0-9 _./\-]+" required />
				<span class="input-group-btn">
					<button class="btn btn-primary" type="button" data-ng-click="createAlbum($event);$event.stopPropagation();">
						<span class="glyphicon glyphicon-plus-sign"></span><span> Add</span>
					</button>
				</span>
			</div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-4 col-xs-10" data-ng-show="selectedAlbum">
			<div class="input-group">
				<input type="text" class="span2 form-control" name="updateAlbumName" id="updateAlbumName" data-ng-model="selectedAlbum.name" 
						placeholder="Update Album name" pattern="[A-Za-z0-9 _./\-]+" required />
				<span class="input-group-btn">
					<button class="btn btn-warning" type="button" data-ng-click="updateSelectedAlbum($event);$event.stopPropagation();">
						<span class="glyphicon glyphicon-save"></span><span> Save</span>
					</button>
				</span>
			</div>
		</div>
	</div>
</div>