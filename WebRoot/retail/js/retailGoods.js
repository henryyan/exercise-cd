/**
 * 零售商品管理
 */
$(function() {
	
	initRetailGoodsList();
	
});

/**
 * 列表
 */
function initRetailGoodsList() {
	$listjqGrid = $("#list").jqGrid({
        url: 'retailGoodsList.do',
        datatype: "json",
        colNames: ['商品名称', '价格', '登记日期', '描述', '操作'],
        colModel: [{
			name: 'retailName',
			align: 'center',
			editable: true
		}, {
			name: 'price',
			align: 'center',
			editable: true,
			formatter: 'number'
		}, {
			name: 'createDate',
			align: 'center',
			formatter: 'date'
		}, {
			name: 'description',
			editable: true
		}, {
			name: 'options',
			align: 'center',
			formatter: function(cellValue, options, rowObject) {
				return "<button class='sell'>出售</button>";
			}
		}],
        jsonReader: {
            repeatitems: false
        },
        width: document.documentElement.clientWidth - 5,
        height: document.documentElement.clientHeight - 83,
        rowNum: 20,
        rowList: [20, 30, 40],
        pager: '#pager',
        viewrecords: true,
        caption: "零售商品列表",
        rownumbers: true,
        editurl: 'saveRetailGoods.do',
		gridComplete: function() {
			$('.sell').button({
				icons: {
					primary: 'ui-icon-minus'
				}
			}).unbind('click').click(sell);
		}
    }).jqGrid('navGrid', '#pager', {
        add: true,
        edit: true,
		view: true,
        del: true,
		searchtext: '查询',
		refreshtext: '刷新'
    },
	{ // edit options
		editCaption: '编辑零售商品',
		width: 400,
		closeAfterEdit: true,
		beforeShowForm: function(formid) {
			validatorForRetailGoods();
			$('.CaptionTD').width(50);
		},
		beforeSubmit: function() {
			var valid = $("#FrmGrid_list").valid();
			return [valid, '请将表单填写完整'];
		}
	},
	{  // add options
		addCaption: '添加零售商品',
		width: 400,
		closeAfterAdd: true,
		beforeShowForm: function(formid) {
			validatorForRetailGoods();
			$('.CaptionTD').width(50);
		},
		beforeSubmit: function() {
			var valid = $("#FrmGrid_list").valid();
			return [valid, '请将表单填写完整'];
		}
	},
	{ // delete options
		url: '../deleteObj.do',
		delData: {
			pn: 'retail.goods'
		}
	},
	{ // search options
		multipleSearch: true
	},
	{ // view options
		beforeShowForm: function(formid) {
			showHiddenElem('trv');
		},
		afterclickPgButtons: function() {
			showHiddenElem('trv');
		}
	})
}


/**
 * 验证会员卡的表单完整性
 * @param {Function} callback	回调函数
 */
function validatorForRetailGoods(callback) {
	$("#FrmGrid_list").validate({
        rules: {
			retailName: {
				required: true,
				remote: {
                    url: 'checkRepeatRetailName.do',
                    type: 'post',
                    data: {
						id: $('#id_g').val(),
                        retailName: function() {
                            return $('#retailName').val();
                        }
                    },
                    dataType: 'json',
                    dataFilter: function(data) {
                        data = eval("(" + data + ")");
                        if (data.exist) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                }
			},
			price: {
				required: true,
				number: true,
				min: 0.1,
				max: 999.99
			},
			balance: {
				required: true,
				min: 3.00,
				max: 9999999.99
			}
        },
        messages: {
			retailName: {
				remote: "商品名称重复"
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
        },
        success: function(label) {
            label.html("&nbsp;").addClass("checked");
			var forEle = label.attr('for');
			if (forEle == 'phone') {
				if ($.isFunction(callback)) {
					callback();
				}
			}
        }
    });
}

/**
 * 销售
 */
function sell() {
	var rowId = $(this).parents('tr').attr('id');
	$('#sellTemplate').dialog({
		modal: true,
		title: '销售商品-' + $('#list').jqGrid('getCell', rowId, 'retailName'),
		open: function() {
			$('#memberCardNumber').blur(loadMemberInfo);
		},
		buttons: {
			取消: function() {
				$(this).dialog('close');
			},
			出售: function() {
				$.ajax({
					url: '../retail/sell.do',
					data: {
						retailGoodsId: rowId,
						amount: $('#amount').val(),
						memberCardNumber: $('#memberCardNumber').val()
					},
					beforeSend: function() {
						var inputOk = $('#amount').val() != '' && $('#memberCardNumber').val() != '';
						if (!inputOk) {
							$('#sellTip').show().html("请填写完整信息！");
						}
						return inputOk;
					},
					success: function(resp) {
						if (resp == 'success') {
							alert('出售成功！');
						} else {
							$('#sellTip').show().html(resp);
						}
					}
				});
			}
		}
	});
}

/**
 * 加载会员卡信息
 */
function loadMemberInfo() {
	$.ajax({
		url: '../member/loadMemberCard.do',
		dataType: 'json',
		data: {
			userCode: $(this).val()
		},
		beforeSend: function(){
			$('#sellTemplate span').html('');
		},
		success: function(card){
			if (!card) {
				return;
			}
			// 有对应的会员卡信息直接设置信息
			// 没有则清空
			if (card.length > 0) {
				$('#sellTip').hide();
				$('#memberUserName').text(card[0].name);
				$('#balance').text(card[0].balance);
				$('#mobilePhone').text(card[0].mobilePhone);
			}
			else {
				$('#sellTip').show().html('没有此会员卡！');
			}
		}
	});
}
