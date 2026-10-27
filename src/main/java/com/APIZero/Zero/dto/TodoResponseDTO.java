package com.APIZero.Zero.dto;

import com.APIZero.Zero.entities.Todo;
import com.APIZero.Zero.enums.TodoState;

public record TodoResponseDTO(
        Long id,
        String title,
        String description,
        TodoState state
) {

    public static TodoResponseDTO fromEntity(Todo todo){
        return new TodoResponseDTO(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getState()
        );
    }
}
