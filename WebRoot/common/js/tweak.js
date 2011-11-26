$(function() {
    //全部折叠
    $("#si-content .sc-title").each(function(i) {
        if (i > 0) {
            $(this).next().slideToggle("normal");
        }
    });
    
    $(".sc-title").toggle(function() {
        $(this).next().slideDown('normal');
    }, function() {
        $(this).next().slideUp('normal');
    });
    
});
