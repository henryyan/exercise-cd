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
        colNames: ['商品名称', '价格', '登记日期', '描述'],
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
        editurl: 'saveRetailGoods.do'
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