var common = new Common();
var form = new Form(common);

$(function() {
	
	if(parent != window) {
		getTopWin().location.href = "index.jsp";
	}
	
	// 回车后登录
	$('#loginName,#password').keypress(function(event){
		if (event.keyCode == 13) {
			$('#loginForm').submit();
		}
	});
	
	$('#login').click(function(){
		$('#loginForm').submit();
	});
	
	$('#regist').click(function(){
		window.location.href = "register.jsp";
	});
	
	// 注册表单提交事件
	form.initFormSubmit('loginForm');//初始化表单提交
	regFormValidator();
	if (common.getCookie('loginName') != null) {
		$('#loginName').val(common.getCookie('loginName'));
		$('#password').val(common.getCookie('password'));
		if (true || common.getCookie('ifSave')) {
			$('#ifSave').attr('checked', true);
		}
		jQuery.formValidator.pageIsValid('1');
	}
	
	if($('#loginName').val() == '') {
		$(":text:first", document.forms[0]).focus();
	}
	
	$("#getBackPwd a").click(function(){
		if(!confirm('确定要找回密码吗？')) {
			return;
		}
		
		if($('#loginName').val() == '') {
			alert('请输入用户名');
		} else {
			$.post('getBackPwd.do', {
				userName: $('#loginName').val()
			}, function(req){
				if(req != 'success') {
					alert(req);
				} else {
					alert('密码已发送到场馆管理员手机.');
				}
			});
		}
		
	});
});

/**
 * 设置表单验证规则
 */
function regFormValidator() {
	$.formValidator.initConfig({formid:"loginForm"});
	$("#loginName").formValidator({
		onshow : "请输入用户名",
		onfocus : "用户名至少2个字,最多4个字"
	}).inputValidator({
		min : 1,
		onerror : "你输入的用户名非法,请确认"
	});
	
	$("#password").formValidator({
		onshow : "请输入密码"
	}).inputValidator({
		min : 4,
		onerror : "密码在6位以上，请确认"
	});
}

/**
 * 表单提交前
 * 
 * @return {Boolean}
 */
function showRequest(formData, jqForm, options) {
	var valid = jQuery.formValidator.pageIsValid('1');
	if (valid) {
		$('#submit').attr('value', '正在登录……').attr('disabled', true);
		return true;
	} else {
		return false;
	}
}

/**
 * 响应结束后
 * @param {} responseText
 * @param {} statusText
 */
function showResponse(responseText, statusText) {
	if (statusText == 'success') {
		// 根据返回消息判断跳转首页还是向导
		if(responseText == 'success') {
			saveUserInfo2Cookie();
			location.href = 'main2.jsp';
		} else if(responseText == 'wizard_venue') {
			saveUserInfo2Cookie();
			location.href = 'wizard/venue.html';
		} else if(responseText == 'wizard_verify') {
			saveUserInfo2Cookie();
			location.href = 'wizard/verify.html';
		} else if(responseText == 'wizard_field') {
			saveUserInfo2Cookie();
			location.href = 'wizard/field.jsp';
		} else if(responseText == 'error') {
			common.dialog('登录失败，用户名或密码错误，请重试！', 'error');
			$('#password').val('');
		}
		
	} else {
		common.dialog('登录失败，用户名或密码错误，请重试！', 'error');
		$('#submit').attr('value', '登 陆').attr('disabled', false);;
	}
}

/**
 * 保存用户名、密码
 */
function saveUserInfo2Cookie() {
	if (true || $('#ifSave').attr('checked')) {
		common.saveCookie("loginName", $('#loginName').val(), 365);
		common.saveCookie("password", $('#password').val(), 365);
		common.saveCookie("ifSave", true, 365);
	} else {
		common.deleteCookie('loginName');
		common.deleteCookie('password');
		common.deleteCookie('ifSave');
	}
}
