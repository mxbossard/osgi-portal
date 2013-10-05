<div id="albums" data-ng-controller="AlbumCtrl" data-ng-init="init()">
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