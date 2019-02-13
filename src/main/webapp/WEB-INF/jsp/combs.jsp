<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE HTML>
<html>
<jsp:include page="../jsp/fragments/header.jsp" />
<div>
    <table>
        <tr><td><center>Комбинация курсов</center></td></tr>
        <tr>
            <td>
                    <c:forEach  items="${listCombs}" var ="listC" varStatus="status">
                            <c:set var="val" value="${countCurrency}"/>
                            <c:choose>
                                <c:when test="${status.getIndex() % (val) == 0 && status.getIndex() != 0}">
                                    </table>
                                </c:when>
                            </c:choose>
                            <c:choose>
                                <c:when test="${status.getIndex() % val == 0 }">
                                    <table width="500" border="1">
                                        <tr>
                                            <th>Отношение</th>
                                            <th>Значение</th>
                                            <th>Дата обновления</th>
                                        </tr>
                                </c:when>
                            </c:choose>
                            <tr>
                                <td>${listC.fromCurrency}_${listC.toCurrency}</td>
                                <td>${listC.value}</td>
                                <td>${listC.lastUpdate}</td>
                            </tr>
                    </c:forEach>
            </td>
        </tr>
    </table>
</div>
</body>
</html>