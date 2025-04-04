package com.habbittracker.api_habbittracker.service.implementation;

import com.habbittracker.api_habbittracker.persistence.entities.HabitEntity;
import com.habbittracker.api_habbittracker.service.interfaces.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService {
    private final JavaMailSender mailSender;
    private final TaskScheduler taskScheduler;
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new HashMap<>();



    @Override
    public void rescheduleHabitReminder(HabitEntity habit) {
        cancelHabitReminder(habit.getId());
        scheduleHabitReminder(habit);
    }

    @Override
    public void cancelHabitReminder(Long habitId) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(habitId);
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
            scheduledTasks.remove(habitId);
        }
    }

    @Override
    public void sendHabitReminder(HabitEntity habit) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(habit.getUser().getEmail());
        message.setSubject("Recordatorio de hábito: " + habit.getName());
        message.setText(String.format(
                "Hola %s,\n\nNo olvides completar tu hábito '%s' hoy.\n\n¡Tú puedes!",
                habit.getUser().getEmail(),
                habit.getName()));

        mailSender.send(message);
    }

    @Override
    public void scheduleHabitReminder(HabitEntity habit) {
        if (!habit.isActive()) return;

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderDateTime = habit.getReminderTime().atDate(LocalDate.now());

        // Si la hora de recordatorio ya pasó hoy, programa para mañana
        if (reminderDateTime.isBefore(now)) {
            reminderDateTime = reminderDateTime.plusDays(1);
        }

        Runnable task = () -> sendHabitReminder(habit);

        // Programar primera ejecución en la fecha/hora exacta
        ScheduledFuture<?> scheduledTask = taskScheduler.scheduleAtFixedRate(
                task,
                Date.from(reminderDateTime.atZone(zoneId).toInstant()),
                24 * 60 * 60 * 1000); // Repetir cada 24 horas

        scheduledTasks.put(habit.getId(), scheduledTask);
    }

}
