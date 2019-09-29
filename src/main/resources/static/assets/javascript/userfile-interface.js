$(document).ready(function() {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

});

function removeFileURL(id) {

    $('#' + id + '-url-remove-btn').attr('disabled', true);
    $('#' + id + '-url-copy-btn').attr('disabled', true);

    $.ajax({
        url: '/rest/urls/' + id,
        type: 'DELETE',
    }).done(function( msg ) {

        $('#' + id + '-url-string').hide(120);
        $('#' + id + '-url-hr-tag').hide(120);

        var deleteBtnId = id + '-file-delete-btn';
        $('#' + id + '-url-remove-btn').attr('id', deleteBtnId);

        $('#' + deleteBtnId).html('Delete File');
        $('#' + deleteBtnId).attr('class', 'btn btn-danger');
        $('#' + deleteBtnId).attr('onclick', 'deleteFile(' + id + ')');
        $('#' + deleteBtnId).attr('disabled', false);

        var generateBtnId = id + '-url-generate-btn';
        $('#' + id + '-url-copy-btn').attr('id', generateBtnId);

        $('#' + generateBtnId).html('Generate URL');
        $('#' + generateBtnId).attr('onclick', 'generateFileURL(' + id + ')');
        $('#' + generateBtnId).attr('disabled', false);

    });

}

function deleteFile(id) {

        $('#' + id + '-file-delete-btn').attr('disabled', true);
        $('#' + id + '-url-generate-btn').attr('disabled', true);

        $.ajax({
            url: '/files/' + id,
            type: 'DELETE',
        }).done(function( msg ) {
            $('#' + id + '-card').hide(200);
            console.log('deleted file')
        });
}

function generateFileURL(id) {

    $('#' + id + '-url-generate-btn').attr('disabled', true);
    $('#' + id + '-file-delete-btn').attr('disabled', true);

    $.ajax({
        url: '/rest/urls/' + id,
        type: 'PUT',
    }).done(function( msg ) {

        var newUrl = msg;

        var removeBtnId = id + '-url-remove-btn';
        $('#' + id + '-file-delete-btn').attr('id', removeBtnId);

        $('#' + removeBtnId).html('Remove URL');
        $('#' + removeBtnId).attr('class', 'btn btn-warning');
        $('#' + removeBtnId).attr('onclick', 'removeFileURL(' + id + ')');
        $('#' + removeBtnId).attr('disabled', false);

        var copyBtnId = id + '-url-copy-btn';
        $('#' + id + '-url-generate-btn').attr('id', copyBtnId);

        $('#' + copyBtnId).html('Copy URL');
        $('#' + copyBtnId).attr('onclick', "copyTextToClipboard('" + newUrl + "')");
        $('#' + copyBtnId).attr('disabled', false);


        // TODO clean this code
        $('#' + id + '-url-string').hide();
        $('#' + id + '-url-hr-tag').hide();
        $('#' + id + '-url-string').removeAttr("hidden");
        $('#' + id + '-url-hr-tag').removeAttr("hidden");

        $('#' + id + '-url-string').html(newUrl);
        $('#' + id + '-url-string').show(120);
        $('#' + id + '-url-hr-tag').show(120);
    });
}

// Copy to clipboard

function fallbackCopyTextToClipboard(text) {
  var textArea = document.createElement("textarea");
  textArea.value = text;
  textArea.style.position = "fixed";
  document.body.appendChild(textArea);
  textArea.focus();
  textArea.select();

  try {
    var successful = document.execCommand('copy');
    var msg = successful ? 'successful' : 'unsuccessful';
    console.log('Fallback: Copying text command was ' + msg);
  } catch (err) {
    console.error('Fallback: Oops, unable to copy', err);
  }

  document.body.removeChild(textArea);
}
function copyTextToClipboard(text) {
  if (!navigator.clipboard) {
    fallbackCopyTextToClipboard(text);
    return;
  }
  navigator.clipboard.writeText(text).then(function() {
    console.log('Async: Copying to clipboard was successful!');
  }, function(err) {
    console.error('Async: Could not copy text: ', err);
  });
}