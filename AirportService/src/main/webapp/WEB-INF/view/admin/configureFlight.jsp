<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="style.css" rel="style">
<c:set var="title" value="ConfigureFlight" scope="page"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/jquery.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/datetimepicker.jspf" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
</head>
<body>
<script type="text/javascript">
$(document).ready(function(){
	$('#datetimepicker').datetimepicker({
		dateFormat: 'yy-mm-dd'
	});
	$('javatime:format').hide();
});
</script>
<form:form method="POST" action="flights/change" modelAttribute="flight">
	<form:hidden path="id" />
	<table>
	<tr>
		<td><form:label path="origin"><spring:message code="flight.origin"/></form:label></td>
		<td><form:input path="origin"/></td>
		<td><form:errors path="origin"/></td>
	</tr>
	<tr>
		<td><form:label path="destination"><spring:message code="flight.destination"/></form:label></td>
		<td><form:input path="destination"/></td>
		<td><form:errors path="destination"/></td>
	</tr>
	<tr>
		<javatime:format value="${flight.departureDate}" pattern="yyyy-MM-dd HH:mm" var="departureDate"/>
		<td><form:label path="departureDate"><spring:message code="flight.departureDate"/></form:label></td>
		<td><form:input path="departureDate" id="datetimepicker" value="${departureDate}"/></td>
		<td><form:errors path="departureDate"/></td>
	</tr>
	<tr>
		<td><form:label path="flightStatus"><spring:message code="status"/></form:label></td>
		<spring:message code="chooseStatus" var="chooseStatus"/>
		<td><form:select path="flightStatus" items="${flightStatuses}"/></td>
	</tr>
	</table>
	<input type="submit" value="<spring:message code="command.update"/>"/>
	</form:form>
</body>
</html>