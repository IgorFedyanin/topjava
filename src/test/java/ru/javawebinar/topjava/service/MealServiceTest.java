package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.time.LocalDateTime.of;
import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Test
    public void get() {
        Meal meal = mealService.get(MEAL1_ID, USER_ID);
        assertMatch(meal, meal1);
    }

    @Test
    public void getNotOwn() {
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, ADMIN_MEAL_ID));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.get(MealTestData.NOT_FOUND, USER_ID));
    }

    @Test
    public void delete() {
        mealService.delete(MEAL1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteNotOwn() {
        assertThrows(NotFoundException.class, () -> mealService.delete(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.delete(MealTestData.NOT_FOUND, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        assertMatch(mealService.getBetweenInclusive(
                LocalDate.of(2020, Month.JANUARY, 30),
                LocalDate.of(2020, Month.JANUARY, 30), USER_ID),
                meal3, meal2, meal1);
    }

    @Test
    public void getBetweenWithNullDates() {
        assertMatch(mealService.getBetweenInclusive(null, null, USER_ID), meals);
    }

    @Test
    public void getAll() {
        List<Meal> meals = mealService.getAll(USER_ID);
        assertMatch(meals, MealTestData.meals);
    }

    @Test
    public void update() {
        mealService.update(MealTestData.getUpdated(), USER_ID);
        assertMatch(mealService.get(MEAL1_ID, USER_ID), MealTestData.getUpdated());
    }

    @Test
    public void updateNotOwn() {
        assertThrows(NotFoundException.class, () -> mealService.update(MealTestData.getUpdated(), ADMIN_ID));
    }

    @Test
    public void create() {
        Meal created = mealService.create(MealTestData.getNew(), USER_ID);
        int newId = created.getId();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(created, mealService.get(newId, USER_ID));
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () -> mealService.create(new Meal(null, meal1.getDateTime(), "duplicate", 100), USER_ID));
    }
}