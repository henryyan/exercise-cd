<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>场地预定日历</title>
		<%@ include file="../common/meta.jsp" %>

		<link rel="stylesheet" type="text/css" href="../css/global.css"/>
		<link rel="stylesheet" type="text/css" href="../css/tablecloth.css" />
		<link rel="stylesheet" type="text/css" href="../css/all.css"/>
		<link rel="stylesheet" type="text/css" href="../css/nyroModal.css"/>
		<link rel="stylesheet" type="text/css" href="../css/activity.css"/>
		<link rel="stylesheet" type="text/css" href="../css/log/blackbird.css"/>
		<link rel="stylesheet" type="text/css" href="../common/js/plugin/tipTip/tipTip.css"/>

		<script type="text/javascript" src="../common/js/jquery-last.js"></script>
		<script type="text/javascript" src="../common/js/plugin/jquery.nyroModal.js"></script>
		<script type="text/javascript" src="../common/js/plugin/tipTip/jquery.tipTip.js"></script>
		<script type="text/javascript" src="../common/js/plugin/blackbird.js"></script>
		<script type="text/javascript" src="../common/js/common.js"></script>
		<script type="text/javascript" src="../common/js/list.js"></script>
		<script type="text/javascript" src="js/fieldActivityCalendar.js"></script>
	</head>

	<body>
		<input type="hidden" id="fieldType" value="${param.fieldType }"/>
		<div id="dateShow">
			<span id="year"></span>&nbsp;年
			<span id="month"></span>&nbsp;月
			<span id="day"></span>&nbsp;日
		</div>

		<div id="calendarBtns">
			<button id="preMonth" disabled="disabled">上个月</button>
			<button id="nextMonth">下个月</button>
			<button id="currentMonth">当  月</button>
		</div>

		<div id="tipInfo" class="onLoad">正在更新场地预定信息</div>

		<div id="bookingCalendar" style="text-align: center;"></div>

		<div class="notice">
			<ul>
				<li><span class="usable">&nbsp;</span>有空余场地</li>
				<li><span class="unusable">&nbsp;</span>无空余场地</li>
				<li><span class="timeout">&nbsp;</span>无空余场地</li>
			</ul>
		</div>
	</body>

</html>