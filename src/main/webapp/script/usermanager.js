// Attach a submit handler to the form
$( "#login-form" ).submit(function( event ) {
 
  // Stop form from submitting normally
  event.preventDefault();
 
  // Get some values from elements on the page:
  var $form = $( this ),
  	loginName = $form.find( "input[name='loginName']" ).val(),
  	password = $form.find( "input[name='password']" ).val(),
    rememberMe = $( "#login-form .switch #rememberMe" )[0].checked,
    url = $form.attr( "action" );
 
  // Send the data using post
  var posting = $.post( url, { loginName: loginName, password: password, rememberMe: rememberMe } );
 
  // Put the results in a div
  posting.done(function( data ) {
    var result = $("#login-modeal #result");
    if (data.result[0]!=="success"){
      result.html( data.result[0]).slideDown();
    } else {
      result.hide();
      $("#login-modeal a").click();
    }
  });
}); 

// Attach the register botton in login form
$( "#login-form #register" ).on("click", function(){ 
	$("#register-dropdown").click();
});

// Attach a submit handler to the form
$( "#register-form" ).submit(function( event ) {
 
  // Stop form from submitting normally
  event.preventDefault();
 
  // Get some values from elements on the page:
  var $form = $( this ),
    loginName = $form.find( "input[name='loginName']" ).val(),
    password = $form.find( "input[name='password']" ).val(),
    confirmPassword = $form.find( "input[name='confirmPassword']" ).val(),
    url = $form.attr( "action" );
 
  // Send the data using post
  var posting = $.post( url, { loginName: loginName, password: password, confirmPassword: confirmPassword } );
 
  // Put the results in a div
  posting.done(function( data ) {
    var result = $("#register-modeal #result");
    if (data.result[0]!=="success"){
      result.html( data.result[0]).slideDown();
    } else {
      result.hide();
      $("#register-modeal a").click();
    }
  });
}); 

// Attach the Login botton in register form
$( "#register-form #register" ).on("click", function(){ 
	$("#login-dropdown").click();
});

// Attach the Logout dropdown
$("#logout-dropdown").on("click",function(){
	$.get( "./logout", function( data ) {
	});
});
