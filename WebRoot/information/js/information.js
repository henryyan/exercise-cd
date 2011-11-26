var common = new Common();
var form = new Form(common);

$(function(){
	
	// 加载数据列表
	loadListDatas();
	
	form.initFormSubmit('infoForm');//初始化表单提交
	
	// 新信息
	$('#addBtn').click(function(){
		form.clearForm('infoForm');
		$.nyroModalManual({
	        title: '新信息',
			minHeight: 290,
	        url: '#formTemplate'
	    });
	});
	
	// 点击TITLE切换显示、隐藏效果
	$('.title').live('click', function(){
		var infoId = $(this).parents('.singleInfo').data('idv');
		$('#info-' + infoId + ' .infoContent').slideToggle('fast');
	});
	
	// 关闭遮罩
	$('#close').bind('click', $.nyroModalRemove);
	
	// 修改、删除
	updateAndDelete();
	
	// 刷新数据
	$('#refreshInfo').click(function(){
		loadListDatas();
	});
	
	// 显示、隐藏内容
	$('#showContent').click(function(){
		$('.infoContent').show('normal');
	});
	
	$('#hideContent').click(function(){
		$('.infoContent').hide('normal');
	});
	
	// 搜索
	$('#searchInfo').click(function(){
		$('#pageBar').data('_search', 'true');
		loadListDatas();
	});
	
});

/**
 * 分页控制
 */
function pageControl() {
	var perPageRows = 10;
	var currentPage = $('#pageBar').data('page') || 1;
	var records = $('#pageBar').data('records') || 0;
	var totalPage = $('#pageBar').data('totalPage') || 1;
	
	if (currentPage == 1 && totalPage > 1) {
		$('#page-head').hide();
		$('#page-prev').hide();
		$('#page-next').show();
		$('#page-foot').show();
	} else if (currentPage < totalPage) {
		$('#page-head').show();
		$('#page-prev').show();
		$('#page-next').show();
		$('#page-foot').show();
	} else if (currentPage == totalPage) {
		if (records > perPageRows) {
			$('#page-head').show();
			$('#page-prev').show();
		} else {
			$('#page-head').hide();
			$('#page-prev').hide();
		}
		$('#page-next').hide();
		$('#page-foot').hide();
	}
	
	// 首页
	$('#page-head').unbind('click').click(function(){
		$('#pageBar').data('page', 1);
		loadListDatas();
	});
	
	// 上一页
	$('#page-prev').unbind('click').click(function(){
		$('#pageBar').data('page', currentPage - 1);
		loadListDatas();
	});
	
	// 下一页
	$('#page-next').unbind('click').click(function(){
		$('#pageBar').data('page', currentPage + 1);
		loadListDatas();
	});
	
	// 尾页
	$('#page-foot').unbind('click').click(function(){
		$('#pageBar').data('page', totalPage);
		loadListDatas();
	});
	
}

/**
 * 加载数据列表
 */
function loadListDatas() {
	
	// 清空数据列表
	$('#infoList').html('');
	var $pb = $('#pageBar');
	
	$.blockUI({ 
        message:  '<h5><img src="../images/nyroModal/ajaxLoader.gif" class="status"/>正在加载信息列表，请稍候……</h5>',
		css: { width: '350px' }
    });
	
	// 异常处理
	$('#infoList').ajaxComplete(function(event,request, settings){
	     $.unblockUI();
	});
	$.getJSON('infoList.do', {
		page: $pb.data('page') || 1,
		rows: 10,
		sidx: '',
		sord: '',
		_search: $pb.data('_search') || 'false',
		title: $('#qtitle').val(),
		infoContent: $('#qinfoContent').val()
	}, function(resp){
		
		// 分页信息设置
		$('#pageBar').data('page', resp.page).data('totalPage', resp.total)
		.data('records', resp.records);
		$('#records').text(resp.records);
		$('#shouldPage').text(resp.total);
		$('#currentPage').text(resp.page);
		pageControl();
		
		// 展示列表
		var infoDatas = resp.rows;
		var tp = $('#infoTemplate').html();
		for (i in infoDatas) {
			var info = infoDatas[i];
			setInfo(info, tp);
		}
		
		// 显示数据
		$('#infoList .singleInfo').slideDown();
		
		// 高亮显示
		common.showHightLigth(['title', 'infoContent']);//设置高亮显示查询字段
	});
}

/**
 * 设置信息
 * @param {Object} info
 * @param {Object} tp
 */
function setInfo(info, tp, pre) {
	var $info = $(tp);
	$info.attr('id', 'info-' + info.id).data('idv', info.id);
	$info.find('.title').html("<a href='#'>" + info.title + "</a>");
	var infoText = info.createDate + "发表" + "&nbsp;&nbsp;分类：" + info.category
					+ "&nbsp;&nbsp;标签：" + info.infoLabel;
	$info.find('.info').html(infoText);
	$info.find('.infoContent').html(info.infoContent.replace(/\r\n/gi, "<br/>"));
	
	if (pre) {
		$info.prependTo('#infoList');
	} else {
		$info.appendTo('#infoList');
	}
	return $info;
}

/**
 * 表单提交前
 *
 * @return {Boolean}
 */
function showRequest(formData, jqForm, options) {
    //$('#submit').attr('value', '正在保存……').attr('disabled', true);
    return true;
}

/**
 * 表单响应处理
 *
 * @param {} responseText	响应内容
 * @param {} statusText		状态信息
 */
function showResponse(responseText, statusText) {
    if (statusText == 'success') {
		
		var info = responseText.toJson();
		if ($('#id').val() == '') {
			var tp = $('#infoTemplate').html();
			var $info = setInfo(info, tp, true);
			$.nyroModalRemove();
			$($info).slideDown();
		} else {
			var $infoDiv = $('#info-' + info.id);
			$infoDiv.find('.title').html(info.title)
			.end().find('.infoContent').html(info.infoContent.replace(/\r\n/gi, "<br/>"));
			$.nyroModalRemove();
		}
		
		
    } else {
        alert('保存失败：\n' + responseText);
    }
}

/**
 * 编辑、删除
 */
function updateAndDelete() {
	// 编辑
	$('.edit').live('click', function(){
		
		// 禁用按钮
		$(':submit').attr('disabled', true);
		form.clearForm('infoForm');
		
		var infoId = $(this).parents('.singleInfo').data('idv');
		$.nyroModalManual({
	        title: '新信息',
			minHeight: 290,
	        url: '#formTemplate',
			endShowContent: function(modal, currentSettings) {
				$.getJSON('../loadObj.do', {
					pn: 'information.master',
					id: infoId
				}, function(info){
					for (key in info) {
						$('#infoForm #' + key).val(info[key]);
					}
					
					// 启用按钮
					$(':submit').attr('disabled', false);
				});
			}
	    });
	});
	
	// 删除
	$('.delete').live('click', function(){
		if (!confirm('确认删除此信息吗？')) {
            return;
        }
        
		var infoId = $(this).parents('.singleInfo').data('idv');
        $.get(contextPath + '/deleteObj.do', {
            id: infoId,
            pn: 'information.master'
        }, function(rep) {
            if (rep == 'success') {
				$('#info-' + infoId).hide("slow", function(){
					$(this).remove();
				});
            } else {
                alert('删除失败！');
            }
        });
		
	});
	
}
