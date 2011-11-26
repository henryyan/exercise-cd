<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/global.jsp" %>
<%@ include file="../field/type/includeFieldTypes.jsp" %>
<html>
  <head>
  	<%@ include file="../common/meta.jsp" %>

	<link rel="stylesheet" type="text/css" href="../themes/${themeName }/jquery-ui-${themeVersion }.custom.css" />
  	<link rel="stylesheet" type="text/css" href="../css/all.css" />
	<style type="text/css">
		body{ font: 8pt "Trebuchet MS", sans-serif; margin: 0px;}
		.divContent {height: 520px;}
		iframe {
			border-right: 0px; border-top: 0px; border-left: 0px; width: 100%; border-bottom: 0px; height:100%;
		}
	</style>
	<script type="text/javascript" src="../common/js/jquery-last.js"></script>
	<script type="text/javascript" src="../common/js/ui/jquery-ui-${themeVersion }.custom.min.js"></script>
	<script type="text/javascript">
		$(function() {
		    $('.tabs').tabs();
		});
	</script>
  </head>

  <body>
  	<div class="tabs" style="height: 593px">
		<ul>
			<c:forEach items="${fieldTypes}" var="type">
				<li><a href="#${type.key }"><span>${type.value }</span></a></li>
			</c:forEach>
		</ul>
		
		<c:forEach items="${fieldTypes}" var="type">
			<div id="${type.key }" class="divContent">
			<div class="tabs">
				<ul>
					<li><a href="#${type.key }-basic"><span>非周末价格</span></a></li>
					<li><a href="#${type.key }-weekend"><span>周末价格</span></a></li>
				</ul>
				<div id="${type.key }-basic" class="divContent">
					<iframe src="fieldPriceList.do?priceType=basic&fieldType=${type.key }" frameborder="0"></iframe>
				</div>
				<div id="${type.key }-weekend" class="divContent">
					<iframe src="fieldPriceList.do?priceType=weekend&fieldType=${type.key }" frameborder="0"></iframe>
				</div>
			</div>
		</div>

		</c:forEach>
		
	</div>
  </body>
</html>