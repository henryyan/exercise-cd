/*******************************************************************************
 * @功能说明:人员树类库
 * @作者:闫洪磊
 * @创建日期:20081104
 * @修改记录：
 * v1.0 20090103
 * 1、修正选择对象后头部提示不正确问题
 ******************************************************************************/

var Tree = function(options) {
	var self = this;
	var treeId = '.simpleTree';
	if ($.trim(options.treeFile) == '') {
		alert('人员树初始化失败，原因：没有树的引用页面!');
		return;
	} else if ($.trim(options.basePath) == ''
			|| $.trim(options.basePath).substring($.trim(options.basePath).length - 1) != '/') {
		alert('人员树初始化失败，原因：\n1、没有定义选择树根路径\n2、路径没有以/结尾!');
		return;
	}
	self.treeFile 	= options.treeFile;
	self.ajaxPath 	= options.ajaxPath;
	self.basePath 	= options.basePath;
	self.selectType	= options.selectType;
	self.onselected	= options.onselected;
	self.dialogBox	= null;// 对话框对象
	
	this.idComp = null;// ID组件
	this.nameComp = null;// 名称组件
	this.eventSrc = null;// 事件触发组件
	
	this.registeTree = function(treeOption) {
		this.idComp = treeOption['id'];
		this.nameComp = treeOption['name'];
		this.eventSrc = treeOption['eventSrc'];
		
		var treeTitle = '选择树';
		if (treeOption['treeId'] != undefined) {
			treeListName = treeOption['treeId'];
		}
		if (treeOption['title'] != undefined) {
			treeTitle = treeOption['title'];
		}
		
		// 设置对话框出现的位置
		var triggerId = document;// 对话框出现在哪个元素下方
		if(typeof self.eventSrc == 'string') {
			triggerId = self.eventSrc;
		} else if(typeof self.eventSrc == 'object') {
			triggerId = $(self.eventSrc).attr('id');
		}
		
		// 打开选择树
		var tempFilePath = self.basePath + self.treeFile;
		$.weeboxs.open(tempFilePath, {
			animate : true,
			contentType : 'ajax',
			width : 300,
			height : 350,
			position : 'element',
			trigger : triggerId,
			title : treeTitle,
			onopen : newTree,
			onclose : function() {
				dealResult('close');
			}
		});
	};
	
	/**
	 * 创建一颗新树
	 * @box 对话框对象
	 */
	function newTree(box) {
		self.dialogBox = box;
		$('#initLoadUrl').html('{url:' + self.basePath + self.ajaxPath + '}');
		log.info('第一次载入路径：' + $('#initLoadUrl').html());
		box.find(treeId)
		   .simpleTree({
				autoclose : true,
				basePath : self.basePath,
				drag : false,
				animate : true,
				afterClick : function(treeObj) {
					var activeObj = $('.active', treeObj);
					var selType = activeObj.attr('type');
					$('.treeTip', treeObj).empty();
					if(selType == self.selectType) {
						$('.treeTip').html("已选择：<font color='green' style='font-weight:bold'>" + activeObj.text() + "</font>");
					} else {
						$('.treeTip').text('请选择对象');
					}
				},
				afterDblClick : function() {
					dealResult('dbclick');
				},
				afterContextMenu : function() {
					alert('right click');
				}
			}
		);
		// 延迟自动打开前两层树
		setTimeout("$('.trigger').eq(0).trigger('click')",10);
		setTimeout("$('.trigger').eq(1).trigger('click')",20);
	}
	
	/**
	 * 内部函数处理选择的结果
	 */
	function dealResult(type) {
		// 获得已选用户
		var selTreeObj = $('.active[type=' + self.selectType +']', treeId);
		var idValue = $(selTreeObj).parent().attr('id');
		var nameValue = $(selTreeObj).html();
		var selType = $('.active').attr('type');// 选择的类型
		if(idValue != undefined && idValue != '' && selType == self.selectType) {
			$(self.idComp).val(idValue);
			// 这里用.focus().blur()解决选择后没有验证成功的问题
			$(self.nameComp).val(nameValue).blur();
			if($.isFunction(self.onselected)) {
				self.onselected(selTreeObj);
			}
			if(type == 'dbclick') {// 双击关闭
				self.dialogBox.close();
			}
		}
	}
}