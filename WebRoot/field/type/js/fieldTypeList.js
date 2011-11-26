$(function(){
	
	// 列表斑马风格
	$('.row tr:even').addClass('even');
    $('.row tr:odd').addClass('odd');
	
	// 开启、关闭
	$('.enable :radio').bind('change', changeEnable);
	
	// jQuery UI Radio To Button
	//$('.enable').buttonset();
	
	if (parent && parent.parent && parent.parent.loadFieldTypeForLeftMenu) {
		parent.parent.loadFieldTypeForLeftMenu();
	}
	
});

/**
 * 场地的‘开启’、‘关闭’
 */
function changeEnable() {
	var srcEle = this;
	$(this).ajaxStart(function(){
		$('.row').block();
	}).ajaxStop(function(){
		$('.row').unblock();
	});
	
	var data = $(this).parents('tr').metadata();
	$.post(ctx + '/field/type/linkFieldType.do', {
		fieldTypeId: data.fid,
		enable: $(srcEle).val()
	}, function(resp){
		if (resp != 'success') {
			alert('操作失败');
		} else {
			// 重新设置首页左栏的菜单
			if (parent && parent.parent && parent.parent.loadFieldTypeForLeftMenu) {
				parent.parent.loadFieldTypeForLeftMenu();
			}
		}
	});
}

/**
 * 向导控制，判断是否设置了场地类型
 * @param {Object} callback
 */
function doNext(callback) {
	var enableLength = 0;
	$('.radioTr').each(function(){
		enableLength += $(':radio[value=true]:checked', this).length;
	});
	if (enableLength == 0) {
		alert('请设置本场馆拥有的场地类型');
	} else if (enableLength > 0) {
		callback();
	}
}
