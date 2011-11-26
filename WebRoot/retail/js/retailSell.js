/**
 * 零售商品销售
 */
$(function() {
	
	initRetailSellList();
	
});

/**
 * 列表
 */
function initRetailSellList() {
	$listjqGrid = $("#list").jqGrid({
        url: 'sellList.do',
        datatype: "json",
        colNames: ['商品名称', '价格', '数量', '销售日期', '会员名称', '会员卡号'],
        colModel: [{
			name: 'retailName',
			align: 'center'
		}, {
			name: 'retailPrice',
			align: 'center',
			formatter: 'number'
		}, {
			name: 'amount',
			align: 'center'
		}, {
			name: 'sellDate',
			align: 'center',
			formatter: 'date'
		}, {
			name: 'memberUserName',
			align: 'center'
		}, {
			name: 'memberCardNumber',
			align: 'center'
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
        rownumbers: true
    }).jqGrid('navGrid', '#pager', {
        add: false,
        edit: false,
		view: true,
        del: false,
		refreshtext: '刷新'
    },
	{ // edit options
		editCaption: '编辑零售商品',
		width: 400,
		closeAfterEdit: true,
		beforeShowForm: function(formid) {
			validatorForRetailSell();
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
			validatorForRetailSell();
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
			pn: 'retail.Sell'
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