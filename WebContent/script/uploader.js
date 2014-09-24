// Disable auto discover for all elements:
Dropzone.autoDiscover = false;

// "uploader" is the camelized version of the HTML element's ID
Dropzone.options.uploader = {
  paramName: "upload", // The name that will be used to transfer the file
  maxFilesize: 2, // MB
  addRemoveLinks: true,
  parallelUploads: 1,
  acceptedFiles: "image/*",
  thumbnailWidth: 350,
  thumbnailHeight: 350,
  previewTemplate:  "<div class=\"dz-preview dz-file-preview\">\n  <div class=\"dz-details\">\n    <div class=\"dz-filename\"><span data-dz-name></span></div>\n    <div class=\"dz-size\" data-dz-size></div>\n  <div class=\"dz-response\" ><strong></strong></div>\n  <img data-dz-thumbnail />\n  </div>\n  <div class=\"dz-progress\"><span class=\"dz-upload\" data-dz-uploadprogress></span></div>\n  <div class=\"dz-success-mark\"><span>✔</span></div>\n  <div class=\"dz-error-mark\"><span>✘</span></div>\n  <div class=\"dz-error-message\"><span data-dz-errormessage></span></div>\n</div>"
};



// Create dropzones programmatically
var myDropzone = new Dropzone("form#uploader", { url: "/ANPRCloud/doUpload.action"});

myDropzone.on("success", function(file) {
		console.log(file.xhr.response);
		//file.previewElement.getElementsByClassName("dz-response")[0].getElementsByTagName("strong")[0].innerHTML = JSON.parse(file.xhr.response).result;
		file.previewElement.getElementsByClassName("dz-response")[0].getElementsByTagName("strong")[0].innerHTML = file.xhr.response;
		
	});