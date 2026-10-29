package com.APIZero.Zero.controllers;

import com.APIZero.Zero.dto.TodoRequestDTO;
import com.APIZero.Zero.dto.TodoResponseDTO;
import com.APIZero.Zero.enums.TodoState;
import com.APIZero.Zero.services.TodoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
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
    public ResponseEntity<Page<TodoResponseDTO>> listTodos(
            Authentication authentication,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false)TodoState state,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit
    ){
        String userEmail = authentication.getName();

        Pageable pageable = PageRequest.of(offset,  limit);

        Page<TodoResponseDTO> todos = todoService.listTodos(userEmail, title, description, state, pageable);

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