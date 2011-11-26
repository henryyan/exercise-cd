<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/global.jsp" %>
<html>
    <head>
      	<%@ include file="../common/meta.jsp" %>
		
		<link rel="stylesheet" type="text/css" href="${ctx }/themes/${themeName }/jquery-ui-${themeVersion }.custom.css" />
		
		<style type="text/css">
			html, body {
		        margin: 0;            /* Remove body margin/padding */
		        padding: 0;
		        overflow: hidden;    /* Remove scroll bars on browser window */
		        font-size: 85%;
	        }
	        #tabs { height: 590px; }
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
				<li><a href="#venueInfo">场馆信息</a></li>
				<li><a href="#venuePictures">场馆图片</a></li>
				<%--<li><a href="#fieldType">场地类型</a></li> --%>
			</ul>
			<div id="venueInfo">
				<iframe src="${ctx }/venue/venueInfo.html" frameborder="0" style="height: 530px; width: 100%"></iframe>
			</div>
			<%--<div id="fieldType">
				<iframe src="${ctx }/field/type/list.do" frameborder="0" style="height: 450px; width: 100%"></iframe>
			</div> --%>
			<div id="venuePictures">
				<iframe src="${ctx }/venue/picture/venuePictureList.do" frameborder="0" style="height: 530px; width: 100%"></iframe>
			</div>
		</div>
    </body>
</html>