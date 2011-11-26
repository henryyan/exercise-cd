/*******************************************************************************
 * @功能说明：资金状况JS
 * @作者：闫洪磊
 ******************************************************************************/

$(function(){

	// 报表日期选择
	$('#reportDate').click(function(){
		WdatePicker({
			el: 'reportDate',
			maxDate: strSystemDate,
	        onpicked: function(dp) {
	           var pickedDate = dp.cal.getDateStr();
	           $('#reportForm').submit();
	        }
	   });
	});

	$.blockUI({
        message:  '<h5><img src="../images/nyroModal/ajaxLoader.gif" class="status"/>正在更新并生成资金状况报表，请稍候……</h5>',
		css: { width: '350px' }
    });

	// 异常处理
	$('#cashTable').ajaxComplete(function(event,request, settings){
	     $.unblockUI();
	});

	// 加载资金数据
	loadCashData();

	$('tr', '#cashTable,#unCashTable').hover(function(){
		$('td', this).addClass("cl");
	}, function(){
		$('td', this).removeClass("cl");
	});
});

/**
 * 加载资金数据
 */
function loadCashData() {

	var maps = new Map();

	// 现金
	maps.put("即时发生的现金", "jsfsxj");
	maps.put("以后发生的现金", "drsdhxj");
	maps.put("会员卡销售的现金", "cshykxj");
	maps.put("现金小计", "xjxj");

	// 非现金
	maps.put("以前收到现金并当日锻炼实现的收入", "yqsdhdrsr");
	maps.put("会员卡消费实现的收入", "hykxs");
	maps.put("非现金小计", "fxjxj");

	// 报表日期
	var reportDate = $('#reportDate').val();
	if (!reportDate) {
		reportDate = strSystemDate;
	}

	$.getJSON('reportCash.do', {
		reportDate: reportDate
	}, function(datas){
		$(datas).each(function(i, v){

			if (v.amountType == '合计及结余') {
				// 非现金合计及结余
				var $dataTr = $('.fxjhjjy');
				$('.current:eq(0)', $dataTr).html(v.currentFields);
				$('.after:eq(0)', $dataTr).html(v.nextFields);
				$('.cash', $dataTr).html(v.cash);
			} else {
				var $dataTr = $('.' + maps.get(v.amountType));
				$('.current:eq(0)', $dataTr).html(v.currentFields);
				$('.current:eq(1)', $dataTr).html(v.currentFees);
				$('.after:eq(0)', $dataTr).html(v.nextFields);
				$('.after:eq(1)', $dataTr).html(v.nextFees);
				$('.cash', $dataTr).html(v.cash);
			}

		});

	});
}
