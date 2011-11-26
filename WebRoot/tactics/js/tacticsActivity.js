var common = new Common();

$(function() {

    /**
     * 取消预定
     */
    $(':button[name=unbooking]').bind('click', unbooking);
	
});


/**
 * 取消场地预订
 */
function unbooking() {
    var srcEle = this;
	var $parentTr = $(srcEle).parent().parent();
    var orderId = $parentTr.find('#orderId').val();
    var maxPrice = $parentTr.find('#paymentSum').val();
    var dialog = $.weeboxs.open('#unbookingTemplate', {
        title: '取消预订场地',
        width: 260,
        height: 70,
        animate: true,
        onopen: function(box) {
            $.getJSON(contextPath + '/activity/loadOrder.do', {
                id: orderId
            }, function(order) {
                var paidTotal = parseFloat(order.paymentSum);
                if (paidTotal > 0) {
                    $('#refundmentDiv').fadeIn().find('#maxRefundment').text(paidTotal).end().find("#refundment").attr({
                        disabled: '',
                        title: ''
                    });
                } else {
                    box.setButtonTitle('ok', ' 是 ');
                    box.setButtonTitle('cancel', ' 否 ');
                    $('#withoutRefundment').fadeIn();
                }
				
				// 取消预订时设置实际支付金额
				$('#orgPrice').click(function(){
					box.find('#refundment').val(box.find('#maxRefundment').text());
				});
				
            });
        },
        onok: function() {
            var refundment = $('#refundment').val() * 1;
            if (!isNaN(refundment)) {
                if (refundment > maxPrice * 1) {
                    $('#refundment').val('');
                    alert('不能大于实际支付金额');
                } else {
					$(this).ajaxComplete(function(){
						 dialog.close();
					});
                    $.getJSON(contextPath + '/activity/orderHandle.do', {
                        activityIds: $parentTr.find("#chk").val(),
                        refundment: refundment,
                        opType: 'cancel',
						activityType: 'tactics'
                    }, function(req) {
						dialog.close();
						
						var alertInfo = "";
						var allResult = true;
						if (req.handle == true) {
							alertInfo += "<span class='respSuccess'>取消成功</span>";
	                        $(srcEle).hide().parent().parent().find(':button[name=validate]').hide();
	                        $(srcEle).parent().find(':button[name=booking],:button[name=preserving]').show();
	                        $(srcEle).parent().find("#status").text('已取消').end().find('#paymentStatus').hide();
							
						}
						if (!req.sendSms || req.sendSms == false) {
							allResult = false;
							alertInfo += "<br/>->短信发送失败，原因：" + req.smsError;
						}
						
						if (allResult) {
							common.dialog(alertInfo, 'success');
						} else {
							$.weeboxs.open(alertInfo, {
								title : '请确认',
								animate : true,
								focus : '.dialog-ok',
								type : 'warning',
								height : 70,
								onclose: function() {
									location.reload();
								}
							});
						}
						
                    });
                }
				//dialog.close();
            } else {
                alert('请输入数字！');
                $('#refundment').val('');
            }
        }
    });
}
