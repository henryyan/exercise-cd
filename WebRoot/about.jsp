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
<title>关于我们</title>
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
        <h5><font color="#FFFFFF">关于我们</font></h5>
      </center>
      <h6>我要锻炼网 （<a href="http://www.51duanlian.com">www.51duanlian.com</a>）的使命是“生命在于运动，让您通过遥远的点击来组织好您的运动”。我要锻炼网，致力于打造全中国首选的在线运动管理及服务平台，由遥点网络于2009年08月投资创办。</h6>
      <h6>作为中国领先的在线运动场馆管理服务公司，我要锻炼网整合并提升了运动场馆的现场管理能力，并帮助锻炼人群有效快捷地搜寻到要锻炼场地实时信息。通过互联网与传统运动场馆的无缝结合，场馆管理者与锻炼者在全方位加强了彼此间的互动及了解。我要锻炼网为其所有会员打造更安全高效的网络管理、信息及交易平台的同时，也全心营造和倡导全民健身、健康生活的氛围。每位通过我要锻炼网进行管理、预定及交易的人，不但更迅速高效的解决运动场地供给与锻炼需求的矛盾，而且能够轻松地享受运动所带来的快乐生活！</h6>
    </div>
  </div>
  <!--Footer-->
  <%@ include file="./footer.jsp" %>
</div>
</body>
</html>