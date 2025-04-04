package com.habbittracker.api_habbittracker.service.interfaces;

import com.habbittracker.api_habbittracker.persistence.entities.HabitEntity;
import org.springframework.scheduling.annotation.Async;
public interface INotificationService {
    void scheduleHabitReminder(HabitEntity habit);

    void rescheduleHabitReminder(HabitEntity habit);

    void cancelHabitReminder(Long habitId);

    @Async
    void sendHabitReminder(HabitEntity habit);
}
