package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealServiceImplInMemory implements MealService {

    public static AtomicInteger COUNTER = new AtomicInteger(1);

    private ConcurrentHashMap<Integer, Meal> meals = new ConcurrentHashMap<Integer, Meal>(){{
        put(COUNTER.get(), new Meal(COUNTER.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        put(COUNTER.get(), new Meal(COUNTER.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        put(COUNTER.get(), new Meal(COUNTER.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        put(COUNTER.get(), new Meal(COUNTER.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        put(COUNTER.get(), new Meal(COUNTER.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        put(COUNTER.get(), new Meal(COUNTER.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }};

    @Override
    public void addMeal(Meal meal) {
        try {
            meal.setId(COUNTER.getAndIncrement());
            meals.put(meal.getId(), meal);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMeal(int idMeal) {
        try{
            meals.remove(idMeal);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateMeal(Meal meal) {
        try{
            meals.put(meal.getId(), meal);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Meal> getAllMeals() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal getMealById(int mealId) {
        return meals.get(mealId);
    }
}
