package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static org.assertj.core.api.Assertions.*;

public class MealTestData {
    public static final int MEAL1_ID = START_SEQ + 3;
    public static final int MEAL2_ID = START_SEQ + 4;
    public static final int MEAL3_ID = START_SEQ + 5;
    public static final int MEAL4_ID = START_SEQ + 6;
    public static final int MEAL5_ID = START_SEQ + 7;


    public static final Meal meal1 = new Meal(MEAL1_ID, "Админ ланч", 510);
    public static final Meal meal2 = new Meal(MEAL2_ID, "A_user dinner1", 1200);
    public static final Meal meal3 = new Meal(MEAL3_ID, "B_user dinner2", 1300);
    public static final Meal meal4 = new Meal(MEAL4_ID, "C_user dinner3", 1400);
    public static final Meal meal5 = new Meal(MEAL5_ID, "Админ ужин", 1500);

    public static Meal getNewMeal(){
        return new Meal(null, LocalDateTime.now(), "newMeal", 10101);
    }

    public static Meal updateMeal(){
        Meal upgrade = new Meal(meal2);
        upgrade.setDateTime(LocalDateTime.now());
        upgrade.setDescription("UpgradeMeal");
        upgrade.setCalories(1001);
        return upgrade;
    }

    public static void assertMatch(Meal actual, Meal expected){
        assertThat(actual).usingRecursiveComparison().ignoringFields("dateTime").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal ... expected){
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected){
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields("dateTime").isEqualTo(expected);
    }
}
