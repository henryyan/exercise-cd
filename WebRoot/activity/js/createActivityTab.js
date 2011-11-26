$(function() {
    $.getJSON('../field/fieldNames.do', {
        //fieldType: fieldType
    }, function(result) {
		for (k in result) {
			$("<li><a href='#" + k + "'>" + result[k] + "</a></li>").appendTo($('#tab-ul'));
			$('#iframeTemplate').find('div').attr('id', k).find('iframe').attr('src', 'http://localhost:8090/exercise/about.jsp');
			$('#tabs').append($('#iframeTemplate div').clone());
		}

		$('#tabs').tabs();
    });

});
