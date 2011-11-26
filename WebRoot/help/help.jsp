<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="../common/global.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title>系统帮助</title>
		<!--CSS-->
		<link rel="stylesheet" type="text/css" href="../css/global.css" />
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="../themes/${themeName }/jquery-ui-${themeVersion }.custom.css" />
		<style type="text/css">
	        html, body {
		        margin: 0;            /* Remove body margin/padding */
		        padding: 0;
		        overflow: hidden;    /* Remove scroll bars on browser window */
		        font-size: 85%;
	        }
			#r_content {
				width:auto;
				background-color:#000000;
				margin:4px;
				float: left;
				margin-top: 0px;
				height: 450px;
				padding-left: 0px;
				padding-right: 0px;
			}
			.qs dl {
				color: #FFFFFF;
				margin: 0px;
				padding: 0px;
				float: left;
				line-height: 20px;
			}
			.qs dd {
				font-size: 11px;
				line-height: 15px;
				color: #66CC00;
			}
			.qs dt {
				font-size: 12px;
				color: #FFFFFF;
				font-family: "黑体";
			}
			.item {background-color: #000000}
	    </style>

		<script type="text/javascript" src="../common/js/jquery-last.js"></script>
	    <script src="../common/js/ui/jquery-ui-${themeVersion }.custom.min.js" type="text/javascript"></script>
	    <script type="text/javascript" src="../common/js/common.js"></script>
	    <script type="text/javascript">
	    	$(function(){
				$('#helpTab').tabs();
		    });
	    </script>
	</head>
<body>
<div id="mainContainer">
  <div id="header">
    <div id="logo1"><img src="../images/logo1.gif"/></div>
    <div id="logo2"><img src="../images/logo3.png"/></div>
  </div>
  <div id="middlecontent">
    <div id="topmenu">
      <ul>
        <li><a href="${ctx }/main2.jsp">首页</a></li>
        <li><a href="${ctx }/about.jsp">关于我们</a></li>
        <li><a href="${ctx }/contact.jsp">联系我们</a></li>
        <li><a href="${ctx }/help/help.jsp">帮助中心</a></li>
      </ul>
    </div>
    <div id="r_content">
		<div id="helpTab" style="width: 986px;">
			<ul>
				<li><a href="#common">常见问题</a></li>
				<li><a href="#account">账号及信息</a></li>
				<li><a href="#flow">场地预定</a></li>
				<li><a href="#tactics">价格策略</a></li>
			</ul>
			<div id="common">
				<iframe src="common.html" frameborder="0" style="width: 100%;height: 380px"></iframe>
			</div>
			<div id="account">
				<iframe src="account.html" frameborder="0" style="width: 100%;height: 380px"></iframe>
			</div>
			<div id="flow">
				<iframe src="flow.html" frameborder="0" style="width: 100%;height: 380px"></iframe>
			</div>
			<div id="tactics">
				<iframe src="tactics.html" frameborder="0" style="width: 100%;height: 380px"></iframe>
			</div>
		</div>
    </div>
  </div>
  <!--Footer-->
  <%@ include file="../footer.jsp" %>
</div>
</body>
</html>