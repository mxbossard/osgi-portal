<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!doctype html>
<html lang="fr">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Pics</title>
	
	<script type="text/javascript" src="http://code.jquery.com/jquery-1.10.2.js"></script>
	<script type="text/javascript" src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
	
	<script type="text/javascript">
		var osgiPortal = window.parent.OsgiPortal.getInstance();
		window.appClient = osgiPortal.registerPortalApplication("${app.signature}");
		
		$(document).ready(function() {
			$(".thumbnail").click(function(e) {
				var thumbnail = e.target;
				var imageId = $(thumbnail).attr('data-imageId');
				
				var imgElement = $('#image img');
				var imageUrl = imgElement.attr('src');
				var updatedImageUrl = imageUrl.replace(/[^\/?]*(\?+[^?]*)$/, imageId + '$1');
				imgElement.attr('src', updatedImageUrl);
				
				$("#picture").show();
				
				var imageWidth = $('#image').innerWidth();
				var imageHeight = $('#image').innerHeight();
				
				imgElement.attr('height', imageHeight);
				//$('#image').css('width', imgElement.attr('width'));
				
				
			});
			
			$("#picture").click(function(e) {
				$("#picture").hide();
				var updatedImageUrl = imageUrl.replace(/[^\/]*$/, 0);
			});
		});

	</script>
	
	<c:url var="appClientScriptUrl" value="/resources/js/portalAppClient.js" />
	
	<script type="text/javascript" src="${appClientScriptUrl}"></script>
	
	<style type="text/css">
		img {
			border: 0px;
			margin: 0px;
			padding: 0px;
		}
		
		#thumbnails {
			display: block;
		}
		
		.thumbnail {
			border: 0px solid transparent;
			margin: 0px 1px 1px 0px;
			padding: 0px;
			overflow: none;
			float: left;
		}
		
		.thumbnail:HOVER {
			cursor: pointer;
		}
		
		#picture {
			display: none;
			position: fixed;
			top: 0px;
			left: 0px;
			width: 100%;
			height: 100%;
			
			background: rgba(255, 255, 255, 0.4);
			
			text-align: center;
			
			display: box;
			-moz-box-pack: center;
			-moz-box-align: center;
			
			-webkit-box-pack: center;
			-webkit-box-align: center;
			
			box-pack: center;
			box-align: center;
		}
		
		#image {
			position: relative;
			margin: auto;
			width: 90%;
			height: 90%;
			background: rgba(255, 255, 255, 1);
			overflow: hidden;
		}
		
		#image img {
			opacity: 1;
		}
	</style>
</head>
<body>
	<h2>Pics</h2>	
	
	<spring:url value="/upload" var="uploadFormUrl" />
	<a href="${uploadFormUrl}">Upload pics form</a>
	
	<h3>Uploaded pics (thumbnails) :</h3>
	
	<div id="thumbnails">
		<c:forEach items="${pictures}" var="picture">
			<div class="thumbnail" style="width: ${picture.thumbnail.width}px; height: ${picture.thumbnail.height}px;" data-imageId="${picture.image.id}">
				<spring:url value="/image/${picture.thumbnail.id}" var="thumbUrl" />
				<img src="${thumbUrl}" data-imageId="${picture.image.id}" />
			</div>
		</c:forEach>
	</div>
	
	<div id="picture">
		<div id="image">
			<spring:url value="/image/0" var="imageUrl" />
			<img src="${imageUrl}" />
		</div>
	</div>
	
</body>
</html>