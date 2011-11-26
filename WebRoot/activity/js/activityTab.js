var common = new Common();
var fieldType = common.getParam('fieldType');
var currentDate = systemDate;

/**
 * tab页面切换功能，为了达到延迟加载iframe页面的效果，先设置非当前标签的iframe的osrc属性，然后绑定标签的单击事件
 */
$(function() {
	
	var bodyHeight = $(document).height() - 72;
	
	/**
     * 注册显示日期
     */
    $('.Wdate').bind('click', pickDate);
	
    $.getJSON('../field/fieldNames.do', {
		fieldType: fieldType
	}, function(result) {
        var listDom = "";
        var iframeStr = "";
        var selectedTab = 0;
        var count = 0;
        var src = "";
        for (k in result) {
            listDom += "<li><a href='#field" + k + "'>" + result[k] + "</a></li> ";
            src = "fieldActivity.do?fieldId=" + k + "&type=" + common.getParam('type')
            + "&fieldType=" + common.getParam('fieldType')
            + "&pickedDate=" + common.getParam('pickedDate')
            iframeStr += "<div id='field" + k + "' name='fieldDiv' style='height:" + bodyHeight + "px'>" +
            $('#iframeTemplate').html().replace('#osrc', src) +
            "</div>";
            if (selectedTab == 0) {
                if (k == common.getParam('fieldId')) {
                    selectedTab = count;
                }
            }
            count++;
        }
        $('#tabs > ul').append(listDom).parent().append(iframeStr).tabs({
            selected: selectedTab
        });

        // 延迟加载iframe页面
		var $iframe;

		// 1、默认显示的标签
		if(common.getParam('fieldId') == "") {
			$iframe = $('div[name=fieldDiv]:first iframe');
            $iframe.attr('src', $iframe.attr('osrc'));
		} else {
			$iframe = $('#field' + common.getParam('fieldId') + ' iframe');
        	$iframe.attr('src', $iframe.attr('osrc'));
		}

		// 单击后加载iframe
		$('#tabs li A').one('click', function() {
            var $iframe = $($(this).attr('href') + ' iframe');
			if(!$iframe.attr('src')) {
	            $iframe.attr('src', $iframe.attr('osrc'));
			}
        });

    });

});


/**
 * 选择预订日期
 */
function pickDate() {
	// 一周之前，从今天向前推一周
	var minDate = common.dateAdd(currentDate, -7, 'd');
	var maxDate = common.dateAdd(currentDate, $('#advance').val() * 1 - 1, 'd');
	if (common.getParam('type') == 'local') {
		maxDate = currentDate;
	}
    WdatePicker({
        minDate: minDate,
        maxDate: maxDate,
        onpicked: function(dp) {
            var strPickedDate = dp.cal.getDateStr();
			
			// 选择的日期date
			var pickedDate = Date.parse(strPickedDate);
			
			var params = {
				type: common.getParam('type'),
				fieldId: common.getParam("fieldId"),
				fieldType: common.getParam("fieldType"),
				pickedDate: strPickedDate
			};
			
			var url = contextPath + '/activity/fieldActivityTab.jsp?' + $.param(params);
            window.location.href = url;
        }
    });
}