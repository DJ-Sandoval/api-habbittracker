package com.habbittracker.api_habbittracker.presentation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CompletionRequestDTO {

    @NotNull(message = "La fecha de completado es obligatoria")
    private LocalDate completionDate;
}
