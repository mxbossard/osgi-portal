<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="module-stash" data-ng-controller="StashCtrl" data-ng-init="init()">

	<div id="stash-toolbar">
		<h3>
		  	Stash 
		  	<span data-ng-show="stash && stash.size > 0">({{stash.size}})</span> 
		  	<span data-ng-show="stash._selectedPictures && stash._selectedPictures.length > 0">({{stash._selectedPictures.length}})</span>
		</h3>
		
		<div class="row">
			<div class="col-lg-2 col-md-3 col-sm-4 col-xs-6">
				<div class="input-group">
					<span class="input-group-addon">Width</span>
					<input type="text" class="form-control" name="stashWidth" id="stashWidth" data-ng-model="stashWidth" />
					<span class="input-group-addon">px</span>
				</div>
			</div>
			<div class="col-lg-2 col-md-3 col-sm-4 col-xs-6">
				<div class="input-group">
					<span class="input-group-addon">Scale</span>
					<input type="number" class="form-control" name="scale" id="scale" min="10" step="10" max="300" data-ng-model="scale" />
					<span class="input-group-addon">%</span>
				</div>
			</div>
			<div class="col-lg-3 col-md-4 col-sm-5 col-xs-8">
				<div class="input-group">
					<span class="input-group-addon">Reechantillonating page size</span>
					<input type="number" class="form-control" name="scale" id="scale" min="10" step="10" max="1000" data-ng-model="reechantillonatingPageSize"  />
				</div>
			</div>
			<div class="col-lg-1 col-md-2 col-sm-3 col-xs-4">
				<button type="button" class="btn btn-danger" data-ng-click="reechantillonateStash($event)">
			    	<span class="glyphicon glyphicon-time"></span><span> Reechantillonate</span>
			  	</button>
			</div>			
		</div>
	</div>
	
	<div id="stash-pictures" data-infinite-scroll="loadNextPictures()" data-infinite-scroll-distance="1" data-infinite-scroll-immediate-check="false">
		<div>
	      
			<div class="stashPicture" data-ng-repeat="picture in stash.pictures" 
	            data-ng-class="{'selected' : picture.selected, 'removed' : picture.removed,
	            'overlayed' : picture.overlayed,
	            'rotatingLeft' : picture.rotatingLeft,'rotatingRight' : picture.rotatingRight}"
	            data-ng-click="selectPicture($event, picture)" 
	            style="height: {{picture.initialHeight}}px; width: {{picture.initialWidth}}px; font-size: {{scale/100}}em;">
	          <div class="stashThumbnail" style="height: {{picture.initialHeight}}px; width: {{picture.initialWidth}}px;">

	            <img data-ng-src="{{picture.thumbnailUrl}}" width="{{picture.displayWidth}}" height="{{picture.displayHeight}}" 
	            	style="height: {{picture.displayHeight}}px; width: {{picture.displayWidth}}px;"
	            	class="img-rounded" title="{{picture.name}}" data-ng-show="picture.show" />
	                
	            <div class="overlay" data-ng-click="$event.stopPropagation();">
	              <p>{{picture.waitingMsg}}</p>
	              <span class="glyphicon glyphicon-spin"></span>
	              <i class="icon-spin icon-spinner icon-large"></i>
	              <button type="button" class="btn btn-lg btn-primary btn-action" data-ng-click="overlayAction($event, picture)">
	                <span class="glyphicon icon-action"></span>
	              </button>
	            </div>
	
	            <div class="buttonBar" data-ng-click="$event.stopPropagation();">
	              <span class="btn-group">
	                <button type="button" class="icon newShoot btn btn-lg btn-mini btn-info" title="new shoot">
	                  <span class="glyphicon glyphicon-camera"></span>
	                </button>
	                <button type="button" class="icon newSession btn btn-lg btn-primary" title="new session">
	                  <span class="glyphicon glyphicon-folder-open"></span>
	                </button>
	              </span>
	              <span class="btn-group">
	                <button type="button" class="icon rotateLeft btn btn-lg btn-warning" title="rotate left"
	                    data-ng-click="rotatePictureLeft($event, picture)">
	                  <span class="glyphicon glyphicon-rotate-left"></span>
	                </button>
	                <button type="button" class="icon rotateRight btn btn-lg btn-warning" title="rotate right"
	                    data-ng-click="rotatePictureRight($event, picture)">
	                  <span class="glyphicon glyphicon-rotate-right"></span>
	                </button>
	              </span>
	              <span class="btn-group">
	                <button type="button" class="icon tags btn btn-lg btn-success" title="add tags">
	                  <span class="glyphicon glyphicon-tags"></span>
	                </button>
	                <button type="button" class="icon tags btn btn-lg btn-info" title="settings">
	                  <span class="glyphicon glyphicon-wrench"></span>
	                </button>
	              </span>
	              <span class="btn-group">
	                <button type="button" class="icon remove btn btn-lg btn-danger" title="remove"
	                    data-ng-click="removePicture($event, picture)">
	                  <span class="glyphicon glyphicon-remove-sign"></span>
	                </button>
	              </span>
	            </div>
	            
	          </div>
	        </div>
	        
	      </div>
	      
		<div class="clearfix"></div>
	</div>

</div>