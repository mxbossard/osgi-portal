var app = angular.module('pics', ['infinite-scroll']);

app.controller('PicsCtrl', function($scope, $http) {
	'use strict';

	$scope.albums = $scope.albums || [];
	$scope.lastSinceTime = 0;
	$scope.scale = 100;
	$scope.searchPicturesLock = false;

	$scope.nextPictures = function() {
		getNextPictures($scope);
	};

	$scope.selectAlbum = function(album) {
		$scope.selectedAlbum = album;

		$scope.stash = initPictureStash($scope, 1);

		getNextPictures($scope);

	};

	$scope.deleteAlbum = function(album) {
		var confirm = window.confirm("Are you sure you want to delete album: " + album.name + " ?");
		if (confirm) {
			var index = $scope.albums.indexOf(album);
			$scope.albums.splice(index, 1);
		}

		if ($scope.selectedAlbum == album) {
			$scope.thumbnailRows = null;
		}

	};

	$scope.changeStashScale = function(scale) {
		// Reset stash
		$scope.stash = initPictureStash($scope, scale / 100);

		// Begin to load the stash
		getNextPictures($scope);

	};

	$scope.selectPicture = function(picture) {

	};

	// On page init
	$http.get(findAllAlbumsJsonUrl).success(function(data, status) {
		$scope.albums = data;
	});

	function getNextPictures($scope) {
		if (!$scope.searchPicturesLock) {
			// Pseudo synchronization
			$scope.searchPicturesLock = true;

			var lastSinceTime = $scope.lastSinceTime;
			var album = $scope.selectedAlbum;
			if (album) {
				var url = findAllPicturesOfAlbumJsonUrl.replace(/{:albumId}/, album.id) + '&since=' + lastSinceTime;

				$http.get(url).success(function(data, status) {
					if (data && data.length > 0) {
						$scope.lastSinceTime = data[data.length - 1].originalTime;

						angular.forEach(data, function(value, key) {
							$scope.stash.addPicture(value);
						});
					}

					$scope.searchPicturesLock = false;
				}).error(function(data, status, headers, config) {
					$scope.searchPicturesLock = false;
				});
			}
		}
	}

});

function initPictureStash($scope) {
	'use strict';

	$scope.lastSinceTime = 0;

	var stash = $scope.stash = {
		rows : [],
		size : 0,
		width : document.getElementById("thumbnails").offsetWidth,
		margin : 1,
		scale : $scope.scale ? $scope.scale / 100 : 1,
		getLastRow : function() {
			if (this.rows.length < 1) {
				this.addNewRow();
			}
			return this.rows[this.rows.length - 1];
		},
		addNewRow : function() {
			var newRow = {
				pictures : [],
				height : 0,
				width : 0,
				addPicture : function(picture) {
					var nbPic = this.pictures.length || 0;
					var hypotheticWidth = nbPic * stash.margin + this.width + picture.thumbnailWidth;
					if (hypotheticWidth > stash.width) {
						return false;
					}

					this.pictures.push(picture);
					this.height = Math.max(this.height, picture.thumbnailHeight);
					this.width += picture.thumbnailWidth;

					// Build thumbnail URL
					var thumbnailUrl = getImageUrl.replace(/{:imageId}/, picture.thumbnailId);
					picture.thumbnailUrl = thumbnailUrl;

					// Build image URL
					var imageUrl = getImageUrl.replace(/{:imageId}/, picture.imageId);
					picture.imageUrl = imageUrl;

					return true;
				},
				finalizeRow : function() {
					var growthRatio = (stash.width - stash.margin * this.pictures.length) / this.width;
					this.height = Math.round(this.height * growthRatio);
					var offset = 0;
					angular.forEach(this.pictures, function(value, key) {
						// Enlarge thumbnails of row to occupy all the width
						var exactWidth = value.thumbnailWidth * growthRatio + offset;
						var roundedWidth = Math.round(exactWidth);

						// offset due to rounding
						offset = exactWidth - roundedWidth;

						value.thumbnailWidth = roundedWidth;
						value.thumbnailHeight = this.height;
					}, this);
				}
			};
			this.rows.push(newRow);
			return newRow;
		},
		addPicture : function(picture) {
			// Rescale picture
			picture.thumbnailWidth = Math.round(picture.thumbnailWidth * this.scale);
			picture.thumbnailHeight = Math.round(picture.thumbnailHeight * this.scale);

			var lastRow = stash.getLastRow();
			var enoughSpace = lastRow.addPicture(picture);
			if (!enoughSpace) {
				// Not enough space in row for the picture
				lastRow.finalizeRow();

				var newRow = this.addNewRow();
				newRow.addPicture(picture);
			}

		}
	};

	return stash;
}

function organizeThumbnails(width, thumbnailsList, thumbnailRows) {
	'use strict';

	var margin = 1;

	var currentRowIndex = thumbnailRows.length;
	thumbnailRows[currentRowIndex] = {};
	var currentRow = thumbnailRows[currentRowIndex];
	currentRow.pictures = [];

	var currentRowWidth = 0;
	var currentRowMargin = 0;

	angular.forEach(thumbnailsList, function(value, key) {
		// Build thumbnail URL
		var thumbnailUrl = getImageUrl.replace(/{:imageId}/, value.thumbnailId);
		value.thumbnailUrl = thumbnailUrl;

		// Build image URL
		var imageUrl = getImageUrl.replace(/{:imageId}/, value.imageId);
		value.imageUrl = imageUrl;

		var thumbnailWidth = value.thumbnailWidth;

		if (currentRowWidth + currentRowMargin + thumbnailWidth > width) {
			// thumbnail overflow from row

			// Finalize the row
			var growthRatio = (width - currentRowMargin) / currentRowWidth;
			var offset = 0;
			angular.forEach(currentRow.pictures, function(value, key) {
				// Enlarge thumbnails of row to occupy all the width
				var exactWidth = value.thumbnailWidth * growthRatio + offset;
				var roundedWidth = Math.round(exactWidth);
				offset = exactWidth - roundedWidth;
				value.thumbnailWidth = roundedWidth;

				value.thumbnailHeight = Math.round(value.thumbnailHeight * growthRatio);
				currentRow.height = Math.max(currentRow.height, value.thumbnailHeight);
			});

			// Initialize a new row
			currentRowIndex++;
			thumbnailRows[currentRowIndex] = {};
			currentRow = thumbnailRows[currentRowIndex];
			currentRow.pictures = [];

			currentRowWidth = 0;
			currentRowMargin = 0;
		}

		// add thumbnail in the row
		currentRow.pictures.push(value);
		currentRow.height = value.thumbnailHeight;
		currentRowWidth += thumbnailWidth;
		currentRowMargin += margin;
	});

	return thumbnailRows;
}
