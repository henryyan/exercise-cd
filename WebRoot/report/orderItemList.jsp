<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/global.jsp" %>
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />

		<link rel="stylesheet" type="text/css" href="../css/global.css" />
		<link rel="stylesheet" type="text/css" href="../css/all.css" />
		<link rel="stylesheet" type="text/css" href="../themes/${themeName }/jquery-ui-${themeVersion }.custom.css" />
    	<link rel="stylesheet" type="text/css" href="../css/ui.jqgrid.css" />
    	<style type="text/css">
	        html, body {
		        margin: 0;            /* Remove body margin/padding */
		        padding: 0;
		        overflow: hidden;    /* Remove scroll bars on browser window */
		        font-size: 75%;
	        }
	    </style>

		<script type="text/javascript" src="../common/js/jquery-last.js"></script>
		<script type="text/javascript" src="../common/datepicker/WdatePicker.js"></script>
		<script type="text/javascript" src="../common/js/common.js"></script>
		<script src="../common/js/plugin/jqGrid/i18n/grid.locale-cn.js" type="text/javascript"></script>
	    <script type="text/javascript">
		    $.jgrid.no_legacy_api = true;
		    $.jgrid.useJSON = true;
	    </script>
	    <script src="../common/js/plugin/jqGrid/jquery.jqGrid.min.js" type="text/javascript"></script>
	    <script src="../common/js/ui/jquery-ui-${themeVersion }.custom.min.js" type="text/javascript"></script>
		<script src="js/orderItemList.js" type="text/javascript"></script>

	</head>

	<body>
		<div>
			<span style="font-size: 14px;">选择订单日期：</span><input type="text" id="bookTime" name="bookTime" class="Wdate" size="12" value="${param.bookTime }" readonly="readonly" />
		</div>
		<table id="list"></table>
		<div id="pager"></div>
	</body>
</html>