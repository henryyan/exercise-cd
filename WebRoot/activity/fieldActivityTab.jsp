<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="../common/global.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
  	<meta http-equiv="content-type" content="text/html; charset=utf-8">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<title>场地预定列表</title>

	<link type="text/css" href="../css/ui/ui.tabs.css" rel="stylesheet" />
	<style type="text/css">
		body{ font: 8pt "Trebuchet MS", sans-serif; margin: 0px;}
		.status-tip {font-weight: bold; color: yellow;float: left; margin-left: .5em; font-size: 14px;}
		.pageStep a {text-decoration: none;}
		.pageStep a:hover {text-decoration: underline;}
	</style>

	<script type="text/javascript" src="../common/js/jquery-last.js"></script>
	<script type="text/javascript" src="../common/datepicker/WdatePicker.js"></script>
	<script type="text/javascript" src="../common/js/ui/jquery-ui-1.7.2.custom.min.js"></script>
	<script type="text/javascript" src="../common/js/common.js"></script>
	<script type="text/javascript" src="js/activityTab.js"></script>

  </head>

  <body>
  	<input type="hidden" id="advance" value="30"/>
	<%
	String strPickedDate = request.getParameter("pickedDate");
	String type = request.getParameter("type");
	String[] dates = strPickedDate.split("-");
	dates[1] = dates[1].length() == 1 ? "0" + dates[1] : dates[1];
	dates[2] = dates[2].length() == 1 ? "0" + dates[2] : dates[2];
	strPickedDate = dates[0] + "-" + dates[1] + "-" + dates[2];
	
	java.sql.Date pickedDate = java.sql.Date.valueOf(strPickedDate);
	java.sql.Date currentSqlDate = new java.sql.Date(year - 1900, month, day);
	
	// 最小日期
	Calendar minPickDateCa = Calendar.getInstance();
	minPickDateCa.setTime(currentSqlDate);
	minPickDateCa.add(Calendar.DATE, -7);
	java.sql.Date minPickDate = new java.sql.Date(minPickDateCa.getTime().getTime());
	boolean notMinPickDate = minPickDate.getTime() != pickedDate.getTime();
	pageContext.setAttribute("notMinPickDate", notMinPickDate);
	
	// 最大日期
	Calendar maxPickDateCa = Calendar.getInstance();
	maxPickDateCa.setTime(currentSqlDate);
	if ("book".equals(type)) {
		maxPickDateCa.add(Calendar.DATE, 29);
	}
	java.sql.Date maxPickDate = new java.sql.Date(maxPickDateCa.getTime().getTime());
	boolean notMaxPickDate = maxPickDate.getTime() != pickedDate.getTime();
	pageContext.setAttribute("notMaxPickDate", notMaxPickDate);
	
	Calendar pickedDateCa = Calendar.getInstance();
	pickedDateCa.setTime(pickedDate);
	pickedDateCa.add(Calendar.DATE, -1);
	
	// 前一天
	java.sql.Date beforePickedDate = new java.sql.Date(pickedDateCa.getTime().getTime());
	pickedDateCa.add(Calendar.DATE, 2);
	
	// 后一天
	java.sql.Date afterPickedDate = new java.sql.Date(pickedDateCa.getTime().getTime());
  	%>
  	
	<div class="pageStep ui-widget ui-widget-content" style="text-align: center;">
		<span class='status-tip'>${param.type == 'book' ? '预定活动' : '现场管理' }</span>
    	<c:if test="${!sameDate}">
    		<a href="fieldActivityTab.jsp?fieldType=${param.fieldType }&type=${param.type }&pickedDate=<%=currentSqlDate %>"><span class="today">今天</span></a>
    	</c:if>

		<c:if test="${notMinPickDate}">
			<a href="fieldActivityTab.jsp?fieldType=${param.fieldType }&type=${param.type }&pickedDate=<%=beforePickedDate %>">
				<img src="../images/arrow_left.png" border="0" />前一天(<%=beforePickedDate %>)
			</a>
		</c:if>

		<span id="pickTip" style="font-size: 13;padding-right: 10px;padding-left: 10px;">
			<input type="text" size="12" class="Wdate" value="${param.pickedDate }" style="cursor:pointer" />
		</span>

		<c:if test="${notMaxPickDate}">
			<a href="fieldActivityTab.jsp?fieldType=${param.fieldType }&type=${param.type }&pickedDate=<%=afterPickedDate %>">
				后一天(<%=afterPickedDate %>)<img src="../images/arrow_right.png" border="0" />
			</a>
		</c:if>
  	</div>
  	
  	<div id="tabs">
		<ul></ul>
	</div>
  	
	<div id="iframeTemplate" class="template">
    	<iframe src="" osrc="#osrc" style='width:100%;height:100%' frameborder='no' marginwidth='0' marginheight='0'></iframe>
    </div>
  </body>
</html>