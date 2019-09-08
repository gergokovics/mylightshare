$("document").ready(function(){

    $(".custom-control-input").click(function(){
        $("#dropdown-sort").submit();
    });

    $(".file-container").infiniteScroll({
        prefill: true,
        path: function() {
                var pageNumber = ( this.loadCount + 1 );
                return '/files/page=' + pageNumber;
              },
        append: '.file-card',
        checkLastPage: '.file-card',
        status: '.page-load-status',
        history: false
    });

});