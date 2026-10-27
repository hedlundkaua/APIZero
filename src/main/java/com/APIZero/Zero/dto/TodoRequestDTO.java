package com.APIZero.Zero.dto;

import com.APIZero.Zero.enums.TodoState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TodoRequestDTO (

        @NotBlank(message = "O titulo é obrigatório")
        String title,

        String description,

        @NotNull(message = "O estado da tarefa é obrigatorio")
        TodoState state
){}
