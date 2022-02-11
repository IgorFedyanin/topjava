<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
    <h1>Edit meal</h1>
    <form action="meals" method="post">
        <input type="hidden" name="mealId" value="${meal.id}">
        <label for="mealDateTime">DateTime: </label>
        <input id="mealDateTime" type="datetime-local" name="mealDateTime" value="${meal.dateTime}">
        <br><br>
        <label for="mealDescription">Description: </label>
        <input id="mealDescription" name="mealDescription" value="${meal.description}">
        <br><br>
        <label for="mealCalories">Calories: </label>
        <input id="mealCalories" type="text" name="mealCalories" value="${meal.calories}">
        <br><br>
        <input type="submit" value="Save">
        <input type="reset" value="Cancel">
    </form>
</body>
</html>
