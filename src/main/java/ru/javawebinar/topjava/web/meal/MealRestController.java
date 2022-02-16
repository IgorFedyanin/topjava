package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        log.info("getAllMealsTo");
        return MealsUtil.getTos(service.getAll(), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getFilteredAll(LocalTime startTime, LocalTime endTime){
        log.info("getFilteredAllMealsTo {}-{}", startTime, endTime);

        return MealsUtil.getFilteredTos(service.getAll(), SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }

    public Meal getById(int id){
        log.info("getById {}", id);
        return service.get(id);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    public void create(Meal meal, int id){
        log.info("create {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal);
    }

    public void save(Meal meal){
        log.info("update {}", meal);
        service.update(meal);
    }

}