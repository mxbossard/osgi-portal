<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="stash" data-infinite-scroll="nextPictures()" data-infinite-scroll-distance="1" data-infinite-scroll-immediate-check="false">
	<div>
      
		<div class="stashPicture" data-ng-repeat="picture in stash.pictures" 
            data-ng-class="{'selected' : picture.selected, 'removed' : picture.removed,
            'overlayed' : picture.overlayed,
            'rotatingLeft' : picture.rotatingLeft,'rotatingRight' : picture.rotatingRight}"
            data-ng-click="selectPicture($event, picture)" 
            style="height: {{picture.initialHeight}}px; width: {{picture.initialWidth}}px; font-size: {{scale/100}}em;">
          <div class="stashThumbnail" style="height: {{picture.initialHeight}}px; width: {{picture.initialWidth}}px;">
            
            <%-- 
            <img src="http://lorempixel.com/{{picture.thumbnailWidth}}/{{picture.thumbnailHeight}}"
                class="img-rounded" />
            --%>
            <img data-ng-src="{{picture.thumbnailUrl}}" width="{{picture.displayWidth}}" height="{{picture.displayHeight}}" 
            	style="height: {{picture.displayHeight}}px; width: {{picture.displayWidth}}px;"
            	class="img-rounded" title="{{picture.name}}" data-ng-show="picture.show" />
                
            <div class="overlay" data-ng-click="$event.stopPropagation();">
              <p>{{picture.waitingMsg}}</p>
              <i class="icon-spin icon-spinner icon-large"></i>
              <button type="button" class="btn btn-large btn-primary btn-action" data-ng-click="overlayAction($event, picture)">
                <i class="icon-action"></i>
              </button>
            </div>

            <div class="buttonBar" data-ng-click="$event.stopPropagation();">
              <span class="btn-group">
                <button type="button" class="icon newShoot btn btn-mini btn-info" title="new shoot"
                    >
                  <i class="icon-camera icon-large"></i>
                </button>
                <button type="button" class="icon newSession btn btn-primary" title="new session">
                  <i class="icon-folder-open icon-large"></i>
                </button>
              </span>
              <span class="btn-group">
                <button type="button" class="icon rotateLeft btn btn-warning" title="rotate left"
                    data-ng-click="rotatePictureLeft($event, picture)">
                  <i class="icon-rotate-left icon-large"></i>
                </button>
                <button type="button" class="icon rotateRight btn btn-warning" title="rotate right"
                    data-ng-click="rotatePictureRight($event, picture)">
                  <i class="icon-rotate-right icon-large"></i>
                </button>
              </span>
              <span class="btn-group">
                <button type="button" class="icon tags btn btn-success" title="add tags">
                  <i class="icon-tags icon-large"></i>
                </button>
                <button type="button" class="icon tags btn btn-info" title="settings">
                  <i class="icon-wrench icon-large"></i>
                </button>
              </span>
              <span class="btn-group">
                <button type="button" class="icon remove btn btn-danger" title="remove"
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
