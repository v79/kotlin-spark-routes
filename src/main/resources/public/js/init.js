// initialise jquery stuff
$(document).ready(function() {
    // the "href" attribute of the modal trigger must specify the modal ID that wants to be triggered
    $('.modal').modal({
        dismissible: true
    });


}); // end of document ready

// generic function to open a modal dialog. Specify the controller path which will supply the view, and the HTML divs to update. Add del=true if you wish to make a DELETE request
function openModal(sparkPath, dataDiv, containerDiv, del = false) {
    if(del) {
        $.ajax({
                url: sparkPath,
                type: 'DELETE',
                success: function(data) {
                    $(dataDiv).html(data);
                    $(containerDiv).modal('open');
                }
            })
    } else {
        $.ajax({
            url: sparkPath,
            success: function(data) {
                $(dataDiv).html(data);
                $(containerDiv).modal('open');
            }
        })
    }

}

function validateAndSubmit(validatorPath, formName, containerDiv, actionPath) {
    var serializedData = $('#' + formName).serialize();
    console.log(serializedData)
    // first, post to validator
    $.ajax({
        url: validatorPath,
        method: 'post',
        data: serializedData,
        success: function(response, statusText, xhr) {
            // if valid, move on to next action
            if(!response) {
                window.location.href = '/validtest/success'
//                $.ajax({
//                    url: actionPath,
//                    method: 'post',
//                    data: serializedData,
//                    success: function(response) {
//                        // do nothing
//                    }
//                });
            } else {
                // if in error, form should be re-rendered
                $('#' + containerDiv).html(response);
            }
        }
    });
}