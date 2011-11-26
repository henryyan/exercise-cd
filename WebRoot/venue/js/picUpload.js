
$(function() {
    $('#upload').click(function() {
        if ($('#pic').val() == "") {
            alert('请选择要上传的文件！');
            return;
        } else {
            $('#uploadForm').submit();
        }
    });
    $('#pic').change(function() {
        $('#fileName').val($(this).val());
        $('#preview').html('<img border="0" src="file:///' + $(this).val() + '"/>');
    });
    
    $('#venueInfo').click(function() {
        location.href = 'venueInfo.html';
    });
    
    load();
});

function load() {
    $.getJSON('loadVenue.do', function(json) {
        for (key in json) {
            var tempData = json[key];
            $('#' + key).val(tempData);
            
            // 设置查看信息
            if (key == 'photoUrl') {
                $('#preview').html(!tempData ? "&nbsp;" : '<a href="' + getContextPath() + '/' + tempData 
				+ '" target="_blank"><img border="0" src="../' + tempData + '" style="width: 100%;height: 100%;"/></a>');
            }
        }
    });
}
