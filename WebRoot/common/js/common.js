/*******************************************************************************
 * @功能说明:公共JS类、函数库
 * @作者:闫洪磊
 * @版本修改记录: 一、20081221: 1、获得当前日期方法支持GOOGLE浏览器-chrome
 ******************************************************************************/

$(function() {

	// 全局ajax设置
	$.ajaxSetup({
		cache : false,
		global : true,
		complete: function(req, status) {
			var reqText = req.responseText;
			if(reqText) {
				if(reqText == 'login'){
					var topWinObj = getTopWin();
					topWinObj.location.href = getContextPath() + '/index.jsp';
				} else if (reqText == 'w.verify') {
					var topWinObj = getTopWin();
					topWinObj.location.href = getContextPath() + '/wizard/verify.html';
				}
			}

		},
		error: function(req, status) {
			var reqText = req.responseText;
			if(reqText == 'login') {
				return;
			}
			if(reqText == 'error') {
				alert('操作失败！');
			} else if (reqText != ''){
				alert(reqText);
			}
		}
	});
	Common.initInputEle();

	/*
	 * 初始化验证插件
	 */
	if ($.validator) {
		addValidatorMethod();
	}
});

/**
 * 得到应用名，如/exercise
 */
function getContextPath() {
	var url = location.pathname;
	var contextPath = url.substr(0, url.indexOf('/', 1));
	return contextPath;
}

/**
 * 获得最上层的window对象
 */
function getTopWin() {
	if(parent) {
		var tempParent = parent;
		while(true) {
			if(tempParent.parent) {
				if(tempParent.parent == tempParent) {
					break;
				}
				tempParent = tempParent.parent;
			} else {
				break;
			}
		}
		return tempParent;
	} else {
		return window;
	}
}

/**
 * 检测当前浏览器是否为IE
 */
function isIE() {
	var _uaMatch = $.uaMatch(navigator.userAgent);
	var _browser = _uaMatch.browser;
	if (_browser == 'msie') {
		return true;
	} else {
		return false;
	}
}

/**
 * 检测当前浏览器是否为Mozilla
 */
function isMozila() {
	var _uaMatch = $.uaMatch(navigator.userAgent);
	var _browser = _uaMatch.browser;
	if (_browser == 'mozilla') {
		return true;
	} else {
		return false;
	}
}

/**
 * 检测当前浏览器是否为CHROME
 */
function isChrome() {
	var userAgent = navigator.userAgent.toLowerCase();
	var chrome = /chrome/.test(userAgent);
	return chrome;
}

/**
 * 检测当前浏览器是否为safari
 */
function isSafari() {
	var userAgent = navigator.userAgent.toLowerCase();
	var chrome = /safari/.test(userAgent);
	return chrome;
}

var log;// 日志对象

function Common() {
	this.initCommon();
}

/**
 * 初始化全局COMMON对象
 */
Common.prototype.initCommon = function() {
	this.showHiddenSpeed = "normal";// 切换显示与隐藏的速度
	this.showDialogStyle = "slideDown";// 显示对话框的速度
	this.initLog();
	Common.initInputEle();
};

/**
 * 初始化日志对象
 */
Common.prototype.initLog = function() {
	if (typeof blacklog == 'undefined') {
		log = {
			toggle : function() {
			},
			move : function() {
			},
			resize : function() {
			},
			clear : function() {
			},
			debug : function() {
			},
			info : function() {
			},
			warn : function() {
			},
			error : function() {
			},
			profile : function() {
			}
		};
	} else {
		log = blacklog;
	}
};

/**
 * 1、注册文本框鼠标移动、获得焦点事件 2、用不同的颜色标识当前的状态
 */
Common.initInputEle = Common.prototype.initInputEle = function() {
	// 注册鼠标、光标样式
	/*$(':text,:password,textarea').bind('mouseover', function() {
				$(this).css('border-color', '#9ecc00');
			}).bind('mouseout', function() {
				$(this).css('border-color', '');
			}).bind('focus', function() {
				$(this).css('background', '#ffffcc');
			}).bind('blur', function() {
				$(this).css('background', '#E8FDFF');
			});*/

	// 必填项
	$(":text[noempty=true],:radio[noempty=true],:checkbox[noempty=true]"
	+ ",textarea[noempty=true],label[noempty=true],select[noempty=true]").each(function(v) {
		if ($(this).parent().find('span[id=nel]').length == 0)
			$(this).after("<span id='nel' title='此为必填项'>&nbsp;<font color='red'>*</font>&nbsp;</span>");
	});

	$(".noempty").each(function(v) {
		if ($(this).parent().find('span[id=nel]').length == 0)
			$(this).after("<span id='nel' title='此为必填项'>&nbsp;<font color='red'>*</font>&nbsp;</span>");
	});
};

/**
 * 获得当前的日期
 *
 * @return {}
 */
Common.getCurrentDate = Common.prototype.getCurrentDate = function() {
	var currentDate = new Date();
	// 因IE的年为2008和FF为108，判断
	var currentYear = currentDate.getYear();
	if ($.browser.mozilla || isChrome()) {
		currentYear += 1900;
	}

	var currentDateStr = currentYear + '-' + (currentDate.getMonth() + 1) + '-'
			+ currentDate.getDate();
	return currentDateStr;
};

/**
 * 获得当前的日期+时间
 *
 * @return {}
 */
Common.getCurrentDateTime = Common.prototype.getCurrentDateTime = function() {
	var userAgent = navigator.userAgent.toLowerCase();
	// 判断是否为google的浏览器
	var chrome = /chrome/.test(userAgent);
	var currentDate = new Date();
	// 因IE的年为2008和FF为108，判断
	var currentYear = currentDate.getYear();
	if ($.browser.mozilla || chrome)
		currentYear += 1900;

	var currentDateStr = currentYear + '-' + (currentDate.getMonth() + 1) + '-'
			+ currentDate.getDate() + ' ' + currentDate.getHours() + ':'
			+ currentDate.getMinutes() + ':' + currentDate.getSeconds();
	return currentDateStr;
};

/**
 * 获得当前的日期、时间、毫秒
 *
 * @return {}
 */
Common.getTimeWithMs = Common.prototype.getTimeWithMs = function() {
	var currentDate = new Date();
	// 因IE的年为2008和FF为108，判断
	var currentYear = currentDate.getYear();
	var currentDateStr = currentDate.getHours() + ':'
			+ currentDate.getMinutes() + ':' + currentDate.getSeconds() + "."
			+ currentDate.getMilliseconds();
	return currentDateStr;
};

/**
 * 显示提示信息
 *
 * @param tipTxt
 *            提示信息内容
 * @param comName
 *            显示提示信息的组件内容
 */
Common.showTip = Common.prototype.showTip = function(tipTxt, comName) {
	if (comName == undefined || comName == '')
		comName = 'resultTip';

	$('#' + comName).html(tipTxt);
	$('#' + comName).fadeIn("slow");
	setTimeout("$('#" + comName + "').slideUp('slow')", 5000);
};

/**
 * 弹出对话框
 *
 * @param {}
 *            msg
 */
Common.dialog = Common.prototype.dialog = function(msg, styleType) {
	if (typeof styleType == 'undefined' || $.trim(styleType) == '')
		styleType = 'prompt';

	return $.weeboxs.open(msg, {
				title : '请确认',
				animate : true,
				focus : '.dialog-ok',
				type : styleType,
				height : 30
			});
};

/**
 * 按照valLength指定的长度返回字符串 如果字符串长度超过valLength再根据withOmit判断是否需要省略号
 *
 * @param {}
 *            orgValue
 * @param {}
 *            valLength
 * @param {}
 *            withOmit
 */
Common.getValue = Common.prototype.getValue = function(orgValue, valLength,
		withOmit) {
	if (typeof orgValue != 'undefined') {
		if (orgValue.length > valLength) {
			var tempValue = orgValue.substring(0, valLength);
			if (withOmit)
				return tempValue + "……";
			else
				return tempValue;
		} else {
			return orgValue;
		}
	}
};

/**
 * 获得HTML文件的请求参数
 *
 * @param {}
 *            sArgName 参数名称
 * @param {}
 *            sHref 链接地址，默认为请求页面的路径
 */
Common.getParam = Common.prototype.getParam = function(sArgName, sHref) {
	if (sHref == undefined || sHref == '')
		sHref = window.location.href;

	var args = sHref.split("?");
	var retval = "";

	/* 参数为空 */
	if (args[0] == sHref)
		return retval; /* 无需做任何处理 */

	var str = args[1];
	args = str.split("&");
	for (var i = 0; i < args.length; i++) {
		str = args[i];
		var arg = str.split("=");
		if (arg.length <= 1)
			continue;
		if (arg[0] == sArgName)
			retval = arg[1];
	}
	return retval;
};

/**
 * 利用正则表达式替换字符
 *
 * @param source
 *            可以是组件对象或要被替换的字符串
 * @param oldString
 *            要被替换的字符（串）
 * @param newString
 *            更换被替换的字符（串）
 * @param replaceArea
 *            替换范围，默认为全局(g)
 */
Common.repalceValue = Common.prototype.repalceValue = function(source,
		oldString, newString, replaceArea) {
	if (replaceArea == null || replaceArea == undefined || replaceArea == '')
		replaceArea = "g";// 默认为全局

	var reg = RegExp(oldString, replaceArea);
	if (typeof source == 'undefined' || source == '')
		return;
	return source.replace(reg, newString);
};

/**
 * 设置ajax请求状态
 *
 * @param {}
 *            tipId
 * @param {}
 *            status
 */
Common.setStatus = Common.prototype.setStatus = function(tipId, status, className) {
	if (status == 'load') {
		$('#' + tipId).removeClass('onError');
		$('#' + tipId).html('正在加载数据，请稍候……').addClass('onLoad');
	} else if (status == 'error') {
		$('#' + tipId).removeClass('onLoad');
		$('#' + tipId).html('加载数据错误，请刷新或稍后再试！').addClass('onError');
	} else {
		$('#' + tipId).removeClass();
		$('#' + tipId).html(status).addClass(className);
	}
};

/**
 * 清除加载数据状态
 *
 * @param {}
 *            tipId
 */
Common.removeStatus = Common.prototype.removeStatus = function(tipId) {
	$('#' + tipId).empty().attr('class', '');
};

/**
 * 高亮显示
 *
 * @param {}
 *            names 数组形式的组件名称
 */
Common.prototype.showHightLigth = function(names) {
	for (var i = 0; i < names.length; i++) {
		var hightLightChars = $(':input[name=' + names[i] + ']').val().trim().split(' ');
		for (var index = 0; index < hightLightChars.length; index++) {
			var tmp = hightLightChars[index];
			if (tmp != '') {
				$('td,div').each(function() {
							$.highlight(this, tmp);
						});
			}
		}
	}
};

/**
 * 加载文件内容
 *
 * @param {}
 *            options htmlele 组件ID filePath 文件路径 callback 回调函数
 */
Common.loadHtml = Common.prototype.loadHtml = function(options) {
	if ($.trim($(options.htmlele).html()).length == 0)
		// 加载HTML文件
		$(options.htmlele).load(options.filePath, function() {
					if ($.isFunction(options.callback))
						options.callback($(options.htmlele));
					return;
				});
	else // 直接回调
		if ($.isFunction(options.callback))
			options.callback($(options.htmlele));
};

/**
 * 加载javascript文件
 *
 * @param {}
 *            options cookien 标识是否已加载过JS文件的cookie名字 scripts 数组形式的文件路径 callback
 *            回调函数
 */
Common.loadScript = Common.prototype.loadScript = function(options) {
	// 页面关闭或者刷新时把cookie删除
	window.onunload = function() {
		this.deleteCookie(options.cookien);
	};
	if (common.getCookie(options.cookien) == null) {
		for (i in options.scripts) {
			$.getScript(options.scripts[i]);
		}
		common.saveCookie(options.cookien, true);
	}
	if ($.isFunction(options.callback))
		options.callback();
};

/**
 * 加载HTML和JAVASCRIPT文件
 *
 * @param {}
 *            options htmlele 组件ID filePath 文件路径 cookien 标识是否已加载过JS文件的cookie名字
 *            scripts 数组形式的文件路径 callback 回调函数
 */
Common.prototype.loadHtmlAndScript = function(options) {
	// 先加载HTML再加载SCRIPT
	if ($.trim($(options.htmlele).html()) == '') {
		Common.loadHtml({
			htmlele : options.htmlele,
			filePath : options.filePath,
			callback : function() {
				Common.loadScript({
					scripts : options.scripts,
					cookien : options.cookien,
					callback : function() {
						if ($.isFunction(options.callback)) {
							options.callback();
						}
					}
				});
			}
		});
	} else {
		Common.loadScript({
			scripts : options.scripts,
			cookien : options.cookien,
			callback : function() {
				if ($.isFunction(options.callback)) {
					options.callback();
				}
			}
		});
	}
};

/**
 * 四舍五入
 *
 * @param {}
 *            dight 原始数字
 * @param {}
 *            how 保留小数点位数
 * @return {}
 */
Common.round = Common.prototype.round = function(dight, how) {
	dight = Math.round(dight * Math.pow(10, how)) / Math.pow(10, how);
	return dight;
};

/**
 * 使用名称参数取得Cookie值,
 *
 * @param {}
 *            name
 * @return {} null表示Cookie不存在
 */
Common.getCookie = Common.prototype.getCookie = function(name) {
	var strCookies = document.cookie;
	var cookieName = name + "="; // Cookie名称
	var valueBegin, valueEnd, value;

	// 寻找是否有此Cookie名称
	valueBegin = strCookies.indexOf(cookieName);
	if (valueBegin == -1)
		return null; // 没有此Cookie

	// 取得值的结尾位置
	valueEnd = strCookies.indexOf(";", valueBegin);
	if (valueEnd == -1)
		valueEnd = strCookies.length; // 最後一个Cookie

	// 取得Cookie值
	value = strCookies.substring(valueBegin + cookieName.length, valueEnd);
	return value;
};

/**
 *
 * @param {}
 *            name
 * @param {}
 *            value
 * @param {}
 *            expires
 * @param {}
 *            path
 * @param {}
 *            domain
 * @param {}
 *            secure
 */
Common.saveCookie = Common.prototype.saveCookie = function(name, value,
		expires, path, domain, secure) { // 保存Cookie
	var strCookie = name + "=" + value;
	if (expires) { // 计算Cookie的期限, 参数为天数
		var curTime = new Date();
		curTime.setTime(curTime.getTime() + expires * 24 * 60 * 60 * 1000);
		strCookie += "; expires=" + curTime.toGMTString();
	}
	// Cookie的路径
	strCookie += (path) ? "; path=" + path : "";
	// Cookie的Domain
	strCookie += (domain) ? "; domain=" + domain : "";
	// 是否需要保密传送,为一个布尔值
	strCookie += (secure) ? "; secure" : "";
	document.cookie = strCookie;
};

/**
 * 检查Cookie是否存在
 *
 * @param {}
 *            name cookie名字
 * @return {Boolean}
 */
Common.checkCookieExist = Common.prototype.checkCookieExist = function(name) {
	if (Common.getCookie(name))
		return true;
	else
		return false;
};

/**
 * 删除Cookie
 *
 * @param {}
 *            name
 * @param {}
 *            path
 * @param {}
 *            domain
 */
Common.deleteCookie = Common.prototype.deleteCookie = function(name, path,
		domain) {
	var strCookie;
	if (this.checkCookieExist(name)) { // 设置Cookie的期限为己过期
		strCookie = name + "=";
		strCookie += (path) ? "; path=" + path : "";
		strCookie += (domain) ? "; domain=" + domain : "";
		strCookie += "; expires=Thu, 01-Jan-70 00:00:01 GMT";
		document.cookie = strCookie;
	}
};

/**
 * 日期增加年数或月数或天数
 * @param {String} BaseDate	要增加的日期
 * @param {Object} interval	增加数量
 * @param {Object} DatePart	增加哪一部分
 */
Common.prototype.dateAdd = function(BaseDate, interval, DatePart, returnType) {
    var dateObj;
	if(typeof BaseDate == 'object') {
		dateObj = BaseDate;
	} else {
		var strDs = BaseDate.split('-');
		var year = parseInt(strDs[0]);
		var month = parseInt(strDs[1]);
		var date = parseInt(strDs[2]);
		dateObj = new Date(year, month, date);
	}
	returnType = returnType || 'string';
    var millisecond = 1;
    var second = millisecond * 1000;
    var minute = second * 60;
    var hour = minute * 60;
    var day = hour * 24;
    var year = day * 365;

    var newDate;
    var dVal = new Date(dateObj);
    var dVal = dVal.valueOf();
    switch (DatePart) {
        case "ms":
            newDate = new Date(dVal + millisecond * interval);
            break;
        case "s":
            newDate = new Date(dVal + second * interval);
            break;
        case "mi":
            newDate = new Date(dVal + minute * interval);
            break;
        case "h":
            newDate = new Date(dVal + hour * interval);
            break;
        case "d":
            newDate = new Date(dVal + day * interval);
            break;
        case "y":
            newDate = new Date(dVal + year * interval);
            break;
        default:
            return escape("日期格式不对");
    }
    newDate = new Date(newDate);
	if (returnType == 'string') {
	    return newDate.getFullYear() + "-" + (newDate.getMonth() + 1) + "-" + newDate.getDate();
	} else if (returnType == 'date') {
		return newDate;
	}
};

/**
 * 获取指定年月的最后一天
 */
Common.prototype.getMonthLastDay = function(year, month) {
	var monthNextFirstDay = new Date(year, month + 1, 1);   
	var monthLastDay = new Date(monthNextFirstDay - 86400000);
	return monthLastDay;
}

// 计算两个日期的间隔天数
Common.prototype.dateDiff = function(date1, date2){
	var iDays = parseInt((date1 - date2) / 1000 / 60 / 60 /24); //把相差的毫秒数转换为天数
	log.debug('相差=' + iDays);
	return iDays;
}

Common.prototype.createHourSelect = function(selectId, defaultValue) {
	var hours = new StringBuffer();
	var tempValue = "";
	for(var i = 0; i < 24; i++) {
		if(i < 10) {
			tempValue = "0" + i;
		} else {
			tempValue = i;
		}
		hours.append("<option value='" + tempValue + "'" + (defaultValue == tempValue ? " selected" : "") + ">" + tempValue + "</option>");
	}
	$(selectId).append(hours.toString());
};

Common.prototype.createMinuteSelect = function(selectId, defaultValue) {
	var hours = new StringBuffer();
	var tempValue = "";
	for(var i = 0; i < 60; i++) {
		if(i < 10) {
			tempValue = "0" + i;
		} else {
			tempValue = i;
		}
		hours.append("<option value='" + tempValue + "'" + (defaultValue == tempValue ? " selected" : "") + ">" + tempValue + "</option>");
	}
	$(selectId).append(hours.toString());
};

/** *****************自定义类-开始********************* */
function StringBuffer() {
	this._strings_ = new Array();
}

StringBuffer.prototype.append = function(str) {
	this._strings_.push(str);
	return this;
};

StringBuffer.prototype.toString = function() {
	return this._strings_.join("").trim();
};

function Map() {
    var struct = function(key, value) {
        this.key = key;
        this.value = value;
    };

    var put = function(key, value) {
        for (var i = 0; i < this.arr.length; i++) {
            if (this.arr[i].key === key) {
                this.arr[i].value = value;
                return;
            }
        }
        this.arr[this.arr.length] = new struct(key, value);
		this._keys[this._keys.length] = key;
    };

    var get = function(key) {
        for (var i = 0; i < this.arr.length; i++) {
            if (this.arr[i].key === key) {
                return this.arr[i].value;
            }
        }
        return null;
    };

    var remove = function(key) {
        var v;
        for (var i = 0; i < this.arr.length; i++) {
            v = this.arr.pop();
            if (v.key === key) {
                continue;
            }
            this.arr.unshift(v);
			this._keys.unshift(v);
        }
    };

    var size = function() {
        return this.arr.length;
    };

	var keys = function() {
        return this._keys;
    };

    var isEmpty = function() {
        return this.arr.length <= 0;
    };

    this.arr = new Array();
	this._keys = new Array();
	this.keys = keys;
    this.get = get;
    this.put = put;
    this.remove = remove;
    this.size = size;
    this.isEmpty = isEmpty;
}

/** *****************自定义类-结束********************* */

/** *****************原始类扩展-开始********************* */

/**
 * 去掉开头、结尾的空格
 *
 * @return {}
 */
String.prototype.trim = function() {
	return this.replace(/(^\s+)|\s+$/g, "");
};

String.prototype.toJson = function() {
	return eval('(' + this + ')');
};

/**
 * 输出2010-02-05格式的日期字符串
 *
 * @return {}
 */
Date.prototype.toDate = function() {
	return this.getYear() + "-"
			+ (this.getMonth() < 10 ? "0" + this.getMonth() : this.getMonth())
			+ "-" + (this.getDate() < 10 ? "0" + this.getDate() : this.getDate());
};
/**
 * 将日期转换为字符串格式
 * @param {Object} nature	是否转换为自然月，会把原来的月份+1
 */
Date.prototype.toDateStr = function(nature) {
	var month = this.getMonth();
	if (nature) {
		month += 1;
	}
	return ($.browser.mozilla || isChrome() ? (this.getYear() + 1900) : this.getYear()) + "-"
			+ (month < 10 ? "0" + month : month)
			+ "-" + (this.getDate() < 10 ? "0" + this.getDate() : this.getDate());
};

//+---------------------------------------------------
//| 日期计算
//+---------------------------------------------------
Date.prototype.DateAdd = function(strInterval, Number) {
    var dtTmp = this;
    switch (strInterval) {
        case 's' :return new Date(Date.parse(dtTmp) + (1000 * Number));
        case 'n' :return new Date(Date.parse(dtTmp) + (60000 * Number));
        case 'h' :return new Date(Date.parse(dtTmp) + (3600000 * Number));
        case 'd' :return new Date(Date.parse(dtTmp) + (86400000 * Number));
        case 'w' :return new Date(Date.parse(dtTmp) + ((86400000 * 7) * Number));
        case 'q' :return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + Number*3, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());
        case 'm' :return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + Number, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());
        case 'y' :return new Date((dtTmp.getFullYear() + Number), dtTmp.getMonth(), dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());
    }
};
/** *****************原始类扩展-结束********************* */

/** *****************jQuery插件扩展-结束********************* */

/**
 * @插件名称	jQuery.validator
 * @功能		公共验证方法
 */
function addValidatorMethod() {
	
	var mpReg = /^1(30|31|32|33|34|35|36|37|38|39|45|47|50|51|52|55|56|57|58|59|80|85|86|87|88|89|31|31|31|31|31)\d{8}$/;
	
	/*
	 * 设置默认处理方式
	 */
	$.validator.setDefaults({
		errorPlacement: function(error, element) {
            if (element.is(":radio")) {
                error.appendTo(element.parent().next().next());
            } else if (element.is(":checkbox")) {
                error.appendTo(element.next());
            } else {
                error.appendTo(element.parent().next());
            }
        },
		success: function(label) {
            label.html("&nbsp;").addClass("checked");
        }
	});

	// 邮政编码验证
    $.validator.addMethod("isZipCode", function(value, element) {
        var tel = /^[0-9]{6}$/;
        return this.optional(element) || (tel.test(value));
    }, "请正确填写邮政编码");

    // 手机号码验证
    $.validator.addMethod("isMobile", function(value, element) {
        var length = value.length;
        return this.optional(element) || (length == 11 && mpReg.test(value));
    }, "请正确填写手机号码");
}
/** *****************jQuery插件扩展-结束********************* */
