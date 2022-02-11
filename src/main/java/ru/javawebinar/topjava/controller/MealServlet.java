package ru.javawebinar.topjava.controller;

import org.slf4j.Logger;
import ru.javawebinar.topjava.service.MealServiceImplInMemory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;


public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);
    private static final int caloriesPerDay = 2000;
    private static final String INSERT_OR_EDIT = "/editMeal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";
    private final MealServiceImplInMemory service;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public MealServlet() {
        super();
        this.service = new MealServiceImplInMemory();
    }

    protected List<MealTo> getList(){
        List<Meal> list = service.getAllMeals();
        Map<LocalDate, Integer> caloriesSumByDate = list.stream().collect(Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum));
        return list.stream().map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay)).collect(Collectors.toList());
    }

    private MealTo createTo(Meal meal, boolean excess){
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward;
        String action = request.getParameter("action");

        if (action != null && action.equalsIgnoreCase("delete")){
            String mealIds = request.getParameter("mealId");
            int mealId = Integer.parseInt(mealIds);
            service.deleteMeal(mealId);
            forward = LIST_MEAL;
            request.setAttribute("mealsTo", getList());
        } else if (action != null && action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            String mealId = request.getParameter("mealId");
            Meal meal;
            if (mealId != null && !mealId.isEmpty()){
                meal = service.getMealById(Integer.parseInt(mealId));
            }
            else {
                meal = new Meal();
                meal.setId(-1);
            }
            request.setAttribute("meal", meal);
        } else {
            forward = LIST_MEAL;
            request.setAttribute("mealsTo", getList());
        }

        getServletContext().getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal();
        meal.setDescription(request.getParameter("mealDescription"));
        meal.setDateTime(LocalDateTime.parse(request.getParameter("mealDateTime")));
        meal.setCalories(Integer.parseInt(request.getParameter("mealCalories")));
        String id = request.getParameter("mealId");
        if (id.equals("-1")){
            service.addMeal(meal);
        }
        else {
            meal.setId(Integer.parseInt(id));
            service.updateMeal(meal);
        }
        List<MealTo> list = getList();
        log.debug(String.valueOf(list.size()));
        request.setAttribute("mealsTo", list);
        getServletContext().getRequestDispatcher(LIST_MEAL).forward(request, response);
    }
}
