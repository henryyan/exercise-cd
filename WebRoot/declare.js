$(function() {
	// 同意
	$('#agreesubmit').click(function() {
		location.href = 'register.jsp?action=agree';
	});
	
	// 不同意
	$('#disagree').click(function(){
		location.href = getContextPath() + "/index.jsp";
	});
});

var secs = 10;
var wait = secs * 1000;
var agreetext = "我同意";
$('#agreesubmit').val(agreetext + "(" + secs + ") ");
$('#agreesubmit').attr('disabled', true);
for (i = 1; i <= secs; i++) {
	window.setTimeout("update(" + i + ")", i * 1000);
}
window.setTimeout("timer()", wait);
function update(num, value) {
	if (num == (wait / 1000)) {
		$('#agreesubmit').val(agreetext);
	} else {
		printnr = (wait / 1000) - num;
		$('#agreesubmit').val(agreetext + "(" + printnr + ") ");
	}
}

function timer() {
	$('#agreesubmit').attr('disabled', false);
	$('#agreesubmit').val(agreetext);
}
