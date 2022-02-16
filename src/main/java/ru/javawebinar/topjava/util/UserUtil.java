package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class UserUtil {
    public static final List<User> users = Arrays.asList(
            new User(null,"Вася", "vasya@mail.ru", "392020", Role.USER),
            new User(null,"Петя", "petya@mail.ru", "252626", Role.USER),
            new User(null,"Коля", "kola@mail.ru", "676686833", Role.USER),
            new User(null,"Саша", "sasha@mail.ru", "fndf656", Role.USER),
            new User(null,"Игорь", "igor@mail.ru", "dhfb55h", Role.ADMIN),
            new User(null,"Сёма", "sema@mail.ru", "65hhb5eh6", Role.USER, Role.ADMIN)
    );



}
