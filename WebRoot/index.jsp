<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file="common/global.jsp" %>
<!--
Site Name:51duanlian
Developed By: amos lin
Date Created: 2009-07-15
Last Updated: 2009-07-16
Copyright:
-->
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<!--CSS-->
<link rel="stylesheet" type="text/css" href="css/global.css" />
<link rel="stylesheet" type="text/css" href="css/weebox.css" />
<style type="text/css">
#getBackPwd {
	padding-top:8px;
	font-size: 10pt;
	text-align: right;
}
</style>
<!--Character Encoding-->
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title>我要锻炼管理系统</title>
<script type="text/javascript" src="common/js/jquery-last.js"></script>
<script type="text/javascript" src="common/js/plugin/jquery.form.js"></script>
<script type="text/javascript" src="common/js/plugin/formValidator.js"></script>
<script type="text/javascript" src="common/js/plugin/bgiframe.js"></script>
<script type="text/javascript" src="common/js/plugin/weebox.js"></script>
<script type="text/javascript" src="common/js/common.js"></script>
<script type="text/javascript" src="common/js/list.js"></script>
<script type="text/javascript" src="common/js/form.js"></script>
<script type="text/javascript" src="common/js/login.js"></script>
<script type="text/javascript">
	var Sys = {};
	var ua = navigator.userAgent.toLowerCase();
	if (window.ActiveXObject)
	    Sys.ie = ua.match(/msie ([\d.]+)/)[1];
	else if (document.getBoxObjectFor)
	    Sys.firefox = ua.match(/firefox\/([\d.]+)/)[1];
	else if (window.MessageEvent && !document.getBoxObjectFor)
	    Sys.chrome = ua.match(/chrome\/([\d.]+)/)[1];
	else if (window.opera)
	    Sys.opera = ua.match(/opera.([\d.]+)/)[1];
	else if (window.openDatabase)
	    Sys.safari = ua.match(/version\/([\d.]+)/)[1];
	
	
	if(Sys.ie<7.0) {
         alert("请将IE版本升级至7.0或以上！");
         window.open("http://www.microsoft.com/china/windows/internet-explorer/","");
     }

	<c:if test="${not empty user}">
	window.location.href='main2.jsp';
	</c:if>
</script>
</head>
<body>
<!--Main Container - Centers Everything-->
<div id="mainContainer">
  <!--Header -->
  <div id="header">
    <div id="logo1"><img src="images/logo1.gif"/></div>
    <div id="logo2"><img src="images/logo3.png"/></div>
  </div>
  <!--Main Content-->
  <div id="middlecontent">
    <div id="topmenu">
      <ul>
        <li><a href="./index.jsp">首页</a></li>
        <li><a href="./about.jsp">关于我们</a></li>
        <li><a href="./contact.jsp">联系我们</a></li>
        <li><a href="#">帮助中心</a></li>
      </ul>
    </div>
    <div id="siderbar"> <img src="images/si-pic.gif" width="201" height="352"/></div>
    <div id="content">
      <div id="register">
        <p>加入我要锻炼</p>
        <ul>
          <li>（1）预订系统，让您对场地的预订情况运筹帷幄。</li>
          <li>（2）实时的运动场馆现场管理系统，轻松验证管理者。</li>
          <li>（3）会员管理系统，让您有效的组织、管理您的会员记录。</li>
          <li>（4）强大的管理信息报表，帮助您分析您的运营及资金状况。</li>
        </ul>
        <div id="regist" class="button" style="padding-top:3px;">Register 注册</div>
      </div>
      <div id="sign">
        <form action="login.do" id="loginForm" name="loginForm" method="post">
          <p>访问您的账户</p>
          <div id="">
            <label for="membername" style="width:80px;">用户名：</label>
            <input name="loginName" type="text" id="loginName" value="" />
          </div>
          <div id="">
            <label for="password" style="width:80px;" >密&nbsp;&nbsp;码：</label>
            <input name="password" type="password" id="password" value=""/>
          </div>
          <div id="getBackPwd" ><a href='javascript:;'>忘记密码？</a></div>
          <div id="login" class="button"><span>Sign in 登陆</span></div>
        </form>
      </div>
    </div>
  </div>
  <!--Footer-->
  <%@ include file="./footer.jsp" %>
</div>
</body>
</html>