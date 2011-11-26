var fieldType = Common.getParam('fieldType');
$(function() {
	$('.row tr:even').addClass('even');
	$('.row tr:odd').addClass('odd');

    $('td.operate a').click(function(event) {
		var data = $(this).parents('tr').metadata();
        if ($(this).text() == '修改') {
            var trObj = $(this).parent().parent();
            var idValue = data.fid;
            window.location.href = 'fieldInfo.html?id=' + idValue + "&fieldType=" + fieldType;
        } else if ($(this).text() == '删除') {
            if (!confirm('确定删除？')) {
                return;
            }
            var trObj = $(this).parent().parent();
            $.get(contextPath + '/deleteObj.do', {
                id: data.fid,
                pn: 'field.' + data.ftype
            }, function(retxt) {
                if (retxt == 'success') {
                    $(trObj).remove();
                }
            });
        }
    });

	 $("#addField").click(function(){
		location.href = 'addField.jsp?fieldType=' + fieldType;
	 });

});

function doNext() {

}
