<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ include file="common/global.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>我要锻炼管理系统_用户登录</title>
<link rel="stylesheet" href="css/weebox.css"/>
<script type="text/javascript" src="common/js/jquery-last.js"></script>
<script type="text/javascript" src="common/js/plugin/jquery.form.js"></script>
<script type="text/javascript" src="common/js/plugin/formValidator.js"></script>
<script type="text/javascript" src="common/js/plugin/bgiframe.js"></script>
<script type="text/javascript" src="common/js/plugin/weebox.js"></script>
<script type="text/javascript" src="common/js/common.js"></script>
<script type="text/javascript" src="common/js/list.js"></script>
<script type="text/javascript" src="common/js/form.js"></script>
<script type="text/javascript" src="common/js/login.js"></script>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-color: #016aa9;
	overflow:hidden;
}
.STYLE1 {
	color: #000000;
	font-size: 12px;
}
-->
</style>
<script type="text/javascript">
			<c:if test="${not empty user}">
			window.location.href='main2.html';
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
        <li><a href="#">关于我们</a></li>
        <li><a href="#">联系我们</a></li>
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
        <div id="zhuce"><img src="images/注册.gif" width="131" height="27"/></div>
      </div>
      <div id="sign">
        <form action="login.do" id="loginForm" name="loginForm" method="post">
          <p>访问您的账户</p>
          <div id="">
            <label for="membername" style="width:80px;">用户名：</label>
            <input name="loginName" type="text" id="loginName" value="" />
          </div>
          <div id="">
            <label for="password" style="width:80px;" >密码：</label>
            <input name="password" type="password" id="password" value=""/>
          </div>
          <a class="forget" href="#">忘记密码？</a>
          <div id="denglu"><img src="images/sign-in.gif" width="132" height="26"/></div>
        </form>
      </div>
    </div>
  </div>
  <!--Footer-->
  <%@ include file="./footer.jsp" %>
</div>
</body>
</html>