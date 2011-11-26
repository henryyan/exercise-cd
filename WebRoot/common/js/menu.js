/*******************************************************************************
 * @功能说明：首页菜单控制函数库
 * @作者： 闫洪磊
 * @创建时间：2008-06-01
 * @版本记录： 1.0.0.20080604
 ******************************************************************************/
 
var common = new Common();

/**
 * 进入首页
 */
function goFirstPage() {
	$('#leftMenu').empty();
	window.location.reload();
}
/**
 * 退出系统
 */
function logout() {
	if (!confirm('确定退出系统吗？')) {
		return;
	}
	var url = "logout.do";
	$.ajax({
		type : "POST",
		url : url,
		success : function(msg) {
			window.location.href = "login.jsp";
		}
	});
}

/**
 * 修改密码
 */
function changePwd() {
	var eventSrc = $.browser.msie ? event.srcElement : document;
	$('#leftMenu').empty();
	$('.currentMenu').removeClass("currentMenu");
	$(eventSrc).addClass("currentMenu");
	$('#centerFrame').attr('src', 'changePwd.jsp');
}

/**
 * 修改左边的菜单显示后显示第一个链接内容
 */
function dealAction() {
	$('#leftMenu li:first a').trigger('onclick');
}

/**
 * 模拟鼠标单击链接功能
 * 
 * @param {}
 *            goUrl
 */
function clickLink(srcEle, goUrl) {
	$('#centerFrame').attr('src', goUrl);
	//alert($(srcEle).parent().html());
	$(srcEle).addClass('yl');
}

/**
 * 更新右边栏提示信息
 * 
 * @param {}
 *            moduleName 模块名称
 * @param {}
 *            txt 更新的内容
 */
function refreshRightTip(moduleName, txt) {
	$('#' + moduleName + 'RightTip').html(txt);
}

/**
 * 更新右边栏提示信息
 * 
 * @param {}
 *            moduleName 模块名称
 * @param {}
 *            txt 更新的内容
 */
function refreshRightTipForDel(moduleName) {
	// window.parent.iouserRightTip.innerHTML;
}

/**
 * 最大化/还原工作区域
 */
function maxWork(eventTag) {
	var eventSrc = eventTag.srcElement || eventTag.target;
	if ($(eventSrc).text() == '最大化') {
		$('#c-col').css('margin', '0 0px 0 0px');
		$(eventSrc).text('还  原');
	} else {
		$('#c-col').css('margin', '0 110px 0 110px');
		$(eventSrc).text('最大化');
	}
}
/*************************************************************************************** */
/*********************************左边栏菜单动作和右边提示*********************************** */
/*************************************************************************************** */


/**
 * 生成左边菜单
 * @param {} menus
 */
function createLeftMenu(menus, callback) {
	var eventSrc = $.browser.msie ? event.srcElement : document;
	$('#leftMenu').empty();
	$('.currentMenu').removeClass("currentMenu");
	$(eventSrc).addClass("currentMenu");
	$(menus).each(function(i, n){
		var actionMethod = "clickLink(this, '" + n['href'] + "');";
		var singleMenu = new StringBuffer();
		singleMenu.append("<li><span><a href='javascript:;' onclick=\"")
		.append(actionMethod)
		.append("\">")
		.append(n['name'])
		.append("</a></span></li><br/>");
		$('#leftMenu').append(singleMenu.toString());
	});
	dealAction();
	if($.isFunction(callback)) {
		callback();
	}
}

/**
 * 刷新右边栏提示信息
 */
function refreshIouser(ph) {
	var url = 'count.do';
	if (ph != undefined && ph != '') {
		url = ph + '/' + url;
	}
	$.get(url, function(msg) {
		if (!isNaN(msg)) {
			refreshRightTip('iouser', msg);
		}
	});
}

/******************债务人管理模块**************** */
function showIouser() {
	var menus = [
		{href : 'iouser/iouserList.do', name : '人员维护'},
		{href : 'iouser/groupList.do', name : '分组维护'}
	];
	createLeftMenu(menus);
}

/**********************物品类型/详细******************* */
function showGoods() {
	var menus = [
		{href : 'goods/type/goodsTypeList.do', name : '物品类型'},
		{href : 'goods/detail/goodsDetailList.do', name : '物品详细'}
	];
	createLeftMenu(menus);
}

/**********************物品类型/详细******************* */

function ajaxTalk() {
	$('#leftMenu').empty();
	var goUrl = 'talk/index.jsp';
	clickLink(goUrl);
}

/**********************借入款/物品********************/
function financeManager() {
	var menus = [
		{href : 'finance/listMoney.do?financeType=0', name : '借入管理'},
		{href : 'finance/listMoney.do?financeType=1', name : '借出管理'},
		{href : 'finance/listMoney.do?financeType=0', name : '进货管理'},
		{href : 'finance/listMoney.do?financeType=0', name : '出货管理'}
	];
	createLeftMenu(menus);
}

/**********************测试模块******************* */
function test() {
	var menus = [
		{href : 'test/test.html', name : '测试'},
		{href : 'test/block/', name : '组件锁定'},
		{href : 'test/tree/', name : '下拉树'},
		{href : 'test/my97/', name : 'My97日历'},
		{href : 'test/log/', name : '日志'},
		{href : 'test/weebox/demo/weebox.html', name : '对话框'},
		{href : 'test/upload/', name : '文件上传'}
	];
	createLeftMenu(menus);
}