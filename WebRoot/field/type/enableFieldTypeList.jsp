<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../common/global.jsp" %>
<%@ page import="net.yanhl.field.pojo.type.FieldType" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  	<head>
  		<%@ include file="../../common/meta.jsp" %>
    	<title>场地类型</title>
    	
    	<link rel="stylesheet" type="text/css" href="${ctx }/css/global.css" />
		<link rel="stylesheet" type="text/css" href="${ctx }/css/tablecloth.css" />
		<link rel="stylesheet" type="text/css" href="${ctx }/css/all.css" />
		<style type="text/css">
		.tip {
			padding-top: 1em;
			font-size: 1em;
		}
		</style>
		
		<script type="text/javascript" src="${ctx }/common/js/jquery-last.js"></script>
        <script type="text/javascript" src="js/fieldTypeList.js"></script>
  	</head>
  
  	<body>
		<center>
		<h2>本场馆已启用场地状态</h2>
		<table border="1" width="60%" cellspacing="0" class="row" align="center" style="text-align: center;">
			<thead>
				<tr>
					<th>场地名称</th>
					<th>是否启用</th>
				</tr>
				<c:forEach var="type" items="${list}" varStatus="vs">
				<bean:define id="type" name="type"></bean:define>
				<tr class="{fid:${type.id }} radioTr">
					<td>${type.typeName }</td>
					<td><img src="../../images/validitor/onSuccess.gif"/></td>
				</tr>
				</c:forEach>
			</thead>
		</table>
		<p class="tip">
		如要添加新场地请与客服联系！
		</p>
		</center>
  </body>
</html>