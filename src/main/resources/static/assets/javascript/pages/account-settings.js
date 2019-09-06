$("document").ready(function(){

    $("#password-input").on("change paste keyup", function(){
        $("#update-btn").prop('disabled', false);
        $("#password-confirm-input").prop('disabled', false);
        addEnterKeyListener('#update-form');
    });

});

function addEnterKeyListener(id) {
    $(id).keypress(function (e) {
       if (e.which == 13) {
        submitUpdate();
        return false;
       }
    });
}


function submitUpdate() {

    password = $("#password-input").val();
    passwordConfirm = $("#password-confirm-input").val();
    passwordCurrent = $("#password-current-input").val();

    if (password != passwordConfirm) {
        $("#password-confirm-input").addClass("is-invalid");

        $("#password-input-feedback").hide();
        $("#password-input").addClass("is-invalid");

        $("#update-btn").prop('disabled', false);

    } else if (passwordCurrent === password) {
        $("#password-confirm-input").removeClass("is-invalid");

        $("#password-input-feedback").html('<i class="fa fa-exclamation-circle fa-fw"></i>'
            + '<span>New password is the same as the current password.</span>');

        $("#password-input").addClass("is-invalid");
        $("#password-input-feedback").show();

    } else {
        $("#update-form").submit();
    }


}