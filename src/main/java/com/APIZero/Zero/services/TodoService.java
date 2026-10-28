package com.APIZero.Zero.services;

import com.APIZero.Zero.dto.TodoRequestDTO;
import com.APIZero.Zero.dto.TodoResponseDTO;
import com.APIZero.Zero.entities.Todo;
import com.APIZero.Zero.entities.User;
import com.APIZero.Zero.enums.TodoState;
import com.APIZero.Zero.repositories.TodoRepository;
import com.APIZero.Zero.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TodoResponseDTO createTodo(TodoRequestDTO dto, String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuário autenticado não encontrado no banco de dados"));

        Todo todo = new Todo(dto.title(), dto.description(), dto.state(), user);
        Todo savedTodo = todoRepository.save(todo);

        return TodoResponseDTO.fromEntity(savedTodo);
    }


    public Page<TodoResponseDTO> listTodos(String userEmail, String title, String description, TodoState state, Pageable pageable){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuário autenticado não encontrado no banco de dados"));

        Page<Todo> todosPage = todoRepository.findTodosWithFilters(user.getId(), title, description, state, pageable);

        return todosPage.map(TodoResponseDTO::fromEntity);
    }




    @Transactional
    public TodoResponseDTO updateTodo(Long id, TodoRequestDTO dto, String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuário autenticado não encontrado"));
        Todo todo = todoRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new IllegalAccessError("Tarefa não encontrada ou não pertence a esse usuario"));

        todo.setTitle(dto.title());
        todo.setDescription(dto.description());
        todo.setState(dto.state());

        return TodoResponseDTO.fromEntity(todo);
    }
    @Transactional
    public void deleteTodo(Long id, String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuário autenticado não encontrado"));
        Todo todo = todoRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new IllegalAccessError("Tarefa não encontrada ou não pertence a esse usuario"));

        todoRepository.delete(todo);
    }
}
