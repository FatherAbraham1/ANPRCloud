<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<!--[if IE 9]><html class="lt-ie10" lang="en" > <![endif]-->
<html class="no-js" lang="en" >

<head>
	<meta charset="utf-8">
	<!-- If you delete this meta tag World War Z will become a reality -->
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>ANPRCloud</title>
	
	<!-- If you are using the CSS version, only link these 2 files, you may add app.css to use for your overrides if you like -->
	<link rel="stylesheet" href="css/normalize.css">
	<link rel="stylesheet" href="css/foundation.css">
	
	<!-- If you are using the gem version, you need this only -->
	<!-- <link rel="stylesheet" href="css/app.css">
	-->
	<script src="js/vendor/modernizr.js"></script>
	
	<!-- form and the dropzone.js styles -->
	<link rel="stylesheet" type="text/css" href="css/dropzone.css" />
    <link rel="stylesheet" type="text/css" href="css/uploader.css" />

</head>
<body>
	<div class="sticky">
	  <!-- body content here -->
	<nav class="top-bar" data-topbar role="navigation" data-options="sticky_on: large">
	  <ul class="title-area">
	    <li class="name">
	      <h1><a href="#">My Site</a></h1>
	    </li>
	     <!-- Remove the class "menu-icon" to get rid of menu icon. Take out "Menu" to just have icon alone -->
	    <li class="toggle-topbar menu-icon"><a href="#"><span>Menu</span></a></li>
	  </ul>
	
	  <section class="top-bar-section">
	    <!-- Right Nav Section -->
	    <ul class="right">
	      <li class="active"><a href="#">Right Button Active</a></li>
	      <li class="has-dropdown">
	        <a href="#">Right Button Dropdown</a>
	        <ul class="dropdown">
	          <li><a href="#">First link in dropdown</a></li>
	          <li class="active"><a href="#">Active link in dropdown</a></li>
	        </ul>
	      </li>
	      <li class="has-form">
			  <div class="row collapse">
			    <div class="large-8 small-9 columns">
			      <input type="text" placeholder="Find Stuff">
			    </div>
			    <div class="large-4 small-3 columns">
			      <a href="#" class="alert button expand">Search</a>
			    </div>
			  </div>
			</li>
	    </ul>
	
	    <!-- Left Nav Section -->
	    <ul class="left">
	      <li><a href="#">Left Nav Button</a></li>
	    </ul>
	  </section>
	</nav>
	</div>
	<!-- Here is the dropzone.js form -->
	<form action="/ANPRCloud/doUpload.action" id="uploader" class="dropzone" method="post" enctype="multipart/form-data">
	</form>
	<script src="js/vendor/jquery.js"></script>
	<script src="js/vendor/fastclick.js"></script>
	<script src="js/foundation.min.js"></script>
	<script src="script/dropzone.js"></script>
	<script src="script/uploader.js"></script>
  <script>
    $(document).foundation();


    //foundation({alert: {speed: 3000}});
  </script>
</body>
</html>