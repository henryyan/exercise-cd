var common = new Common();
var list = new List(common);
var dialog = null;
var maxIssueDate = null;
$(function() {

	$('.row tr:even').addClass('even');
	$('.row tr:odd').addClass('odd');

	//list.regSelectAll();// 注册全选按钮
	//list.regActive();// 注册选择行事件

	// 修改提前生成天数
    $('#modifyCreateAdvance').click(function() {
		var ids = list.getCheckbox();
		if (ids.length == 0) {
			alert('请选择场地！');
			return;
		}
        var srcEle = this;
        dialog = $.weeboxs.open('#changeCreateAdvance', {
            title: '修改提前生成天数',
            width: 300,
            height: 70,
            animate: true,
            onok: function() {
                changeCreateAdvance(srcEle, $(srcEle).attr('idv'));
            }
        });
    });

	// 修改提前生成天数
    $('#modifyIssueAdvance').click(function() {
		var ids = list.getCheckbox();
		if (ids.length == 0) {
			alert('请选择场地！');
			return;
		}
        var srcEle = this;
        dialog = $.weeboxs.open('#changeIssueAdvance', {
            title: '修改提前发布天数',
            width: 300,
            height: 70,
            animate: true,
            onok: function() {
                changeIssueAdvance(srcEle, $(srcEle).attr('idv'));
            }
        });
    });

    $('#lastDate').click(function() {
		// 第一次点击的时候去加载最大
		if (maxIssueDate == null) {
			$.get('getMaxIssueDays.do', {fieldType: $('#fieldType').val()}, function(repMaxDate) {
				var maxDate = parseInt(repMaxDate) + 30;
				var tempMaxDate = common.dateAdd(systemDate, maxDate, 'd');
				maxIssueDate = tempMaxDate;
				WdatePicker({
					el: 'lastDate',
					minDate: systemDate,
					maxDate: tempMaxDate
				});
			});
		} else {
			WdatePicker({
				el: 'lastDate',
				minDate: systemDate,
				maxDate: maxIssueDate
			});
		}
    });

    $('#issueActivity').click(function(event) {
        if ($('#lastDate').val() != '') {
			$.blockUI({
	            message:  '<h5><img src="../images/nyroModal/ajaxLoader.gif" class="status"/>正在生成场地活动，请稍候……</h5>',
				css: { width: '350px' }
	        });
			$(this).ajaxComplete($.unblockUI);
            $.post('issueActivity.do', {
                lastDate: $('#lastDate').val(),
				fieldType: $('#fieldType').val()
            }, function(req) {
                if (req == 'success') {
					$.unblockUI;
                    alert('活动已生成');
                    location.reload();
                } else {
                    alert('生成活动失败');
                }
            });
        } else {
            alert('请选择最后生成日期');
            WdatePicker({
                el: 'lastDate',
                minDate: common.getCurrentDate()
            });
        }
    });
});

/**
 * 修改提前生成天数
 */
function changeCreateAdvance(srcEle) {
	var advance = dialog.find('#createAdvance').val();
	if(advance == '') {
		alert('请输入提前天数');
		return;
	}
	var fieldIds = list.getCheckbox();
    $.post('../field/changeAdvance.do', {
		ids: fieldIds,
		advance: advance,
		advanceType: 'create',
		fieldType: $('#fieldType').val()
	}, function(req) {
		dialog.close();
		$('#row :checkbox:checked').parents('tr').find('.create .advanceSpan').html(advance);
    });
}

/**
 * 修改提前发布天数
 */
function changeIssueAdvance(srcEle, fieldId) {
	var advance = dialog.find('#issueAdvance').val();
	if(advance == '') {
		alert('请输入提前天数');
		return;
	}
	var fieldIds = list.getCheckbox();
    $.post('../field/changeAdvance.do', {
		ids: fieldIds,
		advance: advance,
		advanceType: 'issue',
		fieldType: $('#fieldType').val()
	}, function(req) {
		dialog.close();
		$('#row :checkbox:checked').parents('tr').find('.issue .advanceSpan').html(advance);
    });
}
