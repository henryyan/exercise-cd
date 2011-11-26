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
	        #totalDiv {
	        	font-size: 4;
	        	margin: 0 auto;
	        	width: auto;
	        	text-align: center;
	        	padding: .5em 0 .5em 0;
	        }
	        #orderNumber {
	        	margin-left: .5em;
	        	margin-right: .5em;
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
		<script src="js/protocolFee.js" type="text/javascript"></script>

	</head>

	<body>
		<div>
			<font size="4">协议服务费统计日期：</font>
			<input type="text" id="startDate" class="Wdate" size="12" value="${param.startDate }" readonly="readonly" />
			至
			<input type="text" id="endDate" class="Wdate" size="12" value="${param.endDate }" readonly="readonly" />
			<button id="submit" type="button">统计</button>
		</div>
		<div id="totalDiv">
			<div>共<span id="orderNumber" class="onLoad"></span>项订单，
				<span>
					活动价格共计：<span id="priceSum" class="onLoad"></span> 元，
				</span>
				<span>服务费共计：<span id="feeSum" class="onLoad"></span> 元</span>
			</div>
		</div>
		<table id="list"></table>
		<div id="pager"></div>
	</body>
</html>