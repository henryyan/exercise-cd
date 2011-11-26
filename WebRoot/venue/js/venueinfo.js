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

var openTimeHour, openTimeMinute, closeTimeHour, closeTimeMinute;
var changedOpenTimeHour = changedOpenTimeMinute = changedCloseTimeHour = changedCloseTimeMinute = true;

// 初始化
$(function() {

    common.createHourSelect('#openTimeHour');
    common.createMinuteSelect('#openTimeMinute');
    
    common.createHourSelect('#closeTimeHour');
    common.createMinuteSelect('#closeTimeMinute');
	
	// 修改开馆、闭馆时间后提示重新设置价格
	$('select[name$=TimeHour],select[name$=TimeMinute]').change(function(){
		var venueInfoId = $('#id').val();
		// 只有修改时才提示用户重新设置价格
		if (venueInfoId != 0) {
			var eleId = $(this).attr('id');
			var timeValue = $(this).val();
			if (eleId == 'openTimeHour') {
				if (timeValue == openTimeHour) {
					changedOpenTimeHour = true;
				} else {
					changedOpenTimeHour = false;
				}
			} else if (eleId == 'openTimeMinute') {
				if (timeValue == openTimeMinute) {
					changedOpenTimeMinute = true;
				} else {
					changedOpenTimeMinute = false;
				}
			} else if (eleId == 'closeTimeHour') {
				if (timeValue == closeTimeHour) {
					changedCloseTimeHour = true;
				} else {
					changedCloseTimeHour = false;
				}
			} else if (eleId == 'closeTimeMinute') {
				if (timeValue == closeTimeMinute) {
					changedCloseTimeMinute = true;
				} else {
					changedCloseTimeMinute = false;
				}
			}
			
		}
	});
    
    // 生成区县下拉列表	
    var sectionSelect = new StringBuffer();
    var sections = ["黄浦区", "卢湾区", "徐汇区", "长宁区", "普陀区", "闸北区", "虹口区", "杨浦区", "闵行区", "宝山区", "嘉定区", "浦东新区", "金山区", "松江区", "青浦区", "奉贤区", "崇明县"];
    for (i in sections) {
        sectionSelect.append("<option value='" + sections[i] + "'>" + sections[i] + "</option>");
    }
    $('#district').append(sectionSelect.toString());
    
    // 加载场馆信息
    loadInfo();
    
    // 显示修改界面、隐藏查看界面
    $('#switch').click(function() {
        $('#infoTable').slideUp();
        $('#formTable').slideDown();
    });
    
    // 隐藏修改界面、显示查看界面
    $('#cancleModify').click(function() {
        loadInfo(true);
    });
	
    $('#skip').click(function() {
        loadInfo(true);
    });
    
    $(':button[name=picUpload]').click(function() {
        location.href = 'picUpload.html';
    });
    
    $('#upload').click(function() {
        if ($('#pic').val() == "") {
            alert('请选择要上传的文件！');
            return;
        } else {
            $('#uploadForm').submit();
        }
    });
    
    $('#pic').change(function() {
        $('#fileName').val($(this).val());
    });
    
    // 注册验证设置
    //validation();
	//form.initValidatorConfig();
});

/**
 * 加载场馆信息
 */
function loadInfo(editInfo) {
    // 加载数据 
    $.getJSON('loadVenue.do', function(json) {
        var isNew = false;
        for (key in json) {
        
            if (key == 'id') {
                if ($("#id").length == 0) {
                    if (!json['id']) 
                        isNew = true;
                    var appendId = "<input type='hidden' name='id' id='id' value='" + json['id'] + "'/>";
                    $('#venueInfoForm').append(appendId);
                }
            } else if (key == 'openTime') {
                var openTime = json[key].split(':');
                $('#openTimeHour').val(openTime[0]);
                $('#openTimeMinute').val(openTime[1]);
				openTimeHour = openTime[0];
				openTimeMinute = openTime[1];
            } else if (key == 'closeTime') {
                var closeTime = json[key].split(':');
                $('#closeTimeHour').val(closeTime[0]);
                $('#closeTimeMinute').val(closeTime[1]);
				closeTimeHour = closeTime[0];
				closeTimeMinute = closeTime[1];
            } else if (key == 'photoUrl') {
				$('#venuePicture').html('');
				$('<img/>', {
					src : 'pictures/' + json['id'] + '/thumbnails/120/' + json[key],
					width : 150,
					title : '本场馆主图片；您可以在[场馆图片]中修改',
					alt : '本场馆主图片'
				}).appendTo('#venuePicture');
			}
            
            var tempData = json[key];
            $('#' + key).val(tempData);
			if($('#' + key).length == 0) {
				$("<input type='hidden' id='" + key + "' name='" + key + "' value='" + tempData + "'/>").appendTo('#venueInfoForm');
			}
            
            // 设置查看信息
            if (key == 'photoUrl') {
                $('#i' + key).html(!tempData ? "&nbsp;" : '<img border="0" src="../' + tempData + '"  style="width: 200px;height: 200px;"/>');
            } else {
                $('#i' + key).html(!tempData ? "&nbsp;" : tempData);
            }
            
        }
		
		// 创建隐藏域
        createHidden('venueInfoForm', json);
		
        if (isNew) {
            $('#formTable').slideDown();
            $('#infoTable').slideUp();
        } else {
            // 显示修改按钮
            $('#cancleModify').show();
        }
    });
    
    $('#infoTable').slideDown();
    $('#formTable').slideUp();
}

/**
 * 表单提交前
 *
 * @return {Boolean}
 */
function showRequest(formData, jqForm, options) {
	common.setStatus('statusTip', '正在保存场馆信息……', 'onLoad');
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
    var resTxt = responseText.split('|');
    if (statusText == 'success' && resTxt[0] == 'success') {
        if (resTxt.length > 1 && resTxt[1] != '') {
			// 判断是否需要提示用户重新设置价格
			if (changedOpenTimeHour && changedOpenTimeMinute && changedCloseTimeHour && changedCloseTimeMinute) {
	            location.reload();
			} else {
				alert('您已修改了场馆的开始、结束时间，请重新设置价格！');
				location.href = '../price/priceTab.html';
			}
        }
    } else {
        common.dialog('保存失败，请重试或告知管理员！', 'error');
    }
}
