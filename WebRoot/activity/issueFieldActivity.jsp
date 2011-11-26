<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/global.jsp" %>
<%@page import="net.yanhl.field.pojo.BaseField"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@ include file="../common/meta.jsp" %>
		<title>场地活动生成</title>

		<link rel="stylesheet" type="text/css" href="../css/global.css" />
		<link rel="stylesheet" type="text/css" href="../css/tablecloth.css" />
		<link type="text/css" rel="stylesheet" href="../css/all.css" />
		<link type="text/css" rel="stylesheet" href="../css/weebox.css" />
		<link type="text/css" rel="stylesheet" href="../themes/sexybuttons/sexybuttons.css" />

		<script type="text/javascript" src="../common/js/jquery-last.js"></script>
		<script type="text/javascript" src="../common/js/plugin/bgiframe.js"></script>
		<script type="text/javascript" src="../common/js/plugin/weebox.js"></script>
		<script type="text/javascript" src="../common/js/plugin/jquery.blockUI.js"></script>
		<script type="text/javascript" src="../common/js/common.js"></script>
		<script type="text/javascript" src="../common/js/list.js"></script>
		<script type="text/javascript" src="../common/datepicker/WdatePicker.js"></script>
		<script type="text/javascript" src="js/issueFieldActivity.js"></script>
	</head>
	<body>
		<input type="hidden" id="fieldType" value="${param.fieldType }"/>
		<table border="0" width="100%" cellspacing="0" id="row" class="row" align="center" style="text-align: center;">
			<thead>
				<tr>
					<th>场地名称</th>
					<th>场地环境</th>
					<th>提前生成天数</th>
					<th>可提前发布天数</th>
					<th>最后生成日期</th>
					<th>网上发布到</th>
				</tr>
			</thead>
			<c:forEach var="field" items="${fieldList}">
			<bean:define id="field" name="field"></bean:define>
			<tr>
				<td>${field.name }</td>
				<td>${field.envType }</td>
				<td class="create">
					<span class="advanceSpan">${field.advance }</span>(天)&nbsp;
				</td>
				<td class="issue">
					<span class="advanceSpan">${field.issueAdvance }</span>(天)&nbsp;
				</td>
				<td>${field.issueLastDate }</td>
				<td><%
					Calendar ca1 = Calendar.getInstance();
					ca1.add(Calendar.DATE, ((BaseField)field).getIssueAdvance());
					out.print(DateUtil.format(ca1.getTime(), DateUtil.TYPE_DATE));
				%></td>
			</tr>
			</c:forEach>
		</table>
	</body>
</html>
