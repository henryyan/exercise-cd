/*******************************************************************************
 * @功能说明：表单控制JavaScript函数库
 * @作者： 闫洪磊
 * @创建时间：2008-11-12
 * @修改记录：
 * 20081223
 * 1、删除记录时的弹出对话框焦点移到删除按钮
 ******************************************************************************/
function Form($_common, $_list) {
    var self = this;
    self.common = $_common;
    self.list = $_list;
    if (typeof $_common == 'undefined') 
        self.common = new Common();
    
    if (typeof $_list == 'undefined') 
        self.list = null;
    
    if (typeof regFormValidator != 'undefined') 
        this.validator = regFormValidator;//表单验证注册方法名
    self.editState = 'add';//新增或修改标志，默认为增加
    self.alertValidatorMsg = false;// 验证错误时是否alert信息
    self.editForm = ''; // 操作表单名称(新增、修改表单对象名称)
}

/***************************GET和SET方法--开始***************************/
/**设置验证表单完整性的方法*/
Form.prototype.setValidator = function(validator) {
    self.validator = validator;
};
Form.prototype.getValidator = function() {
    return self.validator;
};
/**设置新增与修改的标志add|update*/
Form.prototype.setEditState = function(editState) {
    self.editState = editState;
};
Form.prototype.getEditState = function() {
    return self.editState;
};
/**设置新增与修改的标志add|update*/
Form.prototype.setAlertValMsg = function(alertValidatorMsg) {
    self.alertValidatorMsg = alertValidatorMsg;
};
Form.prototype.getAlertValMsg = function() {
    return self.alertValidatorMsg;
};

/**设置操作表单名称*/
Form.prototype.setEditForm = function(editForm) {
    self.editForm = editForm;
};
Form.prototype.getEditForm = function(editForm) {
    return self.editForm;
};
/***************************GET和SET方法--结束***************************/

Form.prototype.initForm = function() {
    self.initReset($_list_list);//注册重置事件
};

/**
 * 初始化表单验证
 */
Form.prototype.initValidatorConfig = function() {
    $.formValidator.initConfig({
		alertmessage: this.alertValidatorMsg,
        onerror: function(msg) {
            if (this.alertValidatorMsg){
				if(self.common.dialog) {
	                self.common.dialog(msg, 'error');
				} else {
					alert(msg);
				}
			}
        }
    });
};

/**
 * 初始化新增/修改表单提交事件
 *
 * @param {}
 * 		formName  表单ID
 */
Form.prototype.initFormSubmit = function(formId) {
    // 表单提交设置
    var formSubmitOptions = {
        beforeSubmit: showRequest,
        success: showResponse,
        clearForm: false
    };
    
    // 设置监听表单在修改时使用
    this.setEditForm(formId);
    
    $('#' + formId).submit(function() {
        $(this).ajaxSubmit(formSubmitOptions);
        log.debug('提交……');
        return false;
    });
};

/**
 * 注册重置按钮，所以ID为resetBtn的元素
 * @param {Function} callback	重置完表单后的回调函数 
 */
Form.prototype.initReset = function(callback) {
	var self = this;
    $('*[id=resetBtn]').bind('click', function(){
		self.clearForm(callback);
	});
};

/**
 * 以隐藏域的形式自动创建页面中不存在的POJO属性
 * @param {Object} parentEle	隐藏域位置，可以是对象也可以是ID
 * @param {Object} json	JSON格式数据
 */
function createHidden(parentEle, json) {
	// 如果页面没有POJO的属性则自动创建一个隐藏域
	for (key in json) {
		var element = $('#' + key);
		if (element.length == 0) {
			var appendId = "<input type='hidden' name='" + key + "' id='" + key + "' value='" + json[key] + "'/>";
			if(typeof parentEle == 'object') {
				$(parentEle).append(appendId);
			} else {
				$('#' + parentEle).append(appendId);
			}
		}
	}
};

/**
 * 清除查询区域
 * @param {}
 * 		formName	表单名称	可选
 * 		callback 	回调函数	可选
 */
Form.prototype.clearForm = function(formName, callback) {
    var eventEle = null;
    //暂时不支持FF
    try {
        if (event != null) {
            eventEle = $(event.srcElement);
        }
    } 
    catch (e) {
    }
    var form;
    if (typeof formName == 'object') {
        form = $(this).parents('form');
    } else if (typeof formName == 'string') {
		form = $('#' + formName);
		if (!form) {
	        form = $('form[name=' + formName + ']');
		}
	}
	
    $("input,select,textarea", form).each(function() {
        var t = this.type, tag = this.tagName.toLowerCase();
        if (t == 'text' || t == 'password' || t == 'hidden' || tag == 'textarea') {
            this.value = '';
        } else if (t == 'checkbox' || t == 'radio') {
            //不重置列表、日志插件中的checkbox和radio
            if ($(this).attr('id') == 'chk' || $(this).attr('id') == 'bbVis') {
                return;
            }
            this.checked = false;
        } else if (tag == 'select') {
            this.selectedIndex = 0;
        }
    });
    $('#submit').attr('disabled', false);
    //判断是否执行表单验证重新注册
    if (false && eventEle != null && eventEle.attr('validaor') == 'true') {
        this.validator();//此方法在各个应用模块下
    }
    //执行回调函数
    if ($.isFunction(formName)) {
        formName(form);
    } else if ($.isFunction(callback)) {
        callback(form);
    }
};

/**
 * 提交前的动作：提交按钮的文字及禁用
 * @param {} submitBtnId	提交按钮ID
 */
Form.prototype.beforeSubmit = function(submitBtnId) {
    if (typeof submitBtnId == 'undefined') 
        submitBtnId = 'submit';
    
    $('#' + submitBtnId).attr('value', '正在提交……').attr('disabled', true);
};

/**
 * 更新列表数据
 * @param {} datas
 */
Form.prototype.updateData = function(datas) {
    if (!datas.objects) {
        var updateRow = $('#row [name=chk]:checkbox:checked').parent().parent().children();
        log.warn('没有设置增加数据的对象，增加值列表末尾！');
        datas.objects = updateRow;
    }
    
    // 更新数据
    if (!datas.data || datas.data.length == 0) 
        self.common.dialog('没有数据源，取消操作！');
    else 
        for (i in datas.data) {
            var updateRow = datas.data[i];// 单行数据
            var tempValue = '';// 最终数据
            if (updateRow.id) 
                tempValue = $('#' + updateRow.id).val();
            else if (updateRow.value) 
                tempValue = updateRow.value;
            else 
                continue;
            
            // 区分html和text数据类型
            if (updateRow.type && updateRow.type == 'html') 
                datas.objects.eq(updateRow.index).html(tempValue);
            else 
                datas.objects.eq(updateRow.index).text(tempValue);
            
            // 设置属性
            if (updateRow.attr) 
                datas.objects.attr(updateRow.attr);
        }
    
    // 更新完毕后显示提示信息
    self.common.showTip('记录修改成功!');
    
    // 回调，传入更新数据的对象TR
    if (datas.callback && $.isFunction(datas.callback)) 
        datas.callback(datas.objects);
};

/**
 * 修改一条记录
 *
 * @param {}
 *            formName
 * @param {}
 *            url
 */
Form.prototype.modify = function(url, callback) {
    log.debug('修改->加载数据……');
    var chkValue = self.list.getCheckbox();
    if (url == undefined || $.isFunction(url)) 
        url = "load.do";
    else if ($.isFunction(url)) 
        callback = url;
    
    if (!self.list.canEdit()) 
        return;
    this.clearForm();
    this.setEditState("update");// 修改状态为更新
    self.list.showHiddenEdit({
        id: chkValue,
        editState: this.getEditState()
    });
    self.common.setStatus('statusTip', 'load');
    
    // 计时载入时间
    var random = (Math.random() + '').substring(0, 4);
    var startTime = new Date().getTime();
    if (typeof log == 'object') 
        log.profile(random + '>开始load>' + common.getTimeWithMs());
    
    $.ajax({
        url: url,
        data: {
            id: chkValue
        },
        cache: false,
        dataType: "json",
        success: function(result) {
            $('#submit').val('保存修改');
            if (typeof log == 'object') 
                log.profile(random + '>结束load，耗时：' + (new Date().getTime() - startTime) + "(ms)");
            
            // 调用各个模块修改回调函数
            modifyCallback(result);
            
            log.debug('检查有没有主键:' + self.editForm + "," + $("#id").length);
            //增加隐藏域ID
            if ($("#id").length == 0) {
                var appendId = "<input type='hidden' name='id' id='id' value='" + chkValue + "'/>";
                $('#' + self.editForm).append(appendId);
                log.debug('增加隐藏主键：' + chkValue);
            } else {
                $('#id').val(chkValue);
                log.debug('已有隐藏主键，设置值为:' + chkValue);
            }
            
            // 执行修改方法回调
            if ($.isFunction(callback)) {
                callback(result);
            }
            common.removeStatus('statusTip');// 删除加载数据状态
        },
        error: function() {
            self.common.dialog('加载信息失败，请重试！', 'error');
            self.common.setStatus('statusTip', 'error');
        }
    });
};


/**
 * 删除一条记录
 * @param {} options
 */
Form.prototype.del = function(options) {
    var values = self.list.getCheckbox('chk');
    var defaults = {
        url: 'delete.do',
        msg: '<br/>即将执行删除操作！<br/><br/>确定要删除这<b>' + values.length + '</b>条记录吗？<br/>'
    };
    options = $.extend(defaults, options);
    
    // 没有选择记录时提示
    if (values.length == 0) {
        self.common.dialog('请先选择记录再删除！', 'warning');
        return;
    }
    
    //弹出对话框确定后删除记录
    var dialogObj = $.weeboxs.open(options.msg, {
        title: '确认删除？',
        animate: true,
        type: 'prompt',
        height: 90,
        width: 350,
        okBtnName: '删 除',
        cancelBtnName: '不删除',
        focus: '.dialog-ok',
        onok: doDelete
    });
    //内部函数
    function doDelete() {
        $.ajax({
            url: options.url,
            data: {
                ids: values
            },
            success: function(result) {
                self.list.subPageNo(values.length);
                $("#row [name=chk]:checkbox:checked").each(function() {
                    dialogObj.close();
                    var tmpObj = $(this).parent().parent();
                    $(tmpObj).remove();
                    List.sortList();
                });
                self.common.showTip('已经成功删除了' + values.length + '条记录！');
                if (typeof log == 'object') 
                    log.info('删除了' + values.length + '条记录:' + values);
            },
            error: function(info) {
                self.common.dialog('删除失败!原因如下：<br/>' + info, 'error');
                self.common.setStatus('statusTip', 'error');
            }
        });
    }
};
