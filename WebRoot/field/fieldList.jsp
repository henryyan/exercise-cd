<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="net.yanhl.field.util.FieldUtil"%>
<%@page import="net.yanhl.venue.pojo.*,net.yanhl.field.pojo.*" %>
<%@ include file="../common/global.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head>
		<%@ include file="../common/meta.jsp" %>
		<link rel="stylesheet" type="text/css" href="../css/global.css" />
		<link rel="stylesheet" type="text/css" href="../css/tablecloth.css" />
		<link type="text/css" rel="stylesheet" href="../css/all.css" />
		<style type="text/css">
			#row td {text-align: center;}
		</style>
		<script type="text/javascript" src="../common/js/jquery-last.js"></script>
		<script type="text/javascript" src="../common/js/plugin/jquery.metadata.js"></script>
		<script type="text/javascript" src="../common/js/common.js"></script>
		<script type="text/javascript" src="js/fieldList.js"></script>
	</head>
	<body>
		<table border="1" width="100%" cellspacing="0" id="row" class="row">
			<thead>
				<tr>
					<th>编号</th>
					<th>场地名称</th>
					<th>创建日期</th>
					<th>环境</th>
					<th>状态</th>
					<th>操作</th>
				</tr>
			</thead>

			<c:forEach var="field" items="${fieldList }">
				<tr class="{fid:${field.id },ftype:'${field.fieldType }'}">
					<td>${empty field.fieldCode ? "&nbsp;" : field.fieldCode}</td>
					<td>${empty field.name ? "&nbsp;" : field.name}</td>
					<td>${empty field.createDate ? "&nbsp;" : field.createDate}</td>
					<td>${empty field.envType ? "&nbsp;" : field.envType}</td>
					<td>${empty field.status ? "&nbsp;" : field.status}</td>
					<td class="operate">
						<a href="javascript:;" id="update">修改</a>
						<a href="javascript:;" id="delete">删除</a>
					</td>
				</tr>
			</c:forEach>
		</table>

		<br/>
		<center><button id="addField">添加场地</button></center>
	</body>
</html>