<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/global.jsp" %>
<%@ include file="../field/type/includeFieldTypes.jsp" %>
<html>
  <head>
  	<%@ include file="../common/meta.jsp" %>

	<link type="text/css" rel="stylesheet" href="../css/all.css" />
	<link type="text/css" rel="stylesheet" href="../css/ui/ui.tabs.css" />
	<link rel="stylesheet" type="text/css" href="../themes/${themeName }/jquery-ui-${themeVersion }.custom.css" />
	<style type="text/css">
		body{ font: 8pt "Trebuchet MS", sans-serif; margin: 0px;}
		#tabs {height: 590px}
	</style>

	<script type="text/javascript" src="../common/js/jquery-last.js"></script>
	<script type="text/javascript" src="../common/js/ui/jquery-ui-${themeVersion }.custom.min.js"></script>
	<script type="text/javascript" src="../common/js/plugin/jquery.blockUI.js"></script>
	<script type="text/javascript" src="../common/js/common.js"></script>
	<script type="text/javascript">
		$(function(){
			$('#tabs').tabs();
			var $parentWin = $(window.parent.document.body);
			var $parentNextBtn = $parentWin.find('#next');
			var created = '${param.created }';
			if (!created) {
				$parentNextBtn.attr('disabled', true);
				$.blockUI({
		            message:  '<h2><img src="../images/nyroModal/ajaxLoader.gif" class="status"/>正在生成场地活动，请稍候……</h2>',
					css: { width: '350px' }
		        });
				$(this).ajaxComplete($.unblockUI);
	            $.post('issueActivity.do', {
	            	wizard: true
	            }, function(req) {
	                if (req == 'success') {
	                    $parentNextBtn.attr('disabled', false);
	                    $parentWin.find('#tip').html('活动已生成，请点击<b>完成</b>按钮进入系统首页');
	                    location.href = 'wizardCreateActivityTab.jsp?created=true';
	                } else {
	                    alert('生成活动失败');
	                }
	            });
			}
		});
	</script>
  </head>

  <body>
  	<div id="tabs">
		<ul>
			<c:forEach items="${fieldTypes}" var="type">
				<li><a href="#${type.key }">${type.value }</a></li>
			</c:forEach>
		</ul>
		<c:forEach items="${fieldTypes}" var="type">
			<bean:define id="type" name="type" type="java.util.Map.Entry"></bean:define>
			<div id="${type.key }">
				<iframe src="${ctx }/activity/enableFieldList.do?fieldType=${type.key }" frameborder="0" style="height: 450px; width: 100%"></iframe>
			</div>
		</c:forEach>
	</div>
  </body>
</html>