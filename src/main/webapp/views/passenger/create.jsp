<%--
 * action-1.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.22.2/moment.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/tempusdominus-bootstrap-4/5.0.1/js/tempusdominus-bootstrap-4.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/tempusdominus-bootstrap-4/5.0.1/css/tempusdominus-bootstrap-4.min.css" />
<spring:url value="/styles/profile.css" var="profilecss" />
<link href="${profilecss}" rel="stylesheet" />
<script src="${profilecss}"></script>

<script>

  function checkForm(form)
  {
    if(!form.terms.checked) {
      alert("<spring:message code="passenger.accept.terms"/>");
      form.terms.focus();
      return false;
    }
    return true;
  }
</script>
<form:form id="form" action="${requestURI}" modelAttribute="passenger" onsubmit="return checkForm(this)">
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="comments" />
	<form:hidden path="reservations" />
	<form:hidden path="cash" />
	<form:hidden path="newAlerts" />
	<form:hidden path="newMessages" />
	<form:hidden path="userAccount.id" />
	<form:hidden path="userAccount.enabled" />
	<form:hidden path="userAccount.banned" />
	<form:hidden path="userAccount.version" />
	<form:hidden path="userAccount.authorities" />

	<div class="title background_pink d-flex flex-column align-items-center" style="color: white;">
		
		<a href="security/login.do">
			<img src="images/trondicon-header-white.png" width="82px" height="42px">
		</a>
		<h1>
			<spring:message code="passenger.register.save" />: 
		</h1>
				
	</div>

	<div
		class="resume d-flex flex-column justify-content-center align-items-center">
			

			<div class="form-row">
			<div class="col">
			<span>
				<p>
					
						<spring:message code="passenger.name" />: 
				</p>
			</span>
			<spring:message code="passenger.name" var="passengerName"/>
				<form:input path="name" type="text" class="form-control"
					placeholder="${passengerName}"/>
				<form:errors cssClass="error" path="name" />
			</div>
		</div>
		<div class="form-row">
			<div class="col">
			<span>
				<p>
						<spring:message code="passenger.surname" />: 
				</p>
			</span>
			<spring:message code="passenger.surname" var="passengerSurname"/>
				<form:input path="surname" type="text" class="form-control"
					placeholder="${passengerSurname }"/>
				<form:errors cssClass="error" path="surname" />
			</div>
		</div>


		<div class="form-row">
			<div class="col">
			<span>
				<p>
						<spring:message code="passenger.country" />: 
				</p>
			</span>
			<spring:message code="passenger.country" var="passengerCountry"/>
				<form:input path="country" type="text" class="form-control"
					placeholder="${passengerCountry }"/>
				<form:errors cssClass="error" path="country" />
			</div>
		</div>
		<div class="form-row">
			<div class="col">
			<span>
				<p>
						<spring:message code="passenger.city" />: 
				</p>
			</span>
			<spring:message code="passenger.city" var="passengerCity"/>
				<form:input path="city" type="text" class="form-control"
					placeholder="${passengerCity }"/>
				<form:errors cssClass="error" path="city" />

			</div>

		</div>

		<div class="form-row">
			<div class="col">
			<span>
				<p>
						<spring:message code="passenger.phone" />: 
				</p>
			</span>
			<spring:message code="passenger.phone" var="passengerPhone"/>
				<form:input path="phone" type="text" class="form-control"
					placeholder="${passengerPhone }"/>
				<form:errors cssClass="error" path="phone" />
			</div>

		</div>

		<div class="form-row">
			<div class="col">
			<span>
				<p>
					<spring:message code="passenger.username" />:
				</p>
			</span>
			<spring:message code="passenger.username" var="passengerUsername"/>
				<form:input path="userAccount.username" type="text" class="form-control"
					placeholder="${passengerUsername }"/>
				<form:errors cssClass="error" path="userAccount.username" />
			</div>
		</div>
		<div class="form-row">
			<div class="col">
			<span>
				<p>
						<spring:message code="passenger.password" />: 
				</p>
			</span>	
			<spring:message code="passenger.password" var="passengerPass"/>
				<form:input path="userAccount.password" type="password" class="form-control"
					placeholder="${passengerPass }"/>
				<form:errors cssClass="error" path="userAccount.password" />
			</div>
		</div>
		
		<div class="form-row">
			<div class="col">
			<span>
				<p>
					<spring:message code="passenger.image" />:
				</p>
			</span>
			<spring:message code="passenger.image" var="passengerImage"/>
				<form:input path="image" type="text" class="form-control"
					placeholder="${passengerImage }"/>
				<form:errors cssClass="error" path="image" />
			</div>
		</div>


		<div class="form-row">
			<div class="col">
			<span>
				<p>
					<spring:message code="passenger.bankAccountNumber" />:
				</p>
			</span>
			<spring:message code="passenger.bankAccountNumber" var="passengerBA"/>
				<form:input path="bankAccountNumber" type="text" class="form-control"
					placeholder="${passengerBA }"/>
				<form:errors cssClass="error" path="bankAccountNumber" />
			</div>
		</div>


	<div>
	<br/>
		<input type="checkbox" name="terms"><a href="welcome/termsAndConditions.do"><spring:message code="passenger.terms"/></a>
	</div>
		
		<input type="submit" class="btn btn-success" name="save"
			value="<spring:message code="passenger.register"/>" /> 
			
		<spring:message code="passenger.cancel" var="cancel" />
		<a href="security/login.do" class="btn btn-danger"><jstl:out
					value="${cancel}" /></a>

	</div>

</form:form>






