// AMD (Asynchronous Module Definition) wrapper for jQuery 1.8
define([
    // Load the jQuery source file
    '/WebGoat/js/libs/jquery.min.js'
    ],
    function(){
        // Tell Require.js that this module returns a reference to jQuery
        return $; // Return the global scope object
});