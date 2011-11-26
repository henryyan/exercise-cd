<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="net.yanhl.field.util.FieldUtil"%>
<%@ include file="../../common/global.jsp" %>
<%@ include file="type/includeFieldTypes.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@ include file="../../common/meta.jsp" %>
		<title>增加<%=fieldTypes.get(request.getParameter("fieldType")) %>场地</title>

		<link rel="stylesheet" type="text/css" href="${ctx }/css/global.css" />
		<link rel="stylesheet" type="text/css" href="${ctx }/css/tablecloth.css" />
		<link type="text/css" rel="stylesheet" href="${ctx }/css/all.css" />

		<script type="text/javascript" src="${ctx }/common/js/jquery-last.js"></script>
		<script type="text/javascript" src="${ctx }/common/js/plugin/jquery.form.js"></script>
		<script type="text/javascript" src="${ctx }/common/datepicker/WdatePicker.js"></script>
		<script type="text/javascript" src="${ctx }/common/js/common.js"></script>
		<script type="text/javascript" src="${ctx }/common/js/list.js"></script>
		<script type="text/javascript" src="${ctx }/common/js/form.js"></script>
		<script type="text/javascript" src="js/addField.js"></script>
	</head>
	<body>
		<form action="${ctx }/field/saveField.do" method="post" id="fieldForm" name="fieldForm">
			<input type="hidden" id="fieldType" name="fieldType" value="${param.fieldType }"/>
			<center><h2>增加<%=fieldTypes.get(request.getParameter("fieldType")) %>地块</h2></center>
			<%@ include file="addFieldForm.jsp" %>
		</form>
	</body>
</html>