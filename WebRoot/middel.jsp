<%@ page language="java" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>无标题文档</title>
        <style type="text/css">
        <!--
        
        body {
            margin-left: 0px;
            margin-top: 0px;
            margin-right: 0px;
            margin-bottom: 0px;
        }
		.navPoint {
                COLOR: white;
                pointer: hand;
                FONT-FAMILY: Webdings;
                FONT-SIZE: 9pt
        }
		.STYLE1 {font-size: 12px}
		.STYLE3 {font-size: 12px; font-weight: bold; }
        -->
        </style>
        <script>
            function switchSysBar() {
                var locate = location.href.replace('middel.html', '');
                var ssrc = document.all("img1").src.replace(locate, '');
                if (ssrc == "images/main_55.gif") {
                    document.all("img1").src = "images/main_55_1.gif";
                    document.all("frmTitle").style.display = "none";
                } else {
                    document.all("img1").src = "images/main_55.gif";
                    document.all("frmTitle").style.display = "";
                }
            }
			
			function tt(){
				alert(true);
			}
        </script>
    </head>
    <body style="overflow:hidden">
    	<table width="100%" height="28" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
			
			<tr>
			    <td height="28" background="images/main_36.gif"><table width="100%" border="0" cellspacing="0" cellpadding="0">
			      <tr>
			        <td width="177" height="28" background="images/main_32.gif"><table width="100%" border="0" cellspacing="0" cellpadding="0">
			          <tr>
			            <td width="15%"  height="22">&nbsp;</td>
			            <td width="59%" valign="bottom"><div align="center" class="STYLE1">当前用户：${sessionScope.user.username}</div></td>
			            <td width="21%">&nbsp;</td>
			          </tr>
			        </table></td>
			        <td class="STYLE1"><span class="STYLE3">&nbsp;</td>
			        <td width="21"><img src="images/main_37.gif" width="21" height="28"></td>
			      </tr>
			    </table></td>
  </tr>
			
		</table>
		
        <table width="100%" height="95%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
            <tr>
                <td width="171" id=frmTitle noWrap name="fmTitle" align="center" valign="top">
                    <table width="171" height="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
                        <tr>
                            <td bgcolor="#1873aa" style="width:6px;">
                                &nbsp;
                            </td>
                            <td width="165">
                                <iframe name="I1" height="100%" width="165" src="left.html" border="0" frameborder="0" scrolling="no">
                                    浏览器不支持嵌入式框架，或被配置为不显示嵌入式框架。
                                </iframe>
                            </td>
                        </tr>
                    </table>
                </td>
                <td width="6" style="width:6px;"valign="middle" bgcolor="1873aa" onclick="switchSysBar()">
                    <SPAN class=navPoint id=switchPoint title=关闭/打开左栏><img src="images/main_55.gif" name="img1" width=6 height=40 id=img1></SPAN>
                </td>
                <td width="100%" align="center" valign="top">
                    <iframe name="I2" height="100%" width="100%" border="0" frameborder="0" src="tab/tab.html">
                        浏览器不支持嵌入式框架，或被配置为不显示嵌入式框架。
                    </iframe>
                </td>
            </tr>
        </table>
    </body>
</html>
