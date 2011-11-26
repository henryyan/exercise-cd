$(function() {
	$('#step1').data('tip', '请设置场馆的场地类型，添加完成后点击<b>下一步</b>按钮');
	$('#step2').data('tip', '请添加场馆的场地，添加完成后点击<b>下一步</b>按钮');
	$('#step3').data('tip', '请设置<em><b>活动价格</b></em>，添加完成后点击<b>下一步</b>按钮');
	$('#step4').data('tip', '自动生成活动，完成后请点击<b>完成</b>按钮进入系统首页');
	
    $('#prev').attr('disabled', true);
    var first = 'step1';
    var last = 'step4';
    navTo($('#' + first));
	
	
    $('#prev').click(function() {
        $('#next').val('下一步');
        var prev = $('.active').prev();
        while (prev && prev.attr('tagName') &&
        prev.attr('tagName') != 'A') {
            prev = prev.prev();
        }
        if (prev.attr('src')) {
            navTo(prev);
            $('.active').removeClass('active');
            prev.addClass('active');
        }
        if (prev.attr('id') == first) {
            $(this).attr('disabled', true);
        }
    });
	
    $('#next').click(function() {
        if ($(this).val() == '完成') {
            window.location.href = ('../main2.jsp');
            return;
        }
		
		frames[0].doNext(function(doc){
			$('#prev').attr('disabled', false);
	        var next = $('.active').next();
	        while (next && next.attr('tagName') &&
	        next.attr('tagName') != 'A') {
	            next = next.next();
	        }
	        if (next.attr('src')) {
	            navTo(next);
	            $('.active').removeClass('active');
	            next.addClass('active');
	        }
	        if (next.attr('id') == last) {
	            $('#next').val('完成');
	        }
		});
        
    });
	
    /*$('.wizard-nav a').click(function() {
		return false;
        if ($(this).attr('src')) {
            $('.active').removeClass('active');
            $(this).addClass('active');
            navTo($(this));
			
			// 第一步
            if ($(this).attr('id') == first) {
				$('#prev').attr('disabled', true);
				$('#next').val('下一步');
			} else if ($(this).attr('id') == last) {
				$('#next').val('完成');
				$('#prev').attr('disabled', false);
			} else {
				$('#prev').attr('disabled', false);
				$('#next').attr('disabled', false);
				$('#next').val('下一步');
			}
            
        }
    });*/
    
});

function navTo(link) {
	var left = $('#next').offset().left + $('#next').width() + 10;
	var top = $('#next').offset().top - 3;
	$('#tip').css({left: left, top: top});
	$('#tip').html($(link).data('tip') || '');
    $('#nav-content').attr('src', $(link).attr('src'));
	$('#tip').corner();
}
