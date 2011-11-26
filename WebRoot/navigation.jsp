<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div id="topmenu">
  <ul>
    <li>欢迎 <font size="2"> <b>${empty user.venueInfo ? "" : user.venueInfo.venueName }</b> </font>管理员</li>
    <li><a href="main2.jsp">首页</a></li>
    <li><a href="about.jsp">关于我们</a></li>
    <li><a href="contact.jsp">联系我们</a></li>
    <li><a href="#">帮助中心</a></li>
    <li><a href="logout.do">退出</a></li>
  </ul>
</div>
