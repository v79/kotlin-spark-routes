// initialise jquery stuff
$(document).ready(function() {
    // the "href" attribute of the modal trigger must specify the modal ID that wants to be triggered
    $('.modal').modal({
        dismissible: true
    });


}); // end of document ready

// generic function to open a modal dialog. Specify the controller path which will supply the view, and the HTML divs to update
function openModal(sparkPath, dataDiv, containerDiv) {
    $.ajax({
        url: sparkPath,
        success: function(data) {
            $(dataDiv).html(data);
            $(containerDiv).modal('open')
        }
    })
}