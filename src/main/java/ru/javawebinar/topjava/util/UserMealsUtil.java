package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> list = new ArrayList<>();
        Map<LocalDate, Integer> map = new HashMap<>();
        for (UserMeal meal : meals) {
            map.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
        }
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                if (map.get(meal.getDateTime().toLocalDate()) > caloriesPerDay){
                    list.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), true));
                }
                else {
                    list.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), false));
                }
            }
        }
        return list;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> map = meals.stream().collect(Collectors.toConcurrentMap(p->p.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum));
        return meals.stream().map(temp->{
            UserMealWithExcess meal = null;
            if (TimeUtil.isBetweenHalfOpen(temp.getDateTime().toLocalTime(), startTime, endTime)){
                if (map.get(temp.getDateTime().toLocalDate()) > caloriesPerDay){
                    meal = new UserMealWithExcess(temp.getDateTime(), temp.getDescription(), temp.getCalories(), true);
                }
                else {
                    meal = new UserMealWithExcess(temp.getDateTime(), temp.getDescription(), temp.getCalories(), false);
                }
            }
            return meal;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
