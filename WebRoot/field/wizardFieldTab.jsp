<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.net.URLEncoder"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/global.jsp" %>
<%@ include file="type/includeFieldTypes.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title>添加场地TAB</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />

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
		<script type="text/javascript" src="${ctx }/common/js/common.js"></script>
		<script type="text/javascript">
			$(function(){
				$('#tabs').tabs();
		    });

		    // 如果没有添加场地提示用户，直到添加了场地再进行下一步
			function doNext(callback) {
				$.getJSON('countField.do', function(amount){
					var total = parseInt(amount.total);
					if (total == 0) {
						alert('请添加场地');
					} 
					<c:forEach items="${fieldTypes}" var="type">
					else if (amount.${type.key} == 0) {
						$('#tabs').tabs('select', '${type.key}');
						alert('请添加[${type.value}]场地');
						$('#${type.key} iframe').contents().find('#addField').click();
					}
					</c:forEach>
					else {
						callback($(document));
					}
				});
			}
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
					<iframe src="${ctx }/field/fieldList.do?fieldType=${type.key }&zhFieldType=<%=URLEncoder.encode(type.getValue().toString(), "GBK") %>" frameborder="0" style="height: 450px; width: 100%"></iframe>
				</div>
			</c:forEach>
		</div>
	</body>
</html>
