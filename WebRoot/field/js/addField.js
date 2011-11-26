/**
 * 功能：添加场地
 * @author 闫洪磊
 * @createdate 1.0.0.20090604
 * @版本记录:
 */

/** **************************定义常量************************ */
var common = new Common();
var form = new Form(common);

// 初始化
$(function() {
	form.initFormSubmit('fieldForm');//初始化表单提交
	form.initReset();
	
	// 把焦点定位到fieldTotal中
	$('#fieldTotal').focus();

	$('#backBtn').click(function(){
		location.href = contextPath + '/field/fieldList.do?fieldType=' + $('#fieldType').val();
	});
});

function checkForm() {
	if($('#fieldTotal').val() == "") {
		alert('请输入场地数量!');
		return false;
	} else if($(':radio[name=envType]:checked').length == 0) {
		alert('请选择场地环境类型!');
		return false;
	} else if($('#advance').val() == "") {
		alert('请输入可提前生成天数!');
		return false;
	} else if($('#issueAdvance').val() == "") {
		alert('请输入可提前发布天数!');
		return false;
	}
	return true;
}

/**
 * 表单提交前
 *
 * @return {Boolean}
 */
function showRequest(formData, jqForm, options) {
	$('#submit').attr('value', '正在保存……').attr('disabled', true);
	return checkForm();
	// 检验表单完整性
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
	$('#submit').attr('value', '保 存').attr('disabled', false);
	if (statusText == 'success' && responseText == 'success') {
		alert('保存成功');
		window.location.href = contextPath + '/field/fieldList.do?fieldType=' + $('#fieldType').val();
	} else {
		alert('保存失败，请重试或告知管理员');
	}
}

function doNext() {
	$('#fieldForm').submit();
}
