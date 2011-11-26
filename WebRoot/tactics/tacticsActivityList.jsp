<%@ page language="java" import="java.util.*,net.yanhl.field.pojo.*,net.yanhl.field.util.FieldUtil
,net.yanhl.util.*,java.sql.Time" pageEncoding="UTF-8"%>
<%@include file="../common/global.jsp" %>
<%@page import="org.apache.commons.lang.StringUtils"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>

	<link rel="stylesheet" type="text/css" href="../css/global.css" />
	<link rel="stylesheet" type="text/css" href="../css/tablecloth.css" />
	<link type="text/css" rel="stylesheet" href="../css/all.css" />
	<link type="text/css" rel="stylesheet" href="../css/weebox.css" />
	<link type="text/css" rel="stylesheet" href="../css/jquery.cluetip.css"/>
	<link type="text/css" rel="stylesheet" href="../css/activity.css"/>

	<script type="text/javascript" src="../common/js/jquery-last.js"></script>
	<script type="text/javascript" src="../common/js/plugin/bgiframe.js"></script>
	<script type="text/javascript" src="../common/js/plugin/weebox.js"></script>
	<script type="text/javascript" src="../common/js/plugin/jquery.cluetip.js"></script>
	<script type="text/javascript" src="../common/js/plugin/formValidator.js"></script>
	<script type="text/javascript" src="../common/js/plugin/formValidatorRegex.js"></script>
	<script type="text/javascript" src="../common/datepicker/WdatePicker.js"></script>
	<script type="text/javascript" src="../common/js/common.js"></script>
	<script type="text/javascript" src="../common/js/form.js"></script>
	<script type="text/javascript" src="../common/js/list.js"></script>
	<script type="text/javascript" src="../activity/js/common.activity.js"></script>
	<script type="text/javascript" src="js/tacticsActivity.js"></script>
  </head>

  <body style="text-align: center">
  	<h1><font color="white">已预订待处理活动列表</font></h1>
  	<table border="0" cellspacing="0" width="100%" id="row" class="row">
  		<thead>
  			<tr>
  				<th>场地</th>
  				<th>预定日期</th>
  				<th>时段</th>
  				<th>价格</th>
  				<th>操作</th>
  			</tr>
  		</thead>
  		<tbody>
  			<c:forEach items="${activityList }" var="activity">
  			<bean:define id="activity" name="activity" type="net.yanhl.field.pojo.FieldActivity"></bean:define>
  			<tr align="center">
  				<input type="hidden" id="chk" value="${activity.id }"/>
	  			<input type="hidden" id="orderId" value="<%=activity.getFieldOrder() == null ? "" : activity.getFieldOrder().getId() %>"/>
	  			<input type="hidden" id="paymentSum" value="<%=activity.getFieldOrder() == null ? "" : activity.getFieldOrder().getPaymentSum() %>"/>
  				<td name="period">${activity.fieldName }</td>
  				<td name="period"><%=DateUtil.format(activity.getUsableDate(), DateUtil.TYPE_DATE) %></td>
  				<td name="period">${activity.period }</td>
	    		<td name="price">${activity.price }</td>
	    		<td name="operate">
	    			<button name="unbooking" title="取消预订">退 款</button>
	    		</td>
  			</tr>
  			</c:forEach>
  		</tbody>
  	</table>

  	<div id="unbookingTemplate" class="template">
    	<div id="refundmentDiv" style="display: none">
	    	输入退款金额：<input type="text" id="refundment" size="5" disabled="disabled" title="正在加载已付金额"/>
	    	<a id='orgPrice' href='javascript:;' title="点击自动设置实际支付金额" style="color:blue;">实际支付</a>
	    	<br/>
	    	<font style="margin-left:20px;font-size:9pt;background-color: yellow;">
	    		退款金额不能大于实付金额：<span id="maxRefundment" style="font-weight: bold;"></span>
	    	</font>
    	</div>
    	<div id="withoutRefundment" style="display: none">
    		<br/><font style="background-color: yellow;">无需退款，确认取消预订吗？</font>
    	</div>
    </div>

  </body>
</html>