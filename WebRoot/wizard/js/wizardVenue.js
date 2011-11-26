$(function() {
    $('#prev').attr('disabled', true);
    var first = 'step1';
    var last = 'step1';
    navTo($('#' + first).attr('src'));
    
    $('#prev').click(function() {
        $('#next').val('下一步');
        var prev = $('.active').prev();
        while (prev && prev.attr('tagName') && prev.attr('tagName') != 'A') {
            prev = prev.prev();
        }
        if (prev.attr('src')) {
            navTo(prev.attr('src'));
            $('.active').removeClass('active');
            prev.addClass('active');
        }
        if (prev.attr('id') == first) {
            $(this).attr('disabled', true);
        }
    });
    
    $('#next').click(function() {
		var srcEle = this;
        if ($(this).val() == '完成') {
            //window.location.href = ('field.html');
            window.location.href = ('verify.html');
            return;
        }
		//alert($('a.active').attr('href'));return;
        frames[0].doNext(function(){
			$('#prev').attr('disabled', false);
	        var next = $('.active').next();
	        while (next && next.attr('tagName') && next.attr('tagName') != 'A') {
	            next = next.next();
	        }
	        if (next.attr('src')) {
	            navTo(next.attr('src'));
	            $('.active').removeClass('active');
	            next.addClass('active');
	        }
	        //if (next.attr('id') == last) {
	            $(srcEle).val('完成');
	        //}
		});
        
    });
    
    $('.wizard-nav a').click(function() {
        if ($(this).attr('src')) {
            $('.active').removeClass('active');
            $(this).addClass('active');
            navTo($(this).attr('src'));
            if ($(this).attr('id') == first) {
                $('#prev').attr('disabled', true);
            } else if ($(this).attr('id') == last) {
                $('#next').val('完成');
                $('#prev').attr('disabled', false);
            } else {
                $('#prev').attr('disabled', false);
                $('#next').attr('disabled', false);
            }
            
        }
    });
    
});

function navTo(url) {
    $('#nav-content').attr('src', url);
}
