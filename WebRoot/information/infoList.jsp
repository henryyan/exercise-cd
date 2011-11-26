<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/global.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>信息颁布列表</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		
		<link rel="stylesheet" type="text/css" href="../css/global.css" />
		<link rel="stylesheet" type="text/css" href="../css/all.css" />
		<link type="text/css" rel="stylesheet" href="../css/nyroModal.css"/>
		<link rel="stylesheet" type="text/css" href="../themes/sexybuttons/sexybuttons.css" />
		<link rel="stylesheet" type="text/css" href="info.css" />
		
		<script type="text/javascript" src="../common/js/jquery-last.js"></script>
		<script type="text/javascript" src="../common/js/plugin/jquery.form.js"></script>
		<script type="text/javascript" src="../common/js/plugin/jquery.highlight.js"></script>
		<script type="text/javascript" src="../common/js/plugin/jquery.blockUI.js"></script>
		<script type="text/javascript" src="../common/js/plugin/jquery.nyroModal.js"></script>
		<script type="text/javascript" src="../common/js/plugin/jquery.validate.js"></script>
		<script type="text/javascript" src="../common/js/common.js"></script>
		<script type="text/javascript" src="../common/js/form.js"></script>
		<script type="text/javascript" src="js/information.js"></script>
	</head>
	<body>
		<div class="pageHead">
			<center><h2>信息颁布列表</h2></center>
			<span class="operation">
				<a href="javascript:;" id="showContent">显示</a>/<a href="javascript:;" id="hideContent">隐藏</a>内容
				<button id="addBtn" class="sexybutton"><span><span><span class="add">新信息</span></span></span></button>
				<button id="refreshInfo" class="sexybutton"><span><span><span class="sync">刷新</span></span></span></button>
			</span>
		</div>
		<div class="query">
			<span>
				<label for="qtitle">标题：<input type='text' id='qtitle' name="title" size="20"/></label>
				<label for="qinfoContent">内容：<input type='text' id='qinfoContent' name='infoContent' size="30"/></label>
			</span>
			<button id="searchInfo" type="reset" class="sexybutton"><span><span><span class="search">搜索</span></span></span></button>
		</div>
		<div id="pageBar">
			共<span id="records"></span>条信息，共<span id="shouldPage"></span>页，第<span id="currentPage"></span>页||
			<a href="javascript:;" id="page-head" style="display: none">首页|</a>
			<a href="javascript:;" id="page-prev" style="display: none">上一页|</a>
			<a href="javascript:;" id="page-next" style="display: none">下一页|</a>
			<a href="javascript:;" id="page-foot" style="display: none">尾页</a>
		</div><br/>
		<div id="infoList" class="infoList"></div>
		
		<div id="infoTemplate" class="template">
			<div class="singleInfo">
				<div class="infoHead">
					<span class="title"></span>
					<span class="info"></span>
					<span class="ed">
						<a href="javascript:;" class="edit">编辑</a>
						<a href="javascript:;" class="delete">删除</a>
					</span>
				</div>
				<div class="infoContent"></div>
			</div>
		</div>
		
		<div id="formTemplate" class="template" style="width: 80%">
		<form action="saveInfo.do" method="post" id="infoForm" name="infoForm">
			<input type="hidden" id="id" name="id"/>
            <table border="0" width="100%" cellspacing="0" class="tableForm">
                <tr>
                    <td class="label" width="10%">标题：</td>
                    <td>
                        <input type="text" id="title" name="title" maxlength="50" class="noempty" style="width:90%"/>
                    </td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td class="label">分类：</td>
                    <td>
                        <input type="text" id="category" name="category" maxlength="50" style="width:90%"/>
                    </td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td class="label">标签：</td>
                    <td>
                        <input type="text" id="infoLabel" name="infoLabel" maxlength="50" style="width:90%"/>
                    </td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td class="label">内容：</td>
                    <td>
                        <textarea id="infoContent" name="infoContent" rows="10" style="width:90%" class="noempty"></textarea>
                    </td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                	<td>&nbsp;</td>
                    <td colspan="2">
                    	<button type="submit" class="sexybutton"><span><span><span class="ok">提交</span></span></span></button>
                    	<button type="button" id="close" class="sexybutton"><span><span><span class="cancel">取消</span></span></span></button>
                    </td>
                </tr>
            </table>
        </form>
        </div>
        
	</body>
</html>
