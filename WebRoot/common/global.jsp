<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,net.yanhl.util.*,org.apache.commons.lang.StringUtils" %>
<%@ page import="net.yanhl.venue.pojo.VenueUser"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!-- 定义常、变量 -->
<%
request.setCharacterEncoding("utf-8");
response.setHeader("Cache-Control","no-cache");//HTTP 1.0

// 标题
String siteName = "我要锻炼";

// jquery.ui主题
String themeName = "le-frog";

// jquery.ui版本
String themeVersion = "1.8.1";
pageContext.setAttribute("themeName", themeName);
pageContext.setAttribute("themeVersion", themeVersion);
%>
<%
pageContext.setAttribute("pageSize", "20");
Calendar ca = Calendar.getInstance();
int year = ca.get(Calendar.YEAR);
int month = ca.get(Calendar.MONTH);
int day = ca.get(Calendar.DAY_OF_MONTH);
int hour = ca.get(Calendar.HOUR_OF_DAY);
int minute = ca.get(Calendar.MINUTE);
int second = ca.get(Calendar.SECOND);
String strSystemDate = year + "-" + ((month + 1) < 10 ? "0" + (month + 1) : (month + 1)) + "-" + (day < 10 ? "0" + day : day);
pageContext.setAttribute("strSystemDate", strSystemDate);
%>
<script type="text/javascript">
	var fullUrl = '${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}';
	var contextPath = '${pageContext.request.contextPath}';
	var ctx = contextPath;
	var vid = '${user.venueInfo.id}';

	// 服务器日期
	var systemDate = new Date(<%=year %>, <%=month %>, <%=day %>);
	var strSystemDate = "<%=strSystemDate %>";

	// 服务器日期、时间
	var systemDateTime = new Date(<%=year %>, <%=month %>, <%=day %>, <%=hour %>, <%=minute %>, <%=second %>);
</script>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>