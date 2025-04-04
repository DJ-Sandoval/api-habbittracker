package com.habbittracker.api_habbittracker.presentation.controller;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.habbittracker.api_habbittracker.persistence.entities.UserEntity;
import com.habbittracker.api_habbittracker.presentation.dto.CompletionRequestDTO;
import com.habbittracker.api_habbittracker.presentation.dto.HabitRequestDTO;
import com.habbittracker.api_habbittracker.presentation.dto.HabitResponseDTO;
import com.habbittracker.api_habbittracker.service.interfaces.IHabitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/habits")
@RequiredArgsConstructor
@Tag(name = "Hábitos", description = "Endpoints para la gestión de hábitos")
public class HabitController {
    private final IHabitService habitService;

    @PostMapping
    @io.swagger.v3.oas.annotations.Operation(summary = "Crear un hábito", description = "Permite al usuario crear un nuevo hábito.")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('DEVELOPER') or hasRole('INVITED')")
    public ResponseEntity<HabitResponseDTO> createHabit(
            @Valid @RequestBody HabitRequestDTO habitRequest,
            @AuthenticationPrincipal UserEntity user) {

        HabitResponseDTO response = habitService.createHabit(habitRequest, user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/list")
    @io.swagger.v3.oas.annotations.Operation(summary = "Obtener hábitos", description = "Obtiene todos los hábitos del usuario autenticado.")
    public ResponseEntity<List<HabitResponseDTO>> getUserHabits(
            @AuthenticationPrincipal UserEntity user) {

        return ResponseEntity.ok(habitService.getUserHabits(user));
    }

    @GetMapping("/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Obtener un hábito", description = "Obtiene la información de un hábito específico.")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('DEVELOPER') or hasRole('INVITED')")
    public ResponseEntity<HabitResponseDTO> getHabit(
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntity user) {

        return ResponseEntity.ok(habitService.getHabitById(id, user));
    }

    @PutMapping("/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Actualizar un hábito", description = "Modifica la información de un hábito existente.")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('DEVELOPER') or hasRole('INVITED')")
    public ResponseEntity<HabitResponseDTO> updateHabit(
            @PathVariable Long id,
            @Valid @RequestBody HabitRequestDTO habitRequest,
            @AuthenticationPrincipal UserEntity user) {

        return ResponseEntity.ok(habitService.updateHabit(id, habitRequest, user));
    }

    @DeleteMapping("/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Eliminar un hábito", description = "Elimina un hábito existente del usuario.")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('DEVELOPER') or hasRole('INVITED')")
    public ResponseEntity<Void> deleteHabit(
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntity user) {

        habitService.deleteHabit(id, user);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    @io.swagger.v3.oas.annotations.Operation(summary = "Activar/Desactivar un hábito", description = "Cambia el estado activo/inactivo de un hábito.")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('DEVELOPER') or hasRole('INVITED')")
    public ResponseEntity<Void> toggleHabitStatus(
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntity user) {

        habitService.toggleHabitStatus(id, user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/completions")
    @io.swagger.v3.oas.annotations.Operation(summary = "Registrar la finalización de un hábito", description = "Registra un día en el que el usuario completó un hábito.")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('DEVELOPER') or hasRole('INVITED')")
    public ResponseEntity<Void> recordCompletion(
            @PathVariable Long id,
            @RequestBody CompletionRequestDTO completionRequest,
            @AuthenticationPrincipal UserEntity user) {

        habitService.recordCompletion(id, completionRequest.getCompletionDate(), user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/completions")
    @io.swagger.v3.oas.annotations.Operation(summary = "Eliminar una finalización de un hábito", description = "Elimina un registro de finalización de un hábito.")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('DEVELOPER') or hasRole('INVITED')")
    public ResponseEntity<Void> removeCompletion(
            @PathVariable Long id,
            @RequestBody CompletionRequestDTO completionRequest,
            @AuthenticationPrincipal UserEntity user) {

        habitService.removeCompletion(id, completionRequest.getCompletionDate(), user);
        return ResponseEntity.noContent().build();
    }
}
