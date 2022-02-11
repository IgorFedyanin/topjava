<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Meal list</title>
    <style type="text/css">
        TABLE {
            width: 600px; /* Ширина таблицы */
            border-collapse: collapse; /* Убираем двойные линии между ячейками */

        }
        TD, TH {
            border: 2px solid black; /* Параметры рамки */
            padding: 5px;
        }
        TH {
            background: #b0e0e6; /* Цвет фона */
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h4>Meals</h4>
<p><a href="meals?action=edit">Add Meal</a></p>
<table>
    <tr align="center">
        <td>Date</td>
        <td>Description</td>
        <td>Calories</td>
        <td> </td>
        <td> </td>
    </tr>
    <jsp:useBean id="mealsTo" scope="request" type="java.util.List"/>
    <c:forEach var="mealTo" items="${mealsTo}">
        <tr style="color: ${mealTo.excess? "red": "green"}">
            <td align="center">
                <fmt:parseDate value="${ mealTo.dateTime }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" />
            </td>
            <td>
                    ${mealTo.description}
            </td>
            <td>
                    ${mealTo.calories}
            </td>
            <td align="center">
                <a href="meals?action=edit&mealId=<c:out value="${mealTo.id}"/>">Update</a>
            </td>
            <td align="center">
                <a href="meals?action=delete&mealId=<c:out value="${mealTo.id}"/>">Delete</a>
            </td>
        </tr>
    </c:forEach>

</table>
</body>
</html>
