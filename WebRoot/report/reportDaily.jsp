<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="net.yanhl.report.pojo.ReportDaily"%>
<%@ include file="../common/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />

		<link rel="stylesheet" type="text/css" href="../css/global.css" />
		<link rel="stylesheet" type="text/css" href="../css/tablecloth.css" />
		<link type="text/css" rel="stylesheet" href="../css/all.css" />
		<link rel="stylesheet" type="text/css" href="../css/report.css" />

		<script type="text/javascript" src="../common/js/jquery-last.js"></script>
		<script type="text/javascript" src="../common/js/plugin/jquery.blockUI.js"></script>
		<script type="text/javascript" src="../common/js/plugin/jquery.corner.js"></script>
		<script type="text/javascript" src="../common/js/plugin/chart/highcharts.js"></script>
		<!--[if IE]>
			<script type="text/javascript" src="../common/js/plugin/chart/excanvas.compiled.js"></script>
		<![endif]-->
		<script type="text/javascript" src="../common/datepicker/WdatePicker.js"></script>
		<script type="text/javascript" src="../common/js/common.js"></script>
		<script type="text/javascript" src="js/reportDaily.js"></script>
	</head>

	<body>
		<form action="" id="reportForm" method="post">
			<input type="hidden" id="venueId" value="${user.venueInfo.id }"/>

			<div class="title">
				<span class="venueName">${user.venueInfo.venueName }</span>
				<span>
					<input type="text" id="reportDate" name="reportDate" class="Wdate" size="10" readonly="readonly"
						value="${empty param.reportDate ? strSystemDate : param.reportDate}"/>运营表(锻炼)
				</span>
			</div>
		</form>
		<hr/>

		<table id="reportTable" width="100%" border="0" cellspacing="0" cellpadding="0" class="row" style="text-align: center;"></table>
		<br/>
		<table id="operationsTable" width="50%" border="1" cellspacing="0" cellpadding="0" style="text-align: center;">
			<tr id="exercise">
				<td width="100">运营</td>
				<td class="status exercise"></td>
				<td width="80">&nbsp;</td>
				<td width="80">&nbsp;</td>
			</tr>

			<tr id="unstart">
				<td>未开始(临时)</td>
				<td class="status unstart"></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>

			<tr id="unused">
				<td>闲置</td>
				<td class="status unused"></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>

			<tr id="preserving">
				<td>保留</td>
				<td class="status preserving"></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>

			<tr id="operationTotal">
				<td>合计</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>100%</td>
			</tr>
		</table>
		<br/>
		<div id="chartLine" style="background-color:#FFF8DC; width: 760px; height: 400px; margin: 0 auto"></div><br/>
		<div id="chartPie" style="background-color:#FFF8DC; width: 760px; height: 400px; margin: 0 auto"></div>
	</body>
</html>