package com.habbittracker.api_habbittracker.persistence.repository;
import com.habbittracker.api_habbittracker.persistence.entities.HabitEntity;
import com.habbittracker.api_habbittracker.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;
public interface HabitRepository extends JpaRepository<HabitEntity, Long> {
    List<HabitEntity> findByUser(UserEntity user);

    @Query("SELECT h FROM Habit h WHERE h.reminderTime = :time AND h.active = true")
    List<HabitEntity> findHabitsByReminderTime(@Param("time") LocalTime time);
}
