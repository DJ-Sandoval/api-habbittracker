package com.habbittracker.api_habbittracker.persistence.repository;

import com.habbittracker.api_habbittracker.persistence.entities.HabitCompletionEntity;
import com.habbittracker.api_habbittracker.persistence.entities.HabitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HabitCompletionRepository extends JpaRepository<HabitCompletionEntity, Long> {
    boolean existsByHabitAndCompletionDate(HabitEntity habit, LocalDate date);

    List<HabitCompletionEntity> findByHabitAndCompletionDateBetween(
            HabitEntity habit, LocalDate startDate, LocalDate endDate);

    @Query("SELECT hc FROM HabitCompletionEntity hc WHERE hc.habit = :habit ORDER BY hc.completionDate DESC")
    List<HabitCompletionEntity> findLatestCompletions(@Param("habit") HabitEntity habit);
    void deleteByHabitAndCompletionDate(HabitEntity habit, LocalDate completionDate);
}
