<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="../common/global.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<%@ include file="../common/meta.jsp" %>
	<title>会员卡列表</title>

	<link rel="stylesheet" type="text/css" href="../css/global.css" />
	<link rel="stylesheet" type="text/css" href="../css/all.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="../themes/${themeName }/jquery-ui-${themeVersion }.custom.css" />
   	<link rel="stylesheet" type="text/css" media="screen" href="../css/ui.jqgrid.css" />
   	<style type="text/css">
   		body{ font: 80.5% "Trebuchet MS", sans-serif;}
   		#cardUsageDetailTable td {text-align: center;}
   		.separator {font-weight: bold; color: yellow;}
   	</style>

	<script src="../common/js/jquery-last.js" type="text/javascript"></script>
    <script src="../common/js/plugin/jqGrid/i18n/grid.locale-cn.js" type="text/javascript"></script>
    <script type="text/javascript">
    	$.jgrid.no_legacy_api = true;
    	$.jgrid.useJSON = true;
    </script>
    <script src="../common/js/plugin/jqGrid/jquery.jqGrid.js" type="text/javascript"></script>
    <script src="../common/js/plugin/jquery.validate.js" type="text/javascript"></script>
    <script src="../common/js/ui/jquery-ui-${themeVersion }.custom.min.js" type="text/javascript"></script>
    <script src="../common/datepicker/WdatePicker.js" type="text/javascript"></script>
    <script src="../common/js/common.js" type="text/javascript"></script>
	<script src="js/memberCard.js" type="text/javascript"></script>
</head>
<body>
	<table id="cardList"></table>
	<div id="cardPager"></div>
	<hr />
	<table id="recordList"></table>
	<div id="recordPager"></div>

	<div id="dialog-form" title="会员卡-充值">
		<form id="rechargeForm" name="rechargeForm">
			<table width="100%">
				<tr>
					<td class="label" style="vertical-align: top; width: 100px">充值金额：</td>
					<td>
						<input type="text" id="recharge" name="recharge" maxlength="9" size="12" /><br/>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div id="cardUsageDetailTemplate" class="template">
		<div class="loading">正在加载信息，请稍候……</div>
		<table id="cardUsageDetailTable" cellspacing="0" border="0" width="100%">
			<tr>
				<th>序号</th>
				<%-- <th>卡号</th> --%>
				<th>联系人</th>
				<%-- <th>手机号码</th> --%>
				<th>场地信息</th>
				<th>活动信息</th>
				<th>价格</th>
				<th>预订时间</th>
				<th>付款时间</th>
			</tr>
		</table>
	</div>
</body>
</html>