<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
    <link rel="stylesheet" type="text/css" href="css/dropzone.css" />
    <link rel="stylesheet" type="text/css" href="css/uploader.css" />
</head>
<title>Insert title here</title>
</head>
<body>
 <form action="/ANPRCloud/doUpload.action" id="uploader" class="dropzone" method="post" enctype="multipart/form-data">
</form>
</body>
<script src="script/dropzone.js"></script>
<script src="script/uploader.js"></script>
</html>