/*******************************************************************************
 * @功能说明:价格策略管理JS
 * @作者:闫洪磊
 * @创建时间
 * @版本修改记录:
 ******************************************************************************/
var common = new Common();
var form = new Form(common);
var dialog;

$(function() {

    $('.row tr:even').addClass('even');
    $('.row tr:odd').addClass('odd');

    form.initFormSubmit('tacticsForm');//初始化表单提交
    form.initReset();

    // 绑定事件操作
    registeOperation();

    // 增加策略
    $(':button[name=addEle]').bind('click', addTactics);

	// 管理策略日期
	$('#editDate').bind('click', function(){
		var settings = {
            objId: $(this).parent().attr('idv')
        };
		editDate(settings);
	});

    // 关闭遮罩
    $(':button[name=close]').bind('click', $.nyroModalRemove);

    // 设置列表需要鼠标指向提示功能
    setTipInfo();

    // 限制日期选择
    addDatePicker();

});

/**
 * 设置日期选择器，限制开始日期永远小于结束日期
 */
function addDatePicker() {

	// 开始日期
    $('#fromDate').click(function() {
        WdatePicker({
            minDate: strSystemDate,
            onpicked: function(dp) {
                var pickedDate = dp.cal.getDateStr();
                WdatePicker({
                    el: 'toDate',
                    minDate: pickedDate
                });
            }
        });

    });

	// 结束日期
    $('#toDate').click(function() {
        var tempMinDate = $('#fromDate').val() || strSystemDate;
        WdatePicker({
            minDate: tempMinDate
        });
    });
}

/**
 * 设置列表需要鼠标指向提示功能
 */
function setTipInfo() {

	/**
	 * 查看已设置的日期段
	 */
	$('a[name=showDate]').cluetip({
		tracking: true,
		onShow: function(ct, c){
			var id = $(this).parent().attr('idv');
			$.getJSON('tacticsDateList.do', {
				'tactics.id': id
			}, function(result){
				var $dateList = $('#dateList', ct);
				var htmlContent = new StringBuffer();
				$(result).each(function(i, v){
					htmlContent.append("<tr><td>" + v.fromDate + "</td><td>" + v.toDate + "</td></tr>");
				});
				$dateList.append(htmlContent.toString());
			});
		}
	});

	/**
	 * 查看备注
	 */
	$('a[name=detail]').cluetip({
		splitTitle: '|',
  		tracking: true
	});

}

/**
 * 表单提交前
 *
 * @return {Boolean}
 */
function showRequest(formData, jqForm, options) {
    //$('#submit').attr('value', '正在保存……').attr('disabled', true);
    return true;
}

/**
 * 表单响应处理
 *
 * @param {} responseText	响应内容
 * @param {} statusText		状态信息
 */
function showResponse(responseText, statusText) {
    if (statusText == 'success' && responseText == 'success') {
        alert('保存成功');
        window.location.reload();
    } else {
        alert('保存失败：\n' + responseText);
    }
}

/**
 * 添加价格策略
 */
function addTactics() {

	/*
	 * 因为在修改价格时不能修改场地类型，所以在修改时隐藏类型
	 */
	$('#fieldType').attr('disabled', false);

    $.nyroModalManual({
        title: '添加价格策略',
        minHeight: 160,
        url: '#formTemplate'
    });
}

/**
 * 操作：修改、删除
 */
function registeOperation() {
    $('td[name=operate] a').click(function(event) {

        var srcEleName = $(this).attr('name');
        var settings = {
            srcEle: this,
            objId: $(this).parent().attr('idv')
        };

        if (srcEleName == 'modify') {
			modify(settings);
		} else if (srcEleName == 'editDate') {
            editDate(settings);
        } else if (srcEleName == 'delete') {
            if (false && !confirm('确认删除价格策略吗？')) {
                return;
            }
            deleteTactics(settings);
        } else if (srcEleName == 'addDate') {
            addDate(settings);
        }

    });

	$('td[name=operate] :button').click(function(event) {
		var srcEleName = $(this).attr('name');
        var settings = {
            srcEle: this,
            objId: $(this).parent().attr('idv'),
			fieldType: $(this).parent().attr('ft')
        };
		if (srcEleName == 'recreate') {
			if (confirm('确定要重新根据策略生成[更新]活动吗？')) {
				deleteAndBakActivity(settings, function(){
					$(settings.srcEle).hide();
				});
			}
		}
	});
}

/**
 * 修改价格策略
 * @param {Object} settings
 */
function modify(settings) {
	$('#fieldType').attr('disabled', true);
	$.nyroModalManual({
        title: '修改价格策略',
        minHeight: 160,
        url: '#formTemplate',
		endShowContent: function(modal, currentSettings) {
			$.getJSON('../loadObj.do', {
				pn: 'tactics.master',
				id: settings.objId
			}, function(tactics){
				$("<input type='hidden' id='fieldType' name='fieldType'/>").val(tactics['fieldType']).appendTo($('#tacticsForm'));
				for (key in tactics) {
					$('#tacticsForm #' + key).val(tactics[key]);
				}
			});
		}
    });
}

/**
 * 修改价格策略日期
 * @param {Object} settings
 */
function editDate(settings) {
	$.nyroModalManual({
        title: '管理价格策略日期',
        minHeight: 200,
        url: '#tacticsDateListTemplate',
		endShowContent: function(modal, currentSettings){
			$.getJSON('tacticsDateList.do', {
				'tactics.id': settings.objId
			}, function(result){
				var htmlContent = new StringBuffer();
				$(result).each(function(i, v){
					htmlContent.append("<tr><td>" + v.fromDate + "</td><td>" + v.toDate + "</td>")
					.append("<td><a href='javascript:;' name='delete' idv='" + v.id + "'>删除</a><td></tr>");
				});

				var $tacticsDateTable = $('#tacticsDateTable', modal.content);
				$tacticsDateTable.find('tr:not(:eq(0))').remove().end().append(htmlContent.toString());

				// 绑定策略日期删除事件
				$('a[name=delete]', $tacticsDateTable).bind('click',deleteTacticsDate);

				$(':button[name=addTDate]', modal.content).unbind('click').click(function(){
					addDate(settings);
				});
			});
		}
    });
}

/**
 * 删除策略日期
 */
function deleteTacticsDate() {
	if (!confirm('确认删除价格策略日期吗？')) {
        return;
    }
	var trDom = $(this).parent().parent();
	var objId = $(this).attr('idv');
	$.get(contextPath + '/deleteObj.do', {
        id: objId,
        pn: 'tactics.date'
    }, function(rep) {
        if (rep == 'success') {
            trDom.remove();
        } else {
            alert('删除策略日期失败！');
        }
    });
}

/**
 * 删除策略
 */
function deleteTactics(settings) {
	if (!confirm('确认删除价格策略吗？')) {
        return;
    }
    var trDom = $(settings.srcEle).parent().parent();
	var activityTotal = 0;

	$.get('countActivityOfTactics.do', {
		tacticsId: settings.objId,
		fieldType: 'badmintoon'
	}, function(count) {
		if (count > 0) {
			dialog = $.weeboxs.open('', {
				title : '请选择',
				animate : true,
				type : 'prompt',
				height: 80,
				width: 400,
				onopen: function(box) {
					box.find('.dialog-content').css('text-align', 'left');
					var content = '和此策略相关联的活动有<b>' + count + '</b>条，请做具体选择！';
					content += '<ul>'
							+ '<li><b>应用默认策略：</b>使用默认的策略覆盖和此策略相关的活动，不删除相关联活动</li></ul>';
					box.setContent(content);
				},
				okBtnName: '应用默认策略再删除',
				showCancel: false,
				onok: function(){
					applyDefaultTactics(settings.objId);
				}
			});
		} else {
			innerDelete();
		}
	});

	/**
	 * 直接删除策略
	 */
	function innerDelete() {
		$(this).ajaxStart(function(){
			if (dialog) {
				dialog.find(':button').attr('disabled', true);
			}
		}).ajaxComplete(function(){
			if (dialog) {
				dialog.close();
			}
		});
		$.get(contextPath + '/deleteObj.do', {
	        id: settings.objId,
	        pn: 'tactics.master'
	    }, function(rep) {
	        if (rep == 'success') {
	            trDom.remove();

	            // 重置表单
	            if ($('#id').val() == settings.objId) {
	                form.clearForm('tacticsForm');
	            }
	        } else {
	            alert('删除策略失败！');
	        }
	    });
	}

	/**
	 * 把和此策略相关的活动用默认的策略覆盖
	 */
	function applyDefaultTactics(tacticsId) {
		$.blockUI({
	        message:  '<h5><img src="../images/nyroModal/ajaxLoader.gif" class="status"/>正在应用默认策略，请稍候……</h5>',
			css: { width: '400px' }
	    });
		$().ajaxComplete(function(){
			$.unblockUI();
		});

		$.get('applyDefaultTactics.do', {
			tacticsId: tacticsId,
			fieldType: 'badmintoon'
		}, function(rep){
			if (rep == 'success') {
				alert('应用默认策略成功');
				innerDelete();
			} else {
				alert('应用默认策略失败');
			}
		});
	}

}

/**
 * 设置策略日期
 */
function addDate(settings) {
    $.nyroModalManual({
        title: '添加价格策略日期段',
        minHeight: 80,
        url: '#tacticsDateTemplate'
    });

	$('#editDate').unbind('click').click(function(){
		editDate(settings);
	});

    $('#addDate').unbind('click').click(function() {
		var $srcEle = $(this);
		if ($('#fromDate').val() == '' || $('#toDate').val() == '') {
			alert('请设置日期段');
			return false;
		}

		// 按钮控制
		$srcEle.attr('disabled', true);
		$srcEle.ajaxComplete(function(){
			$srcEle.attr('disabled', false);
		});

        $.post('addTacticsDate.do', {
            tacticsId: settings.objId,
            fromDate: $('#fromDate').val(),
            toDate: $('#toDate').val()
        }, function(resp) {
            if (resp == 'success') {
                location.reload();
            } else {
				try {
					var jsonResp = eval('(' + resp + ')');
					if (jsonResp.repeat) {
						var alertInfo = "您选择的日期段和以下策略日期重叠：\n";

						// 和多个策略重叠
						if (jsonResp.datas instanceof Array) {
							var data, dates;
							for ( i in jsonResp.datas) {
								data = jsonResp.datas[i];
								if (i > 0) {
									alertInfo += "\n\n";
								}
								alertInfo += "策略：" + data.name;
								for (var k = 0; k < data.dates.length; k++) {
									dates = data.dates[k];
									alertInfo += '\n    日期：[' + dates[0] + " 至 " + dates[1] + "]";
								}
							}
						} else {
							// 和单个策略重叠
							alertInfo += "策略：" + jsonResp.datas.name;
							var dates;
							for (var i = 0; i < jsonResp.datas.dates.length; i++) {
								dates = jsonResp.datas.dates[i];
								alertInfo += '\n    日期：[' + dates[0] + " 至 " + dates[1] + "]";
							}
						}

						alert(alertInfo);
					}
				} catch (e) {
					alert('添加策略日期失败：\n' + resp);
				}
            }
        });
    });
}

/**
 * 清空策略范围内的活动，并重新生成，已预定的备份到策略活动备份表中
 */
function deleteAndBakActivity(settings, callback) {

	$.blockUI({
        message:  '<h5><img src="../images/nyroModal/ajaxLoader.gif" class="status"/>正在更新已生成的活动，请稍候……</h5>',
		css: { width: '400px' }
    });
	$(settings.srcEle).ajaxComplete(function(){
		$.unblockUI();
	});
	
    $.post('deleteAndBakActivity.do', {
        tacticsId: settings.objId,
		fieldType: settings.fieldType
    }, function(req) {
        if (req == 'success') {

            alert('重新生成活动成功\请检查[待处理活动]列表对活动进行处理');
			if ($.isFunction(callback)) {
				callback();
			}
        } else {
            alert('重新生成活动失败');
        }
    });

}