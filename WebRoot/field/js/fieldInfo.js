var fieldType = Common.getParam('fieldType');

$(function() {
	$('.row tr:even').addClass('even');
	$('.row tr:odd').addClass('odd');

	$('#back').click(function(){
		location.href = 'fieldList.do?fieldType=' + fieldType;
	});

   // 加载场地信息
   $('#submit').attr('disabled', true);
   var id = Common.getParam('id');

   if (!id || id == "") {
   } else {
      $.getJSON('loadField.do', {
         id: id,
		 fieldType: fieldType
      }, function(json) {
         $('#submit').attr('disabled', false);
         for (key in json) {
            if ($(key)) {
               if (key == 'status') {
                  $(':radio[name=status][value=' + json[key] + ']').attr('checked', true);
               } else {
                  $('#' + key).val(json[key]);
               }
            }
         }
      });
   }

   $('#submit').click(function() {
      $.post('updateField.do', {
         id: id,
         name: $('#name').val(),
		 fieldType: fieldType,
         fieldCode: $('#fieldCode').val(),
         status: $(':radio[name=status]:checked').val()
      }, function(reptxt) {
         if (reptxt == 'success') {
            //alert('保存成功!');
         } else {
            alert('保存失败.');
         }
         window.location.href = 'fieldList.do?fieldType=' + fieldType;
      });
   });
});
