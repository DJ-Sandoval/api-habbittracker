package com.habbittracker.api_habbittracker.presentation.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class HabitResponseDTO {

    private Long id;
    private String name;
    private String description;
    private LocalTime reminderTime;
    private boolean active;
    private int targetDaysPerWeek;
    private int currentStreak;
    private int completionPercentage;
}
