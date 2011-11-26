$(function() {
	$('.sc-item a').attr('target', 'main');
	
	// 加载场地管理菜单
	loadFieldTypeForLeftMenu();
});

/**
 * 获取本场馆已启用的场地类型并设置左边的菜单
 */
function loadFieldTypeForLeftMenu() {
	$.getJSON(ctx + '/field/type/getFieldTypes.do', function(types){
		$('#bookFieldType,#localFieldType').html('');
		for (var t in types) {
			// 预订菜单
			var bookUrl = ctx + "/activity/fieldActivityCalendar.jsp?fieldType=" + t;
			var bookList = "<li class='sc-item'><a href='" + bookUrl + "' target='main'>" + types[t] + "</a></li>";
			$('#bookFieldType').append(bookList);
			
			// 现场管理菜单
			var localUrl = ctx + "/activity/fieldActivityTab.jsp?type=local&pickedDate=" + strSystemDate + "&fieldType=" + t;
			var localList = "<li class='sc-item'><a href='" + localUrl + "' target='main'>" + types[t] + "</a></li>";
			$('#localFieldType').append(localList);
		}
	});
}
