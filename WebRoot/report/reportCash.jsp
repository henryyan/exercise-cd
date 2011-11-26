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
		<style type="text/css">
			table td { color: black; text-align: center;}
			td {
				padding-top: 2px;
				padding-bottom: 2px;
				vertical-align: middle;
			}
			.current {background-color: #FFCC00; color: black; font-weight: bold;}
			.after {background-color: #FFFFCC; color: black; font-weight: bold;}
			.cash {background-color: #CCFFCC; color: black; font-weight: bold;}
			.items {background-color: #E3EEFB;}
			.cl {background-color: #E3EEFB;}
		</style>

		<script type="text/javascript" src="../common/js/jquery-last.js"></script>
		<script type="text/javascript" src="../common/js/plugin/jquery.blockUI.js"></script>
		<script type="text/javascript" src="../common/datepicker/WdatePicker.js"></script>
		<script type="text/javascript" src="../common/js/common.js"></script>
		<script type="text/javascript" src="js/reportCash.js"></script>
	</head>

	<body>
		<h1>资金状况报表</h1>
		<form action="" id="reportForm" method="post">
			<div class="title">
				<span>
					<input type="text" id="reportDate" name="reportDate" class="Wdate" size="10" readonly="readonly"
						value="${empty param.reportDate ? strSystemDate : param.reportDate}"/>资金状况
				</span>
			</div>
		</form>
		<table border="0" width="780" cellspacing="0" class="row">
			<tr>
				<td width="80" class="items">现金交易</td>
				<td colspan="10">
					<table id="cashTable" border="0" width="100%" cellspacing="0" class="row items">
						<tr>
							<td width="150">&nbsp;</td>
							<td colspan="2" class="current">当日实现的运营收入</td>
							<td colspan="2" class="after">日后实现的预收款</td>
							<td class="cash">现金盒</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td class="current">场次</td>
							<td class="current">金额</td>
							<td class="after">场次</td>
							<td class="after">金额</td>
							<td class="cash">&nbsp;</td>
						</tr>
						<tr class="jsfsxj">
							<td>即时发生的锻炼现金</td>
							<td class="current">&nbsp;</td>
							<td class="current">&nbsp;</td>
							<td class="after">&nbsp;</td>
							<td class="after">&nbsp;</td>
							<td class="cash">&nbsp;</td>
						</tr>
						<tr class="drsdhxj">
							<td>当日收到以后锻炼的现金</td>
							<td class="current">&nbsp;</td>
							<td class="current">&nbsp;</td>
							<td class="after">&nbsp;</td>
							<td class="after">&nbsp;</td>
							<td class="cash">&nbsp;</td>
						</tr>
						<tr class="cshykxj">
							<td>出售会员卡收到的现金</td>
							<td class="current">&nbsp;</td>
							<td class="current">&nbsp;</td>
							<td class="after">&nbsp;</td>
							<td class="after">&nbsp;</td>
							<td class="cash">&nbsp;</td>
						</tr>
						<tr class="xjxj">
							<td>小计</td>
							<td class="current">&nbsp;</td>
							<td class="current">&nbsp;</td>
							<td class="after">&nbsp;</td>
							<td class="after">&nbsp;</td>
							<td class="cash">&nbsp;</td>
						</tr>
					</table>
				</td>
			</tr>

			<tr>
				<td class="items">非现金交易</td>
				<td colspan="10">
					<table id="unCashTable" border="0" width="100%" cellspacing="0" class="row items">
						<tr class="yqsdhdrsr">
							<td width="150">以前收到现金并当日锻炼实现的收入</td>
							<td width="120" class="current">&nbsp;</td>
							<td width="120" class="current">&nbsp;</td>
							<td class="after">&nbsp;</td>
							<td class="after">&nbsp;</td>
							<td width="83" class="cash">&nbsp;</td>
						</tr>
						<tr class="hykxs">
							<td>会员卡消费实现的收入</td>
							<td class="current">&nbsp;</td>
							<td class="current">&nbsp;</td>
							<td class="after">&nbsp;</td>
							<td class="after">&nbsp;</td>
							<td class="cash">&nbsp;</td>
						</tr>
						<tr class="fxjxj">
							<td>小计</td>
							<td class="current">&nbsp;</td>
							<td class="current">&nbsp;</td>
							<td class="after">&nbsp;</td>
							<td class="after">&nbsp;</td>
							<td class="cash">&nbsp;</td>
						</tr>
					</table>
				</td>
			</tr>

			<tr>
				<td class="items">合计及结余</td>
				<td colspan="10">
					<table id="totalAndRemain" border="0" width="100%" cellspacing="0" class="row items">
						<tr class="fxjhjjy">
							<td width="150">&nbsp;</td>
							<td width="241" class="current">&nbsp;</td>
							<td class="after">&nbsp;</td>
							<td width="83" class="cash">&nbsp;</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>