<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../common/global.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>会员卡使用记录</title>
	<link rel="stylesheet" type="text/css" href="../css/global.css" />
	<link rel="stylesheet" type="text/css" href="../css/tablecloth.css" />
	<link type="text/css" rel="stylesheet" href="../css/all.css" />
	
	<script type="text/javascript" src="../common/js/jquery-last.js"></script>
	<script type="text/javascript">
		$(function(){
			$('.row tr:even').addClass('even');
		    $('.row tr:odd').addClass('odd');
		});
	</script>
  </head>
  
  <body>
      	<fieldset>
      		<legend>会员卡基本信息</legend>
	      	<table border="1" width="80%" cellspacing="0" class="row" align="center" style="text-align: center;">
	      		<tr>
	      			<td class="label">卡号：</td><td>${memberCard.cardNumber }</td>
	      			<td class="label">姓名：</td><td>${memberCard.name }</td>
	      		</tr>
	      		<tr>
	      			<td class="label">入会时间：</td><td>${memberCard.createDateZh }</td>
	      			<td class="label">手机：</td><td>${memberCard.mobilePhone }</td>
	      		</tr>
	      	</table>
      	</fieldset>
      	<br/>
      	<center><button onclick="history.back();">返回会员卡列表</button></center>
      	<fieldset>
      		<legend>会员卡使用记录</legend>
	    	<table border="1" width="80%" cellspacing="0" class="row" align="center" style="text-align: center;">
				<thead>
					<tr>
						<th>使用日期</th>
						<th>使用时段</th>
						<th>用卡类型</th>
						<th>操作金额</th>
						<th>余额</th>
					</tr>
				</thead>
				<c:forEach var="rec" items="${usageRecords}" varStatus="i">
				<tr>
					<td>${rec.usageDate }</td>
					<td>${rec.usageTimeSlice }</td>
					<td>${rec.usageType }</td>
					<td>${rec.optionTotal }</td>
					<td>${rec.balance }</td>
				</tr>
				</c:forEach>
			</table>
		</fieldset>
		<center><button onclick="history.back();">返回会员卡列表</button></center>
  </body>
</html>
