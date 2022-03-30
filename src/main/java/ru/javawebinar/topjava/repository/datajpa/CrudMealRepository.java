package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:userId")
    int delete(@Param(value = "id") int id, @Param(value = "userId") int userId);

    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId ORDER BY m.dateTime DESC")
    List<Meal> getAll(@Param(value = "userId") int userId);

    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId AND m.dateTime >= :startDateTime AND m.dateTime < :endDateTime ORDER BY m.dateTime DESC")
    List<Meal> getBetween(@Param(value = "userId") int userId,
                          @Param(value = "startDateTime") LocalDateTime startDateTime,
                          @Param(value = "endDateTime") LocalDateTime endDateTime);

    @Query("SELECT m FROM Meal m JOIN FETCH m.user u WHERE m.id=:id AND u.id=:userId ORDER BY m.dateTime DESC")
    Meal getWithUser(@Param(value = "id") int id,
                     @Param(value = "userId") int userId);
}
