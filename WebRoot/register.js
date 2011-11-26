var common = new Common();
var form = new Form(common);

$(document).ready(function() {
    form.initFormSubmit('registForm');
    $("input[type='text']:first", document.forms[0]).focus();
    
	// 注册验证设置
    validation();
});

function validation() {
    form.initValidatorConfig();
    $("#userName").formValidator({
        onshow: "请输入会员名称",
        onfocus: "注册会员名称(字母至多16位，汉字至多8个)",
        oncorrect: "此会员名称可以使用"
    }).inputValidator({
        min: 2,
        max: 100,
        onerror: "你输入的用户名非法,请确认"
    }).ajaxValidator({
        url: "checkVenueUserNameExist.do",
        success: function(data) {
            // 用户名存在返回false，证明不可以使用，不存在返回 true可以使用
            if (data == 'true') {
                return false;
            } else {
                return true;
            }
        },
        buttons: $('#submit'),
        error: function() {
            common.dialog("服务器没有返回数据，可能服务器忙，请重试", 'error');
        },
        onerror: "该用户名已存在",
        onwait: "正在对用户名进行合法性校验，请稍候..."
    });
    
    $("#password1").formValidator({
        onshow: "请输入密码",
        onfocus: "密码至少6位",
        oncorrect: "密码合法"
    }).inputValidator({
        min: 6,
        empty: {
            leftempty: false,
            rightempty: false,
            emptyerror: "密码两边不能有空符号"
        },
        onerror: "密码长度不够,请确认"
    });
	
    $("#password2").formValidator({
        onshow: "请输入重复密码",
        onfocus: "两次密码必须一致哦",
        oncorrect: "密码一致"
    }).inputValidator({
        min: 6,
        empty: {
            leftempty: false,
            rightempty: false,
            emptyerror: "重复密码两边不能有空符号"
        },
        onerror: "重复密码不能为空,请确认"
    }).compareValidator({
        desid: "password1",
        operateor: "=",
        onerror: "2次密码不一致,请确认"
    });
}

/**
 * 表单提交前
 *
 * @return {Boolean}
 */
function showRequest(formData, jqForm, options) {
    var valid = $.formValidator.pageIsValid('1');
    return valid;
}

/**
 * 表单响应处理
 *
 * @param {} responseText
 * @param {} statusText
 */
function showResponse(responseText, statusText) {
    if (statusText == 'success' && responseText == 'success') {
        alert('注册成功，请点击“确定”后根据向导填写信息……');
        location.href = 'wizard/venue.html';
    } else {
        alert('保存失败，请重试或告知管理员');
    }
}
