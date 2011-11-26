var common = new Common();
var form = new Form(common);

$(function() {
    
	$('.row tr:even').addClass('even');
	$('.row tr:odd').addClass('odd');
	
	// 创建时间段下拉框
	initPeriod();
	
	form.initFormSubmit('priceForm');
	
	/*
	 * 根据时间段生成价格设置列表
	 */
	$('#next').bind('click', next);
	
	// 批量设置价格
	$('#setAllPrice').bind('click', setAllPrice);
	
	// 取消设置价格
	$('#cancel').click(function(){
		$('.configure').fadeIn();
		$('#priceWarning,#priceWorkspace,#operation,#notice').fadeOut();
	});
	
	$('#templatePrice').keyup(function(){
		if (!isNaN($(this).val())) {
			$(':text[name=price]').val($(this).val());
			validatePrice();
		}
	});
	
	/**********************价格的CRUD-开始**************************/
	
	// 修改
	$('a[name=update]').bind('click', updatePrice);
	
	// 保存价格
	$('a[name=save]').bind('click', savePrice);
	
	// 取消
	$('a[name=cancel]').click(function(){
		$('#showPrice,#price', $(this).parent()).toggle();
		$(this).hide().parent().find('a[name=update]').show().end().find('a[name=save]').hide();
	});
	
	// 删除
	$('a[name=delete]').bind('click', deletePrice);
	
	/**********************价格的CRUD-结束**************************/
});

/**
 * 初始化时段
 */
function initPeriod() {
	var openTimeArray = openTime.split(':');
	var closeTimeArray = closeTime.split(':');
	
	common.createHourSelect('#fromTimeHour', openTimeArray[0]);
    common.createMinuteSelect('#fromTimeMinute', openTimeArray[1]);
    
    common.createHourSelect('#toTimeHour', closeTimeArray[0]);
    common.createMinuteSelect('#toTimeMinute', closeTimeArray[1]);
}

/**
 * 删除价格
 */
function deletePrice() {
	if(!confirm('确认删除价格吗？')) {
		return;
	}
	
	var trDom = $(this).parent().parent();
	var priceId = $(this).attr('idv');
	$.get(contextPath + '/deleteObj.do', {
		id: priceId,
		pn: 'tactics.price'
	}, function(rep) {
		if(rep == 'success') {
			trDom.remove();
		} else {
			alert('删除失败！');
		}
	});
}

/**
 * 修改控制DOM
 */
function updatePrice() {
	$('#showPrice,#price', $(this).parent()).toggle();
	$('#price', $(this).parent()).val($('#showPrice', $(this).parent()).text()).focus();
	$(this).hide().parent().find('a[name=cancel],a[name=save]').show();
	
	$('#price', $(this).parent()).keydown(function(event){
		if (event.keyCode == 13) {
			$(this).parent().find('a[name=save]').trigger('click');
			return false;
		}
	});
}

/**
 * 保存新价格
 */
function savePrice() {
	var srcEle = this;
	var price = $('#price', $(this).parent()).val();
	if(price == '') {
		return;
	}
	
	$.post('updatePrice.do',{
		id: $(this).attr('idv'),
		price: price
	}, function(req){
		if(req == 'error') {
			alert('保存失败');
		} else {
			$('#showPrice', $(srcEle).parent()).text($('#price', $(srcEle).parent()).val());
		}
		$('#showPrice,#price', $(srcEle).parent()).toggle();
		$(srcEle).hide().parent().find('a[name=update]').show().end().find('a[name=cancel]').hide();
		
	});
}

/**
 * 表单提交前
 *
 * @return {Boolean}
 */
function showRequest(formData, jqForm, options) {
	var empty = validatePrice();
	
	// 表单验证
	return true;
}

function validatePrice() {
	var empty = false;
	var $prices = $(':text[name=price]');
	for (var i = $prices.length - 1; i >= 0; i--){
		var p = $prices[i];
		if (!$(p).val() && !isNaN($(this).val())) {
			empty = true;
			$(p).addClass('empty').blur(function(){
				if ($(this).val() && !isNaN($(this).val())) {
					$(this).removeClass('empty');
				}
			});
		} else {
			$(p).removeClass('empty');
		}
	};
	return empty;
}

/**
 * 表单响应处理
 *
 * @param {}
 *            responseText
 * @param {}
 *            statusText
 */
function showResponse(responseText, statusText) {
	if (statusText == 'success' && responseText == 'success') {
		//alert('价格设置成功');
		location.reload();
		/*deleteAndBakActivity(function(){
			location.reload();
		});*/
		
	}
}

function setAllPrice() {
	if($('#templatePrice').val() == '') {
		alert('请输入价格');
		$('#templatePrice').focus();
		return;
	} else {
		$(':text[name=price]').val($('#templatePrice').val());
	}
}

function next() {
	
	if(!$('#lowestTime').val()) {
		alert('请输入[最低计时单位]');
		return;
	} else if (!$('#fromTimeHour').val() || !$('#fromTimeMinute').val()
			|| !$('#toTimeHour').val() || !$('#toTimeMinute').val()) {
		alert('请设置完整开始、结束时间段');
	}
	
	$('#priceWarning').fadeIn();
	
	$('.configure').fadeOut('fast', function(){
		
		var fromTime = $('#fromTimeHour').val() + ":" + $('#fromTimeMinute').val();
		var toTime = $('#toTimeHour').val() + ":" + $('#toTimeMinute').val();
		
		var timeStep = $('#lowestTime').val() * 1;
		var tempStartHour = fromTime.substring(0, 2) * 1;
		var tempEndHour = toTime.substring(0, 2) * 1;
		var totalHours = tempEndHour - tempStartHour;
		
		var ct = totalHours / timeStep;
		var tempPriceWorkspace = "";
		var tempfromTime = addTime(fromTime, 0);
		for(var i = 0; i < ct; i++) {
			var singleTime = tempfromTime + "~";
			tempPriceWorkspace += "<tr><input type='hidden' name='start' value='" + tempfromTime + "'/>";
			tempfromTime = addTime(tempfromTime, timeStep);
			
			var tmpHour = tempfromTime.substring(0, 2) * 1;
			if(tmpHour >= tempEndHour) {
				tempfromTime = toTime;
			}
			tempPriceWorkspace += "<input type='hidden' name='end' value='" + tempfromTime + "'/>";
			
			singleTime += tempfromTime;
			tempPriceWorkspace += "<td width='90' style='text-align:center'>" + singleTime 
				+ "</td><td class='period'><input type='text' id='price' name='price' size='6' maxlength='3'/></td></tr>";
		}
		
		$('#periodTable').html(tempPriceWorkspace).parent().fadeIn('fast');
		
		// 显示按钮、注意事项
		$('#operation,#notice').fadeIn();
		
		$('#priceWorkspace tr').hover(function() {
			$(this).addClass('active');
			$('#price', this).focus();
		}, function() {
			$(this).removeClass('active');
		});
		
		// 显示警告信息
		$('#warning').show();
	});
}

function addTime(tempTime, step) {
	var result = tempTime.split(':')[0] * 1 + step;
	result += ':' + tempTime.split(':')[1];
	return result;
}

/**
 * 清空策略范围内的活动，并重新生成，已预定的备份到策略活动备份表中
 */
function deleteAndBakActivity(callback) {
	
	$.blockUI({ 
        message:  '<h5><img src="../images/nyroModal/ajaxLoader.gif" class="status"/>正在更新已生成的活动，请稍候……</h5>',
		css: { width: '400px' }
    });
    $.post('deleteAndBakActivity.do', {
        tacticsId: $('#tacticsId').val()
    }, function(req) {
        if (req == 'success') {
			$.unblockUI;
            alert('重新生成活动成功\请检查[待处理活动]列表对活动进行处理');
			if ($.isFunction(callback)) {
				callback();
			}
        } else {
            alert('重新生成活动失败');
        }
    });
	
}

function doNext() {
	
}
