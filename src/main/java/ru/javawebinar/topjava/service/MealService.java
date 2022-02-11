package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealService {

    void addMeal(Meal meal);
    void deleteMeal(int idMeal);
    void updateMeal(Meal meal);
    List<Meal> getAllMeals();
    Meal getMealById(int mealId);
}
