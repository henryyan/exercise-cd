var form = new Form();
var fieldType = '', priceType = '';
$(function() {
	
	fieldType = $('#fieldType').val();
	priceType = $('#priceType').val();

	$('.row tr:even').addClass('even');
	$('.row tr:odd').addClass('odd');

	/*
	 * 根据时间段生成价格设置列表
	 */
	$('#next').bind('click', next);

	$('#setAllPrice').bind('click', setAllPrice);

	$('#cancel').click(function(){
		$('.configure').fadeIn();
		$('#warning,#priceWorkspace,#operation,#notice').fadeOut();
	});

	$('#templatePrice').keyup(function(){
		$(':text[name=price]').val($(this).val());
	});

	form.initFormSubmit('priceForm');

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
 * 删除价格
 */
function deletePrice() {
	if(!confirm('确认删除价格吗？')) {
		return;
	}

	var trDom = $(this).parents('tr');
	var data = trDom.metadata();
	$.get(contextPath + '/deleteObj.do', {
		id: data.pid,
		pn: fieldType + '.price.' + priceType
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

	var data = $(this).parents('tr').metadata();
	$.post('updatePrice.do',{
		id: data.pid,
		priceType: $('#priceType').val(),
		fieldType: $('#fieldType').val(),
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
	// 表单验证
	return true;
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
		alert('价格设置成功');
		location.reload();
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
	if($('#fieldType').val() == '') {
		alert('请选择[场地类型]');
		return;
	} else if($('#lowestTime').val() == '') {
		alert('请输入[最低计时单位]');
		return;
	}

	$('.configure').fadeOut('fast', function(){

		var timeStep = $('#lowestTime').val() * 1;
		var tempStartHour = startTime.substring(0, 2) * 1;
		var tempEndHour = endTime.substring(0, 2) * 1;
		var totalHours = tempEndHour - tempStartHour;

		var ct = totalHours / timeStep;
		var tempPriceWorkspace = "";
		var tempStartTime = addTime(startTime, 0);
		for(var i = 0; i < ct; i++) {
			var singleTime = tempStartTime + "~";
			tempPriceWorkspace += "<div><input type='hidden' name='start' value='" + tempStartTime + "'/>";
			tempStartTime = addTime(tempStartTime, timeStep);

			var tmpHour = tempStartTime.substring(0, 2) * 1;
			if(tmpHour >= tempEndHour) {
				tempStartTime = endTime;
			}
			tempPriceWorkspace += "<input type='hidden' name='end' value='" + tempStartTime + "'/>";

			singleTime += tempStartTime;
			tempPriceWorkspace += "<span class='period'>" + singleTime + "</span><input type='text' id='price' name='price' size='6' maxlength='3'/></div>";
		}

		$('#priceWorkspace').html(tempPriceWorkspace).fadeIn('fast');

		// 显示按钮、注意事项
		$('#operation,#notice').fadeIn();

		$('#priceWorkspace div').hover(function() {
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

function doNext() {

}
