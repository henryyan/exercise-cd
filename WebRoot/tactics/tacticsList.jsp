<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../common/global.jsp"%>
<%@ include file="../field/type/includeFieldTypes.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<%@ include file="../common/meta.jsp" %>
		<title>策略列表</title>

		<link rel="stylesheet" type="text/css" href="../css/global.css" />
		<link rel="stylesheet" type="text/css" href="../css/tablecloth.css" />
		<link type="text/css" rel="stylesheet" href="../css/all.css" />
		<link type="text/css" rel="stylesheet" href="../css/jquery.cluetip.css"/>
		<link type="text/css" rel="stylesheet" href="../css/nyroModal.css"/>
		<link type="text/css" rel="stylesheet" href="../css/weebox.css" />

		<style type="text/css">
			.tableForm td {color: #000000;}
			.tableForm a:link {color: #000000}
			#dateList td {color: #000000;text-align: center;}
			
			<!-- 提示的文字颜色 -->
			.cluetip-default #cluetip-inner {color: #000000;}
		</style>

		<script type="text/javascript" src="../common/js/jquery-last.js"></script>
		<script type="text/javascript" src="../common/js/plugin/jquery.form.js"></script>
		<script type="text/javascript" src="../common/js/plugin/jquery.nyroModal.js"></script>
		<script type="text/javascript" src="../common/js/plugin/jquery.cluetip.js"></script>
		<script type="text/javascript" src="../common/js/plugin/jquery.blockUI.js"></script>
		<script type="text/javascript" src="../common/js/plugin/bgiframe.js"></script>
		<script type="text/javascript" src="../common/js/plugin/weebox.js"></script>
        <script type="text/javascript" src="../common/js/common.js"></script>
		<script type="text/javascript" src="../common/js/form.js"></script>
		<script type="text/javascript" src="../common/datepicker/WdatePicker.js"></script>
		<script type="text/javascript" src="js/tacticsList.js"></script>
	</head>
	<body style="text-align: center">
        <h1><font color="white">特殊价格策略</font></h1>
        <div align="right" style="width:95%">
        	<span class="warning" style="width:80%">修改【价格、策略日期】会重新生成本策略日期范围内的活动</span>
        	<button name='addEle' title="添加策略">添加策略</button>
        </div>
		<table border="1" width="95%" cellspacing="0" id="row" class="row" style="text-align: center;">
			<thead>
				<tr>
					<th width="18%">策略类型</th>
					<th width="18%">场地类型</th>
					<th>操作</th>
				</tr>
			</thead>
			<c:forEach var="tactics" items="${resultList}">
			<tr>
				<td><a href="javascript:;" name="detail" title="策略备注|${tactics.remark }">${tactics.tacticsName }</a></td>
				<td>${tactics.fieldTypeZh }</td>
				<td name="operate" idv="${tactics.id }" ft="${tactics.fieldType }">
					<a href="javascript:;" name="modify">修改</a>
					|
					<a href="javascript:;" name="delete">删除策略</a>
					|
					<a href="tacticsPriceList.do?tacticsId=${tactics.id }&fieldType=${tactics.fieldType }" name="setPrice"
						title="<c:if test="${tactics.tacticsPricesSize > 0}">共${tactics.tacticsPricesSize }项</c:if><c:if test="${tactics.tacticsPricesSize == 0}">此策略没有设置价格</c:if>"
					>策略价格(${tactics.tacticsPricesSize })</a>
					|
					策略日期(${tactics.tacticsDatesSize })[
					<a href="javascript:;" name="addDate" title="添加策略日期">添加</a>
					、<a href="javascript:;" name="editDate" title="管理策略日期">管理</a>
					、<a href="javascript:;" name="showDate" rel="showTactics.html"
						title="<c:if test="${tactics.tacticsDatesSize > 0}">共${tactics.tacticsDatesSize }个日期段</c:if><c:if test="${tactics.tacticsDatesSize == 0}">此策略没有设置日期段</c:if>"
					>查看<c:if test="${tactics.tacticsDatesSize > 0}">(${tactics.tacticsDatesSize })</c:if></a>]
					<c:if test="${tactics.isModify }">
						&nbsp;<button name="recreate" title="策略已修改，点击重新生成场地">应用</button>
					</c:if>
				</td>
			</tr>
			</c:forEach>
		</table>

		<div id="formTemplate" class="template" style="width: 50%">
		<form action="saveTactics.do" method="post" id="tacticsForm" name="tacticsForm">
			<input type="hidden" id="id" name="id"/>
            <table border="0" width="100%" cellspacing="0" class="tableForm">
                <tr>
                    <td width="25%" class="label">场地类型：</td>
                    <td>
                        <select id="fieldType" name="fieldType" class="noempty">
                        <c:forEach items="${fieldTypes}" var="type">
                        	<option value="${type.key }">${type.value }</option>
                        </c:forEach>
                        </select>
                    </td>
                </tr>

                <tr>
                    <td class="label">策略名称：</td>
                    <td>
                        <input type="text" id="tacticsName" name="tacticsName" maxlength="20" class="noempty" />
                    </td>
                </tr>

                <tr>
                    <td class="label">策略备注：</td>
                    <td>
                        <textarea id="remark" name="remark" rows="3" cols="30"></textarea>
                    </td>
                </tr>

                <tr>
                	<td>&nbsp;</td>
                    <td>
                        <input type='submit' name="submit" id='submit' title="保存" value="保 存" />
						<input type='button' id='close' name="close" title="取消本次操作" value="关 闭" />
                    </td>
                </tr>
            </table>
        </form>
        </div>

        <div id="tacticsDateTemplate" class="template" style="width: 50%">
        	<table border="0" width="100%" cellspacing="0" class="tableForm">
        		<tr>
        			<td class="label">从：</td>
        			<td width="100"><input type="text" id="fromDate" size="10" class="Wdate noempty" style="cursor: pointer"/></td>
        			<td class="label" width="20">至：</td>
        			<td><input type="text" id="toDate" size="10" class="Wdate noempty" style="cursor: pointer"/></td>
        		</tr>
        		<tr>
                    <td colspan="4" style="text-align: center;">
                        <input type='button' id='addDate' title="添加策略日期" value="保 存" />
						<input type='button' id='close' name="close" title="取消本次操作" value="关 闭" />
						|&nbsp;<input type='button' id='editDate' name="editDate" title="打开策略日期管理列表" value="管 理" />
                    </td>
                </tr>
        	</table>
        </div>

        <div id="tacticsDateListTemplate" class="template" style="width:50%">
        	<table id="tacticsDateTable" border="0" width="100%" cellspacing="0" class="tableForm row" style="text-align: center">
        		<tr>
        			<th>开始日期</th>
        			<th>结束日期</th>
        			<th>操作</th>
        		</tr>
        	</table>
        	<div align="right">
	        	<input type='button' name='addTDate' title="添加策略日期" value="添加策略日期" />
	        </div>
        </div>

	</body>
</html>
