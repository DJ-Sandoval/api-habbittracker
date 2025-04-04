package com.habbittracker.api_habbittracker.presentation.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalTime;

@Data
public class HabitRequestDTO {

    @NotBlank(message = "El nombre del hábito es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String name;

    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
    private String description;

    @NotNull(message = "La hora de recordatorio es obligatoria")
    private LocalTime reminderTime;

    @Min(value = 1, message = "El objetivo mínimo es 1 día por semana")
    @Max(value = 7, message = "El objetivo máximo es 7 días por semana")
    private int targetDaysPerWeek;
    private Long userId;
}
