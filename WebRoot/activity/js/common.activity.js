/*******************************************************************************
 * @功能说明：场地公共函数
 * @作者： 闫洪磊
 * @创建时间：2009-06-30
 * @修改记录：
 ******************************************************************************/

$(function(){
	
	/**
	 * 显示订单详细
	 */
	showOrderDetail();
	
	$('.row tr:even').addClass('even');
	$('.row tr:odd').addClass('odd');
	
});

/**
 * 显示订单详细
 */
function showOrderDetail() {
	if (!$.fn.tipTip) {return;}
	// 提示
	$('a.basic').tipTip({
		content: $('#orderInfo').html(),
		enter: function(tipObj) {
			var orderId = $(tipObj).attr('orderId');
			$.getJSON('loadOrder.do', {
                id: orderId
            }, function(order) {
                for (key in order) {
                    $('#d' + key, '#tiptip_content').html(order[key]);
                }
            });
		},
		exit: function(tipObj) {
			$('span', '#tiptip_content').text('');
		}
	});
}