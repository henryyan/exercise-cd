<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.net.URLEncoder"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/global.jsp" %>
<%@ include file="type/includeFieldTypes.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<%@ include file="../common/meta.jsp" %>

		<link rel="stylesheet" type="text/css" href="${ctx }/css/global.css" />
		<link rel="stylesheet" type="text/css" href="${ctx }/themes/${themeName }/jquery-ui-${themeVersion }.custom.css" />
		<style type="text/css">
			html, body {
		        margin: 0;            /* Remove body margin/padding */
		        padding: 0;
		        overflow: hidden;    /* Remove scroll bars on browser window */
		        font-size: 85%;
	        }
	        #tabs { height: 593px; }
		</style>

		<script type="text/javascript" src="${ctx }/common/js/jquery-last.js"></script>
		<script type="text/javascript" src="${ctx }/common/js/ui/jquery-ui-${themeVersion }.custom.min.js"></script>
		<script type="text/javascript">
			$(function(){
				$('#tabs').tabs();
		    });
		</script>
	</head>
	<body>
		<div id="tabs">
			<ul>
				<c:forEach items="${fieldTypes}" var="type">
					<li><a href="#${type.key }">${type.value }</a></li>
				</c:forEach>
			</ul>
			<c:forEach items="${fieldTypes}" var="type">
				<bean:define id="type" name="type" type="java.util.Map.Entry"></bean:define>
				<div id="${type.key }">
					<iframe src="${ctx }/field/fieldList.do?fieldType=${type.key }" frameborder="0" style="height: 450px; width: 100%"></iframe>
				</div>
			</c:forEach>
		</div>
	</body>
</html>