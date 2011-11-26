var common = new Common();

// 可以操作X天之前的场地
var can_back_days = 7;

$(function() {
    var fromDate = new Date(systemDate.getYear(), systemDate.getMonth(), systemDate.getDate());

    // 默认为当月
    createCalendar(fromDate);

    // 上个月
    $('#preMonth').click(function() {
        var calendarDate = new Date();
		var preMonth = $('#dateShow #month').text() * 1 - 2;
        calendarDate.setYear($('#dateShow #year').text() * 1);
        calendarDate.setMonth(preMonth);

		// 本月之后的从1号开始加载
		if (preMonth > systemDate.getMonth()) {
			calendarDate.setDate(1);
		}
        createCalendar(calendarDate);
		if (preMonth == systemDate.getMonth()) {
			$(this).attr('disabled', true);
		}
    });
    // 下个月
    $('#nextMonth').click(function() {
        var calendarDate = fromDate;
        calendarDate.setYear($('#dateShow #year').text() * 1);
        calendarDate.setMonth($('#dateShow #month').text() * 1);
        calendarDate.setDate(1);
        createCalendar(calendarDate);
        $('#preMonth').attr('disabled', false);
    });

    // 当月
    $('#currentMonth').click(function() {
        fromDate = new Date(systemDate.getYear(), systemDate.getMonth(), systemDate.getDate());
        createCalendar(fromDate);
        $('#preMonth').attr('disabled', true);
    });

});

/**
 * 创建日历
 * @param {Object} a
 */
function createCalendar(a) {
    var caHtml = new StringBuffer();
    caHtml.append("<center><table width='654px' id='calendarTable' border='0' cellspacing='0'>");
    caHtml.append("<thead><tr>");
    caHtml.append("<th><img src='../images/calendar/2.jpg' /></th>");
    caHtml.append("<th><img src='../images/calendar/3.jpg' /></th>");
    caHtml.append("<th><img src='../images/calendar/4.jpg' /></th>");
    caHtml.append("<th><img src='../images/calendar/5.jpg' /></th>");
    caHtml.append("<th><img src='../images/calendar/6.jpg' /></th>");
    caHtml.append("<th><img src='../images/calendar/7.jpg' /></th>");
    caHtml.append("<th class='last'><img src='../images/calendar/8.jpg' /></th>");
    caHtml.append("</tr></thead>"); /*第一行*/
    caHtml.append("<tr>");

    var date = a.getDate();
	if ($.browser.mozilla || isChrome() || isSafari()) {
		a.setDate(3);
	} else {
		a.setDate(1);
	}
    var day = a.getDay();
    var year = a.getFullYear();

    // FF浏览器年加上1900
    if (year < 1000 && ($.browser.mozilla || isChrome() || isSafari())) {
        year += 1900;
    }

    var month = a.getMonth();
    var maxDay;
    var today = date;
    for (var i = 0; i < day; i++) {
        caHtml.append("<td>&nbsp;</td>");
    }

    for (var j = 1; j <= 7 - day; j++) {
		if (j == today) {
            caHtml.append("<td class='currentDate'>" + j + "</td>");
        } else {
			if (j == 7 - day) {
				caHtml.append("<td class='last'>" + j + "</td>");
			} else {
	            caHtml.append("<td>" + j + "</td>");
			}
        }
    }
    caHtml.append("</tr>"); /*第2行*/
    caHtml.append("<tr>");
    for (var k = j; k <= j + 6; k++) {
        if (k == today) {
            caHtml.append("<td class='currentDate'>" + k + "</td>");
        } else {
			if (k == j + 6) {
				caHtml.append("<td class='last'>" + k + "</td>");
			} else {
	            caHtml.append("<td>" + k + "</td>");
			}
        }
    }
    caHtml.append("</tr>");

    caHtml.append("<tr>"); /*第3行*/
    for (var l = k; l <= k + 6; l++) {
        if (l == today) {
            caHtml.append("<td class='currentDate'>" + l + "</td>");
        } else {
			if (l == k + 6) {
	            caHtml.append("<td class='last'>" + l + "</td>");
			} else {
				caHtml.append("<td>" + l + "</td>");
			}
        }
    }

    caHtml.append("</tr>"); /*第4行*/
    switch (month) {
        case 1:{
            if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
                maxDay = 29;
            else
                maxDay = 28;
            break;
        }
        case 3:
            maxDay = 30;
            break;
        case 5:
            maxDay = 30;
            break;
        case 8:
            maxDay = 30;
            break;
        case 10:
            maxDay = 30;
            break;
        default:
            maxDay = 31;
    }

    caHtml.append("<tr>");
    for (var m = l; m <= l + 6; m++) {
        if (m <= maxDay) {
            if (m == today) {
                caHtml.append("<td class='currentDate'>" + m + "</td>");
            } else {
                if (m == l + 6) {
		            caHtml.append("<td class='last'>" + m + "</td>");
				} else {
					caHtml.append("<td>" + m + "</td>");
				}
            }
        }
    }

    caHtml.append("</tr>"); /*第5行*/
    caHtml.append("<tr>");

    for (var n = m; n <= m + 6; n++) {
        if (n <= maxDay) {
            if (n == today) {
                caHtml.append("<td class='currentDate'>" + n + "</td>");
            } else {
                if (n == m + 6) {
		            caHtml.append("<td class='last'>" + n + "</td>");
				} else {
					caHtml.append("<td>" + n + "</td>");
				}
            }
        }
    }
    caHtml.append("</tr>"); /*第6行*/
    // 最后一行没有数据输出时不输出空TR
    if (n <= maxDay) {
        caHtml.append("<tr>");

        for (var o = n; o <= maxDay; o++) {
            if (o <= maxDay) {
                if (o == today) {
                    caHtml.append("<td class='currentDate bottom-last'>" + o + "</td>");
                } else {
                    if (n == maxDay) {
			            caHtml.append("<td class='last bottom-last'>" + o + "</td>");
					} else {
						caHtml.append("<td class='bottom-last'>" + o + "</td>");
					}
                }
            }
        }
        caHtml.append("</tr>"); /*第7行*/
    }

    caHtml.append("</table></center>");
    var dealedDate = year + "-" + (month + 1) + "-" + today;
    $('#dateShow #year').text(year);
    $('#dateShow #month').text(month + 1);
    $('#dateShow #day').text(today);

    $('#bookingCalendar').fadeOut('fast').html(caHtml.toString()).fadeIn('fast');

    //  获取日期的
    getDateStatus(dealedDate);
}

/**
 * 根据开始日期查找场地预订状态用颜色标识
 * @param {Object} dealedDate
 */
function getDateStatus(dealedDate) {
	
    $('#tipInfo').removeClass('onCorrect').addClass('onLoad').text('正在加载场地预订信息……');
    $.getJSON('fieldActivityStatus.do', {
        fieldType: $('#fieldType').val(),
        fromDate: dealedDate,
        dataType: 'date'
    }, function(status) {

        // 日历当前年月
        var pageYear = $('#year').text() * 1;
        var pageMonth = $('#month').text() * 1;
        var pageDate = $('#day').text() * 1;
		var fullPageDate = pageYear + "-" + pageMonth + "-" + pageDate;

        var systemYear = systemDate.getYear();

        // 提前一周进入链接
        var beforeWeek;

        // TODO 月初会有问题，比如今天是2010-03-30，点击下个月后4.1日为补登计
        var dealedDateObj = dealedDate;
        if (pageDate == 1) {
            dealedDateObj = common.dateAdd(dealedDate, -1, 'd');
        }
        $('.mainBody ol').html('');

		var systeYear = systemDate.getYear();
		// FF浏览器年加上1900
        if ($.browser.mozilla || isChrome() || isSafari()) {
            systeYear += 1900;
        }

		var stepOverMonth = false; //是否跨月
        var tempSysteDate = new Date(systeYear, systemDate.getMonth() + 1, systemDate.getDate());
		// 本月最后一天
		var monthLastDay = common.getMonthLastDay(pageYear, pageMonth - 1);
		
		// 计算本月最后一天和当前日期的差距
		var diffDays = common.dateDiff(monthLastDay, new Date());

		var $tds = 0;
		$('#calendarTable td').each(function(){
			if ($(this).html() != '&nbsp;') {
				$tds++;
			}
		});
		log.debug('共' + $tds + "个TD");
		var forCt = can_back_days;
		if (diffDays < 0) {
			stepOverMonth = true;
			forCt += diffDays;
		}
		
        for (var i = 1; i <= forCt; i++) {

            beforeWeek = tempSysteDate.DateAdd('d', -1 * i);
            var beforeWeekYear = beforeWeek.getYear();
			
			// FF浏览器年加上1900
	        if ($.browser.mozilla || isChrome() || isSafari()) {
	            beforeWeekYear += 1900;
	        }
			
			// 跨月份情况
			if (beforeWeekYear == pageYear &&
				pageMonth == systemDate.getMonth()) {
				
				var tdDate = $tds - i + 1;
				log.debug(tdDate);
				
				// 1、设置CLASS
				var $td = $("#calendarTable td:contains('" + tdDate + "')");
				$($td).addClass('timeout').attr('title', '进入可以补登计');
				
				// 2、设置链接路径
				var params = {
					type: 'book',
					patch: true,
					fieldType: $('#fieldType').val(),
					pickedDate: beforeWeek.toDateStr()
				};
				var goUrl = "fieldActivityTab.jsp?" + $.param(params);
				$($td).css('cursor', 'pointer').data('goUrl', goUrl).click(function(){
					location.href = $(this).data('goUrl');
				});
				
			} else {
				$('#calendarTable td').each(function() {
                var thisDate = parseInt($(this).text());
				
				if (beforeWeekYear == pageYear &&
					beforeWeek.getMonth() == pageMonth &&
					beforeWeek.getDate() == thisDate) {
						
					// 1、设置CLASS
					$(this).addClass('timeout').attr('title', '进入可以补登计');
					
					// 2、设置链接路径
					var params = {
						type: 'book',
						patch: true,
						fieldType: $('#fieldType').val(),
						pickedDate: beforeWeek.toDateStr()
					};
					var goUrl = "fieldActivityTab.jsp?" + $.param(params);
					$(this).css('cursor', 'pointer').data('goUrl', goUrl).click(function(){
						location.href = $(this).data('goUrl');
					});
					
				}
				
            });
			}

        }

        // 设置今天之后的表格
		if (!stepOverMonth) {
			for (k in status) {
			
				var strDates = k.split('-');
				var year = strDates[0];
				var month = strDates[1];
				var date = strDates[2] * 1;
				
				// 根据状态设置颜色
				$('#calendarTable td').each(function(){
					if (year == pageYear && month == pageMonth && $(this).text() == date) {
						var className = status[k] == 1 ? "usable" : "unusable";
						$(this).addClass(className).bind('click', enterSelectPriod);
					}
				});
			}
		}
		

        // 不在预订范围内的
        //$('#calendarTable td:not(.usable,.unusable)').attr('title', '没有可预订场地');
		
		// 检查超时的日历天数
		var $timeout = $('.timeout');
		if ($timeout.length < can_back_days) {
			// 启用上个月按钮
			$('#preMonth').attr('disabled', false);
		} else {
			$('#preMonth').attr('disabled', true);
		}

        // 设置状态信息
        $('#tipInfo').removeClass('onLoad').addClass('onCorrect').text('场地预订信息加载完毕！');
    });
}

/**
 * 选择日期后进入时段选择页面
 */
function enterSelectPriod() {

    var year = $('#dateShow #year').text() * 1;
    var month = $('#dateShow #month').text() * 1;
    var bookingDate = year + "-" + month + "-" + $(this).text();

    var fieldType = $('#fieldType').val();

    // 全部预订后跳转到预定管理页面
    if ($(this).hasClass('unusable')) {
        location.href = 'fieldActivityTab.jsp?type=book&fieldType=' + fieldType + '&pickedDate=' + bookingDate;
        return;
    }

    $.nyroModalManual({
        title: '场地活动预订一览表',
        height: 550,
        width: 800,
        url: 'loadActivityGrid.jsp?usableDate=' + bookingDate + '&fieldType=' + fieldType
    });

}
