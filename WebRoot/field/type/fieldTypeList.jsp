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
		<link rel="stylesheet" type="text/css" href="${ctx }/themes/${themeName }/jquery-ui-${themeVersion }.custom.css" />
		
		<script type="text/javascript" src="${ctx }/common/js/jquery-last.js"></script>
		<script type="text/javascript" src="${ctx }/common/js/plugin/jquery.metadata.js"></script>
		<script type="text/javascript" src="${ctx }/common/js/plugin/jquery.blockUI.js"></script>
		<script type="text/javascript" src="${ctx }/common/js/ui/jquery-ui-${themeVersion }.custom.min.js"></script>
        <script type="text/javascript" src="${ctx }/common/js/common.js"></script>
        <script type="text/javascript" src="js/fieldTypeList.js"></script>
  	</head>
  
  	<body>
		<table border="1" width="80%" cellspacing="0" class="row" align="center" style="text-align: center;">
			<thead>
				<tr>
					<th>场地名称</th>
					<th>是否启用</th>
				</tr>
				<c:forEach var="type" items="${list[1]}" varStatus="vs">
				<bean:define id="type" name="type"></bean:define>
				<% boolean hasThis = ((FieldType)type).hasThis(UserUtil.getCurrentVenueLongId(request)); %>
				<tr class="{fid:${type.id }} radioTr">
					<td>${type.typeName }</td>
					<td class="enable">
						<input type="radio" id="eanble${vs.index }1" name="eanble${vs.index }" value="true" <%=hasThis ? "checked='checked'" : "" %> /><label for="eanble${vs.index }1">开启</label>
						<input type="radio" id="eanble${vs.index }2" name="eanble${vs.index }" value="false" <%=!hasThis ? "checked='checked'" : "" %> /><label for="eanble${vs.index }2">关闭</label>
					</td>
					<c:if test="${not empty param.admin}">
					</c:if>
				</tr>
				</c:forEach>
			</thead>
			
		</table>
  </body>
</html>