<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<%@include file="../common/global.jsp" %>
	<%@ include file="../common/meta.jsp" %>
	<title>零售商品列表</title>

	<link rel="stylesheet" type="text/css" href="../css/global.css" />
	<link rel="stylesheet" type="text/css" href="../css/all.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="../themes/${themeName }/jquery-ui-${themeVersion }.custom.css" />
   	<link rel="stylesheet" type="text/css" media="screen" href="../css/ui.jqgrid.css" />
   	<style type="text/css">
   		body{ font: 80.5% "Trebuchet MS", sans-serif;}
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
	<script src="js/retailGoods.js" type="text/javascript"></script>
</head>
<body>
	<table id="list"></table>
	<div id="pager"></div>
	
	<div id="sellTemplate" title="销售商品" style="display:none">
		<div id="sellTip" style="display:none" class="ui-state-error"></div>
		<p>出售数量：<input id="amount" type="text" size="4" maxlength="4" value="1" /></p>
		<p>会员号码：<input id="memberCardNumber" type="text" size="13" /></p>
		<p>卡内余额：<span id="balance"></span></p>
		<p>手机号码：<span id="mobilePhone"></span></p>
		<p>会员名称：<span id="memberUserName"></span></p>
	</div>
</body>
</html>