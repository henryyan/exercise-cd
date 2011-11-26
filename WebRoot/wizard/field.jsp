<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/global.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
        <title>场馆维护向导</title>
		<%@ include file="../common/meta.jsp" %>
		<link rel="stylesheet" type="text/css" href="wizard.css"/>
        <script type="text/javascript" src="../common/js/jquery-last.js"></script>
        <script type="text/javascript" src="../common/js/plugin/jquery.corner.js"></script>
        <script type="text/javascript" src="js/wizardField.js"></script>
    </head>
    <body>
        <div class="wizard-nav" align="center">
        	<a id="step1" href="#" src="../field/type/list.do" class="active">1、设置场地类型</a>
            <a id="step2" href="#" src="../field/wizardFieldTab.jsp">2、添加场地信息</a>
            ><a id="step3" href="#" src="../price/wizardPriceTab.jsp">3、设置活动价格</a>
            ><a id="step4" href="#" src="../activity/wizardCreateActivityTab.jsp">4、场地信息生成</a>
            <div id="btns">
                <input id="prev" type="button" value="上一步" style="margin-right:50px;"/><input id="next" type="button" value="下一步"/>
            </div>
        </div><hr/>
        <span id="tip"></span>
        
        <div align="center">
            <iframe align="middle" id="nav-content" frameborder="0" height="100%"></iframe>
        </div>
    </body>
</html>
