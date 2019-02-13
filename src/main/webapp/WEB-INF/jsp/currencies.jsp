<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE HTML>
<html>
<jsp:include page="../jsp/fragments/header.jsp" />
<div>
    <table border="1">
        <tr>
            <th>Идентификатор</th>
            <th>Наименование</th>
            <th>Описание</th>
            <th>Дата обновление</th>
        </tr>
        <c:forEach  items="${listCurrnecies}" var ="listC">
            <tr>
                <td>${listC.id}</td>
                <td>${listC.name}</td>
                <td>${listC.description}</td>
                <td>${listC.lastUpdate}</td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>