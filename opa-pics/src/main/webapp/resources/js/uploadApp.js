/*
 * jQuery File Upload Plugin Angular JS Example 1.2.1 https://github.com/blueimp/jQuery-File-Upload Copyright 2013,
 * Sebastian Tschan https://blueimp.net Licensed under the MIT license: http://www.opensource.org/licenses/MIT
 */

/* jslint nomen: true, regexp: true */
/* global window, angular */

(function() {
	'use strict';

	var isOnGitHub = false, url = jqueryUploadActionUrl;

	window.app = window.app
			|| angular.module('pics', ['blueimp.fileupload']).config(
					[
							'$httpProvider',
							'fileUploadProvider',
							function($httpProvider, fileUploadProvider) {
								delete $httpProvider.defaults.headers.common['X-Requested-With'];
								fileUploadProvider.defaults.redirect = window.location.href.replace(/\/[^\/]*$/,
										'/cors/result.html?%s');

								var disableSlowLoading = true;
								// disableImageMetaDataLoad : disableSlowLoading,

								// Demo settings:
								angular.extend(fileUploadProvider.defaults, {
									// Enable image resizing, except for Android and Opera,
									// which actually support image resizing, but fail to
									// send Blob objects via XHR requests:
									disableImageResize : true,
									maxFileSize : false,
									acceptFileTypes : /(\.|\/)(gif|jpe?g|png|zip)$/i,
									limitConcurrentUploads : 3,
									maxNumberOfFiles : 50,

									disableImagePreview : disableSlowLoading,
									disableImageMetaDataSave : disableSlowLoading,
									disableImageLoad : disableSlowLoading,
									disableExifThumbnail : disableSlowLoading,
									disableExifSub : disableSlowLoading,
									disableExifGps : disableSlowLoading,
									loadImageMaxFileSize : 0
								});

							}]);

	window.app.controller('PicsFileUploadController', ['$scope', '$http', 'PicsService',
			function($scope, $http, PicsService) {
				$scope.selectedAlbum = null;
				$scope.uploadFileUrl = null;
				$scope.options = {
					url : null
				};

				/*
				 * if (!isOnGitHub) { $scope.loadingFiles = true; $http.get(url).then(function(response) {
				 * $scope.loadingFiles = false; $scope.queue = response.data.files || []; }, function() {
				 * $scope.loadingFiles = false; }); }
				 */

				// Register album change callback
				PicsService.onAlbumSelection(function(album) {
					$scope.selectedAlbum = album;

					var ulpoadUrl = url.replace(/{:albumId}/, album.id);
					$scope.options.url = ulpoadUrl;
					$scope.uploadFileUrl = ulpoadUrl;
				});
			}]);

	window.app.controller('FileDestroyController', ['$scope', '$http', function($scope, $http) {
		var file = $scope.file, state;
		if (file.url) {
			file.$state = function() {
				return state;
			};
			file.$destroy = function() {
				state = 'pending';
				return $http({
					url : file.deleteUrl,
					method : file.deleteType
				}).then(function() {
					state = 'resolved';
					$scope.clear(file);
				}, function() {
					state = 'rejected';
				});
			};
		} else if (!file.$cancel && !file._index) {
			file.$cancel = function() {
				$scope.clear(file);
			};
		}
	}]);

}());