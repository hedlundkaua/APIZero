package com.APIZero.Zero.controllers;

import com.APIZero.Zero.dto.TodoRequestDTO;
import com.APIZero.Zero.dto.TodoResponseDTO;
import com.APIZero.Zero.services.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    public ResponseEntity<TodoResponseDTO> createTodo(
            @Valid @RequestBody TodoRequestDTO dto,
            Authentication auteAuthentication){
        String userEmail = auteAuthentication.getName();
        TodoResponseDTO createdTodo = todoService.createTodo(dto, userEmail);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
    }
    @GetMapping
    public ResponseEntity<List<TodoResponseDTO>> listTodos(Authentication authentication){
        String userEmail = authentication.getName();
        List<TodoResponseDTO> todos = todoService.listTodos(userEmail);

        return ResponseEntity.ok(todos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponseDTO> updateTodo(
            @PathVariable Long id,
            @Valid @RequestBody TodoRequestDTO dto,
            Authentication authentication
    ){
        String userEmail = authentication.getName();
        TodoResponseDTO updatedTodo = todoService.updateTodo(id, dto, userEmail);

        return ResponseEntity.ok(updatedTodo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(
            @PathVariable Long id,
            Authentication authentication
    ){
        String userEmail = authentication.getName();
        todoService.deleteTodo(id, userEmail);

        return ResponseEntity.noContent().build();
    }

}
