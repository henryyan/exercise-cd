<%@ page import="org.springframework.context.ApplicationContext,
				org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="net.yanhl.venue.pojo.VenueUser"%>
<%@page import="net.yanhl.field.util.FieldUtil, net.yanhl.util.UserUtil"%>
<%@page import="net.yanhl.field.dao.FieldTypeDao"%>
<%
	ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
	FieldTypeDao fieldTypeDao = (FieldTypeDao)context.getBean("fieldTypeDao");
	String[] enableFieldTypes = fieldTypeDao.getEnableFieldTypes(UserUtil.getCurrentVenueLongId(request));
	java.util.Map<String, String> fieldTypes = FieldUtil.getEnableFieldTypes(enableFieldTypes);
	pageContext.setAttribute("fieldTypes", fieldTypes);
%>