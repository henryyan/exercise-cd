<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/global.jsp" %>
<%@ include file="../field/type/includeFieldTypes.jsp" %>
<html>
  <head>
  	<%@ include file="../common/meta.jsp" %>

	<link type="text/css" rel="stylesheet" href="${ctx }/css/all.css" />
	<link type="text/css" rel="stylesheet" href="${ctx }/css/ui/ui.tabs.css" />
	<link rel="stylesheet" type="text/css" href="${ctx }/themes/${themeName }/jquery-ui-${themeVersion }.custom.css" />
	<style type="text/css">
		body{ font: 8pt "Trebuchet MS", sans-serif; margin: 0px;}
		#tabs {height: 590px}
	</style>

	<script type="text/javascript" src="${ctx }/common/js/jquery-last.js"></script>
	<script type="text/javascript" src="${ctx }/common/js/ui/jquery-ui-${themeVersion }.custom.min.js"></script>
	<script type="text/javascript" src="${ctx }/common/js/common.js"></script>
	<script type="text/javascript">
		$(function(){$('#tabs').tabs();});
	</script>
  </head>

  <body>
  	<div id="tabs">
		<ul>
			<c:forEach items="${fieldTypes}" var="type">
				<li><a href="#${type.key }"><span>${type.value }</span></a></li>
			</c:forEach>
		</ul>
		<c:forEach items="${fieldTypes}" var="type">
			<div id="${type.key }">
				<iframe src="${ctx }/activity/enableFieldList.do?fieldType=${type.key }" frameborder="0" style="height: 450px; width: 100%"></iframe>
			</div>
		</c:forEach>
		
	</div>
  </body>
</html>