<%@ page language="java" import="java.util.*,net.yanhl.field.pojo.*,net.yanhl.field.util.FieldUtil
,net.yanhl.util.*,java.sql.Time" pageEncoding="UTF-8"%>
<%@include file="../common/global.jsp" %>
<%@page import="org.apache.commons.lang.StringUtils"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
  	<title>单块场地预定</title>
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>

	<link rel="stylesheet" type="text/css" href="../css/global.css" />
	<link rel="stylesheet" type="text/css" href="../css/tablecloth.css" />
	<link type="text/css" rel="stylesheet" href="../css/all.css" />
	<link type="text/css" rel="stylesheet" href="../css/weebox.css" />
	<link rel="stylesheet" type="text/css" href="../common/js/plugin/tipTip/tipTip.css"/>
	<link type="text/css" rel="stylesheet" href="../css/activity.css"/>

	<script type="text/javascript" src="../common/js/jquery-last.js"></script>
	<script type="text/javascript" src="../common/js/plugin/bgiframe.js"></script>
	<script type="text/javascript" src="../common/js/plugin/weebox.js"></script>
	<script type="text/javascript" src="../common/js/plugin/tipTip/jquery.tipTip.js"></script>
	<script type="text/javascript" src="../common/js/plugin/jquery.form.js"></script>
	<script type="text/javascript" src="../common/js/plugin/jquery.validate.js"></script>
	<script type="text/javascript" src="../common/js/common.js"></script>
	<script type="text/javascript" src="../common/js/form.js"></script>
	<script type="text/javascript" src="../common/js/list.js"></script>
	<script type="text/javascript" src="js/common.activity.js"></script>
	<script type="text/javascript" src="js/booking.js"></script>
	<script type="text/javascript">
		var fieldType = "${param.fieldType }";
	</script>
  </head>

  <body>
  	<div class="batchOption">
  		<span id="selector">选择：<a href="javascript:;" class="none">不选</a>,
  			<a href="javascript:;" class="booking">可预订</a>,<a href="javascript:;" class="preserving">可保留</a>
  		</span>
  		<span id="option">
  			<button id="batchBooking">批量预订</button>&nbsp;
  			<button id="batchPreserving">批量保留</button>
  		</span>
  	</div>

  	<table border="0" cellspacing="0" width="100%" id="row" class="row">
  		<thead>
  			<tr>
  				<th width="3%"><input id="chkAll" type="checkbox" /></th>
  				<th width="15%">时段</th>
  				<th width="10%">价格</th>
  				<th>预订情况</th>
  			</tr>
  		</thead>
    <%
    List result = (List)request.getAttribute("result");

    for(int i = 0; i < result.size(); i++) {
    	FieldActivity activity = (FieldActivity)result.get(i);
    	pageContext.setAttribute("activity", activity);
   		// 验证结果
   		String validateResult = null;
   		if(activity.getVerification() != null) {
   			if(activity.getVerification().equals("1")) {
   				validateResult = "已验证";
   			}
   		}

   		// 验证码，验证成功后显示
   		String validateCode = "&nbsp;";
   		if(activity.getAuthenticode() != null) {

   			// 验证成功后显示验证码
   			if(validateResult != null) {
    			validateCode = activity.getAuthenticode();
   			}
   		}
    %>
    	<tr align="center">
  			<input type="hidden" id="chk" value="${activity.id }"/>
  			<input type="hidden" id="orderId" value="${empty activity.fieldOrder ? "" : activity.fieldOrder.id}"/>
  			<input type="hidden" id="paymentSum" value="${empty activity.fieldOrder ? "" : activity.fieldOrder.paymentSum } "/>

  			<td name="checkbox">&nbsp;</td>
    		<td name="period">${activity.period }</td>
    		<td name="price">${activity.price }</td>
    		<td name="btns">&nbsp;
	    		<%
	    		Time fromTime = activity.getFromTime();
	    		Date d1 = new Date(fromTime.getTime());
	    		Calendar caFrom = Calendar.getInstance();
	    		caFrom.setTime(d1);
	    		int fromHour = caFrom.get(Calendar.HOUR_OF_DAY);
	    		int fromMinute = caFrom.get(Calendar.MINUTE);

	    		Time toTime = activity.getToTime();
	    		Calendar caTo = Calendar.getInstance();
	    		Date d2 = new Date(toTime.getTime());
	    		caTo.setTime(d2);
	    		int toHour = caTo.get(Calendar.HOUR_OF_DAY);
	    		int toMinute = caTo.get(Calendar.MINUTE);


	    		boolean timeout = false;
	    		// 今天
	    		int days = DateUtil.getBetweenDays(new Date(activity.getUsableDate().getTime()), new Date(), false);
	    		if (days > 0) {
	    			timeout = true;
	    		} else if(days == 0) {
	    			timeout = fromHour < hour;
	    		}
	    		if(!timeout) {
		    		// 未预定或者作废状态
		    		String[] bookableStatus = new String[]{FieldUtil.FIELD_ACTIVITY_BLANKOUT, FieldUtil.FIELD_ACTIVITY_UNRESERVED};
		    		if(StringUtil.hasInArray(bookableStatus, activity.getActivity())) {%>
		    			<span id="status"></span>
						<button name="booking" title="预定此时段的活动">预订</button>
						<button name="unbooking" title="取消预订" style="display:none">取消预订</button>
						<button name="preserving" title="保留此场地">保留</button>
						<button name="unpreserving" title="取消保留此场地" style="display:none">取消保留</button>
		    		<%} else if(FieldUtil.FIELD_ACTIVITY_CANCEL.equals(activity.getActivity())) {%>
		    			<span id="status">未预定</span>
		    			<button name="booking" title="预定此时段的活动">预订</button>
		    			<button name="unbooking" title="取消预订" style="display:none">取消预订</button>
		    			<button name="preserving" title="保留此场地">保留</button>
		    			<button name="unpreserving" title="取消保留此场地" style="display:none">取消保留</button>
		    		<%} else if(FieldUtil.FIELD_ACTIVITY_PRESERVING.equals(activity.getActivity())) {%>
		    			<span id="status">已${activity.activity }</span>
		    			<button name="booking" title="预定此时段的活动" style="display:none">预订</button>
		    			<button name="unbooking" title="取消预订" style="display:none">取消预订</button>
		    			<button name="preserving" title="保留此场地" style="display:none">保留</button>
		    			<button name="unpreserving" title="取消保留此场地">取消保留</button>
		    		<%} else {%>
		    			<span id="status">
		    				${activity.orderUser }<a class="basic" orderId="${empty activity.fieldOrder ? "" : activity.fieldOrder.id}" href="javascript:;">已预订</a>
		    				<c:if test="${activity.verification != '1'}">
		    				<a href="javascript:;" name="updateOrderInfo" title="修改预订信息">修改</a>
		    				</c:if>
		    			</span>

		    			<c:if test="${not empty activity.fieldOrder}">
	    					<c:choose>
	    						<c:when test="${activity.fieldOrder.paymentStatus }">
		    						<span id="paymentStatus" class="pay">已付款</span>
		    					</c:when>
		    					<c:when test="${!activity.fieldOrder.paymentStatus }">
		    						<span id="paymentStatus">未付款
		    							<button name="pay">付款</button>
		    						</span>
		    					</c:when>
	    					</c:choose>
	    				</c:if>

		    			<c:if test="${activity.verification != '1'}">
			    			<button name="booking" title="预定此时段的活动" style="display: none">预订</button>
			    			<button name="unbooking" title="取消预订">取消预订</button>
			    			<button name="preserving" title="保留此场地" style="display: none">保留</button>
			    			<button name="unpreserving" title="取消保留此场地" style="display: none">取消保留</button>
						</c:if>
		    		<%}
	    		} else { %>

	    			<%
	    			// 状态：未预定
	    			if (activity.getActivity().equals(FieldUtil.FIELD_ACTIVITY_UNRESERVED)) {%>
	    				<span id="status"></span>
	    				<button name="patchBook" title="对于已过期的活动补登计">补登计</button>
	    			<%}
	    			// 状态：锻炼
	    			else if (activity.getFieldOrder() != null) { %>
	    				<span id="status">
		    				${activity.orderUser }<a class="basic" orderId="${empty activity.fieldOrder ? "" : activity.fieldOrder.id}" href="javascript:;">已预订</a>
		    				<c:if test="${false && activity.verification != '1'}">
		    					<a href="javascript:;" name="updateOrderInfo" title="修改预订信息">修改</a>
		    				</c:if>
		    			</span>
    					<c:choose>
    						<c:when test="${activity.fieldOrder.paymentStatus }">
	    						<span id="paymentStatus" class="pay">已付款</span>
	    					</c:when>
	    					<c:when test="${!activity.fieldOrder.paymentStatus }">
	    						<span id="paymentStatus">未付款
	    							<button name="pay" style="display:none">付款</button>
	    						</span>
	    					</c:when>
    					</c:choose>
    				<%}%>
	    		<%}%>
	    		<c:if test="${activity.isOrderSite}">
	    			<span title="由 ${activity.orderUser } 于 ${activity.fieldOrder.bookTime} 在网站预定">
	    				<img src="../images/activity/book-outside.png" class="book-outside" />
	    			</span>
	    		</c:if>
    		</td>

    	</tr>
    <%} %>
    </table>

    <div id="boxDiv" class="template">
    	<form action="" method="post" id="bookForm">
			<input type="hidden" id="cardId" />
	    	<table border="0" cellspacing="0" id="boxTable" width="100%">
	    		<tr id="searchStyle">
	    			<td class="label" width="20%">查询方式：</td>
	    			<td>
	    				<input type="radio" id="searchStyleCard" name="searchStyle" value="card" checked="checked" />
						<label for="searchStyleCard">会员卡</label>
						<input type="radio" id="searchStylePhone" name="searchStyle" value="phone" />
						<label for="searchStylePhone">手机号码</label>
	    			</td>
	    		</tr>
	    		<tr id="selectPayStyle">
	    			<td class="label" width="20%">付款方式：</td>
	    			<td>
	    				<input type="radio" id="styleCard" name="payStyle" value="会员卡" checked="checked"/><label for="styleCard">会员卡</label>
	    				<input type="radio" id="styleCash" name="payStyle" value="现金"/><label for="styleCash">现金</label>
	    			</td>
	    		</tr>
	    		<tr id="cardTr">
	    			<td class="label">会员号码：</td>
	    			<td>
	    				<input type="text" id="userCode" name="userCode" maxlength="10" size="12"/>
	    			</td>
	    		</tr>
	    		<tr id="phoneTr">
	    			<td class="label">手机号码：</td>
	    			<td><input type="text" id="phone" name="phone" maxlength="11" class="noempty" size="12" /></td>
	    		</tr>
	    		<tr id="cardViewTr">
	    			<td class="label">会员号码：</td>
	    			<td></td>
	    		</tr>
	    		<tr id="balanceTr">
	    			<td class="label">卡内余额：</td>
	    			<td><span class="balance"></span></td>
	    		</tr>
	    		<tr>
	    			<td class="label">联系人：</td>
	    			<td><input type="text" id="contact" name="contact" maxlength="20" class="noempty" size="12" /></td>
	    		</tr>
	    	</table>
    	</form>
    </div>

    <div id="orderInfoTipTemplate" class="template">
    	<a class="basic" href="orderInfo.html?orderId=#orderId">已预订</a>
		<a href="javascript:;" name="updateOrderInfo" title="修改预订信息">修改</a>
    </div>

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

    <div id="checkboxTemplate" class="template">
    	<input type="checkbox" name="chk" class="chk" />
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
   	
   	<div id="selectCardTemplate" class="template">
   		<table id="cardTable" cellspacing="0" cellpadding="0" width="100%" class="row">
   			<caption style="color: green; font-weight: bold;">从多个会员卡中点击选择一个支付活动费用</caption>
   			<tr>
   				<th>卡号</th>
   				<th>姓名</th>
   				<th>余额</th>
   				<th>生效期</th>
   				<th>有效期</th>
   			</tr>
   		</table>
   	</div>
   	
  </body>
</html>