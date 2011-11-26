$(function() {

	// 设置默认日期
    if ($('#bookTime').val() == '') {
        $('#bookTime').val(strSystemDate);
    }

    // 报表日期选择
    $('#bookTime').click(function() {
        WdatePicker({
            el: 'bookTime',
            onpicked: function(dp) {
                var pickedDate = dp.cal.getDateStr();
                location.href = ctx + '/report/orderItemList.jsp?bookTime=' + pickedDate;
            }
        });
    });

	// 加载数据列表
    loadOrderData();

});

function loadOrderData() {
    $("#list").jqGrid({
        url: 'orderItemList.do?bookTime=' + $('#bookTime').val(),
        datatype: "json",
        colNames: ['会员卡号', '订单号', '联系人', '手机号码', '订单时间', '场地信息', 
                   '锻炼日期', '时段', '原价', '实付', '付款方式'],
        colModel: [{
            name: 'fieldOrder.userCode',
            align: 'center'
        }, {
            name: 'fieldOrder.id',
            align: 'center'
        },{
            name: 'fieldOrder.contact',
            align: 'center'
        }, {
            name: 'fieldOrder.phone',
            align: 'center',
            hidden: true
        }, {
            name: 'fieldOrder.bookTime',
            align: 'center',
            search: false,
            formatter: function(cellvalue) {
        		if (!cellvalue) {
        			return "";
        		}
        		var dateAndTime = cellvalue.split(' ');
	        	return dateAndTime[1] + "<br/>" + dateAndTime[0];
	        }
        }, {
            name: 'fieldInfo',
            align: 'center',
            search: false,
            width: 200,
            formatter: function(cellvalue, options, rowObject) {
        		return "<img src='../images/field/" + rowObject.fieldType + ".gif' align='absmiddle' /><br/>" + rowObject.fieldName; 
	        }
        }, {
            name: 'usableDate',
            align: 'center',
            search: false,
			formatter: function ( cellvalue, options, rowObject ) {
				return cellvalue == null ? '' : cellvalue.substr(0, 10);
			}
        }, {
            name: 'period',
            align: 'center',
            search: false
        }, {
            name: 'fieldOrder.standardPrice',
            align: 'center',
            formatter: 'number',
            width: 80,
            align: 'right'
        }, {
            name: 'fieldOrder.paymentSum',
            formatter: 'number',
            width: 80
        }, {
            name: 'fieldOrder.paymentStyle',
            align: 'center',
            search: false
        }],
        jsonReader: {
            repeatitems: false
        },
        width: 780,
        height: 460,
        rowNum: 20,
        rowList: [20, 30, 40],
        pager: '#pager',
        viewrecords: true,
        rownumbers: true,
        sortname: 'fieldOrder.bookTime',
        sortorder: "desc",
        caption: "订单列表"
    }).jqGrid('navGrid', '#pager', {
		add: false,
        edit: false,
		view: true,
        del: false,
        viewtext: '查看',
		searchtext: '查询',
		refreshtext: '刷新'
    }, {}, {}, {}, {
    		multipleSearch: true
    	}, {
    		beforeShowForm : function(formid) {
				$('tr[id^=tr].FormData', formid).show();
		    }
    	}
    ).jqGrid('navButtonAdd', '#pager', {
    	caption : "字段",
		title : "设置列表显示的字段",
		buttonicon : "ui-icon-wrench",
		onClickButton : function() {
			$(this).jqGrid('setColumns', {
				width : 280,
				ShrinkToFit : true
			});
		}
    });
}
