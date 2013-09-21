var app = angular.module('pics', ['infinite-scroll']);

app.controller('PicsCtrl', function($scope, $http) {
	'use strict';

	$scope.albums = $scope.albums || [];

	$http.get(findAllAlbumsJsonUrl).success(function(data, status) {
		$scope.albums = data;
	});

	function getNextPictures($scope) {
		var album = $scope.selectedAlbum;
		var url = findAllPicturesOfAlbumJsonUrl.replace(/{:albumId}/, album.id) + '&since=' + $scope.lastSinceTime;

		$http.get(url).success(function(data, status) {
			if (data && data.length > 0) {
				$scope.pictures = data;
				$scope.lastSinceTime = data[data.length - 1].originalTime;

				var thumbnailsRowWidth = document.getElementById("thumbnails").offsetWidth;
				organizeThumbnails(thumbnailsRowWidth, data, $scope.thumbnailRows);
			}
		});
	}

	$scope.loadMore = function() {
		getNextPictures($scope);
	};

	$scope.selectAlbum = function(album) {
		$scope.selectedAlbum = album;

		$scope.lastSinceTime = 0;
		$scope.thumbnailRows = [];

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

	$scope.selectPicture = function(picture) {

	};

});

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
