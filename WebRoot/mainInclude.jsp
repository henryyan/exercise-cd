<script type="text/javascript" src="common/js/jquery-last.js"></script>
<script type="text/javascript" src="common/js/tweak.js"></script>
<script type="text/javascript">
	$(function(){
	<c:if test="${empty user}">
		location.href = '${ctx }/index.jsp';
		return;
	</c:if>
	<c:if test="${empty user.venueInfo}">
		location.href = '${ctx }/wizard/venue.html';
		return;
	</c:if>
	
	<c:if test="${false && empty user.venueInfo.venueInfoFieldTypeLinks}">
		location.href = '${ctx }/wizard/field.jsp';
		return;
	</c:if>
	
	<c:if test="${!user.venueInfo.verification}">
		location.href = '${ctx }/wizard/verify.html';
	</c:if>
	});
	</script>
<script type="text/javascript" src="main2.js"></script>