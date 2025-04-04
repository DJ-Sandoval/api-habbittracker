package com.habbittracker.api_habbittracker.service.implementation;

import com.habbittracker.api_habbittracker.persistence.entities.HabitCompletionEntity;
import com.habbittracker.api_habbittracker.persistence.entities.HabitEntity;
import com.habbittracker.api_habbittracker.persistence.entities.UserEntity;
import com.habbittracker.api_habbittracker.persistence.repository.HabitCompletionRepository;
import com.habbittracker.api_habbittracker.persistence.repository.HabitRepository;
import com.habbittracker.api_habbittracker.presentation.dto.HabitRequestDTO;
import com.habbittracker.api_habbittracker.presentation.dto.HabitResponseDTO;
import com.habbittracker.api_habbittracker.service.exception.ResourceNotFoundException;
import com.habbittracker.api_habbittracker.service.exception.UnauthorizedAccessException;
import com.habbittracker.api_habbittracker.service.interfaces.IHabitService;
import com.habbittracker.api_habbittracker.service.interfaces.INotificationService;
import com.habbittracker.api_habbittracker.util.StreakCalculator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements IHabitService {
    private final HabitRepository habitRepository;
    private final HabitCompletionRepository completionRepository;
    private final INotificationService notificationService;
    private final StreakCalculator streakCalculator;

    @Override
    @Transactional
    public HabitResponseDTO createHabit(HabitRequestDTO habitRequest, UserEntity user) {
        HabitEntity habit = new HabitEntity();
        habit.setName(habitRequest.getName());
        habit.setDescription(habitRequest.getDescription());
        habit.setReminderTime(habitRequest.getReminderTime());
        habit.setTargetDaysPerWeek(habitRequest.getTargetDaysPerWeek());
        habit.setUser(user);

        HabitEntity savedHabit = habitRepository.save(habit);

        // Programar notificación
        notificationService.scheduleHabitReminder(savedHabit);

        return mapToResponseDTO(savedHabit);
    }

    @Override
    public List<HabitResponseDTO> getUserHabits(UserEntity user) {
        return habitRepository.findByUser(user).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public HabitResponseDTO getHabitById(Long id, UserEntity user) {
        HabitEntity habit = getHabitIfAuthorized(id, user);
        return mapToResponseDTO(habit);
    }

    @Override
    @Transactional
    public HabitResponseDTO updateHabit(Long id, HabitRequestDTO habitRequest, UserEntity user) {
        HabitEntity habit = getHabitIfAuthorized(id, user);

        habit.setName(habitRequest.getName());
        habit.setDescription(habitRequest.getDescription());
        habit.setReminderTime(habitRequest.getReminderTime());
        habit.setTargetDaysPerWeek(habitRequest.getTargetDaysPerWeek());

        HabitEntity updatedHabit = habitRepository.save(habit);

        // Re-programar notificación si cambió la hora
        notificationService.rescheduleHabitReminder(updatedHabit);

        return mapToResponseDTO(updatedHabit);
    }

    @Override
    @Transactional
    public void deleteHabit(Long id, UserEntity user) {
        HabitEntity habit = getHabitIfAuthorized(id, user);
        notificationService.cancelHabitReminder(habit.getId());
        habitRepository.delete(habit);
    }

    @Override
    @Transactional
    public void toggleHabitStatus(Long id, UserEntity user) {
        HabitEntity habit = getHabitIfAuthorized(id, user);
        habit.setActive(!habit.isActive());
        habitRepository.save(habit);

        if (habit.isActive()) {
            notificationService.scheduleHabitReminder(habit);
        } else {
            notificationService.cancelHabitReminder(habit.getId());
        }
    }

    @Override
    @Transactional
    public void recordCompletion(Long habitId, LocalDate date, UserEntity user) {
        HabitEntity habit = getHabitIfAuthorized(habitId, user);

        if (completionRepository.existsByHabitAndCompletionDate(habit, date)) {
            throw new IllegalStateException("El hábito ya fue completado en esta fecha");
        }

        HabitCompletionEntity completion = new HabitCompletionEntity(date, habit);
        completionRepository.save(completion);
    }

    @Override
    @Transactional
    public void removeCompletion(Long habitId, LocalDate date, UserEntity user) {
        HabitEntity habit = getHabitIfAuthorized(habitId, user);
        completionRepository.deleteByHabitAndCompletionDate(habit, date);
    }

    private HabitEntity getHabitIfAuthorized(Long id, UserEntity user) {
        HabitEntity habit = habitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hábito no encontrado"));

        if (!habit.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("No autorizado para acceder a este recurso");
        }

        return habit;
    }

    private HabitResponseDTO mapToResponseDTO(HabitEntity habit) {
        HabitResponseDTO response = new HabitResponseDTO();
        response.setId(habit.getId());
        response.setName(habit.getName());
        response.setDescription(habit.getDescription());
        response.setReminderTime(habit.getReminderTime());
        response.setActive(habit.isActive());
        response.setTargetDaysPerWeek(habit.getTargetDaysPerWeek());

        // Calcular estadísticas
        List<HabitCompletionEntity> completions = completionRepository.findLatestCompletions(habit);
        response.setCurrentStreak(streakCalculator.calculateCurrentStreak(completions));

        long completedThisWeek = completions.stream()
                .filter(c -> c.getCompletionDate().isAfter(LocalDate.now().minusDays(7)))
                .count();
        response.setCompletionPercentage((int) ((completedThisWeek * 100) / habit.getTargetDaysPerWeek()));

        return response;
    }
}
