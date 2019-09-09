
Dropzone.options.uploadForm = {

    parallelUploads: 1,
    createImageThumbnails: false,
    paramName: "file",
    maxFilesize: 30, // MB
    maxFiles: 15,
    dictMaxFilesExceeded: "You can't upload any more files in this batch.",

    queuecomplete: function(file) {

        // Add finish button
        if(this.getAcceptedFiles().length > 0 && !$('#finish-upload-btn').length)
            $('#upload-card-body').append('<br><div align="center"><a id="finish-upload-btn" class="btn btn-primary btn-lg" href="/" role="button">Finish</a></div>');

    }
};
