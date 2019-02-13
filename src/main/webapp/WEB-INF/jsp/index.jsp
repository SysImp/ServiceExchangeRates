<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE HTML>
<html>
<jsp:include page="../jsp/fragments/header.jsp" />
<spring:url value="/index" var="actionUrl" />

<form:form method="post" modelAttribute="exchangeForm" action="${actionUrl}">
    <table>
        <tr>
            <td><form:input path="factor" /></td>
            <td>
                <form:select path="fromCurrency">
                    <c:forEach items="${AllowCurrencies}" var ="listCurrencies">
                        <c:set var="val" value="${from.name}"/>
                        <form:option title="${listCurrencies.description}" value="${listCurrencies.name}">${listCurrencies.name}</form:option>
                    </c:forEach>
                </form:select>
            </td>
            <td>
                <form:select path="toCurrency">
                    <c:forEach  items="${AllowCurrencies}" var ="listCurrencies">
                        <c:set var="val" value="${to.name}"/>
                        <form:option title="${listCurrencies.description}" value="${listCurrencies.name}">${listCurrencies.name}</form:option>
                    </c:forEach>
                </form:select>
            </td>
            <td><form:input disabled="true" path="value" /></td>
            <td><button type="submit" class="btn-lg btn-primary pull-right">go</button></td>
        </tr>
        <tr>
            <td><form:errors path="factor" /></td>
            <td><form:errors path="fromCurrency" /></td>
            <td><form:errors path="toCurrency" /></td>
            <td><form:errors path="value" /></td>
        </tr>
    </table>
</form:form>
</body>
</html>