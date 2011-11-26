/**
 * 功能：场馆信息维护
 * @author 闫洪磊
 * @createdate 1.0.0.20090628
 * @版本记录:
 */
/** **************************定义常量************************ */
var common = new Common();
var list = new List(common);
var form = new Form(common, list);

// 初始化
$(function() {

    common.createHourSelect('#openTimeHour');
    common.createMinuteSelect('#openTimeMinute');
    
    common.createHourSelect('#closeTimeHour');
    common.createMinuteSelect('#closeTimeMinute');
    
    // 生成区县下拉列表	
    var sectionSelect = new StringBuffer();
    var sections = ["黄浦区", "卢湾区", "徐汇区", "长宁区", "普陀区", "闸北区", "虹口区", "杨浦区", "闵行区", "宝山区", "嘉定区", "浦东新区", "金山区", "松江区", "青浦区", "奉贤区", "崇明县"];
    for (i in sections) {
        sectionSelect.append("<option value='" + sections[i] + "'>" + sections[i] + "</option>");
    }
    $('#district').append(sectionSelect.toString());
    
    // 加载场馆信息
    loadInfo();
    
    // 绝对定位真实信息提醒DIV
    var venueInfoOffset = $('#venueInfoTable').offset();
    var waringInfoLeft = venueInfoOffset.left + $('#venueInfoTable').width() + 20;
    $('#waringInfo').css({top: venueInfoOffset.top, left: waringInfoLeft});
    
    // 圆角并且用动画展示提醒
    $('#waringInfo').corner().slideUp('slow', function(){
    	$(this).slideDown('slow');
    });
    
});

/**
 * 加载场馆信息
 */
function loadInfo() {
    // 加载数据 
    $.getJSON('loadVenue.do', function(json) {
        var isNew = false;
        for (key in json) {
        
            if (key == 'id') {
                if ($("#id").length == 0) {
                    if (!json['id']) {
                        isNew = true;
                    }
                }
            } else if (key == 'openTime') {
                var openTime = json[key].split(':');
                $('#openTimeHour').val(openTime[0]);
                $('#openTimeMinute').val(openTime[1]);
            } else if (key == 'closeTime') {
                var openTime = json[key].split(':');
                $('#closeTimeHour').val(openTime[0]);
                $('#closeTimeMinute').val(openTime[1]);
            } else {
                var tempData = json[key];
                $('#' + key).val(tempData);
            }
            
        }
        createHidden('venueInfoForm', json);
    });
}

/**
 * 表单提交前
 *
 * @return {Boolean}
 */
function showRequest(formData, jqForm, options) {
	common.setStatus('statusTip', '正在保存场馆信息并发送验证邮件……', 'onLoad');
    return true;
}

var wizardCallback;
/**
 * 表单响应处理
 *
 * @param {}
 *            responseText
 * @param {}
 *            statusText
 */
function showResponse(responseText, statusText) {
    var resTxt = responseText.split('|');
    if (statusText == 'success' && resTxt[0] == 'success') {
        if (resTxt.length > 1 && resTxt[1] != '') {
            //增加隐藏域ID
            if ($("#id").length == 0) {
                var appendId = "<input type='hidden' name='id' id='id' value='" + resTxt[1] + "'/>";
                $('#venueInfoForm').append(appendId);
            }
        }
        common.setStatus('statusTip', '保存成功', 'onCorrect');
        wizardCallback();
    } else {
        alert('保存失败，请重试或告知管理员！');
    }
}

function doNext(callback) {
    $('#venueInfoForm').submit();
    wizardCallback = callback;
}
