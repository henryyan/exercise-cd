<%@ page language="java" import="java.util.*,net.yanhl.field.pojo.*,net.yanhl.field.util.FieldUtil
,net.yanhl.util.*" pageEncoding="UTF-8"%>
<%@page import="org.apache.commons.lang.BooleanUtils"%>
<%@include file="../common/global.jsp" %>
<%@page import="org.apache.commons.lang.StringUtils"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
	<%@ include file="../common/meta.jsp" %>
	<title>场地活动现场管理</title>	

	<link rel="stylesheet" type="text/css" href="../css/global.css" />
	<link rel="stylesheet" type="text/css" href="../css/tablecloth.css" />
	<link type="text/css" rel="stylesheet" href="../css/all.css" />
	<link type="text/css" rel="stylesheet" href="../css/weebox.css" />
	<link rel="stylesheet" type="text/css" href="../common/js/plugin/tipTip/tipTip.css"/>
	<link type="text/css" rel="stylesheet" href="../css/activity.css"/>

	<script type="text/javascript" src="../common/js/jquery-last-unzip.js"></script>
	<script type="text/javascript" src="../common/js/plugin/bgiframe.js"></script>
	<script type="text/javascript" src="../common/js/plugin/weebox.js"></script>
	<script type="text/javascript" src="../common/js/plugin/tipTip/jquery.tipTip.js"></script>
	<script type="text/javascript" src="../common/js/plugin/jquery.countDown.js"></script>
	<script type="text/javascript" src="../common/datepicker/WdatePicker.js"></script>
	<script type="text/javascript" src="../common/js/common.js"></script>
	<script type="text/javascript" src="../common/js/list.js"></script>
	<script type="text/javascript" src="js/common.activity.js"></script>
	<script type="text/javascript" src="js/local.js"></script>
	<script type="text/javascript">
		var systemTime = <%=new Date().getTime() %>;
		var fieldType = "${param.fieldType }";
	</script>

  </head>

  <body>
  	<input type="hidden" id="advance" value="${field.advance }"/>
  	<%
  	String strStep = request.getAttribute("step") == null ? "0" : request.getAttribute("step").toString();
  	int step = Integer.parseInt(strStep);
  	String focusDate = DateUtil.format(DateUtil.dateAdd(DateUtil.getSysdate(), step), DateUtil.TYPE_DATE);
  	%>

  	<table border="0" cellspacing="0" width="100%" id="row" class="row">
  		<thead>
  			<tr>
  				<th width="13%">时段</th>
  				<th width="8%">价格</th>
  				<th width="35%">预订情况</th>
  				<th width="10%">验证码</th>
  				<th>show</th>
  				<th width="13%">验证结果</th>
  				<th width="13%">锻炼情况</th>
  			</tr>
  		</thead>

	    <c:forEach var="activity" items="${result }">
	    	<bean:define id="activity" name="activity"></bean:define>
	    	<tr align="center">
	   			<input type="hidden" id="chk" value="${activity.id }"/>
	   			<input type="hidden" id="orderId" value="${empty activity.fieldOrder ? "" : activity.fieldOrder.id}"/>

	   			<%
	   			boolean canOperation = true;
	   			// 今天之前的两天可以操作，再向前不可操作
	   			int betweenDays = DateUtil.getBetweenDays(focusDate, DateUtil.getSysdate());
	   			if(betweenDays >= 3) {
	   				canOperation = false;
	   			}
	   			pageContext.setAttribute("canOperation", canOperation);

	   			FieldActivity activity1 = (FieldActivity)activity;
	   			Calendar timeCa = Calendar.getInstance();
	   			timeCa.setTime(activity1.getToTime());
	   			// 作废倒计时
	   			long cancelCountDown = FieldUtil.getCountDown(timeCa.get(Calendar.HOUR_OF_DAY), timeCa.get(Calendar.MINUTE));

	   			// 锻炼结果倒计时
	   			long exerciseEndCountDown = FieldUtil.getCountDown(timeCa.get(Calendar.HOUR_OF_DAY), timeCa.get(Calendar.MINUTE));

	   			// 正在锻炼倒计时
	   			timeCa.setTime(activity1.getFromTime());
	   			long exerciseCountDown = FieldUtil.getCountDown(timeCa.get(Calendar.HOUR_OF_DAY), timeCa.get(Calendar.MINUTE));
	   			%>

	    		<td>${activity.period }
	    			<c:if test="${canOperation && not empty activity.fieldOrder && activity.activity != '作废'
	    							&& activity.activity != '取消' && activity.activity != '保留'
	    							&& activity.activity != '锻炼'}">
		   				<input type="hidden" name="cancelCountDown" value="<%=cancelCountDown %>"/>
		   			</c:if>
		   			<c:if test="${activity.activity == '锻炼'}">
			   			<%if(exerciseCountDown > 0) { %>
			   				<input type="hidden" name="exerciseCountDown" value="<%=exerciseCountDown %>"/>
			   			<%} %>
			   			<%if(exerciseEndCountDown > 0) { %>
			   				<input type="hidden" name="exerciseEndCountDown" value="<%=exerciseEndCountDown %>"/>
			   			<%} %>
		   			</c:if>
	   			</td>
	    		<td>${activity.price }</td>
	    		<td>&nbsp;
	    			<c:if test="${activity.fieldOrder.patch}">
	    				<span class="patch">补登计</span>
	    			</c:if>
	    			<c:if test="${not empty activity.fieldOrder && activity.activity != '作废' && activity.activity != '取消' && activity.activity != '保留'}">
		    			<span id='status'>
		    				${activity.orderUser}<a class='basic' orderId="${empty activity.fieldOrder ? "" : activity.fieldOrder.id}" href='javascript:;'>已预订</a>
		    			</span>

	    				<c:if test="${canOperation && not empty activity.fieldOrder}">
	    					<c:choose>
	    						<c:when test="${activity.fieldOrder.paymentStatus }">
		    						<span id="paymentStatus" class="pay">已付款</span>
		    					</c:when>
		    					<c:when test="${!activity.fieldOrder.paymentStatus }">
		    						<span id="paymentStatus">未付款
		    							<button name="pay" activeId="${activity.id }">付款</button>
		    						</span>
		    					</c:when>
	    					</c:choose>
	    				</c:if>
	    			</c:if>

	    			<c:if test="${empty activity.fieldOrder || activity.verification == '0'}">
		    			<span id='status' style='display:none'>
		    				${activity.orderUser}<a class='basic' orderId="${empty activity.fieldOrder ? "" : activity.fieldOrder.id}" href=''>已预订</a>
		    			</span>
	    			</c:if>

	    			<c:if test="${activity.activity == '保留'}"><span id='status'>已保留</span></c:if>

					<c:if test="${activity.isOrderSite}">
		    			<span title="由 ${activity.orderUser } 于 ${activity.fieldOrder.bookTime} 在网站预定">
		    				<img src="../images/activity/book-outside.png" class="book-outside" />
		    			</span>
		    		</c:if>
	    		</td>
	    		<td>
	    			<c:if test="${activity.verification == '1' }">${activity.authenticode }</c:if>
	    			<c:if test="${not empty activity.orderUser && activity.activity != '取消' && activity.activity != '作废'
	    						&& (activity.verification == '0' || empty activity.verification)
	    						&& not empty activity.fieldOrder && activity.fieldOrder.paymentStatus}">
	    				<button name="validate" activityId="${activity.id }">验证</button>
	    			</c:if>
	    		</td>
	    		<td>${activity.authenticode }</td>
	    		<td <c:if test="${activity.verification == '1' }">class="correct"</c:if><c:if test="${activity.verification == '0' }">class="error"</c:if>>&nbsp;</td>
	    		<td activeId="${activity.id }">
	    			<%
	    				String activityStatus = FieldUtil.getActivityStatus(activity);
	    				pageContext.setAttribute("activityStatus", activityStatus);
	    			%>
	    			${activityStatus }
	    			<c:if test="${activity.verification != '1' && activityStatus == '过期' && activity.activity != '作废'}">
	    				<button name="blankout" activeId="${activity.id }">作废</button>
	    			</c:if>
	    		</td>
	    	</tr>
	    </c:forEach>
	    </table>

    <div id="validateCodeTemplate" style="display: none">
    	<input type="text" size="6" maxlength="4" name="validateCode"/>
    </div>
    <div id="orderInfo" class="template">
    	<br/>
    	<div style="padding-left:15px;">联系人：<span id="dcontact"></span></div>
		<div>会员号码：<span id="duserCode"></span></div>
		<div>联系电话：<span id="dphone"></span></div>
		<div>付款金额：￥<span id="dpaymentSum"></span></div>
		<div>付款订单编号：<span id="daccountOrderId">正在加载……</span></div>
		<div>场地预定订单编号：<span id="did"></span></div>
   	</div>
  </body>
</html>