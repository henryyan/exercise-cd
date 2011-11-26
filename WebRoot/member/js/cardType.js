/** **************************定义常量************************ */
var common = new Common();
var form = new Form(common);

$(function() {

    $('.row tr:even').addClass('even');
    $('.row tr:odd').addClass('odd');
    
    form.initFormSubmit('cardTypeForm');//初始化表单提交
    form.initReset(function() {
        // 重置后触发更改折扣方式
        $('#discountType').val('1').trigger('change');
    });
    
    $(':submit').click(function() {
        var s = "";
        $(':checkbox').each(function() {
            s += $(this).attr('checked') ? "1" : "0";
        });
        $('#discountTime').val(s);
    });
    
    $('#discountType').bind('change', changeDiscountType);
    
    $('#operate a').click(function(event) {
        var optionTag = $(this).text();
        var cardTypeId = $(this).attr('idv');
        if (optionTag == '修改') {
            $.getJSON("loadCardType.do", {
                id: cardTypeId
            }, function(type) {
                for (key in type) {
                    if (key == 'discountTime') {
                        var val = type[key];
                        if (/1[0-1][0-1]/i.test(val)) {
                            $('#common').attr('checked', true);
                        }
                        if (/[0-1]1[0-1]/i.test(val)) {
                            $('#weekday').attr('checked', true);
                        }
                    } else {
                        $('#' + key).val(type[key]);
                    }
                }
                
                // 根据折扣类型相应设置
                if (type.discountType == '1') {
                    $('#discountType1').show();
                    $('#discountType2').hide();
                } else if (type.discountType == '2') {
                    $('#discountType1').hide();
                    $('#discountType2').show();
                    $('#moneyAmountSpan').text(type.moneyAmount);
                    var session = calcDiscount(type.moneyAmount, type.discountPrice);
                    $('#discountSession').val(session);
                    
                    // 绑定事件
                    $('#moneyAmount').unbind('keyup').bind('keyup', changeMoney);
                    $('#discountPrice').unbind('keyup').bind('keyup', changePrice);
                }
            });
        } else if (optionTag == '删除') {
        
            if (!confirm('确认删除该类型吗？')) {
                return;
            }
            
            var trDom = $(this).parent().parent();
            $.get(contextPath + '/deleteObj.do', {
                id: cardTypeId,
                pn: 'card.type'
            }, function(rep) {
                if (rep == 'success') {
                    trDom.remove();
                    
                    // 重置表单
                    if ($('#id').val() == cardTypeId) {
                        form.clearForm('cardTypeForm');
                    }
                } else {
                    alert('删除失败！');
                }
            });
        }
        
    });
    
});

/**
 * 表单提交前
 *
 * @return {Boolean}
 */
function showRequest(formData, jqForm, options) {
    //$('#submit').attr('value', '正在保存……').attr('disabled', true);
    return true;
    // 检验表单完整性
    // .........
}

/**
 * 表单响应处理
 *
 * @param {} responseText
 * @param {} statusText
 */
function showResponse(responseText, statusText) {
    if (statusText == 'success' && responseText == 'success') {
        alert('保存成功');
        window.location.reload();
    } else {
        alert('保存失败，请重试或告知管理员');
    }
}

/**
 * 更换折扣方式
 */
function changeDiscountType() {
    var type = $(this).val();
    if (type == '1') {
        // 清空折扣单价
        $('#discountPrice').val('');
        $('#discountType1').show();
        $('#discountType2').hide();
    } else if (type == '2') {
        // 清空折扣率
        $('#discountRate').val('');
        
        $('#discountType1').hide();
        $('#discountType2').show();
        
        $('#moneyAmountSpan').text($('#moneyAmount').val());
        $('#moneyAmount').unbind('keyup').bind('keyup', changeMoney);
        $('#discountPrice').unbind('keyup').bind('keyup', changePrice);
    }
}

/**
 * 改变卡内余额
 */
function changeMoney() {
    $('#moneyAmountSpan').text($(this).val());
}

/**
 * 改变单价计算可用场次
 */
function changePrice() {
    var tempAmount = $('#moneyAmount').val();
    var tempPrice = $(this).val();
    if (!tempAmount || !tempPrice) {
        $('#discountSession').val('');
        return;
    }
    var moneyAmount = parseFloat(tempAmount);
    var discountPrice = parseFloat(tempPrice);
    var session = calcDiscount(moneyAmount, discountPrice);
    $('#discountSession').val(session);
}

/**
 * 计算场次
 * @param {Integer} moneyAmount		总额
 * @param {Integer} discountPrice	单价
 * @return 可以使用场次
 */
function calcDiscount(moneyAmount, discountPrice) {
    return parseInt(moneyAmount / discountPrice);
}
