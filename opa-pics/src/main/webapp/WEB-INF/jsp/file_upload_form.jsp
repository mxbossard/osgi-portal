<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
    <title>Pics Upload</title>

	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>

	<script type="text/javascript">
	$(document).ready(function() {
	    //add more file components if Add is clicked
	    $('#addPicture').click(function() {
	        var fileIndex = $('#fileTable tr').children().length;
	        $('#fileTable').append(
	                '<tr><td>'+
	                '   <input type="file" name="pictures['+ fileIndex +']" />'+
	                '</td></tr>');
	    });
	     
	});
	</script>
</head>
<body>
	<h1>Pics Upload</h1>
	 
	<form:form method="post" action="upload/save"
	        modelAttribute="uploadForm" enctype="multipart/form-data">
	 
	    <p>Select pictures to upload. Press Add button to add more file inputs.</p>
	 
	    <input id="addPicture" type="button" value="Add Picture" />
	    <table id="fileTable">
	        <tr>
	            <td><input name="pictures[0]" type="file" /></td>
	        </tr>
	        <tr>
	            <td><input name="pictures[1]" type="file" /></td>
	        </tr>
	    </table>
	    <br/><input type="submit" value="Upload" />
	</form:form>
	
	<spring:url value="/" var="backUrl" />
	<a href="${backUrl}">Back to index</a>
</body>
</html>