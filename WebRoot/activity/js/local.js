/*******************************************************************************
 * @功能说明：现场管理
 * @作者： 闫洪磊
 * @创建时间：2009-06-30
 * @修改记录：
 ******************************************************************************/

var common = new Common();
var list = new List(common);
var dialog = null;
var currentDate = systemDate;

$(function() {
	
	/**
	 * 作废
	 */
    $(':button[name=blankout]').live('click', blankout);
	
	/**
	 * 校验验证码
	 */
	$(':button[name=validate]').live('click', validateCode);
	
	/**
	 * 现场管理中的付款
	 */
	$(':button[name=pay]').bind('click', pay);
	
	// 作废倒计时
	$(':hidden[name=cancelCountDown]').each(function(){
		var srcEle = this;
		$(this).parent().parent().CRcountDown({
	        startNumber: $(srcEle).val(),
	        callBack: function() {
				$('td:eq(6)', $(srcEle).parent().parent()).html("已过期<button name='blankout'>作废</button>");
			}
		});
	});
	
	// 正在锻炼倒计时
	$(':hidden[name=exerciseCountDown]').each(function(){
		var srcEle = this;
		var ct = $(srcEle).val() * 1;
		if(ct > 0) {
			$(this).parent().parent().CRcountDown({
		        startNumber: $(srcEle).val(),
		        callBack: function() {
					$('td:eq(6)', $(srcEle).parent().parent()).html("正在锻炼");
				}
			});
		}
		
	});
	
	// 锻炼结束倒计时
	$(':hidden[name=exerciseEndCountDown]').each(function(){
		var srcEle = this;
		var ct = $(srcEle).val() * 1;
		if(ct > 0) {
			$(this).parent().parent().CRcountDown({
		        startNumber: $(srcEle).val(),
		        callBack: function() {
					$('td:eq(6)', $(srcEle).parent().parent()).html("锻炼结束");
				}
			});
		}
		
	});
	
});

/**
 * 作废
 */
function blankout() {
	var srcEle = this;
    $.post('orderHandle.do', {
        activityIds: $(this).attr('activityId'),
		opType: 'blankout',
		fieldType: fieldType
    }, function(req) {
		$(srcEle).parent().parent().find('td:eq(2),td:eq(3),td:eq(4),td:eq(5)').removeAttr('class').html('&nbsp;');
        $(srcEle).parent().find(':button[name=booking],:button[name=preserving]').show();
        $(srcEle).hide().parent().find("#status,#paymentStatus").text('').end().html('作废');
    });
}


/**
 * 校验验证码
 * @param {Object} event
 * @版本：改为用肉眼对比前版本号：98
 */
function validateCode(){

	var srcEle = this;
	var validateCode = "";
	
	var dialog = $.weeboxs.open('正在获取验证码……', {
		title : '请确认验证码是否正确？',
		animate : true,
		onopen: function(){
			$.get('getValidateCode.do', {
				fieldType: fieldType,
				activityId: $(srcEle).attr('activityId')
			}, function(req) {
				validateCode = req;
				dialog.setContent('验证码为：<b>' + req + '</b>');
			});
		},
		okBtnName: '正 确',
		cancelBtnName: '错 误',
		focus : '.dialog-ok',
		type : 'warning',
		height : 30,
		onok: function(){
			$.getJSON('validateCode.do', {
				activityId: $(srcEle).attr('activityId'),
				code: validateCode,
				fieldType: fieldType
			}, function(req) {
				if (req.status == 'success') {
					$.get('getActivityStatus.do', {
						activityId: $(srcEle).attr('activityId'),
						fieldType: fieldType
					}, function(status) {
						$(srcEle).parent().siblings("td:eq(4)").removeClass('error').addClass('correct').siblings("td:eq(5)").html(status);
						$(srcEle).parent().html(validateCode);
					});
				}
			});
			dialog.close();
		}
	});
	
}

/**
 * 等待锻炼
 */
function innerWaitExercise(srcEle, fromHour, fromMinute, systemDateTime, validateCode) {
	$(srcEle).parent().siblings("td:eq(4)").removeClass('error').addClass('correct').siblings("td:eq(5)").html('等待锻炼');
	
	var countDownMinutes = (fromHour - systemDateTime.getHours() - 1) * 60;
	countDownMinutes < 0 ? 0 : countDownMinutes;
	fromMinute = fromMinute == 0 ? 60 : fromMinute;
	
	if (fromMinute > systemDateTime.getMinutes()) {
		countDownMinutes += fromMinute - systemDateTime.getMinutes();
	}
	if (countDownMinutes > 0) { // 已经开始的
		$(srcEle).parent().parent().CRcountDown({
			startNumber: 5,
			callBack: function() {
				$('td:eq(6)', $(srcEle).parent().parent()).html("正在锻炼");
			}
		});
	} else if (countDownMinutes < 0) { // 已过期的
		$(srcEle).parents('tr').find("td:eq(6)").text('锻炼结束');
	}
	$(srcEle).parent().html(validateCode);
};

/**
 * 付款
 */
function pay() {
	if(!confirm('是否已收款？')) {
		return;
	}
	
	var srcEle = this;
	var activityId = $(this).attr('activeId');
	$.post('payForOrder.do', {
		activityId: activityId,
		fieldType: fieldType
	}, function(req){
		if(req == 'true') {
			$(srcEle).parent().parent().parent().find('td:eq(3)').html("<button name='validate' activityId='" + activityId + "'>验证</button>")
			.end().find('td:last').html('等待验证');
			
			$(srcEle).parent().addClass('pay').text('已付款').end().remove();
			$(':input[name=validateCode]').blur(validateCode);
		} else {
			alert('付款失败 ！');
		}
	});
}
