package ru.javawebinar.topjava.service;

import org.junit.*;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertThrows;
import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.NOT_FOUND;
import static ru.javawebinar.topjava.MealTestData.meals;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.UserTestData.getNew;
import static ru.javawebinar.topjava.UserTestData.getUpdated;
import static ru.javawebinar.topjava.UserTestData.user;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public abstract class BaseServiceTest {
    private static final Logger log = getLogger("result");

    private static final StringBuilder results = new StringBuilder();

    @Rule
    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
    public final Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result);
            log.info(result + " ms\n");
        }
    };

    @Autowired
    private MealService mealService;

    @Autowired
    private UserService userService;

    @Autowired
    private CacheManager cacheManager;

    @Before
    public void setup() {
        cacheManager.getCache("users").clear();
    }

    @AfterClass
    public static void printResult() {
        log.info("\n---------------------------------" +
                "\nTest                 Duration, ms" +
                "\n---------------------------------" +
                results +
                "\n---------------------------------");
    }

    @Test
    public void deleteMeal() {
        mealService.delete(MEAL1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteMealNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void deleteMealNotOwn() {
        assertThrows(NotFoundException.class, () -> mealService.delete(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void createMeal() {
        Meal created = mealService.create(MealTestData.getNew(), USER_ID);
        int newId = created.id();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateMealDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                mealService.create(new Meal(null, meal1.getDateTime(), "duplicate", 100), USER_ID));
    }

    @Test
    public void getMeal() {
        Meal actual = mealService.get(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(actual, adminMeal1);
    }

    @Test
    public void getMealNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getNotOwn() {
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void updateMeal() {
        Meal updated = MealTestData.getUpdated();
        mealService.update(updated, USER_ID);
        MEAL_MATCHER.assertMatch(mealService.get(MEAL1_ID, USER_ID), MealTestData.getUpdated());
    }

    @Test
    public void updateMealNotOwn() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> mealService.update(MealTestData.getUpdated(), ADMIN_ID));
        Assert.assertEquals("Not found entity with id=" + MEAL1_ID, exception.getMessage());
        MEAL_MATCHER.assertMatch(mealService.get(MEAL1_ID, USER_ID), meal1);
    }

    @Test
    public void getAllMeal() {
        MEAL_MATCHER.assertMatch(mealService.getAll(USER_ID), meals);
    }

    @Test
    public void getMealBetweenInclusive() {
        MEAL_MATCHER.assertMatch(mealService.getBetweenInclusive(
                        LocalDate.of(2020, Month.JANUARY, 30),
                        LocalDate.of(2020, Month.JANUARY, 30), USER_ID),
                meal3, meal2, meal1);
    }

    @Test
    public void getMealBetweenWithNullDates() {
        MEAL_MATCHER.assertMatch(mealService.getBetweenInclusive(null, null, USER_ID), meals);
    }

    @Test
    public void createUser() {
        User created = userService.create(getNew());
        int newId = created.id();
        User newUser = UserTestData.getNew();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userService.get(newId), newUser);
    }

    @Test
    public void duplicateUserMailCreate() {
        assertThrows(DataAccessException.class, () ->
                userService.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.USER)));
    }

    @Test
    public void deleteUser() {
        userService.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> userService.get(USER_ID));
    }

    @Test
    public void deletedUserNotFound() {
        assertThrows(NotFoundException.class, () -> userService.delete(UserTestData.NOT_FOUND));
    }

    @Test
    public void getUser() {
        User user = userService.get(USER_ID);
        USER_MATCHER.assertMatch(user, UserTestData.user);
    }

    @Test
    public void getUserNotFound() {
        assertThrows(NotFoundException.class, () -> userService.get(UserTestData.NOT_FOUND));
    }

    @Test
    public void getUserByEmail() {
        User user = userService.getByEmail("admin@gmail.com");
        USER_MATCHER.assertMatch(user, admin);
    }

    @Test
    public void updateUser() {
        User updated = getUpdated();
        userService.update(updated);
        USER_MATCHER.assertMatch(userService.get(USER_ID), UserTestData.getUpdated());
    }

    @Test
    public void getAllUser() {
        List<User> all = userService.getAll();
        USER_MATCHER.assertMatch(all, admin, guest, user);
    }
}
