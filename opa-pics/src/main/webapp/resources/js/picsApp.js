var app = angular.module('pics', []);

app.controller('PicsCtrl', function($scope, $http) {
	$http.get(findAllAlbumsJsonUrl).success(function(data, status) {
		$scope.albums = data;
	});

	$scope.selectAlbum = function(album) {
		$scope.selectedAlbum = album;

		var url = findAllPicturesOfAlbumJsonUrl.replace(/{:albumId}/, album.id);

		$http.get(url).success(function(data, status) {

			/*
			 * angular.forEach(data, function(value, key) { var thumbnailUrl = getImageUrl.replace(/{:imageId}/,
			 * value.thumbnailId); value.thumbnailUrl = thumbnailUrl; var imageUrl = getImageUrl.replace(/{:imageId}/,
			 * value.imageId); value.imageUrl = imageUrl; });
			 */
			var thumbnailsRowWidth = document.getElementById("thumbnails").offsetWidth;
			$scope.thumbnailRows = organizeThumbnails(thumbnailsRowWidth, data);
		});

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

function organizeThumbnails(width, thumbnailsList) {
	var margin = 1;

	var thumbnailRows = [];

	var currentRowIndex = 0;
	thumbnailRows[currentRowIndex] = {};
	var currentRow = thumbnailRows[currentRowIndex];
	currentRow.pictures = [];

	var currentRowWidth = 0;

	angular.forEach(thumbnailsList, function(value, key) {
		// Build thumbnail URL
		var thumbnailUrl = getImageUrl.replace(/{:imageId}/, value.thumbnailId);
		value.thumbnailUrl = thumbnailUrl;

		// Build image URL
		var imageUrl = getImageUrl.replace(/{:imageId}/, value.imageId);
		value.imageUrl = imageUrl;

		var thumbnailWidth = value.thumbnailWidth;

		if (currentRowWidth + thumbnailWidth > width) {
			// thumbnail overflow from row

			// Finalize the row
			var growthRatio = width / currentRowWidth;
			angular.forEach(currentRow.pictures, function(value, key) {
				// Enlarge thumbnails of row to occupy all the width
				if (key % 2 == 0) {
					value.thumbnailWidth = Math.ceil(value.thumbnailWidth * growthRatio);
				} else {
					value.thumbnailWidth = Math.floor(value.thumbnailWidth * growthRatio);
				}
				value.thumbnailHeight = Math.round(value.thumbnailHeight * growthRatio);
				currentRow.height = Math.max(currentRow.height, value.thumbnailHeight);
			});

			// Initialize a new row
			currentRowIndex++;
			thumbnailRows[currentRowIndex] = {};
			currentRow = thumbnailRows[currentRowIndex];
			currentRow.pictures = [];

			currentRowWidth = 0;
		}

		// add thumbnail in the row
		currentRow.pictures.push(value);
		currentRow.height = value.thumbnailHeight;
		currentRowWidth += thumbnailWidth + margin;
	});

	return thumbnailRows;
}
