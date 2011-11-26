// 会员卡列表jqGrid对象
var $cardListjqGrid;

$(function() {

    // 会员卡列表
    cardList();

    // 会员卡使用记录列表
    recordList();
	
	// 查看详细使用记录
	$('.view-detail').live('click', viewCardUsageDetail);

});

/**
 * 会员卡列表
 */
function cardList() {
    $cardListjqGrid = $("#cardList").jqGrid({
        url: 'memberCardList.do',
        datatype: "json",
        colNames: ['会员卡号', '会员卡类型', '生效日期', '有效期', '会员姓名',
					'入会日期', '手机号码', '卡内余额', '状态', '身份证号码', '联系住址'],
        colModel: [{
            name: 'cardNumber',
            editable: true,
            edittype: 'text',
            editoptions: {
                size: 10,
                maxlength: 15
            }
        }, {
            name: 'cardType.typeName',
            editable: true,
            edittype: 'select',
			editoptions: {
				dataUrl: 'getMemberCardTypes.do',
				buildSelect: function(vs) {
					vs = vs.responseText.toJson();
					var types = new StringBuffer();
					types.append("<select><option value=''>请选择</option>");
					for (var v in vs) {
						types.append("<option value='" + vs[v].id + "'"
							+ " moneyAmount='" + vs[v].moneyAmount + "'"
							+ " periodMonth='" + vs[v].periodMonth + "'>" + vs[v].typeName + "</option>");
					}
					types.append("</select>");
					return types.toString();
				},
				dataEvents: [
					{
						type: 'change',
						fn: function(e){
							var $opt = $(this).find('option:selected');
							if ($opt.val() != '') {
								$('#balance').val($opt.attr('moneyAmount'));

								// 设置有效期
								setPeriodValidity();
							}
						}
					}
				]
			}
        }, {
			name: 'effectDate',
			editable: true,
			hidden: true,
			editoptions: {
				dataInit: function(ele) {
					$(ele).addClass('Wdate');
				},
				dataEvents: [
					{
						type: 'click',
						fn: function(e) {
							WdatePicker({
					            dateFmt: 'yyyy-MM-dd',
								minDate: strSystemDate,
								onpicked: setPeriodValidity
					        });
						}
					}
				]
			}
		}, {
			name: 'periodValidity',
			editable: true,
			hidden: true,
			editoptions: {
				readonly: 'readonly'
			}
		}, {
            name: 'name',
            editable: true,
            edittype: 'text'
        }, {
            name: 'createDate',
            formatter: 'date'
        }, {
            name: 'mobilePhone',
            editable: true,
            edittype: 'text',
			editoptions: {
                maxlength: 11
            }
        }, {
            name: 'balance',
            editable: true,
            edittype: 'text'
        }, {
            name: 'cardStatus',
			sortable: true
        }, {
            name: 'idNo',
            editable: true,
            edittype: 'text',
			hidden: true
        }, {
            name: 'address',
            editable: true,
            edittype: 'text',
			hidden: true,
            editoptions: {
                size: 40,
                maxlength: 50
            }
        }],
        jsonReader: {
            repeatitems: false
        },
        width: 780,
        height: 240,
        rowNum: 20,
        rowList: [20, 30, 40],
        pager: '#cardPager',
        viewrecords: true,
        sortorder: "desc",
        sortname: 'createDate',
        caption: "会员卡列表",
        rownumbers: true,
        editurl: 'saveMemberCard.do',
        onSelectRow: function(ids) {
            if (ids == null) {
                ids = 0;
                if ($("#recordList").jqGrid('getGridParam', 'records') > 0) {
                    $("#recordList").jqGrid('setGridParam', {
                        url: "cardUsageRecordList.do?cardId=" + ids,
                        page: 1
                    });
                    $("#recordList").jqGrid().trigger('reloadGrid');
                }
            } else {
                $("#recordList").jqGrid('setGridParam', {
                    url: "cardUsageRecordList.do?cardId=" + ids,
                    page: 1
                });
                $("#recordList").jqGrid().trigger('reloadGrid');
            }
        }
    }).jqGrid('navGrid', '#cardPager', {
        add: true,
        edit: true,
		view: true,
        del: true,
		//addtext: '添加',
		//edittext: '编辑',
		//viewtext: '查看',
		//deltext: '删除',
		searchtext: '查询',
		refreshtext: '刷新'
    },
	{ // edit options
		editCaption: '编辑会员卡',
		width: 500,
		closeAfterEdit: true,
		beforeShowForm: function(formid) {
			showHiddenElem();
			validatorForMemberCard();

			// 设置只读属性
			$('#balance').attr('readonly', true);

			$('#cardNumber').rules('remove', 'remote');
		},
		beforeSubmit: function() {
			var valid = $("#FrmGrid_cardList").valid();
			return [valid, '请将表单填写完整'];
		}
	},
	{  // add options
		addCaption: '添加会员卡',
		width: 500,
		closeAfterAdd: true,
		beforeShowForm: function(formid) {
			showHiddenElem();
			validatorForMemberCard();
			// 设置只读属性
			$('#balance').attr('readonly', false);
		},
		beforeSubmit: function() {
			var valid = $("#FrmGrid_cardList").valid();
			return [valid, '请将表单填写完整'];
		}
	},
	{ // delete options
		url: '../deleteObj.do',
		delData: {
			pn: 'member.card'
		},
		afterComplete: function(response, postdata, formid) {
			$("#recordList").jqGrid('setGridParam', {
                url: "cardUsageRecordList.do?cardId=0",
                page: 1
            });
            $("#recordList").jqGrid('setCaption', "会员卡使用记录: ").trigger('reloadGrid');
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
	}).jqGrid('navButtonAdd', '#cardPager', {
		caption: "充值",
		title: "充值",
	   	buttonicon: "ui-icon-carat-1-e",
	   	onClickButton: function(){
			var id = $("#cardList").jqGrid('getGridParam','selrow');
			if (id) {
		     	$('#dialog-form').data('cardId', id).dialog('open');
			} else {
				$('<span title="注意">请选择记录</span>').dialog({modal: true, width:200, minHeight: 20});
			}
	   	}
	});

	initRechargeDialog();
}

/**
 * 会员卡充值对话框
 */
function initRechargeDialog() {
	$('#dialog-form').dialog({
		autoOpen: false,
		modal: true,
		resizable: false,
		minHeight: 100,
		buttons: {
			确认充值: function() {
				var cardId = $('#dialog-form').data('cardId');
				var valid = $("#rechargeForm").valid();
				if (valid) {
					var cardId = $('#dialog-form').data('cardId');
					$.getJSON('recharge.do', {
                        id: cardId,
                        total: $('#recharge').val()
                    }, function(req) {
                        if (req && req.finalBalance) {

							// 更新余额
							var rowData = $("#cardList").jqGrid('getRowData', cardId);
							$('#dialog-form').dialog('close');
							$($cardListjqGrid).jqGrid('setRowData', cardId, {balance: req.finalBalance});

							// 清空form
							$('#recharge').val('');
							$('#dialog-form.error').remove();
						}
					});
				}
			},
			取消: function() {
				$(this).dialog('close');
				$('#recharge').val('');
				$('#dialog-form..error').remove();
			}
		},
		open: function() {
			$("#rechargeForm").validate({
		        rules: {
					recharge: {
						required: true,
						number: true,
						range: [1, 999999999]
					}
		        },
				messages: {
					recharge: {
						required: '请输入充值金额'
					}
				},
				errorPlacement: function(error, element) {
					error.appendTo(element.parent());
		        }
		    });
		}
	});
}

/**
 * 显示隐藏的字段TR
 * @param {Object} prefix	TR的ID前缀{add、edit: 'tr', view: 'trv'}
 */
function showHiddenElem(prefix) {
	prefix = prefix || 'tr';
	// 显示隐藏的字段
	var sc;
	var showColumns = [
						{ele: 'effectDate'}, {ele: 'periodValidity'}, {ele: 'idNo'},
						{ele: 'address'}
					];
	for (var i in showColumns) {
		sc = showColumns[i];
		$('#' + prefix + '_' + sc.ele).show();//.find('.FormElement').addClass(sc['class']);
	}

	// 设置会员卡生效日期为当前日期
	$('#effectDate').val(strSystemDate);
}

/**
 * 验证会员卡的表单完整性
 * @param {Function} callback	回调函数
 */
function validatorForMemberCard(callback) {
	$("#FrmGrid_cardList").validate({
        rules: {
			'cardType.typeName': "required",
			cardNumber: {
				required: true,
				range: [1, 999999999],
				rangelength: [1, 10],
				remote: {
                    url: 'checkCardNumber.do',
                    type: 'post',
                    data: {
                        cardNumber: function() {
                            return $('#cardNumber').val();
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
			name: {
				required: true,
				rangelength: [1, 5]
			},
			balance: {
				required: true,
				min: 3.00,
				max: 9999999.99
			},
			effectDate: "required",
			mobilePhone: {
				required: true,
				isMobile: true
			}
        },
        messages: {
			cardNumber: {
				remote: "会员卡ID重复"
			},
			mobilePhone: {
				required: '请输入手机号码'
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
 * 根据会员卡类型和生效日期设置有效期
 */
function setPeriodValidity() {
	var $opt = $('select[name=cardType.typeName] option:selected');
	var periodMonth = $opt.attr('periodMonth') * 1;
	var eds = $('#effectDate').val().split('-');
	var year = parseInt(eds[0]);
	if (isMozila()) {
		year += 1900;
	}
	var tempSysDate = new Date(year, parseInt(eds[1]), parseInt(eds[2]));
	tempSysDate.setMonth(tempSysDate.getMonth() + periodMonth);
	$('#periodValidity').val(tempSysDate.toDate());
	$("#FrmGrid_cardList").validate().element( "#balance" );
}

/**
 * 会员卡使用记录列表
 */
function recordList() {
    $("#recordList").jqGrid({
		caption: '会员卡使用记录',
        url: 'cardUsageRecordList.do?cardId=0',
        datatype: "json",
        colNames: ['使用日期', '使用时段', '用卡类型', '操作金额', '详细信息'],
        colModel: [{
            name: 'usageDate',
			width: 80,
			align: 'center',
            formatter: 'datetime'
        }, {
            name: 'usageTimeSlice',
			align: 'center'
        }, {
            name: 'usageType',
			width: 60,
			align: 'center'
        }, {
            name: 'optionTotal',
			width: 60,
			align: 'center'
        }, {
			name: 'detail',
			width: 50,
			align: 'center',
			formatter: function(cellvalue, options, rowObject) {
				if (rowObject.usageType != '支付') {
					return "";
				}
				return "<a href='#' cui='" + rowObject.id + "' class='view-detail'>查看</a>";
			}
		}],
        jsonReader: {
            repeatitems: false
        },
        width: 780,
        height: 190,
        rowNum: 20,
        rowList: [20, 30, 40],
        pager: '#recordPager',
        viewrecords: true,
        sortorder: "desc",
        sortname: 'usageDate',
        rownumbers: true
    }).jqGrid('navGrid', '#recordPager', {
        edit: false,
        add: false,
        del: false
    });
}

/**
 * 查看详细使用记录 
 */
function viewCardUsageDetail() {
	var usageId = $(this).attr('cui');
	$('#cardUsageDetailTemplate').dialog({
		modal: true,
		title: '查看会员卡使用记录详细信息',
		width: 750,
		height: 300,
		open: function() {
			$('.loading').show();
			$('#cardUsageDetailTable').hide();
			$('#cardUsageDetailTable .data').remove();
			$.getJSON('loadCardUsageDetail.do', {
				id: usageId
			}, function(data) {
				$('.loading').hide();
				$('#cardUsageDetailTable').show();
				var orders = data.orders;
				$.each(orders, function(i) {
					var $tr = $('<tr/>', {'class' : 'data'});
					$('<td/>', {html: i + 1}).appendTo($tr);
					//$('<td/>', {html: this.userCode}).appendTo($tr);
					$('<td/>', {html: this.contact}).appendTo($tr);
					//$('<td/>', {html: this.phone}).appendTo($tr);
					$('<td/>', {
						title: '场地类型/场地编号',
						html: this.fieldZhType + "<span class='separator'>/</span>"
							+ this.fieldActivity.fieldName
					}).appendTo($tr);
					$('<td/>', {
						title: '活动日期/活动时段',
						html: this.fieldActivity.usableDate + "<span class='separator'>/</span>"
							+ this.fieldActivity.period
					}).appendTo($tr);
					$('<td/>', {
						title: '标准/优惠',
						html: this.standardPrice + "<span class='separator'>/</span>" + this.paymentSum
					}).appendTo($tr);
					$('<td/>', {html: this.bookTime.replace(' ', '<br/>')}).appendTo($tr);
					$('<td/>', {html: this.paymentTime.replace(' ', '<br/>')}).appendTo($tr);
					$('#cardUsageDetailTable').append($tr);
				});
				
				// 高亮
				$('#cardUsageDetailTable tr').hover(function() {
					$('td', this).addClass('ui-state-hover');
				}, function() {
					$('td', this).removeClass('ui-state-hover');
				});
			});
		}
	});
}
