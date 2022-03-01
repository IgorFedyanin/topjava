package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "file:src/test/resources/spring/spring-app_JDBS.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal created = service.create(getNewMeal(), SecurityUtil.authUserId());
        Integer newId = created.getId();
        Meal newMeal = getNewMeal();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, SecurityUtil.authUserId()), newMeal);
    }

    @Test
    public void get() {
        Meal meal = service.get(MEAL2_ID, SecurityUtil.authUserId());
        assertMatch(meal, meal2);
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, SecurityUtil.authUserId()));
    }

    @Test
    public void delete() {
        service.delete(MEAL2_ID, SecurityUtil.authUserId());
        assertThrows(NotFoundException.class, () -> service.get(MEAL2_ID, SecurityUtil.authUserId()));
    }

    @Ignore
    @Test
    public void getBetweenInclusive() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Meal> filtered = service
                .getBetweenInclusive(LocalDate.parse("2022-02-27", formatter), LocalDate.parse("2022-02-27", formatter), SecurityUtil.authUserId());
        assertMatch(filtered, meal1, meal2, meal3);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(SecurityUtil.authUserId());
        assertMatch(all, meal4, meal3, meal2);
    }

    @Test
    public void update() {
        Meal upgrade = updateMeal();
        service.update(upgrade, SecurityUtil.authUserId());
        assertMatch(service.get(MEAL2_ID, SecurityUtil.authUserId()), upgrade);
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, SecurityUtil.authUserId()));
    }

    @Test
    public void duplicateDateTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        Meal meal = new Meal(LocalDateTime.parse("2022-02-27 01:02:59.000000", formatter), "description", 4555);
        assertThrows(DataAccessException.class, () -> service.create(meal, SecurityUtil.authUserId()));
    }


}