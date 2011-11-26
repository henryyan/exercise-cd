$(function(){
	getData();
});

function getData() {
	$.getJSON('getData.do', {
		fieldType: 'badmintoon'
	}, function(data){

		// 运营
		$('.occupancy').text(data.operation.counter.运营);

		// 闲置
		$('.idling').text(data.operation.counter.闲置);

		// 收入金额
		$('.income').text(data.operation.income);

		// 会员卡总数
		$('.countMemberCard').text(data.memeber.countMemberCard);

		// 有效会员卡总数
		$('.countEnableMemberCard').text(data.memeber.countEnableMemberCard);

		$('.countMemberHasOrder').text(data.memeber.countMemberHasOrder);

		/*// 待验证
		$('.verification').text(data.field.verification);

		// 作废
		$('.invalid').text(data.field.invalid);

		// 已预订待处理
		$('.tacticsActivity').text(data.field.tacticsActivity);*/
		
		var fields = data.fields;
		var verification = '', invalid = '', tacticsActivity = '', fieldZhName = '';
		var i = 1;
		for (var f in fields) {
			if (i++ > 1) {
				verification += "，";
				invalid += "，";
				tacticsActivity += "，";
			}
			if (f == 'badmintoon') {
				fieldZhName = '羽毛球';
			} else if (f == 'tennis') {
				fieldZhName = '网球';
			} else if (f == 'football') {
				fieldZhName = '足球';
			}
			verification += fieldZhName + ":" + fields[f].verification + "";
			invalid += fieldZhName + ":" + fields[f].invalid + "";
			tacticsActivity += fieldZhName + ":" + fields[f].tacticsActivity + "";
		}
		$('.verification').text(verification);
		$('.invalid').text(invalid);
		$('.tacticsActivity').text(tacticsActivity);
		
	});

	$.getJSON('../information/infoList.do', {
		page: 1,
		rows: 2,
		sidx: '',
		sord: '',
		_search: 'false',
		size: 2
	}, function(data){
		var rows = data.rows;
		var $infoList = $('#infoList');
		for (var i in rows) {
			var row = rows[i];
			$('<li></li>').html("* <a href='#infoTemp' title='" + row.title + "'>" + row.title + "</a>").data('info', row).appendTo($infoList);
		}

		$infoList.find('a').click(function(){
			var ele = this;
			var info = $(this).parent().data('info');
			setInfo(info);
			$.nyroModalManual({
		        title: '活动详细',
		        height: 550,
		        url: '#infoTemp'
		    });
		});
	});

}

/**
 * 设置信息
 * @param {Object} info
 */
function setInfo(info) {
	var $info = $('#infoTemp');
	$info.find('.title').html(info.title);
	var infoText = info.createDate + "发表" + "&nbsp;&nbsp;分类：" + info.category
					+ "&nbsp;&nbsp;标签：" + info.infoLabel;
	$info.find('.info').html(infoText);
	$info.find('.infoContent').html(info.infoContent.replace(/\r\n/gi, "<br/>"));

	return $info;
}