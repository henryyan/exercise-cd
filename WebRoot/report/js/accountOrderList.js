$(function() {

	// 设置默认日期
    if ($('#payTime').val() == '') {
        $('#payTime').val(strSystemDate);
    }

    // 报表日期选择
    $('#payTime').click(function() {
        WdatePicker({
            el: 'payTime',
            onpicked: function(dp) {
                var pickedDate = dp.cal.getDateStr();
                location.href = ctx + '/report/accountOrderList.jsp?payTime=' + pickedDate;
            }
        });
    });

	// 加载数据列表
    loadOrderData();

});

function loadOrderData() {
    $("#list").jqGrid({
        url: 'accountOrderList.do?payTime=' + $('#payTime').val(),
        datatype: "json",
        colNames: ['付款编号', '联系人', '电话', '付款金额', '付款时间', '付款方式', '帐号', '操作'],
        colModel: [{
			name: 'id',
			width: 100
		}, {
			name: 'contact',
			align: 'center',
			width: 70,
		}, {
			name: 'phone',
			align: 'center',
			width: 80
		}, {
			name: 'payTotal',
			width: 80
		}, {
			name: 'payTime',
			align: 'center',
			searchoptions: {
				dataInit: function(elem) {
					$(elem).addClass('Wdate').click(WdatePicker);
				},
				sopt: ['cn']
			}
		}, {
			name: 'payPlatform',
			align: 'center',
			width: 80
		}, {
			name: 'platformAccount',
			width: 90
		}, {
			name: 'options',
			align: 'center',
			width: 80,
			formatter: function(cellvalue, options, rowObject) {
				if (rowObject.fieldOrderSize && rowObject.fieldOrderSize > 0) {
					return "<a href='#' aoid='" + rowObject.id + "' class='view-field-order' title='有" + rowObject.fieldOrderSize + "个和此付款订单有关的活动'>查看活动订单</a>";
				} else {
					return '';
				}
			}
		}],
        jsonReader: {
            repeatitems: false
        },
        width: 780,
        height: 517,
        rowNum: 20,
        rowList: [20, 30, 40],
        pager: '#pager',
        viewrecords: true,
        rownumbers: true,
        sortname: 'payTime',
        sortorder: "desc",
        caption: "付款订单列表",
		gridComplete: function() {
			
			// 查看活动订单列表
			$('.view-field-order').unbind('click').click(function() {
				var accountOrderId = $(this).attr('aoid');
				$('#fieldOrderTemplate').dialog({
					title: '查看活动订单详细',
					modal: true,
					width: 600,
					height: 200,
					open: function() {
						$('#fieldOrderTable .data-item').remove();
						$('#loading').show();
						$.getJSON(ctx + '/report/findFieldActivity.do', {
							accountOrderId: accountOrderId
						}, function(data) {
							$('#loading').hide();
							$.each(data.fieldOrders, function(i, v) {
								var $tr = $('<tr/>', {
									'class' : 'data-item'
								});
								
								$('<td/>', {
									html: v.zhFieldType
								}).appendTo($tr);
								
								$('<td/>', {
									html: v.fieldName
								}).appendTo($tr);
								
								$('<td/>', {
									html: v.usableDate
								}).appendTo($tr);
								
								$('<td/>', {
									html: v.period
								}).appendTo($tr);
								
								$('<td/>', {
									html: v.fieldOrder.contact
								}).appendTo($tr);
								
								$('<td/>', {
									html: v.fieldOrder.phone
								}).appendTo($tr);
								
								$tr.hover(function() {
									$(this).addClass('ui-state-hover');
								}, function() {
									$(this).removeClass('ui-state-hover');
								}).appendTo('#fieldOrderTable');
							});
						});
					}
				});
			});
		}
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
    );
}
