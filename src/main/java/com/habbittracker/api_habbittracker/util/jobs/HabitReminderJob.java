package com.habbittracker.api_habbittracker.util.jobs;

import com.habbittracker.api_habbittracker.persistence.entities.HabitEntity;
import com.habbittracker.api_habbittracker.persistence.repository.HabitRepository;
import com.habbittracker.api_habbittracker.service.interfaces.INotificationService;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HabitReminderJob implements Job {
    private final HabitRepository habitRepository;
    private final INotificationService notificationService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        List<HabitEntity> habits = habitRepository.findHabitsByReminderTime(now);
        habits.forEach(notificationService::sendHabitReminder);
    }
}
