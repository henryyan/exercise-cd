<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
<title>联系我们</title>
<!--CSS-->
<link rel="stylesheet" type="text/css" href="css/global.css" />
<!--Lightbox CSS - Remove if not needed
	<link rel="stylesheet" type="text/css" href="css/lightbox.css" media="screen" />
	-->
<!--Character Encoding-->
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<!--Lightbox JS - Remove if not needed
	<script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/scriptaculous.js?load=effects"></script>
	<script type="text/javascript" src="js/lightbox.js"></script>-->
</head>
<body>
<div id="mainContainer">
  <div id="header">
    <div id="logo1"><img src="images/logo1.gif"/></div>
    <div id="logo2"><img src="images/logo3.png"/></div>
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
      <center>
        <h5><font color="#ffffff">联系我们</font> </h5>
      </center>
      <br />
      <p>
      <h6>上海市静安区南京西路699号<br />
        东方众鑫大厦设备层8A-05b室<br />
        邮编：200040 <br />
        <br />
        电话：+8621 52110318<br />
        传真：+8621 52110119 <br />
      </h6>
      <h6>电邮：</h6>
      <a href="mailto:info@remote-click.com">info@remote-click.com</a> <br />
      <h6>网站：</h6>
      <a href="http://www.51duanlian.com">www.51duanlian.com</a>
      </p>
    </div>
  </div>
  <!--Footer-->
  <%@ include file="./footer.jsp" %>
</div>
</body>
</html>