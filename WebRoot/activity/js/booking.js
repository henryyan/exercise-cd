/*******************************************************************************
 * @功能说明：场地预订
 * @作者： 闫洪磊
 * @创建时间：2009-06-30
 * @修改记录：
 * 1、改为批量操作前版本：116
 ******************************************************************************/
var common = new Common();
var list = new List(common);
var form = new Form(common, list);
var dialog = null;

$(function() {
	
    /**
     * 取消预定
     */
    $(':button[name=unbooking]').bind('click', unbooking);
	
    /**
     * 预订
     */
    $(':button[name=booking]').bind('click', booking);
	
	/**
	 * 补登计
	 */
	$(':button[name=patchBook]').bind('click', patchBook);
    
    /**
     * 修改订单信息
     */
    $('a[name=updateOrderInfo]').live('click', updateOrderInfo);
    
    /**
     * 保留场地
     */
    $(':button[name=preserving]').bind('click', preserving);
    
    /**
     * 取消保留场地
     */
    $(':button[name=unpreserving]').bind('click', unpreserving);
    
    /**
     * 预订中的付款
     */
    $(':button[name=pay]').live('click', pay);
	
	// 根据预订、保留状态显示checkbox组件
	showCheckbox();
	
	/**
	 * 状态选择器
	 */
	$('#selector a').bind('click', selector);
	
	/**
	 * 批量预订
	 */
	$('#batchBooking').bind('click', batchBooking);
	
	/**
	 * 批量保留
	 */
	$('#batchPreserving').bind('click', batchPreserving);
	
	// 全选
	$('#chkAll').click(function(){
        if ($(this).attr('checked')) {
            $('.chk').attr('checked', true);
        }
        else {
            $('.chk').attr('checked', false);
        }
    });
    
	// 单个活动选择
    $('.chk').live('click', function(){
        var $tr = $(this).parent().parent();
        if ($(this).attr('checked')) {
            if ($('.chk:checked').length == $('.chk').length) {
                $('#chkAll').attr('indeterminate', false).attr('checked', true);
            }
            else {
                $('#chkAll').attr('indeterminate', true).attr('checked', true);
            }
        }
        else {
            if ($('.chk:checked').length == 0) {
                $('#chkAll').attr('checked', false).attr('indeterminate', false);
            }
        }
    });
	
	// 注册验证事件
	validatorForBooking();
	
});

/**
 * 选择付款方式
 */
function selecctPayStyle(box) {
	$('#selectPayStyle').show();
	$(':radio[name=payStyle]').click(function(){
		var style = $(this).val();
		
		// 清空表单
		$('#cardId, #userCode, #phone, #contact', '#bookForm').clearFields();
		$('.balance').html('');
		
		if (style == '会员卡') {
			$('#cardTr,#balanceTr').show();
		} else if (style == '现金') {
			$('#phone').attr('readonly', false);
			$('#cardTr,#balanceTr').hide();
		}
	});
}

/**
 * 保留场地
 */
function preserving() {
    var srcEle = this;
    $.post('orderHandle.do', {
        activityIds: $(this).parent().parent().find("#chk").val(),
        opType: 'preserving',
		fieldType: fieldType
    }, function(req) {
        $(srcEle).hide().parent().parent().find(':button').hide().end().find(':button[name=unpreserving]').show();
        $(srcEle).parent().find("#status").text('已保留');
		// 重新设置checkbox状态
		showCheckbox();
    });
}

/**
 * 取消保留场地
 */
function unpreserving() {
    var srcEle = this;
    $.post('orderHandle.do', {
        activityIds: $(this).parent().parent().find("#chk").val(),
        opType: 'unpreserving',
		fieldType: fieldType
    }, function(req) {
        $(srcEle).parent().parent().find('#status').html('');
        $(srcEle).hide().parent().find(':button[name=booking],:button[name=preserving]').show();
		// 重新设置checkbox状态
		showCheckbox();
    });
}

/**
 * 创建订单
 * @param {Object} obj
 */
function createOrder(params) {
	
	// 一个活动的TR对象
	var $activityTr = params.obj;
	
	// 是否为补登计
	var patch = params.patch || false;
	
    $.ajax({
        type: 'POST',
        url: 'addOrder.do',
        dataType: 'json',
        data: {
            activityIds: $($activityTr).find("#chk").val(),
			fieldType: fieldType,
			cardId: $('#cardId').val(),
            contact: $('#contact').val(),
            userCode: $('#userCode').val(),
            phone: $('#phone').val(),
			patch: patch,
			payStyle: $(':radio[name=payStyle]:checked').val()
        },
        success: function(req) {
			dialog.close();
			
            if (req == 'error' || !req.addOrder || req.addOrder == false) {
				common.dialog('预订失败', 'error');
            } else {
				var alertInfo = '';
				var orderInfo = req.orderInfo;
				var allResult = true;
				if (req.addOrder) {
					var action = patch ? "补登计" : "预订";
					alertInfo = "<span class='respSuccess'>" + action + "成功</span>";
					allResult = true;
				}
				if (!req.sendSms) {
					allResult = false;
					alertInfo += '<br/>->短信发送失败，原因：' + req.smsError;
				}
				if (!orderInfo.paymentStatus) {
					allResult = false;
					alertInfo += '<br/>->支付失败，原因：' + req.payError;
				}
				
				// DOM操作
				setOrderInfo($activityTr, orderInfo);
				
				// 重新设置checkbox状态
				showCheckbox();
				
				if (allResult) {
					common.dialog(alertInfo, 'success');
				} else {
					$.weeboxs.open(alertInfo, {
						title : '请确认',
						animate : true,
						focus : '.dialog-ok',
						type : 'warning',
						height : 70
					});
				}
				
            }
        }
    });
}


/**
 * 设置订单信息
 * @param {Object} obj
 * @param {Object} orderInfo
 */
function setOrderInfo(obj, orderInfo) {
	$(obj).find("#orderId").val(orderInfo.id).parent().find("#paymentSum").val(orderInfo.paymentSum);
    var orderInfoContent = $('#orderInfoTipTemplate').find('.basic').attr('orderId', orderInfo.id).end()
							.clone().html().replace('#orderId', orderInfo.id);
    $(obj).find(':button[name=booking],:button[name=patchBook]').hide().parent()
			.find('#status').html(orderInfo.orderName + orderInfoContent);
    
    // 显示付款状态
    if (orderInfo.paymentStatus) {
        $(obj).find('#status').after("<span id='paymentStatus' class='pay'>已付款</span>");
    } else {
        $(obj).find('#status').after("<span id='paymentStatus'>未付款<button name='pay'>付款</button></span>");
    }
    
    $(obj).find(':button[name=preserving]').hide();
    $(obj).find(':button[name=unbooking]').show();
    showOrderDetail();
}

/**
 * 修改订单信息
 */
function updateOrderInfo() {
    var srcEle = this;
    var orderId = $(srcEle).parent().parent().parent().find('#orderId').val();
    $.weeboxs.open('#boxDiv', {
        title: '场地预订——>修改订单信息',
        width: 450,
        height: 160,
        animate: true,
        onopen: function(box) {
			dialog = box;
			$('#userCode').attr('readonly', true);
			$('#searchStyle, #selectPayStyle, #cardViewTr, #balanceTr').hide();
            autoCompleteUserInfo(function() {
                // 加载订单信息
                $.getJSON('loadOrder.do', {
                    id: orderId
                }, function(order) {
                    for (key in order) {
						if(key == 'phone') {
							$('#phone').val(order[key]).data('paymentStatus', order['paymentStatus']);
						} else {
	                        $('#' + key).val(order[key]);
						}
                    }
                });
                
            });
        },
        onok: function() {
			var valid = $('#bookForm').valid();
			if (valid) {
				// 异常处理
				$().ajaxComplete(function(event,request, settings){
				     //dialog.close();
				});
				$.post('updateOrder.do', {
					id: orderId,
					contact: $('#contact').val(),
					userCode: $('#userCode').val(),
					phone: $('#phone').val()
				}, function(rep) {
					if (rep != 'success') {
						alert(rep);
					} else {
						// 更新缓存中的数据
						var aObj = $(srcEle).parent().find('a.basic');
						var keys = ['phone', 'userCode', 'contact']
						for (key in keys) {
							aObj.data(keys[key], $('#' + keys[key]).val());
						}
						dialog.close();
					}
				});
			}
        }
    });
}

/**
 * 1、输入手机号码后自动查找用户相关信息
 *    锁定会员卡号的输入
 * 2、输入会员卡号后自动查找用户相关信息
 * @param {Object} box	对话框对象
 * @param {Function} callback	成功回调函数
 */
function autoCompleteUserInfo(callback) {
	
	var box = dialog; 
	
	// 根据会员卡号加载信息
	var $userCode = $('#userCode');
	$userCode.unbind('blur').bind('blur', function(){
	
		// 开始查询前设置手机号码为只读
		// 查询完成后如果没有对应会员卡信息设置会员卡输入框为只读，手机号码输入框可写
		$.ajax({
			url: '../member/loadMemberCard.do',
			dataType: 'json',
			data: {
				userCode: $userCode.val()
			},
			beforeSend: function(){
				$('#cardTr .validatorTip').html('');
			},
			complete: function(XMLHttpRequest, textStatus){
				if ($('#phone').val() == '') {
					$('#phone').attr('readonly', false);
					$('#userCode').attr('readonly', false);
				}
			},
			success: function(card){
				if (!card) {
					return;
				}
				// 有对应的会员卡信息直接设置信息
				// 没有则清空
				if (card.length > 0) {
					$('#cardId').val(card[0].id);
					$('#contact').val(card[0].name);
					$('.balance').html(card[0].balance);
					$('#phone').val(card[0].mobilePhone).rules('add', {
						required: true,
						isMobile: true
					});
					$('#bookForm').valid();
				}
				else {
					$('#cardTr .validatorTip').html('没有此会员卡，请使用手机号预定！');
					$('#cardId,#contact,#phone').val('');
					$('.balance').html('');
					$('#phone').focus().rules("remove");
				}
			}
		});
		
	});
	
	// 根据手机号码加载信息
	$('#phone').unbind('blur').bind('blur', function() {
		var searchStyle = $(':radio[name=searchStyle]:checked').val();
		if (searchStyle != 'phone') {
			return;
		}
		var paymentStatus = $('#phone').data('paymentStatus');
		var phone = $('#phone').val();
		
		var valid = $("#bookForm").validate().element("#phone");
		if (valid && !paymentStatus) {
			$('#cardTr .validatorTip').html('');
			$.getJSON('../member/loadMemberCard.do', {
				mobilePhone: phone
			}, function(cards){
				if (cards.length == 1) {
					$('#cardId').val(cards[0].id);
					$('#contact').val(cards[0].name);
					$('.balance').html(cards[0].balance);
					$('#cardViewTr td:eq(1)').text(cards[0].cardNumber);
					$('#bookForm').valid();
				} else if (cards.length > 1) {
					$.weeboxs.open('#selectCardTemplate', {
				        title: '请选择会员卡',
				        height: 165,
						width: 400,
				        animate: true,
						showButton: false,
						onopen: function(box) {
							$.each(cards, function(i, v) {
								var card = this;
								var $cardtr = $('<tr/>', {
									id : card.id,
									userCode: card.userCode,
									'class' : i % 2 == 0 ? 'odd' : 'even'
								});
								$('<td/>', {
									text: card.cardNumber
								}).appendTo($cardtr);
								
								$('<td/>', {
									text: card.name
								}).appendTo($cardtr);
								
								$('<td/>', {
									text: card.balance
								}).appendTo($cardtr);
								
								$('<td/>', {
									text: card.effectDate
								}).appendTo($cardtr);
								
								$('<td/>', {
									text: card.periodValidity
								}).appendTo($cardtr);
								
								$cardtr.appendTo('#cardTable');
							});
							$('#cardTable tr').click(function() {
								var cardNumber = $('td:eq(0)', this).text();
								$('#cardId').val($(this).attr('id'));
								$('#userCode').val(cardNumber);
								$('#contact').val($('td:eq(1)', this).text());
								$('.balance').html($('td:eq(2)', this).text());
								$('#cardViewTr td:eq(1)').text(cardNumber);
								$('#bookForm').valid();
								box.close();
							});
						},
						onok: function() {
							dialog.close();
						},
						oncancel: function(){
							dialog.close();
						}
					});
				} else {
					$('#cardId,#userCode,#contact').val('');
					$('.balance').html('');
				}
			});
		}
	});
    
	// 回调函数
    if ($.isFunction(callback)) {
        callback();
    }
}

/**
 * 预订场地
 */
function booking() {
    var srcEle = this;
	var ids = list.getCheckbox('chk');
	
	// 1、更换查询方式
	$(':radio[name=searchStyle]').live('change', changeSearchStyle);
	
	if (ids.length > 1) {
		$.weeboxs.open('由于您选择了多个活动，请选择具体操作！', {
	        title: '请选择',
	        height: 50,
	        animate: true,
			type: 'warning',
			okBtnName: '批量预订',
			cancelBtnName: '单个预订',
			onopen: function(box) {
				dialog = box;
			},
			onok: function() {
				dialog.close();
				$('#batchBooking').trigger('click');
			},
			oncancel: function(){
				dialog.close();
				innerBooking();
			}
		});
	} else {
		innerBooking();
	}
	
	function innerBooking() {
		$.weeboxs.open('#boxDiv', {
	        title: '场地预订——>生成订单信息',
	        width: 400,
	        height: 140,
	        animate: true,
	        onopen: function(box) {
				dialog = box;
				
				$('#searchStyle, #cardViewTr').show();
				$(':radio[name=searchStyle]').trigger('change');
				
				// 隐藏选择支付类型
				$('#selectPayStyle').hide();
				
	        },
	        onok: function() {
				var valid = $('#bookForm').valid();
				if (valid) {
		            $(this).val('正在处理……').attr('disabled', true);
					var params = {
						patch: false,
						obj: $(srcEle).parent().parent()
					};
		            createOrder(params);
				}
	        }
	    });
	}
	
}

/**
 * 查询方式更换
 */
function changeSearchStyle() {
	var searchStyle = $(':radio[name=searchStyle]:checked').val();
	
	if (searchStyle == 'card') {
		$('#cardTr').show();
		$('#cardViewTr').hide();
		$('#userCode').focus();
	} else if (searchStyle == 'phone') {
		$('#cardTr').hide();
		$('#cardViewTr').show();
		$('#phone').focus();
	}
	
	// 注册验证事件
	validatorForBooking();
				
	// 清空表单
	$('#cardId, #userCode, #phone, #contact').val('');
	$('#cardViewTr td:eq(1), .balance').empty();
	
	// 自动查询用户信息
	autoCompleteUserInfo();
}

/**
 * 验证预订的表单完整性
 * @param {Object} box	对话框对象
 */
function validatorForBooking(callback) {
	
	$("#bookForm").validate({
        rules: {
			phone: {
				required: true,
				isMobile: true
			},
			contact: {
				required: true,
				minlength: 2,
				maxlength: 25
			}
        },
        messages: {
			phone: {
				required: '请输入手机号码'
			},
			contact: {
				required: '请输入联系人'
			}
        },
        /* 设置验证触发事件 */
        errorPlacement: function(error, element) {
            if (element.is(":radio")) {
                error.appendTo(element.parent());
            } else if (element.is(":checkbox")) {
                error.appendTo(element.next());
            } else {
                error.appendTo(element.parent());
            }
        }
    });
}

/**
 * 补登计
 */
function patchBook() {
	var srcEle = this;
	
	$.weeboxs.open('#boxDiv', {
        title: '活动补登计',
        width: 450,
        height: 140,
        animate: true,
        onopen: function(box) {
			dialog = box;
            autoCompleteUserInfo(box);
			selecctPayStyle(box);
			
			$('#searchStyle, #cardViewTr').hide();
        },
        onok: function() {
			var valid = $('#bookForm').valid();
			if (valid) {
	            $(this).val('正在处理……').attr('disabled', true);
				var params = {
					patch: true,
					obj: $(srcEle).parent().parent()
				};
	            createOrder(params);
			}
        }
    });
}

/**
 * 取消场地预订
 */
function unbooking() {
    var srcEle = this;
	var $parentTr = $(srcEle).parent().parent();
    var orderId = $parentTr.find('#orderId').val();
    var maxPrice = $parentTr.find('#paymentSum').val();
    $.weeboxs.open('#unbookingTemplate', {
        title: '取消预订场地',
        width: 260,
        height: 70,
        animate: true,
        onopen: function(box) {
			dialog = box;
            $.getJSON(contextPath + '/activity/loadOrder.do', {
                id: orderId
            }, function(order) {
                var paidTotal = parseFloat(order.paymentSum);
                if (paidTotal > 0) {
                    $('#refundmentDiv').fadeIn().find('#maxRefundment').text(paidTotal).end().find("#refundment").attr({
                        disabled: '',
                        title: ''
                    });
                } else {
                    box.setButtonTitle('ok', ' 是 ');
                    box.setButtonTitle('cancel', ' 否 ');
                    $('#withoutRefundment').fadeIn();
                }
				
				// 取消预订时设置实际支付金额
				$('#orgPrice').click(function(){
					$('#refundment').val($('#maxRefundment').text());
				});
				
            });
        },
        onok: function() {
            var refundment = $('#refundment').val() * 1;
            if (!isNaN(refundment)) {
                if (refundment > maxPrice * 1) {
                    $('#refundment').val('');
                    alert('不能大于实际支付金额');
                } else {
					$(this).ajaxComplete(function(){
						 dialog.close();
					});
                    $.getJSON(contextPath + '/activity/orderHandle.do', {
                        activityIds: $parentTr.find("#chk").val(),
                        refundment: refundment,
                        opType: 'cancel',
                        fieldType: fieldType
                    }, function(req) {
						dialog.close();
						
						var alertInfo = "";
						var allResult = true;
						if (req.handle == true) {
							alertInfo += "<span class='respSuccess'>取消成功</span>";
	                        $(srcEle).hide().parent().parent().find(':button[name=validate]').hide();
	                        $(srcEle).parent().find(':button[name=booking],:button[name=preserving]').show();
	                        $(srcEle).parent().find("#status").text('未预定').end().find('#paymentStatus').hide();
							
							// 重新设置checkbox状态
							showCheckbox();
						}
						if (!req.sendSms || req.sendSms == false) {
							allResult = false;
							alertInfo += "<br/>->短信发送失败，原因：" + req.smsError;
						}
						
						if (allResult) {
							common.dialog(alertInfo, 'success');
						} else {
							$.weeboxs.open(alertInfo, {
								title : '请确认',
								animate : true,
								focus : '.dialog-ok',
								type : 'warning',
								height : 70
							});
						}
						
                    });
                }
				//dialog.close();
            } else {
                alert('请输入数字！');
                $('#refundment').val('');
            }
        }
    });
}

/**
 * 付款
 */
function pay() {
    if (!confirm('是否已收款？')) {
        return;
    }
    var srcEle = this;
	var parentTr = $(this).parent().parent().parent();
    var activityId = parentTr.find('#chk').val();
    $.post('payForOrder.do', {
        activityId: activityId,
		fieldType: fieldType
    }, function(req) {
        if (req == 'true') {
			var orderPrice = parentTr.find('td:eq(1)').text();
			parentTr.find('#paymentSum').val(orderPrice);
            $(srcEle).parent().addClass('pay').text('已付款').end().remove();
        } else {
            alert('付款失败 ！');
        }
    });
}

/**
 * 根据按钮的状态显示checkbox
 */
function showCheckbox() {
	$('#row tr:gt(0)').each(function(){
		// 清空checkbox
		$('td[name=checkbox]', this).html('');
		var activityId = $('#chk', this).val();
		var btns = $(':button[name=booking]:visible,:button[name=preserving]:visible', this).length;
		if (btns > 0) {
			var $checkbox = $($('#checkboxTemplate').html()).val(activityId);
			$('td[name=checkbox]', this).html('');
			$checkbox.appendTo($('td[name=checkbox]', this));
		}
	});
}

/**
 * 根据状态[预定、保留]选择
 */
function selector() {
	var type = $(this).attr('class');
	if (type == 'none') {
		$('#row :checkbox').attr('checked', false);
	} else if (type == 'booking') {
		$('#row :checkbox').attr('checked', false);
		$('#row :button[name=booking]:visible').each(function(){
			$(this).parents('tr').find(':checkbox').attr('checked', true);
		});
	} else if (type == 'preserving') {
		$('#row :checkbox').attr('checked', false);
		$('#row :button[name=preserving]:visible').each(function(){
			$(this).parents('tr').find(':checkbox').attr('checked', true);
		});
	}
}

/**
 * 获得选中的记录
 */
function getChks() {
	var ids = list.getCheckbox('chk');
	if (ids.length == 0) {
		common.dialog('请先选择活动！', 'warning');
		return false;
	}
	return ids;
}

/**
 * 批量预订执行函数
 */
function createOrderForBatch(ids) {
	$(this).ajaxComplete(function(){
		dialog.close();
	});
    $.ajax({
        type: 'POST',
        url: 'addOrder.do',
        dataType: 'json',
        data: {
            activityIds: ids.toString(),
			fieldType: fieldType,
			cardId: $('#cardId').val(),
            contact: $('#contact').val(),
            userCode: $('#userCode').val(),
            phone: $('#phone').val(),
			patch: false,
			payStyle: $(':radio[name=payStyle]:checked').val()
        },
        success: function(req) {
			if (req.errorInfo) {
				alert(req.errorInfo);
				return;
			} else {
				dialog.close();
				var alertInfo = "<span class='respSuccess'>预订成功</span>";
				var orderInfo = req.orderInfo;
				var allResult = true;
				if (!req.sendSms) {
					allResult = false;
					alertInfo += '<br/>->短信发送失败，原因：' + req.smsError;
				}
				
				var orderInfo;
				var orders = req.results;
				
				// 成功、失败个数
				var payCounter = [0, 0];
				$(orders).each(function(i, o) {
					orderInfo = o.orderInfo;
					setOrderInfo($('.chk[value=' + orderInfo.activityId + ']').parents('tr'), orderInfo);
					if (orderInfo.paymentStatus) {
						payCounter[0] = payCounter[0] + 1;
					} else {
						payCounter[1] = payCounter[1] + 1;
					}
				});
				
				if (payCounter[1] > 0) {
					allResult = false;
				}
				
				// 提示付款成功、失败个数
				alertInfo += '<br/>->共预订 <b>' + (payCounter[0] + payCounter[1]) 
						+ '</b> 块场地，其中付款成功 <b>' + payCounter[0] + '</b> 块，付款失败 <b>'
						+ payCounter[1] + '</b> 块';
				
				// 重新设置checkbox状态
				showCheckbox();
				
				if (allResult) {
					common.dialog(alertInfo, 'success');
				} else {
					$.weeboxs.open(alertInfo, {
						title : '请确认',
						animate : true,
						focus : '.dialog-ok',
						type : 'warning',
						height : 90
					});
				}
				
            }
        }
    });
}

/**
 * 批量预订
 */
function batchBooking() {
	var ids = getChks();
	if (!ids) {
		return;
	}
	
	// 处理：如果选中一条记录点击“批量预定”按钮情况
	if (ids.length == 1) {
		$('#row :checkbox:checked').parents('tr').find(':button[name=booking]').trigger('click');
		return;
	}
	
	// 1、更换查询方式
	$(':radio[name=searchStyle]').live('change', changeSearchStyle);
	
	// 打开批量预订对话框 
	$.weeboxs.open('#boxDiv', {
        title: '场地批量预订',
        width: 450,
        height: 140,
        animate: true,
        onopen: function(box) {
			dialog = box;
			
			$('#searchStyle, #cardViewTr').show();
			$(':radio[name=searchStyle]').trigger('change');
			
			// 隐藏选择支付类型
			$('#selectPayStyle').hide();
			
        },
        onok: function() {
			var valid = $('#bookForm').valid();
			if (valid) {
	            $(this).val('正在处理……').attr('disabled', true);
	            createOrderForBatch(ids);
			}
        }
    });
	
}

/**
 * 批量保留
 */
function batchPreserving() {
	var ids = getChks();
	if (!ids) {
		return;
	}
	dialog = $.weeboxs.open('确认要保留' + ids.length + '个场地吗？', {
		title : '请确认',
		animate : true,
		focus : '.dialog-ok',
		type : 'warning',
		height : 30,
		okBtnName : '保 留',
		cancelBtnName : '不保留',
		onok: function(){
			$(this).ajaxComplete(function(){
				dialog.close();
			});
			$.post('orderHandle.do', {
		        activityIds: ids.toString(),
		        opType: 'preserving',
				fieldType: fieldType
		    }, function(req) {
		        $('#row :checkbox:checked').each(function(){
					$(this).parent().parent().find(':button').hide().end().find(':button[name=unpreserving]').show();
					$(this).parent().parent().find("#status").text('已保留');
				});
				// 重新设置checkbox状态
				showCheckbox();
		    });
			
		}
	});
}
