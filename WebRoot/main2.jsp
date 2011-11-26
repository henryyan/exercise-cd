<%@ page language="java"import="java.util.*"pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN""http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file="common/global.jsp"%>
<!--
Site Name:51duanlian
Developed By: amos lin
Date Created: 2009-07-15
Last Updated: 2009-07-16
Copyright:
-->
<html xmlns="http://www.w3.org/1999/xhtml"xml:lang="en"lang="en">
<head>
<meta http-equiv="X-UA-Compatible"content="IE=7"/>
<!--CSS-->
<link rel="stylesheet"type="text/css"href="css/global.css"/>
<!--Character Encoding-->
<meta http-equiv="Content-Type"content="text/html;charset=utf-8"/>
<title>我要锻炼管理系统</title>
<%@ include file="./mainInclude.jsp"%>
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
    <!--Nav-->
    <div id="topmenu">
	  <ul>
	    <li>欢迎 <font size="2"> <b>${empty user.venueInfo ? "": user.venueInfo.venueName }</b> </font>管理员</li>
	    <li><a href="${ctx }/main2.jsp">首页</a></li>
        <li><a href="${ctx }/about.jsp">关于我们</a></li>
        <li><a href="${ctx }/contact.jsp">联系我们</a></li>
        <li><a href="${ctx }/help/help.jsp">帮助中心</a></li>
	    <li><a href="${ctx }/logout.do">退出</a></li>
	  </ul>
	</div>
    <div id="siderbar"class="mainsiderbar">
      <div id="si-content">
        <ul class="sc">
          <li class="sc-title">场馆信息</li>
          <li>
            <ul style="margin-left:-20px;">
              <li class="sc-item"><a href='venue/changePassword.html' target="main">修改密码</a></li>
              <li class="sc-item"><a href='venue/venueInfoTab.jsp'>修改场馆信息</a></li>
              <li class="sc-item"style="display: none">升级B类场馆会员申</li>
              <li class="sc-item"><a href='information/infoList.jsp'>场馆信息颁布</a></li>
              <li class="sc-item"><a href='tactics/tacticsActivityTab.jsp'>待处理活动</a></li>
              <li class="sc-item"><a href='activity/createActivityTab.jsp'>场地生成策略</a></li>
              <li class="sc-item"><a href='field/type/enableTypeList.do'>场地类型列表</a></li>
            </ul>
          </li>
        </ul>
        <%--<ul class="sc">
          <li class="sc-title">场地基本信息</li>
          <li>
            <ul style="margin-left:-20px;">
              <li class="sc-item"><a href='field/fieldTab.jsp'>修改场地信息</a></li>
              <li class="sc-item"><a href='price/priceTab.jsp'>默认价格策略</a></li>
              <li class="sc-item"><a href='tactics/tacticsList.do'>特殊价格策略</a></li>
              <li class="sc-item"><a href='tactics/tacticsActivityTab.jsp'>待处理活动</a></li>
              <li class="sc-item"><a href='activity/createActivityTab.jsp'>场地生成策略</a></li>
            </ul>
          </li>
        </ul>--%>
        <ul class="sc">
          <li class="sc-title">场地预订</li>
          <li>
            <ul id="bookFieldType" style="margin-left:-20px;"></ul>
          </li>
        </ul>
        <ul class="sc">
          <li class="sc-title">场地现场维护</li>
          <li>
            <ul id="localFieldType" style="margin-left:-20px;"></ul>
          </li>
        </ul>
        <ul class="sc">
          <li class="sc-title">会员卡管理</li>
          <li>
            <ul style="margin-left:-20px;">
              <li class="sc-item"><a href='member/cardTypeList.do'>会员卡类型</a></li>
              <li class="sc-item"><a href='member/memberCardList.jsp'>会员录入及查询</a></li>
            </ul>
          </li>
        </ul>
        <ul class="sc">
          <li class="sc-title">零售商品</li>
          <li>
            <ul style="margin-left:-20px;">
              <li class="sc-item"><a href='retail/retailGoodsList.jsp'>商品管理</a></li>
              <%--li class="sc-item"><a href='retail/retailSellList.jsp'>已售出商品</a></li> --%>
            </ul>
          </li>
        </ul>
        <ul class="sc">
          <li class="sc-title">场地报表查阅</li>
          <li>
            <ul style="margin-left:-20px;">
              <li class="sc-item"><a href='report/reportDaily.jsp'>每日报表</a></li>
              <%--<li class="sc-item"><a href='report/reportCash.jsp'>资金状况</a></li> --%>
              <li class="sc-item"><a href='report/orderItemList.jsp'>活动订单列表</a></li>
              <li class="sc-item"><a href='report/accountOrderList.jsp'>付款订单列表</a></li>
              <c:if test="${user.venueInfo.isProtocol }">
              <li class="sc-item"><a href='report/protocolFee.jsp'>协议服务费结算</a></li>
              </c:if>
            </ul>
          </li>
        </ul>
      </div>
    </div>
    <div id="content">
      <iframe id="main"name="main"frameborder="0"width="100%"height="100%"src="statistics/summary.jsp"></iframe>
    </div>
  </div>
  <!--Footer-->
  <%@ include file="./footer.jsp"%>
</div>
</body>
</html>
