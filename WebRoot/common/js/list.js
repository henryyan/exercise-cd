/*******************************************************************************
 * @功能说明：列表控制JavaScript函数库
 * @作者： 闫洪磊
 * @创建时间：2008-06-02
 ******************************************************************************/

/**
 * @列表控制JavaScript函数库 List对象构造方法
 * @param {}
 *            cm Common对象，没有传值将new一个Common对象
 */
function List($_common) {
	var self = this;
	self.common = $_common;
	if (typeof $_common == 'undefined')
		self.common = new Common();

	self.chkState = true;// 全选状态
}

/** *************************GET和SET方法--开始************************** */
List.prototype.getChkState = function() {
	return this.chkState;
};
/** *************************GET和SET方法--结束************************** */

/**
 * 初始化列表
 */
List.prototype.initList = function() {
	this.moveAndleave();// 实现斑马效果
	//this.regSelectAll();// 注册全选按钮
	//this.regActive();// 注册选择行事件
	//this.initQueryArea(this);// 设置查询区域
	$('#cancel').unbind('click').bind('click', this.cancel);// 取消事件
};

/**
 * 初始化查询区域
 */
List.prototype.initQueryArea = function() {
	$('#queryImg').bind('click', this.queryArea);
	this.queryArea('init');
};

/**
 * 注册斑马效果
 * 
 * @param {}
 *            obj 数据列表的table的ID 默认为row
 */
List.moveAndleave = List.prototype.moveAndleave = function(obj) {
	var ty = this;
	if (obj == undefined || obj == '')
		obj = 'row';

	$('#' + obj + ' tr').hover(function() {
				$(this).addClass('over');
			}, function() {
				$(this).removeClass('over');
			});
};

/**
 * 注册全选与撤销全选操作
 */
List.prototype.regSelectAll = function() {
	$('#selAll').bind("click", function() {
		if (list.chkState) {
			$('#row :checkbox:not(:eq(0))').attr('checked', true).parent().parent().addClass('row_active');
			list.chkState = false;
			$(this).attr("title", "撤销全选记录");
		} else {
			$('#row :checkbox:not(:eq(0))').attr('checked', false).parent().parent().removeClass('row_active');
			list.chkState = true;
			$(this).attr("title", "全选记录");
		}
	});
};

/**
 * 是否可以修改记录 当不可修改时弹出对话框
 * 
 * @return {Boolean} 可以修改 true 不可以 false
 */
List.prototype.canEdit = function() {
	var id = this.getCheckbox();
	if (id.length == 0) {
		self.common.dialog('请先选择记录再修改！', 'warning');
		return false;
	} else if (id.length > 1) {
		self.common.dialog('只能修改一条记录，请重新选择!', 'warning');
		return false;
	}
	return true;
}

/**
 * 注册单击一行事件 单击后选中一行
 */
List.prototype.regActive = function() {
	// 如果已经注册了事件则要先取消绑定在执行绑定方法
	$('#row tr td').each(function() {
		if ($(this).attr('class') != 'noclick')
			$(this).unbind("click").bind('click', 
				function() {
					list.selectColumn(this);
				}
			);
	});
};

/**
 * 选择一行记录 radio只能选中一行 checkbox可以选多行
 * 
 * @param {}
 *            obj
 */
List.prototype.selectColumn = function(obj) {
	var tdObj = $(obj).parent().children().get(0);
	var chkObj = $(tdObj).children().get(0);
	if ($(obj).parent().hasClass('row_active')) {
		$(chkObj).attr("checked", false);
		$(obj).parent().removeClass('row_active');
	} else {
		if ($(chkObj).attr("type") == 'radio')
			$('.row_active').removeClass('row_active');

		$(chkObj).attr("checked", true);
		$(obj).parent().addClass('row_active');
	}
};

/**
 * 获得checkbox或者radio的值
 * 
 * @param {}
 *            name
 * @return {}
 */
List.getCheckbox = List.prototype.getCheckbox = function(name) {
	if (name == undefined || name == '')
		name = 'chk';

	var val = [];
	$("#row [name=" + name + "]:checkbox:checked").each(function() {
				val.push($(this).val());
			}
	);
	return val;
};

/**
 * 返回组装的多选、单选对象生成代码
 * options 
 */
List.createChkEle = List.prototype.createChkEle = function(options) {
	var defaults = {
		type: 'checkbox',
		idLabel: 'chk',
		value: ''
	};
	options = $.extend(defaults, options);
	
	return "<input type='" + options.type + "' "
			+ "name='" + options.idLabel + "' id='" + options.idLabel 
			+ "' value='" + options.value + "' />";
};

/**
 * 检查列表是否为空
 * 
 * @return {Boolean}
 */
List.isEmptyList = List.prototype.isEmptyList = function() {
	if ($('#row #chk').length == 0)
		return true;
	return false;
};

/**
 * 获得列表中最后一个TR的CLASSNAME
 * 
 * @return {String} 返回奇偶行CLASSNAME
 */
List.prototype.getLastClass = function() {
	var className = $('.row tr:last').attr('class');
	if (className == 'odd') {
		return 'even';
	} else {
		return 'odd';
	}
};

/**
 * 显示/隐藏编辑区域
 * 
 * @param {}
 *            options
 */
List.prototype.showHiddenEdit = function(options) {
	// 如果还没有列表直接返回
	if (List.isEmptyList()) {
		$('#edit').slideDown(self.common.showHiddenSpeed);
		return;
	}
	if ($('#edit').css('display') == 'none') {
		if (options.editState == 'update') {
			$('#row :checkbox[value!=' + options.id + ']').parent().parent().hide();
			$('.row tr:first th').parent().show();
			$('#edit').slideDown(self.common.showHiddenSpeed);
		} else if (options.editState == 'add') {
			$('#row tr').slice(2, 11).hide();
			$('#edit').slideDown(self.common.showHiddenSpeed);
		}
	} else {
		if (options.sign == 'hide') {
			$('#row tr').show();
			$('#edit').hide('slow');
		}
	}
	
	// 如果是IE通过JS设置编辑块的显示样式
	if ($.browser.msie)
		$('#formTable tr:not(:eq(0)) td:nth-child(1)').css('text-align', 'right');
		
	if ($.isFunction(options['callback'])) {
		options['callback']();
	}
};

/**
 * 取消新增/修改操作 隐藏编辑区域
 */
List.prototype.cancel = function() {
	if ($('#edit').css('display') == 'block') {
		$('#edit').hide('slow');
		$('#row tr').show();
	}
};

/**
 * 切换显示/隐藏查询区域
 * 
 * @param {}
 *            type
 */
List.prototype.queryArea = function(type) {
	// 查询区域中不为空的组件个数
	var notEmpty = $("#query :text[value!='']").length;
	if ($('#query').css('display') == 'block') {
		$('#query').slideToggle('fast');
	} else if (notEmpty > 0
			|| (type != 'init' && $('#query').css('display') == 'none')) {
		// 不为空的组件个数多于一个就显示
		$('#query').slideToggle('fast');
	}
}

List.sortList = function() {
	var trs = $('#row tr').slice(1, $('#row tr').length);
	$.each(trs, function(i, v){
		//log.debug(i + " " + $(v).find('td:eq(1)').text());
		if(i % 2 == 0){
			if(!$(v).hasClass('odd')) {
				$(v).removeClass('even').addClass('odd');
			}
		} else {
			if(!$(v).hasClass('even')) {
				$(v).removeClass('odd').addClass('even');
			}
		}
		$(v).find('td:eq(1)').text(i + 1);
	});
};

/**
 * 记录总数增加num条
 * 
 * @param {}
 *            num
 */
List.prototype.addPageNo = function(num) {
	$('#pageno').text(parseInt($('#pageno').text()) + num);
};

/**
 * 记录总数减少num条
 * 
 * @param {}
 *            num
 */
List.prototype.subPageNo = function(num) {
	var orgNum = parseInt($('#pageno').text());
	if (orgNum == 1 || orgNum == num)
		$('#pagebanner,.pagebanner').text('没有找到记录');
	else
		$('#pageno').text(parseInt($('#pageno').text()) - num);
};