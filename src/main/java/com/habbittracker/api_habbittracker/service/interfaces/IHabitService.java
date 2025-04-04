package com.habbittracker.api_habbittracker.service.interfaces;
import com.habbittracker.api_habbittracker.persistence.entities.UserEntity;
import com.habbittracker.api_habbittracker.presentation.dto.HabitRequestDTO;
import com.habbittracker.api_habbittracker.presentation.dto.HabitResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface IHabitService {
    HabitResponseDTO createHabit(HabitRequestDTO habitRequest, UserEntity user);

    List<HabitResponseDTO> getUserHabits(UserEntity user);

    HabitResponseDTO getHabitById(Long id, UserEntity user);

    HabitResponseDTO updateHabit(Long id, HabitRequestDTO habitRequest, UserEntity user);

    void deleteHabit(Long id, UserEntity user);

    void toggleHabitStatus(Long id, UserEntity user);

    void recordCompletion(Long habitId, LocalDate date, UserEntity user);

    void removeCompletion(Long habitId, LocalDate date, UserEntity user);
}
