<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../common/global.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  	<head>
  		<%@ include file="../common/meta.jsp" %>
    	<title>会员卡类型</title>
    	
    	<link rel="stylesheet" type="text/css" href="../css/global.css" />
		<link rel="stylesheet" type="text/css" href="../css/tablecloth.css" />
		<link type="text/css" rel="stylesheet" href="../css/all.css" />
		<link rel="stylesheet" type="text/css" href="../common/js/plugin/tipTip/tipTip.css"/>
		
		<script type="text/javascript" src="../common/js/jquery-last.js"></script>
		<script type="text/javascript" src="../common/js/plugin/tipTip/jquery.tipTip.js"></script>
        <%--<script type="text/javascript" src="js/cardType.js"></script> --%>
        <script type="text/javascript">
        $(function(){
        	$('.row tr:even').addClass('even');
            $('.row tr:odd').addClass('odd');

         	// 类型描述提示
        	$('.row td[title]').tipTip();
        });
        </script>
  	</head>
  
  	<body>
    	<%--<center><h2>会员卡类型设置</h2></center>
        <form action="addCardType.do" method="post" id="cardTypeForm" name="cardTypeForm">
            <input type="hidden" id="id" name="id"/>
            <table border="1" width="80%" cellspacing="0" class="row" align="center">
                <tr>
                    <td width="20%" class="label">会员卡种类：</td>
                    <td>
                        <input type="text" id="typeName" name="typeName"/>
                    </td>
                </tr>
                <tr>
                    <td class="label">卡内金额：</td>
                    <td>
                        <input type="text" id="moneyAmount" name="moneyAmount"/>
                    </td>
                </tr>
                <tr>
                    <td class="label">有效期时长：</td>
                    <td>
                        <input type="text" id=periodMonth name="periodMonth"/>（月）
                    </td>
                </tr>
                <tr>
                    <td class="label">折扣方式：</td>
                    <td>
                        <select id="discountType" name="discountType">
                        	<option value="1">折扣率</option>
                        	<option value="2">折扣额</option>
                        </select>
                    </td>
                </tr>
                <tr id="discountType1">
                    <td class="label">折扣率：</td>
                    <td>
                        <input type="text" id="discountRate" name="discountRate"/>%
                    </td>
                </tr>
                <tr id="discountType2" style="display: none">
                    <td class="label">折扣单价</td>
                    <td>
                        <input type="text" id="discountPrice" name="discountPrice" size="6"/> * 场次 <input type="text" id="discountSession" size="4"/> = 
                        <span id="moneyAmountSpan"></span>
                    </td>
                </tr>
                <tr>
                    <td class="label">折扣时段：</td>
                    <td>
                    	<input type="checkbox" id="common" checked="true"/><label for="common">非周末时段</label>
                        <input type="checkbox" id="weekday"/><label for="weekday">周末时段</label>
                        <input type="hidden" id="discountTime" name="discountTime"/>
                    </td>
                </tr>
                <tr>
                	<td>&nbsp;</td>
                    <td>
                        <input type='submit' name="submit" id='submit' title="保存" value="保 存" />
						<input type='button' id='resetBtn' validaor='true' title="重置表单" value="重 置" />
                    </td>
                </tr>
            </table>
        </form>
        <br/> --%>
        <center><h2>会员卡类型</h2></center>
		<table border="1" width="100%" cellspacing="0" class="row" align="center" style="text-align: center;">
			<thead>
				<tr>
					<th>会员卡种类</th>
					<th>打折方式</th>
					<th>卡内金额</th>
					<th>协议服务费</th>
					<th>有效期（月）</th>
					<th>折扣率</th>
					<th>折扣额</th>
					<th>折扣范围</th>
					<th>描述</th>
					<%--<th>操作</th> --%>
				</tr>
			</thead>
			<c:forEach var="type" items="${cardTypes}" varStatus="i">
			<tr>
				<td>${type.typeName }</td>
				<td>${type.discountZhType }</td>
				<td>${type.moneyAmount }</td>
				<td>${type.paymentCommision }</td>
				<td>${type.periodMonth }</td>
				<td>
					<c:if test="${type.discountRate > 0}">${type.discountRate}%</c:if>
				</td>
				<td>${type.discountPrice > 0 ? type.discountPrice : '' }</td>
				<td>
				<c:if test="${type.commonDay}">非周末</c:if>
				<c:if test="${type.weekDay}">，周末</c:if>
				</td>
				<td title="${type.describtion }">&nbsp;${fn:length(type.describtion) > 5 ? fn:substring(type.describtion, 0, 5) : type.describtion}</td>
				<%--<td id="operate">
					<a href="javascript:;" id="update" idv="${type.id }">修改</a>&nbsp;
					<a href="javascript:;" id="delete" idv="${type.id }">删除</a>
				</td> --%>
			</tr>
			</c:forEach>
		</table>
  </body>
</html>