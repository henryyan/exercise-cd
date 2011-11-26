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
	<link rel="stylesheet" type="text/css" href="css/all.css" />
	<style type="text/css">
	body {
		text-align: center;
		font-size: 12px;
		overflow: hidden;
	}
	/*---信息框--*/
	#MsgContent {
		height: auto;
		overflow: auto;
		text-align: left;
		padding: 1em;
		margin:auto auto 20px;
		border: 2px solid white;
	}
	#MsgContent ol {
		padding-left: 20px;
	}
	#MsgContent ol li {
		padding-bottom: 8px;
	}
	.label {color:white;font-size: 13px}
	#getBackPwd {
		padding-top:8px;
		font-size: 10pt;
		text-align: right;
	}
	</style>
	<!--Character Encoding-->
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<title>我要锻炼管理系统-会员注册</title>
	<script type="text/javascript" src="common/js/jquery-last.js"></script>
	<script type="text/javascript" src="common/js/plugin/formValidator.js"></script>
       <script type="text/javascript" src="common/js/plugin/jquery.form.js"></script>
       <script type="text/javascript" src="common/js/common.js"></script>
	<script type="text/javascript" src="common/js/list.js"></script>
	<script type="text/javascript" src="common/js/form.js"></script>
	<c:if test="${param.action == 'agree'}">
	<script type="text/javascript" src="register.js"></script>
	</c:if>
	<c:if test="${param.action != 'agree'}">
	<script type="text/javascript" src="declare.js"></script>
	</c:if>
</head>
<body>
<!--Main Container - Centers Everything-->
<div id="mainContainer" style="background-color: black;">
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
    <div id="content" style="width:1000px">
      <c:if test="${param.action == 'agree'}">
        <form action="register.do" name="registForm" id="registForm" method="post">
            <div style="margin-top:20px; color: white; font-size: 2em" align="center">
            	<b>我要锻炼场馆管理员注册</b>
            </div>
            <div style="margin-top: 20px; margin-bottom: 50px;" align="center">
                <table cellpadding="3" cellspacing="0" align="center" border="1" class="tableborder1" id="table1">
                    <tr>
                        <td class="label" width="100">场馆会员名：</td>
                        <td class="tablebody2">
                            <input type="text" maxlength="16" id="userName" name="userName" noempty="true" style="width:140px"/>
                        </td>
						<td id="userNameTip" width="52%" style="color: #000000; background-color: #F0FFFF"></td>
                    </tr>
                    <tr>
                        <td class="label">登录密码：</td>
                        <td class="tablebody2">
                            <input type="password" maxlength="16" id="password1" name="password1" noempty="true" style="width:140px"/>
                        </td>
						<td id="password1Tip" style="color: #000000; background-color: #F0FFFF"></td>
                    </tr>
                    <tr>
                        <td class="label">确认密码：</td>
                        <td class="tablebody2">
                            <input type="password" maxlength="16" id="password2" name="password2" noempty="true" style="width:140px"/>
                        </td>
						<td id="password2Tip" style="color: #000000; background-color: #F0FFFF"></td>
                    </tr>
					<tr style="display: none">
                        <td class="tablebody2" valign="middle" colspan="3" align="center">
                            <div id="declare">免责声明及协议    <a href="declare.html">点击查看</a></div>
							<div id="agree">
								<input type="radio" id="agreeFlag1" name="agreeFlag" value="0"/><label for="agreeFlag1">我同意</label>
								<input type="radio" id="agreeFlag2" name="agreeFlag" value="1"/><label for="agreeFlag2">不同意</label>
							</div>
                        </td>
                    </tr>
                    <tr>
                        <td class="tablebody2" valign="middle" colspan="3" align="center">
                            <span>
                            	<input type="submit" id="submit" value="注 册"/>
								<input type="reset" name="reset" value="清 除" />
								<a href="index.jsp">返回登录</a>
							</span>
                        </td>
                    </tr>
                </table>
            </div>
        </form>
        </c:if>
        <c:if test="${param.action != 'agree'}">
        <p align="center" style="margin-top: 1em;"><font size="5" face="宋体"><b>我要锻炼网场馆用户协议</b></font></p>
        <div id="MsgContent" style="height: 400px;"> 
			<p><font size="2" face="宋体"><b>一、总则</b></font></p> 
			<ol type="1"> 
			  <li><a name="0.1_OLE_LINK1"></a><font size="2" face="宋体">本服务协议双方为上海遥点网络有限公司（下称“遥点网络</font><font size="2" face="simsun">”</font><font size="2" face="宋体">）与</font><font size="2" face="simsun"><a href="http://51duanlian.com" target="_blank">5<WBR>1duanlian.com</a></font><font size="2" face="宋体">我要锻炼网场馆用户，<WBR>本服务协议具有合同效力。</font></li> 
			  <li><font size="2" face="宋体">为获得遥点网络通过我要锻炼网（以下简称“我要锻炼</font><font size="2" face="simsun">”</font><font size="2" face="宋体">）<WBR>提供基于互联网的相关服务，服务使用人</font><font size="2" face="simsun">(</font><font size="2" face="宋体">以下称“场馆用户”</font><font size="2" face="simsun">)</font><font size="2" face="宋体">必<WBR>须同意本协议的全部条款并按照页面上的提示完成注册程序。<WBR>如果场馆用户在注册程序过程中点击“</font><font size="2" face="simsun">我</font><font size="2" face="宋体">同意”</font><font size="2" face="simsun">按</font><font size="2" face="宋体">钮即表示场馆用户<WBR>与遥点网络达成协议，完全接受本协议项下的全部条款。</font></li> 
			  <li><font size="2" face="宋体">本服务协议内容包括协议正文及所有遥点网络已经发布的或将来可能<WBR>发布的各类规则。所有规则为协议不可分割的一部分，<WBR>与协议正文具有同等法律效力。<WBR>当场馆用户使用我要锻炼各项场地管理及预订交易服务时，<WBR>场馆用户的使用行为视为其对该项服务的服务条款以及我要锻炼在该<WBR>单项服务中发出的各类公告的同意。</font></li> 
			  <li><font size="2" face="宋体">我要锻炼场馆用户协议以及各个预订服务条款和公告可由遥点网络随<WBR>时更新，且无需另行通知。您在使用相关服务时，<WBR>应关注并遵守其所适用的相关条款。<WBR>经修订的协议一经在我要锻炼公布后，立即自动生效。<WBR>各类规则会在发布后生效，亦成为本协议的一部分。<WBR>登录或继续使用“</font><font size="2" face="simsun">服</font><font size="2" face="宋体">务”</font><font size="2" face="simsun">将</font><font size="2" face="宋体">表示场馆用户接受经修订的协议。<WBR>除另行明确声明外，任何使“</font><font size="2" face="simsun">服</font><font size="2" face="宋体">务”</font><font size="2" face="simsun">范</font><font size="2" face="宋体">围扩大或功能增强的新内容均<WBR>受本协议约束。</font></li> 
			  <li><font size="2" face="宋体">您声明，在您同意接受本协议并注册成为我要锻炼场馆用户时，<WBR>您是具有法律规定的完全民事权利能力和民事行为能力，<WBR>能够独立承担民事责任的自然人、法人或其他组织；<WBR>本协议内容不受您所属国家或地区的排斥。不具备前述条件的，<WBR>您应立即终止注册或停止使用本服务。</font></li> 
			</ol> 
			 <br> 
			<p><font size="2" face="宋体"><b>二、服务内容</b></font></p> 
			<ol type="1"> 
			  <li><font size="2" face="宋体">我要锻炼场地管理及预订交易平台：<WBR>有关我要锻炼场地管理及预订交易平台上的术语或图示的含义，<WBR>详见我要锻炼帮助。服务的具体内容由遥点网络根据实际情况提供，<WBR>遥点网络保留随时变更、<WBR>中断或终止部分或全部网络服务包括收费网络服务的权利。</font></li> 
			  <li><font size="2" face="宋体">我要锻炼仅作为场馆用户经营其场地的场地管理及预订交易平台，<WBR>场馆用户对其在场地管理系统平台操作后所产生的经营管理信息负责<WBR>，所有场馆的运动场地实时状态由场馆用户发布并维护。</font></li> 
			  <li><font size="2" face="宋体">我要锻炼系获取相关场地信息与预定该锻炼场地的地点，<WBR>不能控制场馆管理者发布场地信息所涉及信息、安全或合法性，<WBR>服务信息的真实性或准确性，<WBR>以及交易方履行其在服务协议项下的各项义务的能力。</font></li> 
			  <li><font size="2" face="宋体">场馆用户同意遥点网络使用场馆用户对其场地管理系统平台操作后所<WBR>产生的经营管理信息，通过我要锻炼场地管理及预订交易平台，<WBR>将场馆用户发布的运动场地实时状态提供给我要锻炼其他网络用户。</font></li> 
			  <li><font size="2" face="宋体">我要锻炼并不作为锻炼用户或是场馆提供者的身份参与服务行为的本<WBR>身。场馆用户对其发布相关信息的真实性、合法性和有效性负责。</font></li> 
			  <li><font size="2" face="宋体">场馆用户因接受预订及提供锻炼场地服务，<WBR>获取有偿服务或接触我要锻炼服务器而发生的所有应纳税赋，<WBR>以及一切硬件、软件、<WBR>服务及其它方面的费用均由场地用户负责支付。<WBR>我要锻炼仅作为场地管理及预订交易信息平台。</font></li> 
			  <li><font size="2" face="宋体">场馆用户必须同意接受遥点网络通过电子邮件，<WBR>短信及其他方式向锻炼用户发送服务信息或其他相关商业信息。<WBR>该服务信息是由场馆用户进行场地管理操作所触发的，<WBR>场馆用户应当对其操作所发送的服务信息负责。</font></li> 
			  <li><font size="2" face="宋体">我要锻炼仅提供相关的网络服务，<WBR>除此之外与相关网络服务有关的设备</font><font size="2" face="simsun">(</font><font size="2" face="宋体">如电脑、<WBR>调制解调器及其他与接入互联网有关的装置</font><font size="2" face="simsun">)</font><font size="2" face="宋体">及所需的费用</font><font size="2" face="simsun">(</font><font size="2" face="宋体">如为接<WBR>入互联网而支付的电话费及上网费</font><font size="2" face="simsun">)</font><font size="2" face="宋体">均应由用户自行负担。</font></li> 
			</ol> 
			<p><font size="2" face="宋体"><b>三、使用规则</b></font></p> 
			<ol type="1"> 
			  <li><font size="2" face="宋体">场馆用户有权利拥有自己在我要锻炼的用户名及交易密码，<WBR>并有权利使用自己的用户名及密码随时登陆我要锻炼预订及交易平台<WBR>。<WBR>用户不得以任何形式擅自转让或授权他人使用自己的我要锻炼用户名<WBR>。</font></li> 
			  <li><font size="2" face="宋体">场馆用户在申请使用我要锻炼服务时，必须向我要锻炼提供及时、<WBR>详尽及准确的场馆用户基本资料，并不断更新注册资料，符合及时、<WBR>详尽及准确的要求。如果因注册信息不真实而引起的问题，<WBR>并对问题发生多带来的后果，遥点网络不负任何责任。</font></li> 
			  <li><font size="2" face="宋体">场馆用户注册成功后，<WBR>我要锻炼将给予每个场馆用户一个用户帐号及相应密码，<WBR>该场馆用户帐号和密码由场馆用户负责保管；<WBR>场馆用户应当对以其用户帐号进行的所有活动和事件负法律责任。</font></li> 
			  <li><font size="2" face="宋体">场馆用户在使用我要锻炼服务过程中，必须遵循以下原则：</font></li> 
			  <ol type="a"> 
			    <li><font size="2" face="宋体">遵守中华人民共和国相关法律法规，包括但不限于《<WBR>中华人民共和国计算机信息系统安全保护条例》、《<WBR>计算机软件保护条例》、《<WBR>最高人民法院关于审理涉及计算机网络著作权纠纷案件适用法律若干<WBR>问题的解释</font><font size="2" face="simsun">(</font><font size="2" face="宋体">法释</font><font size="2" face="simsun">[2004]1</font><font size="2" face="宋体">号</font><font size="2" face="simsun">)</font><font size="2" face="宋体">》、《<WBR>全国人大常委会关于维护互联网安全的决定》、《<WBR>互联网电子公告服务管理规定》、《互联网新闻信息服务管理规定》<WBR>、《互联网著作权行政保护办法》和《信息网络传播权保护条例》<WBR>等有关计算机互联网规定和知识产权的法律和法规、实施办法；</font></li> 
			    <li><font size="2" face="宋体">我要锻炼不具备互联网新闻信息发布资质，<WBR>我要锻炼所有用户不得在我要锻炼任何板块发布、登载、<WBR>转载或以其它任何方式发送任何时政类相关信息及含有下列内容之一<WBR>的信息： <br> 
			    </font><font size="2" face="simsun">(1)</font><font size="2" face="宋体">违反宪法确定的基本原则的； <br> 
			    </font><font size="2" face="simsun">(2)</font><font size="2" face="宋体">危害国家安全，泄漏国家机密，颠覆国家政权，<WBR>破坏国家统一的； <br> 
			    </font><font size="2" face="simsun">(3)</font><font size="2" face="宋体">损害国家荣誉和利益的； <br> 
			    </font><font size="2" face="simsun">(4)</font><font size="2" face="宋体">煽动民族仇恨、民族歧视，破坏民族团结的； <br> 
			    </font><font size="2" face="simsun">(5)</font><font size="2" face="宋体">破坏国家宗教政策，宣扬邪教和封建迷信的； <br> 
			    </font><font size="2" face="simsun">(6)</font><font size="2" face="宋体">散布谣言，扰乱社会秩序，破坏社会稳定的； <br> 
			    </font><font size="2" face="simsun">(7)</font><font size="2" face="宋体">散布淫秽、色情、赌博、暴力、恐怖或者教唆犯罪的； <br> 
			    </font><font size="2" face="simsun">(8)</font><font size="2" face="宋体">侮辱或者诽谤他人</font><font size="2" face="simsun">,</font><font size="2" face="宋体">侵害他人合法权益的； <br> 
			    </font><font size="2" face="simsun">(9)</font><font size="2" face="宋体">煽动非法集会、结社、游行、示威、聚众扰乱社会秩序的； <br> 
			    </font><font size="2" face="simsun">(10)</font><font size="2" face="宋体">以非法民间组织名义活动的； <br> 
			    </font><font size="2" face="simsun">(11)</font><font size="2" face="宋体">含有虚假、有害、胁迫、侵害他人隐私、骚扰、侵害、<WBR>中伤、粗俗、或</font><font size="2" face="simsun"> </font><font size="2" face="宋体">其它道德上令人反感的内容； <br> 
			    </font><font size="2" face="simsun">(12)</font><font size="2" face="宋体">含有法律、法规、规章、<WBR>条例以及任何具有法律效力之规范所限制或禁止的其他内容的。</font></li> 
			    <li><font size="2" face="宋体">场馆用户承诺对其上传或者发表于我要锻炼的所有信息（即属于《<WBR>中华人民共和国著作权法》规定的作品，包括但不限于文字、图片、<WBR>音乐、电影、表演和录音录像制品和电脑程序等）<WBR>均享有完整的知识产权，或者已经得到相关权利人的合法授权；<WBR>如用户违反本条规定造成遥点网络被第三人索赔的，<WBR>用户同意全额补偿遥点网络（包括但不限于各种赔偿费用和律师费）<WBR>；</font></li> 
			    <li><font size="2" face="宋体">当第三方认为用户上传或者发表于我要锻炼的信息侵犯其权利，<WBR>并根据《信息网络传播权保护条例》<WBR>或者相关法律规定向遥点网络发送权利通知书时，<WBR>场馆用户同意遥点网络可以自行判断决定删除涉嫌侵权的场馆用户上<WBR>传或者发表于我要锻炼的信息，<WBR>除非收到符合相关法律规定且可以证明遥点网络不承担任何法律责任<WBR>的书面说明，遥点网络将不会自动恢复上述删除的信息；</font></li> 
			    <li><font size="2" face="宋体">不得为任何非法目的而使用网络服务系统；</font></li> 
			    <li><font size="2" face="宋体">遵守所有与网络服务有关的网络协议、规定和程序；</font></li> 
			    <li><font size="2" face="宋体">不得利用我要锻炼服务进行任何可能对互联网的正常运转造成不利影<WBR>响的行为；</font></li> 
			    <li><font size="2" face="宋体">不得利用我要锻炼服务进行任何不利于我要锻炼的行为；</font></li> 
			    <li><font size="2" face="宋体">如发现任何非法使用用户帐号或帐号出现安全漏洞的情况，<WBR>应立即通告我要锻炼网站方。</font></li> 
			    <li><font size="2" face="宋体">如用户在使用网络服务时违反上述任何规定，<WBR>遥点网络有权要求用户改正或直接采取一切必要的措施（<WBR>包括但不限于删除用户张贴的内容、<WBR>暂停或终止用户使用网络服务的权利）<WBR>以减轻用户不当行为而造成的影响。</font></li> 
			</ol></ol> 
			<p><font size="2" face="宋体"><b>四、内容权利</b></font></p> 
			<ol type="1"> 
			  <li><font size="2" face="宋体">场馆用户对于其使用场地管理及预订交易平台而产生及发布的合法信<WBR>息依法享有所有权及相关权利。</font></li> 
			  <li><font size="2" face="宋体">对于场馆用户通过我要锻炼场地管理及预订交易平台，<WBR>维护所产生在我要锻炼网站上的任何信息，<WBR>场馆用户同意遥点网络在全世界范围内具有永久性的、<WBR>不可撤销的权利和免许可费、可完全转授的权利。<WBR>遥点网络可将场馆用户通过我要锻炼场地管理及预订交易平台服务所<WBR>产生的任何信息免费进行复制、修改、<WBR>改编等以用于我要锻炼的其他业务，<WBR>并可通过各种形式免费使用上述信息。</font></li> 
			</ol> 
			<p><font size="2" face="宋体"><b>五、隐私保护</b></font></p> 
			<ol type="1"> 
			  <li><font size="2" face="宋体">保护场馆用户隐私是遥点网络的重点原则，<WBR>遥点网络保证不对外公开或向第三方提供用户注册资料及用户在使用<WBR>服务时存储在我要锻炼的非公开内容，但下列情况除外：</font></li> 
			  <ol type="a"> 
			    <li><font size="2" face="宋体">事先获得场馆用户的明确授权；</font></li> 
			    <li><font size="2" face="宋体">根据有关的法律法规要求；</font></li> 
			    <li><font size="2" face="宋体">按照相关政府主管部门的要求；</font></li> 
			    <li><font size="2" face="宋体">为维护社会公众的利益；</font></li> 
			    <li><font size="2" face="宋体">为维护遥点网络的合法权益。</font></li> 
			    </ol> 
			  <li><font size="2" face="宋体">遥点网络可能会与第三方合作向用户提供相关的服务，在此情况下，<WBR>如该第三方同意承担与遥点网络同等的保护场馆用户隐私的责任，<WBR>则遥点网络可将场馆用户的注册资料等提供给该第三方。</font></li> 
			  <li><font size="2" face="宋体">遥点网络有权对整个用户数据库进行分析并对用户数据库进行商业上<WBR>的利用。</font></li> 
			</ol> 
			<p><font size="2" face="宋体"><b>六、免责声明</b></font></p> 
			<ol type="1"> 
			  <li><font size="2" face="宋体">场馆用户明确同意其使用我要锻炼服务所存在的风险将完全由其自己<WBR>承担；因其使用我要锻炼服务而产生的一切后果也由其自己承担，<WBR>遥点网络对用户不承担任何责任。</font></li> 
			  <li><font size="2" face="宋体">场馆用户明确同意其使用我要锻炼服务所存在的风险将完全由其自己<WBR>承担；因其使用我要锻炼服务而产生的一切后果也由其自己承担，<WBR>遥点网络对用户不承担任何责任。</font></li> 
			  <li><font size="2" face="宋体">举例说明：互联网是一个开放平台，<WBR>用户将个人照片上传到互联网上，有可能会被其他组织或个人复制、<WBR>转载、擅改或做其它非法用途。用户必须充分意识此类风险的存在，<WBR>并知晓如果发生此类事件，我要锻炼</font><font size="2" face="simsun">.com</font><font size="2" face="宋体">不承担任何责任。<WBR>如发生此类事件，用户可与我要锻炼联系以获得援助，<WBR>也可直接诉至相关部门。</font></li> 
			  <li><font size="2" face="宋体">遥点网络不保证服务一定能满足用户的要求，<WBR>也不保证服务不会中断，对服务的及时性、安全性、<WBR>准确性也都不作保证。</font></li> 
			  <li><font size="2" face="宋体">对于因不可抗力或遥点网络无法控制的原因造成的网络服务中断或其<WBR>他缺陷，遥点网络不承担任何责任。</font></li> 
			</ol> 
			<p><font size="2" face="宋体"><b>七、服务变更、中断或终止</b></font></p> 
			<ol type="1"> 
			  <li><font size="2" face="宋体">如因系统维护或升级的需要而需暂停网络服务，<WBR>遥点网络将尽可能事先通过我要锻炼重要页面进行通告。</font></li> 
			  <li><font size="2" face="宋体">如发生下列任何一种情形，<WBR>遥点网络有权随时中断或终止向场馆用户提供服务而无需通知场馆用<WBR>户：</font></li> 
			  <ol type="a"> 
			    <li><font size="2" face="宋体">场馆用户提供的基本资料不真实。</font></li> 
			    <li><font size="2" face="宋体">场馆用户违反本协议中规定的使用规则。</font></li> 
			    <li><font size="2" face="宋体">场馆用户注册后，连续三个月没有登陆账号的。</font></li> 
			    <li><font size="2" face="宋体">场馆用户在使用收费网络服务时未按规定向遥点网络支付相应的服务<WBR>费。</font></li> 
			    </ol> 
			  <li><font size="2" face="宋体">除前款所述情形外，<WBR>遥点网络同时保留在不事先通知场馆用户的情况下随时中断或终止部<WBR>分或全部服务的权利，<WBR>对于所有服务的中断或终止而造成的任何损失，<WBR>遥点网络无需对用户或任何第三方承担任何责任。</font></li> 
			</ol> 
			<p><font size="2" face="宋体"><b>八、违约赔偿</b></font></p> 
			<ol type="1"> 
			  <li><font size="2" face="宋体">场馆用户同意保障和维护遥点网络及其他网络用户的利益，<WBR>如因场馆用户违反有关法律、<WBR>法规或本协议项下的任何条款而给遥点网络或任何其他第三人造成损<WBR>失，用户同意承担由此造成的损害赔偿责任。</font></li> 
			</ol> 
			<p><font size="2" face="宋体"><b>九、修改协议</b></font></p> 
			<ol type="1"> 
			  <li><font size="2" face="宋体">遥点网络将可能不时的修改本协议的有关条款，<WBR>一旦条款内容发生变动，<WBR>遥点网络将通过我要锻炼相关的页面提示修改内容。</font></li> 
			  <li><font size="2" face="宋体">如果不同意遥点网络对服务条款所做的修改，<WBR>场馆用户有权停止使用我要锻炼服务。如果场馆用户继续使用服务，<WBR>则视为场馆用户接受服务条款的变动。</font></li> 
			</ol> 
			<p><font size="2" face="宋体"><b>十、法律管辖</b></font></p> 
			<ol type="1"> 
			  <li><font size="2" face="宋体">本协议的订立、执行和解释及争议的解决均应适用中国法律。</font></li> 
			  <li><font size="2" face="宋体">如双方就本协议内容或其执行发生任何争议，<WBR>双方应尽量友好协商解决；协商不成时，<WBR>任何一方均可向遥点网络所在地的人民法院提起诉讼。</font></li> 
			</ol> 
			<p><font size="2" face="宋体"><b>十一、通知和送达</b></font></p> 
			<ol type="1"> 
			  <li><font size="2" face="宋体">本协议项下所有的通知均可通过重要页面公告、<WBR>电子邮件或常规的信件传送等方式进行；<WBR>该等通知于发送之日视为已送达收件人。</font></li> 
			  <li><font size="2" face="宋体">场馆用户对于遥点网络的通知应当通过我要锻炼对外正式公布的通信<WBR>地址、电子邮件地址等联系信息进行送达。</font></li> 
			</ol> 
			<p><font size="2" face="宋体"><b>十二、其他规定</b></font></p> 
			<ol type="1"> 
			  <li><font size="2" face="宋体">本协议构成双方对本协议之约定事项及其他有关事宜的完整协议，<WBR>除本协议规定的之外，未赋予本协议各方其他权利。</font></li> 
			  <li><font size="2" face="宋体">如本协议中的任何条款无论因何种原因完全或部分无效或不具有执行<WBR>力，本协议的其余条款仍应有效并且有约束力。</font></li> 
			  <li><font size="2" face="宋体">本协议中的标题仅为方便而设，在解释本协议时应被忽略。</font></li> 
			</ol> 
			<p><font size="2" face="宋体"><b>十三、本协议解释权归遥点网络所有。</b></font></p> 
		</div> 
        <input type="button" id="agreesubmit" name="agreesubmit" value="我同意(10)" disabled="disabled"/>
        <input type="button" id="disagree" name="disagree" value="不同意"/>
        </c:if>
    </div>
  </div>
  <!--Footer-->
  <%@ include file="./footer.jsp" %>
</div>
</body>
</html>