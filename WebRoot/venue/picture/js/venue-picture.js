$(function() {
	
	// 效果提示
	$('.noneText,.picNameCt,.picRemarkCt').hover(function() {
		$(this).addClass('activeNoneText');
	}, function(){
		$(this).removeClass('activeNoneText');
	});
	
	// 显示编辑框
	$('.nshow').click(function(event){
		var $td = $(this).parents('td');
		var pid = $td.attr('id');
		$td.find('.nshow').hide();
		$td.find('.nedit').show().corner().html($('#picNameTemplate').html());
		$td.find('.picNameInput').val($td.find('.picNameCt').text()).focus().data('pid', pid);
	});
	
	// 保存图片名称
	$('.picNameOk').live('click', function(){
		var _this = this;
		var input = $(this).parent().parent().find('.picNameInput');
		$.post(ctx + '/venue/picture/updatePictureInfo.do', {
			id : input.data('pid'),
			type : 'name',
			value : input.val()
		}, function(resp) {
			if (resp == 'success') {
				var $td = $(_this).parents('td');
				$td.find('.nedit').hide();
				if ($td.find('.nshow .noneText')) {
					$td.find('.nshow .noneText').remove();
					$('<span/>').addClass('picNameCt').insertBefore($td.find('.ui-icon-pencil'));
				}
				$td.find('.nshow').show().find('.picNameCt').text(input.val());
			} else {
				alert(resp);
			}
		});
	});
	
	// 取消编辑图片名称
	$('.picNameCancel').live('click', function(){
		var $td = $(this).parents('td');
		$td.find('.nshow').show();
		$td.find('.nedit').hide();
	});
	
	// 显示编辑框
	$('.rshow').click(function(event){
		var $td = $(this).parents('td');
		var pid = $td.attr('id');
		$td.find('.rshow').hide();
		$td.find('.redit').show().corner().html($('#picRemarkTemplate').html());
		$td.find('.picRemarkInput').val($td.find('.picRemarkCt').text()).focus().data('pid', pid);
	});
	
	// 保存图片备注
	$('.picRemarkOk').live('click', function(){
		var _this = this;
		var input = $(this).parent().parent().find('.picRemarkInput');
		$.post(ctx + '/venue/picture/updatePictureInfo.do', {
			id : input.data('pid'),
			type : 'remark',
			value : input.val()
		}, function(resp) {
			if (resp == 'success') {
				var $td = $(_this).parents('td');
				$td.find('.redit').hide();
				if ($td.find('.rshow .noneText')) {
					$td.find('.rshow .noneText').remove();
					$('<span/>').addClass('picRemarkCt').insertBefore($td.find('.ui-icon-pencil'));
				}
				$td.find('.rshow').show().find('.picRemarkCt').text(input.val());
			} else {
				alert(resp);
			}
		});
	});
	
	// 取消编辑图片描述
	$('.picRemarkCancel').live('click', function(){
		var $td = $(this).parents('td');
		$td.find('.rshow').show();
		$td.find('.redit').hide();
	});
	
	$('.deletePic').click(deletePic);
	$('.setMajor').click(setMajor);
	
	$('#uploadPicture').click(uploadPicture);
	
});

/**
 * 删除图片
 */
function deletePic() {
	if (!confirm('确定删除此图片吗？')) {
		return;
	}
	if ($(this).parent().find('.majorPicture').length > 0) {
		if (!confirm('这是一张主图片，还要删除吗？')) {
			return;
		}
	}
	var _this = this;
	$.post(ctx + '/venue/picture/deletePicture.do', {
		id : $(this).parent().attr('id')
	}, function(resp) {
		if (resp == 'success') {
			$(_this).parents('tr').remove();
		} else {
			alert(resp);
		}
	});
}

/**
 * 设置为场馆主图片
 */
function setMajor() {
	var $_this = this;
	$.post(ctx + '/venue/picture/setMajorPicture.do', {
		pictureRealName : $($_this).attr('prn')
	}, function(resp) {
		if (resp == 'success') {
			$('.majorPicDiv').show();
			$('.majorPicture').remove();
			$('<div class="majorPicture ui-corner-all">主图片</div>').insertAfter($($_this).parent());
			$($_this).parent().hide();
		} else {
			alert(resp);
		}
	});
}

/**
 * 打开上传图片页面
 */
function uploadPicture(event) {
	$.nyroModalManual({
        title: '上传场馆图片',
		height: 380,
		type : 'iframe',
        url: fullUrl + '/venue/picture/venue-picture-upload.jsp'
    });
	
	event.stopPropagation();
}

/**
 * 图片上传完后的回调函数
 */
function uploadComplete() {
	location.reload();
}

function exchangeRow(row1,row2){ 
 if (row1.attr("tagName") == "TR" && row2.attr("tagName") == "TR" && 
     row1.children("td").length == row2.children("td").length){
  
  //var temp = new Array();
  
  for (i=0;i<row1.children("td").length;i++)
  {
   temp=row1.children("td").eq(i).text();
   row1.children("td").eq(i).text(row2.children("td").eq(i).text());
   row2.children("td").eq(i).text(temp);
  }
  
 }
}