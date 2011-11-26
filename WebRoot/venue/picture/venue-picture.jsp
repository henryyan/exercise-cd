<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/common/meta.jsp" %>
	<title>场馆图片查看、编辑</title>
	<link rel="stylesheet" type="text/css" href="${ctx }/css/tablecloth.css" />
	<link rel="stylesheet" href="${ctx }/css/all.css" type="text/css" />
	<link rel="stylesheet" type="text/css" href="${ctx }/themes/${themeName }/jquery-ui-${themeVersion }.custom.css" />
	<link rel="stylesheet" type="text/css" href="${ctx }/css/nyroModal.css"/>
	<link href="${ctx }/css/style.css" type="text/css" rel="stylesheet"/>
	
	<style type="text/css">
	body {background-color: black;}
	td {border-bottom: 1px solid white; padding-bottom: 3px; padding-top: 5px;}
	.ui-icon {display: inline;}
	.noneText {color: #979797;}
	.activeNoneText {background-color: #ADD8E6}
	.nshow,.rshow {cursor: pointer;}
	.picNameDiv {margin-bottom: 5px; margin-top: 5px;}
	.nedit,.redit { background-color: #D0FE81; padding: 10px; display: none;}
	.btns {padding-top: 20px;}
	.majorPicture {padding: 5px; background-color: yellow; margin-top: 5px; color: red; font-weight: bold;}
	#uploadPictureDiv { background-color: yellow; padding: 5px 20px 10px 20px; text-align: right; float: right;}
	#uploadPicture {font-weight: bold;}
	</style>
	
	<script src="${ctx }/common/js/jquery-last.js" type="text/javascript"></script>
	<script src="${ctx }/common/js/plugin/jquery.corner.js" type="text/javascript"></script>
	<script src="${ctx }/common/js/plugin/jquery.nyroModal.js" type="text/javascript"></script>
	<script src="js/venue-picture.js" type="text/javascript"></script>
</head>

<body>
	<span id="uploadPictureDiv" class="ui-corner-all">
		<a id="uploadPicture" href="javascript:;">上传图片</a>
	</span>
	<table border="0" width="100%" cellpadding="0" cellspacing="0" style="background-color: black;border: none; margin-top: 50px;">
	<c:forEach items="${pictures[1] }" var="pic">
	<tr class="picContainer">
		<td class="singlePicture" width="100">
			<a href="${ctx }/venue/pictures/${user.venueInfo.id}/${pic.pictureRealName}" title="${pic.pictureName }" class="nyroModal">
				<img src="${ctx }/venue/pictures/${user.venueInfo.id}/thumbnails/120/${pic.pictureRealName}" width="100" height="100" border="0"/>
			</a>
		</td>
		<td id="${pic.id }" style="vertical-align: top;" class="{pid: ${pic.id }}">
			<div class="picNameDiv">
				<b>图片名称：</b>
				<div class="nshow" style="display: inline;">
					<c:if test="${empty pic.pictureName}"><span class="noneText">点击添加图片名称</span></c:if>
					<c:if test="${not empty pic.pictureName}"><span class="picNameCt">${pic.pictureName}</span></c:if>
					<span class="ui-icon ui-icon-pencil" style="display: inline">&nbsp;&nbsp;&nbsp;</span>
				</div>
				<div class="nedit"></div>
			</div>
			<div class="picRemarkDiv">
				<b>图片描述：</b>
				<div class="rshow" style="display: inline;">
					<c:if test="${empty pic.pictureRemark}"><span class="noneText">点击添加图片描述</span></c:if>
					<c:if test="${not empty pic.pictureRemark}"><span class="picRemarkCt">${pic.pictureRemark}</span></c:if>
					<span class="ui-icon ui-icon-pencil">&nbsp;&nbsp;&nbsp;</span>
				</div>
				<div class="redit"></div>
			</div>
		</td>
		<td id="${pic.id }" style="vertical-align: top;" width="220">
			<b>上传日期：</b>${pic.uploadDate }<br/>
			<b>图片大小：</b>${pic.pictureSizeShow }<br/>
			<span class="ui-icon ui-icon-trash">&nbsp;&nbsp;&nbsp;</span><a href="javascript:;" class="deletePic">删除此图片</a><br/>
			<div class="majorPicDiv" style="display:<c:if test="${majorPicture == pic.pictureRealName}">none</c:if>"><span class="ui-icon ui-icon-wrench">&nbsp;&nbsp;&nbsp;</span><a href="javascript:;" prn="${pic.pictureRealName}" class="setMajor">设置为场馆主图片</a></div>
			<c:if test="${majorPicture == pic.pictureRealName}">
				<div class="majorPicture ui-corner-all">主图片</div>
			</c:if>
		</td>
	</tr>
	</c:forEach>
	</table>
	
	<div id="picNameTemplate" class="template">
		<div class="picNameEdit" style="display: inline;">
			<input class="picNameInput" size="60" maxlength="80" /><br/>
			<span class="btns">
				<button type="button" class="picNameOk">确 定</button>
				<a href="javascript:;" class="picNameCancel">取 消</a>
			</span>
		</div>
	</div>
	
	<div id="picRemarkTemplate" class="template">
		<div class="picRemarkEdit" style="display: inline;">
			<textarea class="picRemarkInput" rows="3" cols="60"></textarea><br/>
			<span class="btns">
				<button type="button" class="picRemarkOk">确 定</button>
				<a href="javascript:;" class="picRemarkCancel">取 消</a>
			</span>
		</div>
	</div>
	
</body>
</html>