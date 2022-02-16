package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.UserUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private final Map<Integer, User> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counterUserIds = new AtomicInteger(0);

    {
        UserUtil.users.forEach(this::save);
    }

    @Override
    public boolean delete(int id) {
        //log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        //log.info("save {}", user);
        if (user.isNew()){
            user.setId(counterUserIds.incrementAndGet());
            repository.put(user.getId(), user);
        }
        return repository.computeIfPresent(user.getId(), (id, oldUser)->user);
    }

    @Override
    public User get(int id) {
        //log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        //log.info("getAll");
        List<User> list = new ArrayList<>(repository.values());
        list.sort(Comparator.comparing(User::getName).thenComparing(User::getEmail));
        return list;
    }

    @Override
    public User getByEmail(String email) {
        //log.info("getByEmail {}", email);
        try{
            return repository.entrySet().stream().filter(s-> Objects.equals(s.getValue().getEmail(), email)).findFirst().get().getValue();
        }
        catch (NoSuchElementException e){
            return null;
        }
    }
}
