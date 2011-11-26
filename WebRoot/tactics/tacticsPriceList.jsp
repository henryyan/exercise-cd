<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../common/global.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<%@page import="net.yanhl.field.util.FieldUtil"%><html>
	<head>
		<%@ include file="../common/meta.jsp" %>
		<title>策略价格列表</title>
		<link type="text/css" rel="stylesheet" href="../css/global.css" />
		<link type="text/css" rel="stylesheet" href="../css/tablecloth.css" />
		<link type="text/css" rel="stylesheet" href="../css/all.css" />
		<style type="text/css">
			.configure,#priceWordspace{ text-align: center;}
			.active {background-color: yellow;font-weight: bold;}
			#periodTable td {color: #000000}
			.field {font-size: 10pt; font-weight: bold;}
			.tennis {}
			.empty {border: 1px solid red;}
		</style>
		
		<script type="text/javascript" src="../common/js/jquery-last.js"></script>
		<script type="text/javascript" src="../common/js/plugin/jquery.form.js"></script>
		<script type="text/javascript" src="../common/js/plugin/jquery.blockUI.js"></script>
		<script type="text/javascript" src="../common/js/common.js"></script>
		<script type="text/javascript" src="../common/js/form.js"></script>
		<script type="text/javascript" src="js/price.js"></script>
		
		<script type="text/javascript">
			var openTime = '${user.venueInfo.openTime }';
			var closeTime = '${user.venueInfo.closeTime }';
		</script>
	</head>
	<body>
		<form action="saveTacticsPrice.do" method="post" id="priceForm" name="priceForm">
			<input type="hidden" id="startTime" name="startTime"/>
			<input type="hidden" id="endTime" name="endTime"/>
			<input type="hidden" id="tacticsId" name="tacticsId" value="${param.tacticsId }"/>
			<input type="hidden" id="fieldType" name="fieldType"/>
			
			<div class="configure">
				<fieldset style="width: 60%">
					<legend>请选择</legend>
					<table border="0" width="100%" cellspacing="0" class="row">
						<tr>
							<td width="40%" class="label">场地类型：</td>
							<td align="left" class="field tennis"><%=FieldUtil.getFieldZhType(request.getParameter("fieldType")) %></td>
						</tr>
						<tr>
							<td class="label">最低计时单位：</td>
							<td align="left">
								<input type="text" id="lowestTime" name="lowestTime" size="5" />
								<select id="lowestTimeType" name="lowestTimeType">
									<option value="小时">小时</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="label">开始时间：</td>
							<td>
								<select id='fromTimeHour' name='fromTimeHour' noempty='true'><option>请选择</option></select>时
								<select id='fromTimeMinute' name='fromTimeMinute' noempty='true'><option>请选择</option></select>分
								<font color='red'>*</font>
							</td>
							<td class="validatorTip"></td>
						</tr>
						<tr>
							<td class="label">结束时间：</td>
							<td>
								<select id='toTimeHour' name='toTimeHour' noempty='true'><option>请选择</option></select>时
								<select id='toTimeMinute' name='toTimeMinute' noempty='true'><option>请选择</option></select>分
								<font color='red'>*</font>
							</td>
							<td class="validatorTip"></td>
						</tr>
						<tr>
							<td colspan="2" align="center">
								<button id="next">设置详细价格</button>
								<button onclick="history.back();">返 回</button>
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
					<tr>
						<td>${priceObj.fromTime }</td>
						<td>${priceObj.toTime }</td>
						<td>
							<span id="showPrice">${priceObj.price }</span>
							<input id="price" size="5" maxlength="3" style="display:none"/>
							<a href="javascript:;" name="update">修改</a>
							<a href="javascript:;" name="save" idv="${priceObj.id }" style="display:none">保存</a>
							<a href="javascript:;" name="cancel" style="display:none">取消</a>
						</td>
						<td id="operate">
							<a href="javascript:;" name="delete" idv="${priceObj.id }">删除</a>
						</td>
					</tr>
					</c:forEach>
				</table>
			</div>
			
			<div id="priceWarning" class="warning" style="display: none">
				重新设置价格会清空已设置价格信息,并且会重新生成本策略日期范围内的活动
			</div>
			
			<div id="priceWorkspace" class="template">
				<table id="periodTable" border="0" width="50%" cellpadding="0" cellspacing="0" class="tableForm"></table>
			</div>
			
			<div id="operation" class="template" style="color:black;">
				全部设置为：<input id="templatePrice" maxlength="3" size="6"/>元<br/>
				<input type="submit" id="submit" value="保存价格设置"/>
				<input type="button" id="cancel" value="取消设置"/>
			</div>
			
			<div id="notice" class="template"></div>
		</form>
	</body>
</html>