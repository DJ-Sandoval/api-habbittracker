package com.habbittracker.api_habbittracker.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record AuthCreateUserRequest(@NotBlank String username,
                                    @NotBlank String password,
                                    @NotBlank String email,
                                    @Valid AuthCreateRoleRequest roleRequest) {
}
