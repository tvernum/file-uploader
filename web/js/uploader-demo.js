$(document).ready(function() {
    var uploader = new qq.FileUploader({
        element: $('#basicUploadSuccessExample')[0],
        action: "/upload/receiver",
        debug: true
    });



    var uploader2 = new qq.FileUploader({
        element: $('#manualUploadModeExample')[0],
        action: "/upload/receiver",
        autoUpload: false,
        uploadButtonText: "Select Files"
    });

    $('#triggerUpload').click(function() {
        uploader2.uploadStoredFiles();
    });


    var uploader3 = new qq.FileUploader({
        element: $('#basicUploadFailureExample')[0],
        action: "/upload/receiver"
    });


    var uploader4 = new qq.FileUploader({
        element: $('#uploadWithVariousOptionsExample')[0],
        action: "/upload/receiver",
        multiple: false,
        allowedExtensions: ['jpeg', 'jpg', 'txt'],
        sizeLimit: 50000,
        uploadButtonText: "Click Or Drop"
    });

    var uploader5 = new qq.FileUploaderBasic({
        multiple: false,
        autoUpload: false,
        action: "/upload/receiver",
        button: $("#fubButton")[0]
    });
});
