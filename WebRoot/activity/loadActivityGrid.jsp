<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/global.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<%@ include file="../common/meta.jsp" %>

	<script type="text/javascript">
	$(function(){
		$.getJSON('getActivityGrid.do', {
	      fieldType: '${param.fieldType }',
	      usableDate: '${param.usableDate }'
	   }, function(grid) {
		   var fullFromDate = '${param.usableDate }'.split('-');
		   var fromYear = fullFromDate[0];
		   var fromMonth = fullFromDate[1] * 1 - 1;
		   var fromDate = fullFromDate[2];
		   var gridTableContent = new StringBuffer();

		   // 日期兼容浏览器处理
		   if ($.browser.mozilla || isChrome() || isSafari()) {
			   fromYear = parseInt(fromYear) - 1900;
		   }

			for (k in grid) {
				var period = k;
				var activity = grid[k];
				var appendContent = new StringBuffer();
				appendContent.append("<td class='period'>" + period + "</td>");

				for (a in activity) {
					var className, fieldUrl, canBook1 = false;
					var orderId = activity[a][0];
					var fieldId = activity[a][1];
					var fieldName = activity[a][2];
					var activityStatus = activity[a][3];
					var order = activity[a][4];
					
					// 特殊处理
					//fieldName = (order ? order.isOrderSite ? fieldName + "(网站预定)" : fieldName : fieldName);
					if (order && order.isOrderSite) {
						fieldName = "<img src='../images/activity/book-outside.png' class='book-outside' title='网站预定'/>" + fieldName;
				    }
					
					canBook1 = canBook(activityStatus);

					className = "usable";
					fieldUrl = "fieldActivityTab.jsp?type=book&fieldType=${param.fieldType}&pickedDate=${param.usableDate }";
					// 处理当天情况
					// alert(systemDate.getYear() + "," + systemDate.getMonth() + "," + systemDate.getDate());
				    if(fromYear == systemDate.getYear() && fromMonth == systemDate.getMonth() && fromDate == systemDate.getDate()) {
				    	var times = k.split(':');
						var hour = times[0] * 1;
						var minute = times[1] * 1;
						// 已经过时，预定过的场地不在过期范围内
						if(orderId == 0 && hour < systemDateTime.getHours() || (hour == systemDateTime.getHours() && minute < systemDateTime.getMinutes())) {
							className = 'timeout';
						} else {
							if(activityStatus == '保留') {
								className = "reservation";
							} else if(canBook1 == false) {
								className = "unusable";
							}
						}
				    } else {
				    	if(activityStatus == '保留') {
							className = "reservation";
						} else if(canBook1 == false) {
							className = "unusable";
						}
				    }

					if(className != 'timeout'/*fieldUrl.length > 1*/) {
						var linkEleBefore = "<a href='" + fieldUrl + "'";
						if (orderId > 0) {
							linkEleBefore += " class='basic'";
							linkEleBefore += " orderId=" + orderId;
							linkEleBefore += " style='cursor:help'";
						}
						var linkEleAfter = ">" + fieldName + "</a>";
						var linkEle = linkEleBefore + linkEleAfter;
						appendContent.append("<td class='" + className + "'>" + linkEle + "</td>");
					} else {
						if (className == 'timeout') {
							var linkEle = "<a href='" + fieldUrl + "'>" + fieldName + "</a>";
							appendContent.append("<td class='" + className + "'>" + linkEle + "</td>");
						} else {
							appendContent.append("<td class='" + className + "'>" + fieldName + "</td>");
						}
					}
				}

				gridTableContent.append("<tr>" + appendContent.toString() + "</tr>");

			}// end for
			$('#gridTable').html(gridTableContent.toString());
			
			// 提示
			$('#gridTable .basic').tipTip({
				content: $('#orderInfo').html(),
				enter: function(tipObj) {
					var orderId = $(tipObj).attr('orderId');
					$.getJSON('loadOrder.do', {
		                id: orderId
		            }, function(order) {
		                for (key in order) {
		                    $('#d' + key, '#tiptip_content').html(order[key]);
		                }
		            });
				},
				exit: function(tipObj) {
					$('span', '#tiptip_content').text('');
				}
			});
			
			// 当前时段和场地交叉点
			$('.usable').hover(function(){
				$(this).addClass("currentGrid");
			}, function(){
				$(this).removeClass("currentGrid");
			});

			$('td a').click(function(event) {
				event.stopPropagation();
			});
			$('td:has(a)').bind('click', function() {
				location.href = $('a', this).attr('href');
			});

	   });
	});

	// 判断是否可预定
	function canBook(activity) {
		if(activity == '' || activity == '未预订' || activity == '取消') {
			return true;
		} else {
			return false;
		}
	}
</script>

	</head>
	<body>
		<table id="gridTable" width="100%" border="0" cellspacing="0"></table>
		<div class="status">
			<span class='usable'>&nbsp;</span>可预订
			<span class='unusable'>&nbsp;</span>不可预订
			<span class='timeout'>&nbsp;</span>过期
			<span class='reservation'>&nbsp;</span>保留
		</div>
		<div id="orderInfo" class="template">
			<div>订单号码：<span id="did">正在加载……</span></div>
			<div>会员号码：<span id="duserCode"></span></div>
	    	<div style="padding-left:15px;">联系人：<span id="dcontact"></span></div>
			<div>联系电话：<span id="dphone"></span></div>
    	</div>
	</body>
</html>