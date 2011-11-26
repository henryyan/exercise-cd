/*******************************************************************************
 * @功能说明：每日报表JS
 * @作者：闫洪磊
 ******************************************************************************/
$(function() {

    // 报表日期选择
    $('#reportDate').click(function() {
        WdatePicker({
            el: 'reportDate',
            onpicked: function(dp) {
                var pickedDate = dp.cal.getDateStr();
                $('#reportForm').submit();
            }
        });
    });
    
    var $reportTable = $('#reportTable');
    $.blockUI({
        message: '<h5><img src="../images/nyroModal/ajaxLoader.gif" class="status"/>正在生成报表，请稍候……</h5>',
        css: {
            width: '350px'
        }
    });
    
    // 异常处理
    $($reportTable).ajaxError(function(event, request, settings) {
        $.unblockUI();
    });
    
    // 报表日期
    var reportDate = $('#reportDate').val();
    if (!reportDate) {
        reportDate = strSystemDate;
    }
    
    // 请求数据
    $.getJSON('reportDaily.do', {
        venueId: $('#venueId').val(),
        reportDate: reportDate,
        activityType: '锻炼'
    }, function(result) {
    
        // 各场地运营合计金额
        var sumCounter = new Map();
        
        // 保存统计的KEY累加器
        var keyCounter = 0;
        
        // 合计运营场次
        var totalCounter = 0;
        
        // 合计运营金额
        var totalSumCounter = 0;
        
        // 时段运营场次统计
        var periodCounter = new Array();
        
        // 运营状态场次统计
        var operationsCounter = new Map();
        operationsCounter.put("运营", 0);
        operationsCounter.put("未开始", 0);
        operationsCounter.put("闲置", 0);
        operationsCounter.put("保留", 0);
        
        // 1、表头
        var trContent = new StringBuffer();
        var headObj = result[0];
        
        // 时段
        var periods = new Array();
        
        // 报表数据
        var tempDatas = [];
        
        trContent.append("<tr>").append("<th>" + headObj.id + "</th>").append("<th>" + headObj.period + "</th>");
        $.each(headObj.activities, function(i, v) {
            sumCounter.put(keyCounter++, 0);
            trContent.append("<th>" + v + "</th>");
        });
        trContent.append("<th>" + headObj.total + "</th>").append("<th>" + headObj.sumPrice + "</th>").append("</tr>");
        
        // 2、数据列表
        $.each(result, function(i, v) {
            // 从第2行开始
            if (i >= 2) {
                periods.push(v.period.replace(/~/, '<br/>'));
                tempDatas[tempDatas.length] = parseInt(v.total);
                
                trContent.append("<tr>").append("<td>" + (i - 1) + "</td>").append("<td>" + v.period + "</td>");
                $.each(v.activities, function(i1, v1) {
                
                    var tdContent = "&nbsp;";
                    
                    // 非数字的为状态和价格
                    if (isNaN(v1)) {
                        var className = "";
                        var tempStatus = v1.split(',');
                        var status = tempStatus[0];
                        
                        if (status == '未预订') {
                            // 统计运营场次
                            var opCt = operationsCounter.get('闲置');
                            operationsCounter.put('闲置', ++opCt);
                        } else if (status == '未开始') {
                            // 统计未开始场次
                            var opCt = operationsCounter.get('未开始');
                            operationsCounter.put('未开始', ++opCt);
                        } else if (status == '锻炼') {
                            className = "exercise";
                            tdContent = tempStatus[1];// + ",i=" + i + ",i1=" + i1;
                            // 统计运营金额
                            var tempSum = sumCounter.get(i1) + parseInt(tdContent);
                            sumCounter.put(i1, tempSum);
                            
                            // 统计运营场次
                            var opCt = operationsCounter.get('运营');
                            operationsCounter.put('运营', ++opCt);
                        } else if (status == '保留') {
                            className = "preserving";
                            // 统计运营场次
                            var opCt = operationsCounter.get('保留');
                            operationsCounter.put('保留', ++opCt);
                        }
                    }
                    trContent.append("<td class='" + className + "'>" + tdContent + "</td>");
                });
                
                totalCounter += parseInt(v.total);
                totalSumCounter += parseInt(v.sumPrice);
                
                trContent.append("<td>" + v.total + "</td>");
                trContent.append("<td>" + v.sumPrice + "</td>");
                periodCounter[periodCounter.length] = [v.period, parseInt(v.total)];
                trContent.append("</tr>");
            }
        });
        
        // 3、合计运营场次
        headObj = result[1];
        trContent.append("<tr>").append("<td>" + (result.length - 1) + "</td>").append("<td>合计运营场次</td>");
        $.each(headObj.activities, function(i, v) {
            trContent.append("<td>" + v + "</td>");
        });
        trContent.append("<td>" + totalCounter + "</td>").append("<td>&nbsp;</td>").append("</tr>");
        
        // 4、合计金额
        trContent.append("<tr>").append("<td>" + (result.length) + "</td>").append("<td>合计金额</td>");
        var keys = sumCounter.keys();
        $.each(keys, function(i, k) {
            trContent.append("<td>" + sumCounter.get(k) + "</td>");
        });
        trContent.append("<td>&nbsp;</td>").append("<td>" + totalSumCounter + "</td>").append("</tr>");
        
        // 5、把结果填充到表格中
        $(trContent.toString()).appendTo($reportTable);
        
        
        $('.row tr:even').addClass('even');
        $('.row tr:odd').addClass('odd');
        
        // 6、设置运营状态统计数字
        $('#operationsTable').slideDown();
        var exerciseTotal = operationsCounter.get('运营');
        var unstartTotal = operationsCounter.get('未开始');
        var unusedTotal = operationsCounter.get('闲置');
        var preservingTotal = operationsCounter.get('保留');
        $('#exercise td:eq(2)').html(exerciseTotal);
        $('#unused td:eq(2)').html(unusedTotal);
        $('#unstart td:eq(2)').html(unstartTotal);
        $('#preserving td:eq(2)').html(preservingTotal);
        var operationTotal = exerciseTotal + unstartTotal + unusedTotal + preservingTotal;
        $('#operationTotal td:eq(2)').html(operationTotal);
        
        // 6.1、设置百分比
        $('#exercise td:eq(3)').html(Common.round(exerciseTotal / operationTotal * 100, 2) + '%');
        $('#unstart td:eq(3)').html(Common.round(unstartTotal / operationTotal * 100, 2) + '%');
        $('#unused td:eq(3)').html(Common.round(unusedTotal / operationTotal * 100, 2) + '%');
        $('#preserving td:eq(3)').html(Common.round(preservingTotal / operationTotal * 100, 2) + '%');
        
        // 7.1、生成饼状图表
        chartPie(exerciseTotal, unstartTotal, unusedTotal, preservingTotal);
        
        // 7.2、生成线状图表
        var seriesDatas = [{
            name: '各时段运营场次',
            data: tempDatas
        }];
        chartLine(periods, seriesDatas);
        $('#chartLine,#chartPie').corner();
        // 8、解锁
        $.unblockUI();
    });
    
});

function chartPie(exerciseTotal, unstartTotal, unusedTotal, preservingTotal) {
    var chart = new Highcharts.Chart({
        chart: {
            renderTo: 'chartPie'
        },
        credits: {
            enabled: false
        },
        title: {
            text: '场地活动状态饼状图'
        },
        plotArea: {
            shadow: null,
            borderWidth: null,
            backgroundColor: null
        },
        tooltip: {
            formatter: function() {
                return '<b>' + this.point.name + '</b>: ' + this.y + ' 场次';
            }
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                dataLabels: {
                    enabled: true,
                    formatter: function() {
                        if (this.y > 5) 
                            return this.point.name;
                    },
                    color: 'white',
                    style: {
                        font: '13px Trebuchet MS, Verdana, sans-serif'
                    }
                }
            }
        },
        legend: {
            layout: 'vertical',
            style: {
                left: 'auto',
                bottom: 'auto',
                right: '50px',
                top: '100px'
            }
        },
        series: [{
            type: 'pie',
            name: '运营状态',
            data: [['运营', exerciseTotal], ['未开始', unstartTotal], ['闲置', unusedTotal], ['保留', preservingTotal]]
        }]
    });
    
    
}

function chartLine(periods, seriesDatas) {
	theme = 'dark-blue';

    var chart = new Highcharts.Chart({
        chart: {
            renderTo: 'chartLine',
            defaultSeriesType: 'line',
            margin: [50, 150, 60, 80]
        },
        credits: {
            enabled: false
        },
        title: {
            text: '各时段运营分析',
            style: {
                margin: '10px 100px 0 0' // center it
            }
        },
        subtitle: {
            style: {
                margin: '0 100px 0 0' // center it
            }
        },
        xAxis: {
            categories: periods,
            title: {
                text: '时段'
            }
        },
        yAxis: {
            title: {
                text: '场次'
            },
			min: 0,
			tickInterval : 1,
			maxZoom: 4,
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            formatter: function() {
                return '<b>时段：</b><br/>' +
                this.x.replace(/<br\/>/, '~') +
                ': ' +
                this.y +
                '次';
            }
        },
        legend: {
            layout: 'vertical',
            style: {
                left: 'auto',
                bottom: 'auto',
                right: '10px',
                top: '100px'
            }
        },
        series: seriesDatas
    });
    
}
