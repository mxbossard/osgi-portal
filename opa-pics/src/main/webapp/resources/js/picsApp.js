var app = angular.module('pics', ['infinite-scroll']);

app.service('PicsService', function() {
	"use strict";

	var service = {
		_eventTarget : new MbyUtils.event.EventTarget('PicsService'),
		changeAlbum : function(album) {
			this._eventTarget.fireEvent(new MbyUtils.event.Event('albumChange', {
				'album' : album
			}));
		},
		onAlbumChange : function(callback) {
			this._eventTarget.addEventListener(new MbyUtils.event.EventListener('albumChange', function(event) {
				callback(event.properties.album);
			}));
		}
	};

	return service;
});

app.controller('AlbumCtrl', function($scope, $http, $timeout, PicsService) {
	"use strict";

	$scope.selectedAlbum = null;

	// On page init
	$scope.init = function() {
		$http.get(findAllAlbumsJsonUrl).success(function(data, status) {
			$scope.albums = data;
		});
	};

	$scope.selectAlbum = function(album) {
		PicsService.changeAlbum(album);
	};

	$scope.createAlbum = function($event) {
		var name = $scope.newAlbumName;
		if (name) {
			var newAlbum = {};
			newAlbum.name = name;

			var url = createAlbumJsonUrl;
			$http.post(url, newAlbum);
		}
	};

});

app.controller('StashCtrl', function($scope, $http, $timeout, PicsService) {
	"use strict";

	$scope.stash = null;
	$scope.lastSinceTime = 0;
	$scope.scale = 100;
	$scope.searchPicturesLock = false;
	$scope.stashWidth = null;
	$scope.scale = 100;
	$scope.selectedPictures = [];
	$scope.reechantillonatingPageSize = 200;
	$scope.picturesToAddInStash = [];

	// On page init
	$scope.init = function() {
		$scope.stashWidth = getBodyWidth();
	};

	$scope.reechantillonateStash = function(event) {
		var pageSize = $scope.reechantillonatingPageSize;
		$scope.stash.reechantillonate($scope.stashWidth, $scope.scale, pageSize, $timeout);
	};

	$scope.addRandomPicture = function(number) {
		if (!number || number < 1) {
			number = 1;
		}

		for ( var k = 0; k < number; k++) {
			var newPic = {};
			var width = Math.floor(Math.random() * 600) + 100; // 50 - 450
			// var height = Math.floor(Math.random() * 100) + 50; // 50 - 150
			var height = 200;

			newPic.thumbnail = 'http://placehold.it/' + width + 'x' + height;
			newPic.image = 'http://placehold.it/' + width + 'x' + height;
			newPic.thumbnailWidth = width;
			newPic.thumbnailHeight = height;

			$scope.stash.addPicture(newPic);
		}
	};

	$scope.startAddRandomPicture = function() {
		$scope.autoAdding = setTimeout(function() {
			$scope.startAddRandomPicture();
			$scope.addRandomPicture();
		}, 10);
	};

	$scope.stopAddRandomPicture = function() {
		clearInterval($scope.autoAdding);
	};

	$scope.deleteAlbum = function(album) {
		var confirm = window.confirm("Are you sure you want to delete album: " + album.name + " ?");
		if (confirm) {
			var index = $scope.albums.indexOf(album);
			$scope.albums.splice(index, 1);
		}

		if ($scope.selectedAlbum == album) {
			$scope.selectedAlbum = null;
		}

	};

	$scope.selectPicture = function($event, picture) {
		console.log('toggle selection on picture');
		picture.selected = !picture.selected;
		picture.overlayed = picture.selected;

		var array = $scope.selectedPictures;
		if (picture.selected) {
			array.push(picture);
			// picture.zoom = 1;
		} else {
			array.splice(array.indexOf(picture), 1);
			// picture.zoom = 1;
		}
	};

	$scope.rotatePictureLeft = function($event, picture) {
		picture.waitingMsg = 'Rotating to the left...';
		picture.rotatingLeft = true;
		picture.overlayed = true;

		var url = rotatePictureUrl.replace(/{:pictureId}/, picture.id).replace(/{:angle}/, -90);
		rotatePicture($http, picture, url);
	};

	$scope.rotatePictureRight = function($event, picture) {
		picture.waitingMsg = 'Rotating to the right...';
		picture.rotatingRight = true;
		picture.overlayed = true;

		var url = rotatePictureUrl.replace(/{:pictureId}/, picture.id).replace(/{:angle}/, 90);
		rotatePicture($http, picture, url);
	};

	$scope.removePicture = function($event, picture) {
		picture.removed = !picture.removed;
		picture.overlayed = picture.removed;
	};

	$scope.overlayAction = function($event, picture) {
		if (picture.overlayed) {
			if (picture.selected) {
				$scope.selectPicture($event, picture);
			} else if (picture.removed) {
				$scope.removePicture($event, picture);
			}
		}
	};

	// Register album change callback
	PicsService.onAlbumChange(function(album) {

		$scope.selectedAlbum = album;
		$scope.lastSinceTime = 0;

		var width = $scope.stashWidth;
		console.log(width);
		$scope.stash = buildNewStash(width, $scope.scale);

		getNextPictures($http, $timeout, $scope);
	});

});

function getNextPictures($http, $timeout, $scope) {
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
						$scope.picturesToAddInStash.push(value);
					});

					var lock = $scope.loadingStashLock;
					if (!lock) {
						// addPicturesToStashDelayed($timeout, $scope);
					}
				}

				$scope.searchPicturesLock = false;
			}).error(function(data, status, headers, config) {
				$scope.searchPicturesLock = false;
			});
		}
	}
}

function addPicturesToStashDelayed($timeout, $scope) {
	$scope.loadingStashLock = true;

	var picturesToAdd = $scope.picturesToAddInStash;
	if (picturesToAdd && picturesToAdd.length > 0) {
		var picture = picturesToAdd.splice(0, 1)[0];
		picture.show = true;
		// $scope.stash.addPicture(picture);

		if (picturesToAdd.length > 0) {
			$timeout(function() {
				addPicturesToStashDelayed($timeout, $scope);
			}, 200);
		} else {
			$scope.loadingStashLock = false;
		}
	}
}

function getBodyWidth() {
	return document.getElementsByTagName('body')[0].offsetWidth;
}

function buildNewStash(width, scale) {
	var stash = {
		pictures : [],
		size : 0,
		rows : [],
		scale : (scale) ? scale / 100 : 1,
		width : (width) ? width : 600,
		margin : 1,
		getLastRow : function() {
			if (this.rows.length < 1) {
				this.addNewRow();
			}
			return this.rows[this.rows.length - 1];
		},
		addNewRow : function() {
			var parentStash = this;
			var newRow = {
				pictures : [],
				height : 0,
				width : 0,
				addPicture : function(picture) {
					var nbPic = this.pictures.length || 0;
					var hypotheticWidth = nbPic * parentStash.margin + this.width + picture.displayWidth;
					if (hypotheticWidth > parentStash.width) {
						return false;
					}

					this.pictures.push(picture);
					this.height = Math.max(this.height, picture.displayHeight);
					this.width += picture.displayWidth;

					return true;
				},
				finalizeRow : function() {
					var ratio = (parentStash.width - parentStash.margin * this.pictures.length) / this.width;
					this.height = Math.round(this.height * ratio);

					var offset = 0;
					angular.forEach(this.pictures, function(value, key) {
						// Enlarge thumbnails of row to occupy all the width
						var exactWidth = value.displayWidth * ratio + offset;
						var roundedWidth = Math.round(exactWidth);

						// offset due to rounding
						offset = exactWidth - roundedWidth;

						value.displayWidth = roundedWidth;
						value.displayHeight = this.height;

						value.initialWidth = value.displayWidth;
						value.initialHeight = value.displayHeight;
					}, this);
				}
			};
			this.rows.push(newRow);
			console.log('Added new row: #' + (this.rows.length - 1));

			return newRow;
		},
		addPicture : function(picture) {
			initPicture(picture);

			this.pictures.push(picture);

			this._addPictureInternal(picture);
		},
		_addPictureInternal : function(picture) {
			// Rescale
			picture.displayWidth = Math.round(picture.thumbnailWidth * this.scale);
			picture.displayHeight = Math.round(picture.thumbnailHeight * this.scale);

			var lastRow = this.getLastRow();
			var enoughSpace = lastRow.addPicture(picture);
			if (!enoughSpace) {
				// Not enough space in row for the picture
				lastRow.finalizeRow();

				var newRow = this.addNewRow();
				newRow.addPicture(picture);
			}
			this.size++;
		},
		reechantillonate : function(width, scale, pageSize, $timeout) {
			// Stop current job
			if (this.job) {
				$timeout.cancel(this.job);
			}

			// Update stash configuration
			this.scale = (scale) ? scale / 100 : 1;
			this.width = (width) ? width : 600;
			this.size = 0;

			// Purge rows
			this.rows.length = 0;

			// Launch reechantonation process
			this._reechantillonatePageInternal(0, pageSize, $timeout);
		},
		_reechantillonatePageInternal : function(page, pageSize, $timeout) {
			for ( var k = page * pageSize; k < (page + 1) * pageSize; k++) {
				if (k < this.pictures.length) {
					this._addPictureInternal(this.pictures[k]);
				}
			}

			var parentStash = this;
			if ((page + 1) * pageSize < this.pictures.length) {
				parentStash.job = $timeout(function() {
					parentStash._reechantillonatePageInternal(page + 1, pageSize, $timeout);
				}, 500);
			}
		}
	};

	return stash;
}

function initPicture(picture) {
	picture.selected = false;
	picture.show = true;

	// Build thumbnail URL
	var thumbnailUrl = getImageUrl.replace(/{:imageId}/, picture.thumbnailId);
	picture.thumbnailUrl = thumbnailUrl;

	// Build image URL
	var imageUrl = getImageUrl.replace(/{:imageId}/, picture.imageId);
	picture.imageUrl = imageUrl;
}

function updatePicture(pictureInStash, newPicture) {
	initPicture(newPicture);

	pictureInStash.width = newPicture.width;
	pictureInStash.height = newPicture.height;
	pictureInStash.size = newPicture.size;
	pictureInStash.format = newPicture.format;

	pictureInStash.thumbnailWidth = newPicture.thumbnailWidth;
	pictureInStash.thumbnailHeight = newPicture.thumbnailHeight;
	pictureInStash.thumbnailSize = newPicture.thumbnailSize;
	pictureInStash.thumbnailFormat = newPicture.thumbnailFormat;

	pictureInStash.thumbnailUrl = newPicture.thumbnailUrl;
	pictureInStash.imageUrl = newPicture.imageUrl;
}

function rotatePicture($http, picture, url) {
	$http.get(url).success(function(data, status) {
		var newPicture = data;
		updatePicture(picture, newPicture);

		var initialRatio = picture.initialWidth / picture.initialHeight;
		var newRatio = newPicture.thumbnailWidth / newPicture.thumbnailHeight;

		if (initialRatio >= 1 && newRatio >= 1 || initialRatio < 1 && newRatio < 1) {
			// Same orientation than initial display
			picture.displayHeight = picture.initialHeight;
			picture.displayWidth = picture.initialWidth;
		} else {
			// Orientation change
			var ratioW = picture.thumbnailWidth / picture.initialWidth;
			var ratioH = picture.thumbnailHeight / picture.initialHeight;
			if (ratioW > ratioH) {
				// We need to fix width
				picture.displayWidth = picture.initialWidth;
				picture.displayHeight = Math.round(picture.displayWidth / newRatio);
			} else {
				// We need to fix height
				picture.displayHeight = picture.initialHeight;
				picture.displayWidth = Math.round(picture.displayHeight * newRatio);
			}
		}

		picture.rotatingLeft = false;
		picture.rotatingRight = false;
		picture.overlayed = false;
	});
}
