$(function() {

	// 开始、结束时间
    $.validator.addMethod("openAndCloseTime", function(value, element) {
        var length = value.length;
        return length > 1;
    }, function(result, element){
		if (element.id.indexOf('open') != -1) {
			return "请选择开始时间";
		} else {
			return "请选择结束时间";
		}
	});
    
    validator = $("#venueInfoForm").validate({
        rules: {
            venueName: {
                required: true,
                minlength: 2,
                remote: {
                    url: 'checkVenueNameExist.do',
                    type: 'post',
                    data: {
                        venueName: function() {
                            return $('#venueName').val();
                        }
                    },
                    dataType: 'json',
                    dataFilter: function(data) {
                        data = eval("(" + data + ")");
                        if (data.exist) {
                            // 已经存在的场馆ID和当前ID相等返回true
                            if (data.infoId == $('#id').val()) {
                                return true;
                            }
                            return false;
                        } else {
                            return true;
                        }
                    }
                }
            },
            adress: {
                required: true,
                minlength: 5
            },
            zipcode: {
                isZipCode: true
            },
            contact: {
                required: true,
                minlength: 2
            },
			cell: {
				required: true,
				isMobile: true
			},
			email: {
				required: true,
				email: true
			},
			openTimeHour: {
				openAndCloseTime: true
			},
			openTimeMinute: {
				openAndCloseTime: true
			},
			closeTimeHour: {
				openAndCloseTime: true
			},
			closeTimeMinute: {
				openAndCloseTime: true
			}
        },
        messages: {
            venueName: {
                required: "请输入场馆名称",
                minlength: $.format("场馆名称至少 {0} 个汉字"),
                remote: "此场馆名称已被占用"
            },
            adress: {
                required: "请输入场馆地址",
                minlength: $.format("场馆地址至少  {0} 个汉字")
            },
            contact: {
                required: "请输入联系人",
                minlength: $.format("联系人至少  {0} 个汉字")
            }
        },
        /* 设置验证触发事件 */
        errorPlacement: function(error, element) {
            if (element.is(":radio")) {
                error.appendTo(element.parent().next().next());
            } else if (element.is(":checkbox")) {
                error.appendTo(element.next());
            } else {
                error.appendTo(element.parent().next());
            }
        },
        submitHandler: function(form) {
            var submitOption = {
                beforeSubmit: showRequest,
                success: showResponse,
                clearForm: false
            };
            $('#venueInfoForm').ajaxSubmit(submitOption);
        },
        success: function(label) {
            label.html("&nbsp;").addClass("checked");
        }
    });
});
