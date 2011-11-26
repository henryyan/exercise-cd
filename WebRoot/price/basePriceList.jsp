<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../common/global.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head>
		<%@ include file="priceMeta.jsp" %>
	</head>
	<body>
		<form action="savePrice.do" method="post" id="priceForm" name="priceForm">
			<input type="hidden" id="fieldType" name="fieldType" value="${empty param.fieldType ? fieldType : param.fieldType}"/>
			<input type="hidden" id="priceType" name="priceType" value="${empty param.priceType ? priceType : param.priceType}" />
			<div class="configure">
				<fieldset style="width: 40%">
					<legend>设置详细价格</legend>
					<table border="0" width="100%" cellspacing="0" class="row">
						<tr>
							<td width="40%" class="label">最低计时单位：</td>
							<td align="left">
								<input type="text" id="lowestTime" name="lowestTime" size="5" />
								<select id="lowestTimeType" name="lowestTimeType">
									<option value="小时">小时</option>
								</select>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">
								<button id="next">设置详细价格</button>
							</td>
						</tr>
					</table>
				</fieldset>
				<table border="1" width="80%" cellspacing="0" id="row" class="row" style="text-align: center;">
					<thead>
						<tr>
							<th>开始时间</th>
							<th>结束时间</th>
							<th width="30%">每单位收费</th>
							<th>操作</th>
						</tr>
					</thead>
					<c:forEach var="priceObj" items="${priceList}">
					<tr class="{pid:${priceObj.id }}">
						<td>${priceObj.fromTime }</td>
						<td>${priceObj.toTime }</td>
						<td>
							<span id="showPrice">${priceObj.price }</span>
							<input id="price" size="5" maxlength="3" style="display:none"/>
							<a href="javascript:;" name="update">修改</a>
							<a href="javascript:;" name="save" style="display:none">保存</a>
							<a href="javascript:;" name="cancel" style="display:none">取消</a>
						</td>
						<td id="operate">
							<a href="javascript:;" name="delete">删除</a>
						</td>
					</tr>
					</c:forEach>
				</table>
			</div>

			<div id="warning" style="display: none">重新设置价格会清空已设置价格信息</div>

			<div id="priceWorkspace" style="display: none"></div>

			<div id="operation" style="display: none">
				全部设置为：<input id="templatePrice" maxlength="3" size="6"/>元<br/>
				<input type="submit" id="submit" value="保存价格设置"/>
				<input type="button" id="cancel" value="取消设置"/>
			</div>

			<div id="notice" style="display: none"></div>
		</form>
	</body>
</html>